document.addEventListener("DOMContentLoaded", function () {
  if (window.innerWidth <= 320) {
    const shellTab = document.querySelector(".shell_tab");
    const tabList = document.querySelector(".shell_tab .cmp-tabs__tablist");
    if (shellTab && tabList) {
      shellTab.addEventListener("click", function (e) {
        const rect = shellTab.getBoundingClientRect();
        const clickX = e.clientX - rect.left;
        // Check if click is on left arrow area
        if (clickX <= 35) {
          tabList.scrollLeft -= 100;
        }
        // Check if click is on right arrow area
        else if (clickX >= rect.width - 35) {
          tabList.scrollLeft += 100;
        }
      });
    }
  }
});
