function updateSecondLastBreadcrumb() {
    const items = document.querySelectorAll('.cmp-breadcrumb__item');
    const displayArea = document.getElementById('second-last-display');

    if (items.length >= 2 && displayArea) {
        const secondLast = items[items.length - 2];
        const name = secondLast.querySelector('[itemprop="name"]')?.textContent;
        const href = secondLast.querySelector('a')?.getAttribute('href');
        displayArea.innerHTML = `<a href="${href}">${name}</a>`;
    }
}

function insertSecondLastDisplayIfMobile() {
    const isMobile = window.innerWidth <= 959;
    const container = document.querySelector('.breadcrumb_v2_container');

    // If screen is mobile and div not yet added
    if (isMobile && !document.getElementById('second-last-display')) {
        const div = document.createElement('div');
        div.id = 'second-last-display';
        div.classList.add('mobile-only');
        container.insertBefore(div, container.firstChild); // Add at top
        updateSecondLastBreadcrumb();
    }
}

window.addEventListener("load", insertSecondLastDisplayIfMobile);
window.addEventListener("resize", insertSecondLastDisplayIfMobile); // Optional: add for dynamic responsiveness
