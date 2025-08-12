document.addEventListener("DOMContentLoaded", function () {
  const consentBar = document.querySelector(".cookie-consent");
  const acceptBtn = document.querySelector(".cookie-actions .accept");
  const rejectBtn = document.querySelector(".cookie-actions .reject");

  // Helper to set cookie
  function setCookie(name, value, days) {
    // const expires = new Date(Date.now() + days * 864e5).toUTCString();
    let date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    const expires = date.toUTCString();
    document.cookie = name + "=" + encodeURIComponent(value) + "; expires=" + expires + "; path=/";
  }

  // Helper to get cookie
  function getCookie(name) {
    const cookie = document.cookie.split("; ").find(row => row.startsWith(name + "="));
    return cookie ? cookie.split("=")[1] : null;
}

  // Hide bar if already accepted
  if (getCookie("cookieConsent")) {
    if (consentBar) consentBar.style.display = "none";
    return;
  }

  // Accept logic
  if (acceptBtn) {
    acceptBtn.addEventListener("click", function () {
      setCookie("cookieConsent", "accepted", 30);
      if (consentBar) consentBar.style.display = "none";
    });
  }

  // Reject logic
if (rejectBtn) {
    rejectBtn.addEventListener("click", function () {
      // Do not set cookie, just hide bar
      if (consentBar) consentBar.style.display = "none";
    });
  }
});

