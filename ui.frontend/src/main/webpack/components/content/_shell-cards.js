  document.addEventListener("DOMContentLoaded", function () {
    const cards = document.querySelectorAll(".shell-card[data-card-link]");
    cards.forEach(function (card) {
      const link = card.getAttribute("data-card-link");
      if (link) {
        card.style.cursor = "pointer";
        card.addEventListener("click", function (e) {
          // Prevent click if user clicks on button/link inside card
          if (e.target.closest('.card-button') || e.target.tagName === 'A' || e.target.tagName === 'BUTTON') {
            return;
          }
          window.open(link, '_self');
        });
      }
    });
  });