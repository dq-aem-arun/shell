document.addEventListener("DOMContentLoaded", function () {
  const currentPath = window.location.pathname;

  // Highlight the active nav link
  const navLinks = document.querySelectorAll(".nav-links a");
  navLinks.forEach((link) => {
    const linkPath = new URL(link.href).pathname;
    if (linkPath === currentPath) {
      link.classList.add("active");
    }
  });

  // Show rewards button only if first nav link matches current page
  const firstNavLink = document.querySelector(".nav-links a");
  const rewardsBtn = document.getElementById("rewardsBtn");

  if (firstNavLink && rewardsBtn) {
    const firstPath = new URL(firstNavLink.href).pathname;
    if (firstPath === currentPath) {
      rewardsBtn.style.display = "flex";
    }
  }
});