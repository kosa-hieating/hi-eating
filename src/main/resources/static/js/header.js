(() => {
  const root = document.documentElement;
  const threshold = 24;

  const updateHeaderState = () => {
    root.classList.toggle("is-header-condensed", window.scrollY > threshold);
  };

  updateHeaderState();
  window.addEventListener("scroll", updateHeaderState, { passive: true });
})();
