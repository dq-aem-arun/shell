(function ($, $document) {
    "use strict";

    const categorySelector = "coral-select[name='./category']";
    const subcategorySelector = "coral-select[name='./subcategory']";
    const tagsSelector = "coral-select[name='./tag']";

    // Function to toggle visibility of wrapper based on selection
    function toggleWrapper(wrapperClass, show) {
        const wrapper = $(".cq-dialog").find("." + wrapperClass);
        show ? wrapper.removeAttr("hidden") : wrapper.attr("hidden", "true");
    }

    // Generic function to populate Coral select dropdowns
    function populateCoralSelect(selector, items) {
        const coralSelect = $(selector)[0];
        if (!coralSelect) return;

        Coral.commons.ready(coralSelect, function (select) {
            select.items.clear();
            select.items.add({ value: '', content: { innerHTML: 'Select' } });

            items.forEach(item => {
                select.items.add({
                    value: item.value,
                    content: { innerHTML: item.text }
                });
            });

            select.value = '';
        });
    }

    // Bind events to handle category and subcategory changes
    function bindEvents() {
        $(document).off("change.dynamicCategory").on("change.dynamicCategory", categorySelector, function () {
            const category = this.value;

            if (category) {
                // Fetch subcategories based on selected category
                $.get("/bin/shell/dynamicdropdown/subcategories", { category }, function (data) {
                    populateCoralSelect(subcategorySelector, data);
                    toggleWrapper("subcategory-wrapper", true);
                    toggleWrapper("tags-wrapper", false);
                });
            } else {
                toggleWrapper("subcategory-wrapper", false);
                toggleWrapper("tags-wrapper", false);
            }
        });

        $(document).off("change.dynamicSubcategory").on("change.dynamicSubcategory", subcategorySelector, function () {
            const subcategory = this.value;
            const category = $(categorySelector).val();

            if (subcategory && category) {
                // Fetch tags based on selected category and subcategory
                $.get("/bin/shell/dynamicdropdown/tags", { category, subcategory }, function (data) {
                    populateCoralSelect(tagsSelector, data);
                    toggleWrapper("tags-wrapper", true);
                });
            } else {
                toggleWrapper("tags-wrapper", false);
            }
        });
    }

    // Initialize the dialog on dialog-ready event
    $document.on("dialog-ready", function () {
        toggleWrapper("subcategory-wrapper", false);
        toggleWrapper("tags-wrapper", false);

        // Fetch categories on dialog load
        $.get("/bin/shell/dynamicdropdown/categories", function (data) {
            populateCoralSelect(categorySelector, data);
        });

        // Bind event handlers
        bindEvents();
    });

})(jQuery, jQuery(document));
