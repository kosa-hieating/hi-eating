// 현재 활성화/선택된 배너의 ID
let selectedBannerId = null;

document.addEventListener('DOMContentLoaded', () => {
    // 첫 번째 배너 자동 선택 처리
    const firstBannerItem = document.querySelector('.banner-item');
    if (firstBannerItem) {
        selectBannerItem(firstBannerItem);
    } else {
        updateRightPanelState(null);
    }
});

// 배너 선택 시 우측 상세 패널 정보 변경
function selectBannerItem(element) {
    if (!element) return;

    // 모든 배너 아이템의 active 클래스 제거
    document.querySelectorAll('.banner-item').forEach(item => {
        item.classList.remove('active');
    });

    // 선택한 아이템에 active 클래스 추가
    element.classList.add('active');

    // 데이터 속성 읽기
    selectedBannerId = parseInt(element.getAttribute('data-id'), 10);
    const link = element.getAttribute('data-link') || '';
    const img = element.getAttribute('data-img') || '';

    // 우측 패널 상태 갱신
    updateRightPanelState({ id: selectedBannerId, link, img });
}

// 우측 상세 링크 패널 정보 주입 및 갱신
function updateRightPanelState(bannerData) {
    const previewImg = document.getElementById('bannerPreviewImg');
    const previewPlaceholder = document.getElementById('bannerPreviewPlaceholder');
    const linkInput = document.getElementById('bannerLinkInput');
    const linkPreviewBox = document.getElementById('linkPreviewBox');

    if (!bannerData) {
        // 배너가 하나도 없을 때
        if (previewImg) previewImg.style.display = 'none';
        if (previewPlaceholder) previewPlaceholder.style.display = 'flex';
        if (linkInput) {
            linkInput.value = '';
            linkInput.disabled = true;
        }
        updateLinkPreview('');
        selectedBannerId = null;
        return;
    }

    // 입력창 활성화 및 주입
    if (linkInput) {
        linkInput.disabled = false;
        linkInput.value = bannerData.link;
    }

    // 이미지 미리보기 처리
    if (previewImg && bannerData.img) {
        previewImg.src = bannerData.img;
        previewImg.style.display = 'block';
        if (previewPlaceholder) previewPlaceholder.style.display = 'none';
    } else {
        if (previewImg) previewImg.style.display = 'none';
        if (previewPlaceholder) previewPlaceholder.style.display = 'flex';
    }

    // 링크 프리뷰 업데이트
    updateLinkPreview(bannerData.link);
}

// 링크 입력 시 실시간 미리보기 연동
function updateLinkPreview(value) {
    const previewText = document.getElementById('linkPreviewText');
    const previewAnchor = document.getElementById('linkPreviewAnchor');
    
    const displayValue = value.trim() || '링크를 등록하시면 배너 클릭 시 이동 경로가 미리 노출됩니다.';
    
    if (previewText) {
        previewText.innerText = displayValue;
        if (value.trim()) {
            previewText.classList.remove('text-muted');
        } else {
            previewText.classList.add('text-muted');
        }
    }

    if (previewAnchor) {
        if (value.trim()) {
            previewAnchor.href = value.trim();
            previewAnchor.style.pointerEvents = 'auto';
            previewAnchor.style.opacity = '1';
        } else {
            previewAnchor.removeAttribute('href');
            previewAnchor.style.pointerEvents = 'none';
            previewAnchor.style.opacity = '0.4';
        }
    }
}

// 파일 선택 트리거
function triggerFileUpload() {
    const fileInput = document.getElementById('bannerFileInput');
    if (fileInput) {
        fileInput.click();
    }
}

// 1. 배너 등록 (파일 업로드) API 호출
function uploadBannerImage(input) {
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('file', file);

    // 사용자 알림
    const addDesc = document.querySelector('.add-desc');
    const originalText = addDesc ? addDesc.innerText : '';
    if (addDesc) addDesc.innerText = '업로드 중...';

    fetch('/admin/api/promotions', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(response => {
        if (response.isSuccess) {
            alert('배너 이미지가 성공적으로 등록되었습니다.');
            location.reload();
        } else {
            alert('배너 이미지 등록에 실패했습니다: ' + (response.message || '알 수 없는 오류'));
            if (addDesc) addDesc.innerText = originalText;
        }
    })
    .catch(err => {
        console.error('Error uploading banner image:', err);
        alert('서버와 통신하는 중 오류가 발생했습니다.');
        if (addDesc) addDesc.innerText = originalText;
    });
}

// 2. 배너 링크 정보 업데이트 (저장) API 호출
function saveBannerLink() {
    if (!selectedBannerId) {
        alert('링크를 저장할 배너를 왼쪽 목록에서 선택해 주세요.');
        return;
    }

    const linkInput = document.getElementById('bannerLinkInput');
    const linkValue = linkInput ? linkInput.value.trim() : '';

    fetch(`/admin/api/promotions/${selectedBannerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ link: linkValue })
    })
    .then(res => res.json())
    .then(response => {
        if (response.isSuccess) {
            alert('배너 링크 정보가 수정되었습니다.');
            
            // 로컬 DOM의 데이터 속성 갱신하여 페이지 리로드 없이도 데이터 보존
            const activeItem = document.querySelector(`.banner-item[data-id="${selectedBannerId}"]`);
            if (activeItem) {
                activeItem.setAttribute('data-link', linkValue);
            }
        } else {
            alert('링크 저장에 실패했습니다: ' + (response.message || '알 수 없는 오류'));
        }
    })
    .catch(err => {
        console.error('Error updating banner link:', err);
        alert('서버와 통신하는 중 오류가 발생했습니다.');
    });
}

// 3. 배너 삭제 API 호출
function deleteBanner(id, event) {
    if (event) {
        event.stopPropagation(); // 부모 아이템 클릭(상세조회) 이벤트 방지
    }

    if (!confirm('정말로 이 프로모션 배너를 삭제하시겠습니까?\n삭제 시 메인 화면에 더 이상 노출되지 않습니다.')) {
        return;
    }

    fetch(`/admin/api/promotions/${id}`, {
        method: 'DELETE'
    })
    .then(res => res.json())
    .then(response => {
        if (response.isSuccess) {
            alert('배너가 성공적으로 삭제되었습니다.');
            location.reload();
        } else {
            alert('배너 삭제에 실패했습니다: ' + (response.message || '알 수 없는 오류'));
        }
    })
    .catch(err => {
        console.error('Error deleting banner:', err);
        alert('서버와 통신하는 중 오류가 발생했습니다.');
    });
}
