document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('.review-write-btn').forEach(function (link) {
    link.addEventListener('click', async function (event) {
      event.preventDefault();
      var purchaseId = link.dataset.purchaseId;
      var url = '/review/new?purchaseId=' + purchaseId;

      try {
        var response = await fetch(url, {
          headers: { 'X-Requested-With': 'XMLHttpRequest' },
        });

        if (!response.ok) {
          var body = await response.json().catch(function () {
            return null;
          });
          var errorCode = body && body.code;

          if (errorCode === 'REVIEW4002') {
            alert('이미 리뷰를 작성한 상품입니다.');
          } else {
            alert('구매한 적 없는 상품입니다.');
          }
          return;
        }

        window.location.href = link.href;
      } catch (error) {
        alert('리뷰 작성 페이지를 불러올 수 없습니다.');
      }
    });
  });
});
