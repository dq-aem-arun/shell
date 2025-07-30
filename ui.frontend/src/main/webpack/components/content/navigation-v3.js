// Wait for the DOM to fully load before executing the script
document.addEventListener("DOMContentLoaded", function () {
  // Select DOM elements for the hamburger menu, close button, navbar, and top-level nav links
  const hamburger = document.getElementById("hamburger");
  const closeMenu = document.getElementById("close-menu");
  const bottomNavbar = document.querySelector(".cmp-bottom-navbar");
  const navLinks = document.querySelectorAll(".cmp-nav-links > li > a");

  // Function to close all dropdowns and subdropdowns, resetting active and selected states
  function closeAllDropdowns() {
    // Remove 'active' class from all dropdowns
    document.querySelectorAll(".cmp-dropdown").forEach((d) => {
      d.classList.remove("cmp-active");
    });
    // Remove 'active' class from submenu toggles and hide their submenus
    document.querySelectorAll(".cmp-submenu-toggle").forEach((st) => {
      st.classList.remove("cmp-active");
      if (st.nextElementSibling) {
        st.nextElementSibling.classList.remove("cmp-show");
      }
    });
    // Remove 'selected' class from all dropdown items
    document.querySelectorAll(".cmp-dropdown-item").forEach((item) => {
      item.classList.remove("cmp-selected");
    });
  }

  // Function to close the mobile menu by removing the 'mobile-menu-active' class
  function closeMobileMenu() {
    bottomNavbar.classList.remove("cmp-mobile-menu-active");
  }

  // Event listener to open the mobile menu when the hamburger icon is clicked
  hamburger.addEventListener("click", function () {
    bottomNavbar.classList.add("cmp-mobile-menu-active");
  });

  // Event listener to close the mobile menu and all dropdowns when the close button is clicked
  closeMenu.addEventListener("click", function () {
    closeMobileMenu();
    closeAllDropdowns();
  });

  // Add click event listeners to top-level nav links (excluding dropdown toggles)
  navLinks.forEach((link) => {
    if (!link.classList.contains("cmp-dropdown-toggle")) {
      link.addEventListener("click", (e) => {
        e.preventDefault(); // Prevent default link behavior
        closeAllDropdowns(); // Close all dropdowns
        // Remove 'active' class from all nav links
        navLinks.forEach((l) => l.classList.remove("cmp-active"));
        // Set the clicked link as active
        link.classList.add("cmp-active");
        // Close the mobile menu if the screen width is 960px or less
        if (window.innerWidth <= 960) {
          closeMobileMenu();
        }
      });
    }
  });

  // Add click event listeners to dropdown toggle links
  const dropdownToggles = document.querySelectorAll(".cmp-dropdown-toggle");
  dropdownToggles.forEach((toggle) => {
    toggle.addEventListener("click", (e) => {
      e.preventDefault(); // Prevent default link behavior
      // Find the closest dropdown container
      const dropdown = toggle.closest(".cmp-dropdown");
      // Check if the dropdown is already active
      const isActive = dropdown.classList.contains("cmp-active");
      // Close all dropdowns
      closeAllDropdowns();
      // If the dropdown was not active, activate it and the toggle
      if (!isActive) {
        dropdown.classList.add("cmp-active");
        // Remove 'active' class from all nav links
        navLinks.forEach((l) => l.classList.remove("cmp-active"));
        toggle.classList.add("cmp-active");
      }
    });
  });

  // Add click event listeners to submenu toggle items
  const submenuToggles = document.querySelectorAll(".cmp-submenu-toggle");
  submenuToggles.forEach((toggle) => {
    toggle.addEventListener("click", (e) => {
      e.preventDefault(); // Prevent default link behavior
      e.stopPropagation(); // Prevent the click from bubbling up to parent dropdowns
      // Get the next sibling submenu
      const submenu = toggle.nextElementSibling;
      // Check if the toggle is already active
      const isActive = toggle.classList.contains("cmp-active");
      if (isActive) {
        // If active, deactivate the toggle and hide the submenu
        toggle.classList.remove("cmp-active");
        submenu.classList.remove("cmp-show");
      } else {
        // If not active, activate the toggle and show the submenu
        toggle.classList.add("cmp-active");
        submenu.classList.add("cmp-show");
        // Find all dropdown items in the submenu
        const submenuItems = submenu.querySelectorAll(".cmp-dropdown-item");
        let goToItem = null;
        // Identify the first item starting with "Go to:"
        submenuItems.forEach((item) => {
          if (item.textContent.trim().startsWith("Go to:") && !goToItem) {
            goToItem = item;
          }
        });
        // If a "Go to:" item is found, mark it as selected
        if (goToItem) {
          submenu.querySelectorAll(".cmp-dropdown-item").forEach((item) => {
            item.classList.remove("cmp-selected");
          });
          goToItem.classList.add("cmp-selected");
        }
      }
    });
  });

  // Add click event listeners to non-toggle dropdown items
  const dropdownItems = document.querySelectorAll(
    ".cmp-dropdown-item:not(.cmp-submenu-toggle)"
  );
  dropdownItems.forEach((item) => {
    item.addEventListener("click", (e) => {
      e.stopPropagation(); // Prevent the click from bubbling up
      closeAllDropdowns(); // Close all dropdowns
      // Close the mobile menu if the screen width is 960px or less
      if (window.innerWidth <= 960) {
        closeMobileMenu();
      }
    });
  });

  // Add global click event listener to close all dropdowns when clicking outside the nav-links
  document.addEventListener("click", function (e) {
    // Check if the click target is outside the nav-links container
    if (!e.target.closest(".cmp-nav-links")) {
      closeAllDropdowns();
    }
  });
});
