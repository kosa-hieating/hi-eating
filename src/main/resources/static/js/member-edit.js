(function () {
  const withdrawButton = document.getElementById('member-withdraw-button');
  const withdrawModal = document.getElementById('member-withdraw-modal');
  const withdrawConfirmButton = document.getElementById('member-withdraw-confirm-button');
  const withdrawForm = document.getElementById('member-withdraw-form');

  if (!withdrawButton || !withdrawForm) {
    return;
  }

  const showWithdrawModal = () => {
    if (withdrawModal && window.bootstrap?.Modal) {
      window.bootstrap.Modal.getOrCreateInstance(withdrawModal).show();
      return;
    }

    if (confirm('회원 탈퇴 후에는 현재 계정으로 로그인할 수 없습니다. 탈퇴하시겠습니까?')) {
      withdrawForm?.submit();
    }
  };

  withdrawButton.addEventListener('click', showWithdrawModal);

  withdrawConfirmButton?.addEventListener('click', () => {
    withdrawForm.submit();
  });
})();
