const navLinks = document.querySelectorAll(".nav-links a");
const rewardsBtn = document.getElementById("rewardsBtn");
function updateActive(link) {
  navLinks.forEach((l) => l.classList.remove("active"));
  link.classList.add("active");
  if (link.textContent.trim() === "Customer") {
    rewardsBtn.style.display = "flex";
  } else {
    rewardsBtn.style.display = "none";
  }
}
navLinks.forEach((link) => {
  link.addEventListener("click", function (e) {
    e.preventDefault();
    updateActive(this);
  });
});
// Initial check on page load
document.addEventListener("DOMContentLoaded", () => {
  const activeLink = document.querySelector(".nav-links a.active");
  if (activeLink) updateActive(activeLink);
});
