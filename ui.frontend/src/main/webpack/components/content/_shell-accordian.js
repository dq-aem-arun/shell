document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.cmp-accordion__button').forEach(function (btn) {
        btn.addEventListener('click', function () {
            // Toggle expanded class on button
            btn.classList.toggle('cmp-accordion__button--expanded');
            // Toggle panel visibility (assumes next sibling is the panel)
            const panel = btn.nextElementSibling;
            if (panel && panel.classList.contains('cmp-accordion__panel')) {
                panel.classList.toggle('cmp-accordion__panel--expanded');
                panel.classList.toggle('cmp-accordion__panel--hidden');
            }
        });
    });
});