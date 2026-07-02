package kr.or.hieating.promotion.admin.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
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
  public Promotions registerPromotion(
      MultipartFile file, String title, String link, LocalDate startsAt, LocalDate endsAt) {
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
        Files.copy(
            destFileSrc.toPath(), destFileBuild.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      int maxOrder = adminPromotionMapper.selectMaxDisplayOrder();
      int nextOrder = maxOrder + 1000;

      Promotions promotion =
          Promotions.builder()
              .title(title)
              .imgSrc("/images/promotions/" + fileName)
              .link(link)
              .displayOrder(nextOrder)
              .startsAt(startsAt.atStartOfDay())
              .endsAt(endsAt.atTime(23, 59, 59))
              .build();

      adminPromotionMapper.insertPromotion(promotion);
      return promotion;
    } catch (Exception e) {
      throw new RuntimeException("프로모션 등록(파일 업로드) 실패", e);
    }
  }

  @Transactional
  public void updatePromotionDetails(
      int id, MultipartFile file, String title, String link, LocalDate startsAt, LocalDate endsAt) {
    String newImgSrc = null;

    // 새로운 이미지 파일이 전달된 경우 업로드 처리 및 기존 이미지 삭제
    if (file != null && !file.isEmpty()) {
      // 기존 이미지 삭제를 위해 정보 조회
      Promotions oldPromotion = adminPromotionMapper.selectPromotionById(id);
      if (oldPromotion != null && oldPromotion.getImgSrc() != null) {
        try {
          String projectPath = System.getProperty("user.dir");
          // 정적 리소스 파일 삭제
          File srcFile =
              new File(projectPath, "src/main/resources/static" + oldPromotion.getImgSrc());
          if (srcFile.exists()) {
            srcFile.delete();
          }
          // 빌드 결과 파일 삭제
          File buildFile =
              new File(projectPath, "build/resources/main/static" + oldPromotion.getImgSrc());
          if (buildFile.exists()) {
            buildFile.delete();
          }
        } catch (Exception e) {
          System.err.println("기존 이미지 파일 물리적 삭제 실패: " + e.getMessage());
        }
      }

      // 새 이미지 파일 업로드
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

        File uploadDirBuild =
            new File(projectPath, "build/resources/main/static/images/promotions");
        if (uploadDirBuild.exists()) {
          File destFileBuild = new File(uploadDirBuild, fileName);
          Files.copy(
              destFileSrc.toPath(), destFileBuild.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // 새로운 이미지 상대 경로 설정
        newImgSrc = "/images/promotions/" + fileName;
      } catch (Exception e) {
        throw new RuntimeException("새 프로모션 이미지 등록 실패", e);
      }
    }

    Promotions promotion =
        Promotions.builder()
            .id(id)
            .title(title)
            .link(link)
            .imgSrc(newImgSrc)
            .startsAt(startsAt.atStartOfDay())
            .endsAt(endsAt.atTime(23, 59, 59))
            .build();

    adminPromotionMapper.updatePromotion(promotion);
  }

  @Transactional
  public void deletePromotion(int id) {
    // 이미지 파일 삭제를 위해 먼저 DB에서 배너 정보를 조회
    Promotions promotion = adminPromotionMapper.selectPromotionById(id);
    if (promotion != null && promotion.getImgSrc() != null) {
      String imgSrc = promotion.getImgSrc(); // DB에 저장된 이미지 웹 상대경로 (예: /images/promotions/uuid.png)

      try {
        String projectPath = System.getProperty("user.dir");

        // 프로젝트 static 소스 디렉토리 내 실제 파일 삭제
        File srcFile = new File(projectPath, "src/main/resources/static" + imgSrc);
        if (srcFile.exists()) {
          srcFile.delete();
        }

        // 빌드 출력(build) 디렉토리 내 복제된 파일도 삭제
        File buildFile = new File(projectPath, "build/resources/main/static" + imgSrc);
        if (buildFile.exists()) {
          buildFile.delete();
        }
      } catch (Exception e) {
        // 실제 물리 파일 삭제 시 에러가 나더라도 DB 데이터 무결성을 위해 삭제 작업 자체를 롤백시키지는 않고 로그만 기록합니다.
        System.err.println("실제 이미지 파일 물리적 삭제 실패: " + e.getMessage());
      }
    }

    // 데이터베이스에서 최종 배너 레코드 삭제
    adminPromotionMapper.deletePromotion(id);
  }
}
