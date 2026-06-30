document.addEventListener('DOMContentLoaded', () => {
  const mainImage = document.getElementById('mainProductImage');
  const thumbnails = document.querySelectorAll('.product-thumbnail');
  const allImageUrls = Array.from(thumbnails).map(
      thumb => thumb.dataset.imageUrl);
  const quantityInput = document.getElementById('productQuantity');
  const quantityButtons = document.querySelectorAll('[data-quantity-action]');

  let currentImageIndex = mainImage ? allImageUrls.indexOf(mainImage.src) : 0;
  if (mainImage && currentImageIndex === -1 && allImageUrls.length > 0) {
    currentImageIndex = 0;
    mainImage.src = allImageUrls[0];
  }

  function showImage(index) {
    if (!mainImage || allImageUrls.length === 0) {
      return;
    }

    if (index < 0) {
      index = allImageUrls.length - 1;
    } else if (index >= allImageUrls.length) {
      index = 0;
    }

    mainImage.src = allImageUrls[index];
    currentImageIndex = index;

    thumbnails.forEach((thumbnail, i) => {
      thumbnail.classList.toggle('active', i === index);
    });
  }

  window.showNextImage = function () {
    showImage(currentImageIndex + 1);
  }

  window.showPrevImage = function () {
    showImage(currentImageIndex - 1);
  }

  thumbnails.forEach((thumbnail, index) => {
    thumbnail.addEventListener('click', () => {
      showImage(index);
    });
  });

  quantityButtons.forEach(button => {
    button.addEventListener('click', () => {
      if (!quantityInput) {
        return;
      }

      const currentValue = Number.parseInt(quantityInput.value, 10) || 1;
      const nextValue = button.dataset.quantityAction === 'increase'
          ? currentValue + 1
          : Math.max(1, currentValue - 1);

      quantityInput.value = String(nextValue);
    });
  });

  if (quantityInput) {
    quantityInput.addEventListener('change', () => {
      const currentValue = Number.parseInt(quantityInput.value, 10) || 1;
      quantityInput.value = String(Math.max(1, currentValue));
    });
  }
});
