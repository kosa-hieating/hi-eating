package kr.or.hieating.promotion.admin.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.or.hieating.promotion.admin.mapper.AdminPromotionMapper;
import kr.or.hieating.promotion.domain.Promotions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPromotionService {

  private final AdminPromotionMapper adminPromotionMapper;

  public List<Promotions> getAllPromotions() {
    List<Promotions> promotions = adminPromotionMapper.selectAllPromotions();
    return promotions != null ? promotions : List.of();
  }

  @Transactional
  public Promotions registerPromotion(MultipartFile file, String title, String link, LocalDate startsAt, LocalDate endsAt) {
    try {
      // 고유 파일명 생성 (중복 파일명 방지를 위해 UUID 활용)
      String originalName = file.getOriginalFilename();
      String ext = "";
      if (originalName != null && originalName.contains(".")) {
        ext = originalName.substring(originalName.lastIndexOf("."));
      }
      String fileName = UUID.randomUUID().toString() + ext;

      // 프로젝트 정적 리소스(static) 폴더 경로를 획득하여 물리 파일 저장
      String projectPath = System.getProperty("user.dir");
      File uploadDirSrc = new File(projectPath, "src/main/resources/static/images/promotions");
      if (!uploadDirSrc.exists()) {
        uploadDirSrc.mkdirs();
      }
      File destFileSrc = new File(uploadDirSrc, fileName);
      file.transferTo(destFileSrc);

      // 서버 구동 중 스태틱 리소스를 즉시 브라우저가 읽을 수 있도록 빌드 결과(build/) 디렉토리에 동기화 복사 진행
      File uploadDirBuild = new File(projectPath, "build/resources/main/static/images/promotions");
      if (uploadDirBuild.exists()) {
        File destFileBuild = new File(uploadDirBuild, fileName);
        Files.copy(destFileSrc.toPath(), destFileBuild.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      /*
       * [display_order 계산 로직 설명]
       * display_order는 사용자 화면(예: 메인화면)에 노출될 배너의 순서를 지정하는 값으로, 오름차순(ASC) 정렬에 사용됩니다.
       * 
       * 1. adminPromotionMapper.selectMaxDisplayOrder()를 통해 현재 저장된 배너 순서 중 가장 높은(마지막) 값을 가져옵니다.
       *    만약 등록된 배너가 없다면 0이 반환됩니다.
       * 2. 새로 추가되는 배너가 기본적으로 리스트의 가장 하단(마지막)에 위치하도록 기존 최대 순서 값에 1000을 더해 nextOrder로 설정합니다.
       * 3. [왜 1씩 늘리지 않고 1000씩 간격을 두는가?]
       *    관리자 페이지에서는 마우스 드래그 앤 드롭을 이용해 배너의 순서를 실시간으로 유연하게 조정할 수 있습니다.
       *    순서가 1000, 2000, 3000 처럼 일정 간격을 벌려 놓으면, 예를 들어 순서 1000인 배너와 2000인 배너 사이로 특정 배너를 이동시켰을 때
       *    새로운 배너 순서값을 단순히 두 배너 순서의 중간값인 1500으로 변경하여 DB에 반영할 수 있습니다.
       *    이렇게 하면 리스트 내 다른 모든 배너들의 display_order 값을 매번 전부 재조정하는 비효율적인 DB 전체 일괄 갱신(Full-reordering) 쿼리를
       *    실행하지 않고도 단 하나의 배너 레코드만 업데이트할 수 있게 되어 성능이 최적화됩니다.
       */
      int maxOrder = adminPromotionMapper.selectMaxDisplayOrder();
      int nextOrder = maxOrder + 1000;

      // 4. 도메인(Promotions) 객체를 초기화하고 DB 저장 위임
      Promotions promotion = new Promotions();
      promotion.setTitle(title);
      promotion.setImgSrc("/images/promotions/" + fileName);
      promotion.setLink(link);
      promotion.setDisplayOrder(nextOrder);
      // 시작일은 당일 00:00:00 으로 설정
      promotion.setStartsAt(startsAt.atStartOfDay());
      // 종료일은 당일 23:59:59 까지 노출되도록 설정
      promotion.setEndsAt(endsAt.atTime(23, 59, 59));

      adminPromotionMapper.insertPromotion(promotion);
      return promotion;
    } catch (Exception e) {
      throw new RuntimeException("프로모션 등록(파일 업로드) 실패", e);
    }
  }

  /**
   * 특정 프로모션 배너의 상세 정보(이미지 파일 포함 가능)를 수정합니다.
   *
   * @param id 수정 대상 배너의 고유 식별자 ID
   * @param file 새로 변경할 배너 이미지 파일 (선택 사항)
   * @param title 변경할 배너 제목
   * @param link 변경할 상품 링크 URL
   * @param startsAt 변경할 노출 시작 일자
   * @param endsAt 변경할 노출 종료 일자
   */
  @Transactional
  public void updatePromotionDetails(int id, MultipartFile file, String title, String link, LocalDate startsAt, LocalDate endsAt) {
    Promotions promotion = new Promotions();
    promotion.setId(id);
    promotion.setTitle(title);
    promotion.setLink(link);
    promotion.setStartsAt(startsAt.atStartOfDay());
    promotion.setEndsAt(endsAt.atTime(23, 59, 59));

    // 새로운 이미지 파일이 전달된 경우 업로드 처리 및 기존 이미지 삭제
    if (file != null && !file.isEmpty()) {
      // 1. 기존 이미지 삭제를 위해 정보 조회
      Promotions oldPromotion = adminPromotionMapper.selectPromotionById(id);
      if (oldPromotion != null && oldPromotion.getImgSrc() != null) {
        try {
          String projectPath = System.getProperty("user.dir");
          // 정적 리소스 파일 삭제
          File srcFile = new File(projectPath, "src/main/resources/static" + oldPromotion.getImgSrc());
          if (srcFile.exists()) {
            srcFile.delete();
          }
          // 빌드 결과 파일 삭제
          File buildFile = new File(projectPath, "build/resources/main/static" + oldPromotion.getImgSrc());
          if (buildFile.exists()) {
            buildFile.delete();
          }
        } catch (Exception e) {
          System.err.println("기존 이미지 파일 물리적 삭제 실패: " + e.getMessage());
        }
      }

      // 2. 새 이미지 파일 업로드
      try {
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
          ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + ext;

        String projectPath = System.getProperty("user.dir");
        File uploadDirSrc = new File(projectPath, "src/main/resources/static/images/promotions");
        if (!uploadDirSrc.exists()) {
          uploadDirSrc.mkdirs();
        }
        File destFileSrc = new File(uploadDirSrc, fileName);
        file.transferTo(destFileSrc);

        File uploadDirBuild = new File(projectPath, "build/resources/main/static/images/promotions");
        if (uploadDirBuild.exists()) {
          File destFileBuild = new File(uploadDirBuild, fileName);
          Files.copy(destFileSrc.toPath(), destFileBuild.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // 도메인 객체에 새로운 이미지 상대 경로 설정
        promotion.setImgSrc("/images/promotions/" + fileName);
      } catch (Exception e) {
        throw new RuntimeException("새 프로모션 이미지 등록 실패", e);
      }
    }

    adminPromotionMapper.updatePromotion(promotion);
  }

  /**
   * 특정 프로모션 배너 정보를 DB에서 삭제하고, 실제 로컬 저장소(정적 및 빌드 폴더)에 존재하는 원본 이미지 파일도 물리적으로 지웁니다.
   *
   * @param id 삭제 대상 프로모션 배너의 고유 식별자 ID
   */
  @Transactional
  public void deletePromotion(int id) {
    // 1. 이미지 파일 삭제를 위해 먼저 DB에서 배너 정보를 조회
    Promotions promotion = adminPromotionMapper.selectPromotionById(id);
    if (promotion != null && promotion.getImgSrc() != null) {
      String imgSrc = promotion.getImgSrc(); // DB에 저장된 이미지 웹 상대경로 (예: /images/promotions/uuid.png)
      
      try {
        String projectPath = System.getProperty("user.dir");
        
        // 2. 프로젝트 static 소스 디렉토리 내 실제 파일 삭제
        File srcFile = new File(projectPath, "src/main/resources/static" + imgSrc);
        if (srcFile.exists()) {
          srcFile.delete();
        }
        
        // 3. 빌드 출력(build) 디렉토리 내 복제된 파일도 삭제
        File buildFile = new File(projectPath, "build/resources/main/static" + imgSrc);
        if (buildFile.exists()) {
          buildFile.delete();
        }
      } catch (Exception e) {
        // 실제 물리 파일 삭제 시 에러가 나더라도 DB 데이터 무결성을 위해 삭제 작업 자체를 롤백시키지는 않고 로그만 기록합니다.
        System.err.println("실제 이미지 파일 물리적 삭제 실패: " + e.getMessage());
      }
    }
    
    // 4. 데이터베이스에서 최종 배너 레코드 삭제
    adminPromotionMapper.deletePromotion(id);
  }
}
