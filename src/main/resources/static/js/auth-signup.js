document.addEventListener("DOMContentLoaded", () => {
  const emailLocalInput = document.getElementById("signup-email-local");
  const emailDomainSelect = document.getElementById("signup-email-domain");
  const emailCheckButton = document.getElementById("signup-email-check-button");
  const modalElement = document.getElementById("signup-email-check-modal");
  const modalMessage = document.getElementById("signup-email-check-modal-message");

  if (!emailLocalInput || !emailDomainSelect || !emailCheckButton || !modalElement || !modalMessage) {
    return;
  }

  const showModal = (message, state) => {
    modalMessage.textContent = message;
    modalMessage.classList.remove("is-success", "is-danger");

    if (state) {
      modalMessage.classList.add(state);
    }

    if (window.bootstrap?.Modal) {
      window.bootstrap.Modal.getOrCreateInstance(modalElement).show();
      return;
    }

    alert(message);
  };

  const buildEmail = () => {
    const emailLocal = emailLocalInput.value.trim();
    const emailDomain = emailDomainSelect.value.trim();

    if (!emailLocal) {
      return "";
    }

    if (emailLocal.includes("@") || !emailDomain) {
      return emailLocal;
    }

    return `${emailLocal}@${emailDomain}`;
  };

  emailCheckButton.addEventListener("click", async () => {
    const email = buildEmail();
    const checkUrl = emailCheckButton.dataset.checkUrl;

    if (!email) {
      showModal("이메일을 입력해 주세요.", "is-danger");
      emailLocalInput.focus();
      return;
    }

    try {
      const response = await fetch(`${checkUrl}?email=${encodeURIComponent(email)}`, {
        headers: {
          Accept: "application/json"
        }
      });

      if (!response.ok) {
        throw new Error("Email check request failed");
      }

      const result = await response.json();
      showModal(result.message, result.available ? "is-success" : "is-danger");
    } catch (error) {
      showModal("중복확인 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.", "is-danger");
    }
  });
});
