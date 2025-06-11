(function () {
    "use strict";
  
    $(document).on("foundation-contentloaded", function () {
      const dialog = document.querySelector(".cq-dialog");
  
      if (!dialog) return;
  
      const categorySelector = "coral-select[name='./category']";
      const subcategorySelector = "coral-select[name='./subcategory']";
      const tagSelector = "coral-select[name='./tag']";
  
      const categoryWrapper = dialog.querySelector(".category-wrapper");
      const subcategoryWrapper = dialog.querySelector(".subcategory-wrapper");
      const tagWrapper = dialog.querySelector(".tags-wrapper");
  
      const categoryDropdown = dialog.querySelector(categorySelector);
      const subcategoryDropdown = dialog.querySelector(subcategorySelector);
      const tagDropdown = dialog.querySelector(tagSelector);
  
      if (!categoryDropdown || !subcategoryDropdown || !tagDropdown) return;
  
      const pagePath = Granite.author?.pages?.currentPage?.path || "";
      const siteRootMatch = pagePath.match(/^(\/content\/[^\/]+\/[^\/]+\/[^\/]+)/);
      const siteRoot = siteRootMatch ? siteRootMatch[1] : "/content/shell/us/en";
  
      function resetDropdown(dropdown, placeholder = "Select") {
        while (dropdown.items.length) dropdown.items.remove(dropdown.items.first());
        dropdown.placeholder = placeholder;
      }
  
      function populateDropdown(dropdown, data, selectedValue) {
        resetDropdown(dropdown);
  
        const defaultItem = new Coral.Select.Item();
        defaultItem.setAttribute("value", "");
        defaultItem.textContent = "-- Select --";
        dropdown.items.add(defaultItem);
  
        data.forEach(item => {
          const option = new Coral.Select.Item();
          option.setAttribute("value", item.value);
          option.textContent = item.text;
          if (selectedValue && item.value === selectedValue) {
            option.setAttribute("selected", true);
          }
          dropdown.items.add(option);
        });
      }
  
      function toggleWrapper(wrapper, show) {
        wrapper.style.display = show ? "" : "none";
      }
  
      function loadCategories() {
        fetch(`/bin/shell/dynamicdropdown/categories?site=${siteRoot}`)
          .then(res => res.json())
          .then(data => {
            populateDropdown(categoryDropdown, data, categoryDropdown.value);
            toggleWrapper(categoryWrapper, data.length > 0);
            // Always hide subcategory and tag initially
            toggleWrapper(subcategoryWrapper, false);
            toggleWrapper(tagWrapper, false);
          })
          .catch(err => console.error("Error loading categories:", err));
      }
  
      function loadSubcategories(category) {
        if (!category) {
          toggleWrapper(subcategoryWrapper, false);
          return;
        }
  
        fetch(`/bin/shell/dynamicdropdown/subcategories?category=${category}&site=${siteRoot}`)
          .then(res => res.json())
          .then(data => {
            populateDropdown(subcategoryDropdown, data, "");
            toggleWrapper(subcategoryWrapper, data.length > 0);
            toggleWrapper(tagWrapper, false); // Hide tags until subcategory is chosen
          })
          .catch(err => console.error("Error loading subcategories:", err));
      }
  
      function loadTags(category, subcategory) {
        if (!category || !subcategory) {
          toggleWrapper(tagWrapper, false);
          return;
        }
  
        fetch(`/bin/shell/dynamicdropdown/tags?category=${category}&subcategory=${subcategory}&site=${siteRoot}`)
          .then(res => res.json())
          .then(data => {
            populateDropdown(tagDropdown, data, "");
            toggleWrapper(tagWrapper, data.length > 0);
          })
          .catch(err => console.error("Error loading tags:", err));
      }
  
      Coral.commons.ready(categoryDropdown, () => {
        // 🔸 On dialog open, show only category
        toggleWrapper(categoryWrapper, true);
        toggleWrapper(subcategoryWrapper, false);
        toggleWrapper(tagWrapper, false);
  
        loadCategories();
      });
  
      categoryDropdown.addEventListener("change", function () {
        const selectedCategory = this.value;
  
        subcategoryDropdown.value = "";
        tagDropdown.value = "";
        resetDropdown(subcategoryDropdown);
        resetDropdown(tagDropdown);
  
        toggleWrapper(tagWrapper, false);
        loadSubcategories(selectedCategory);
      });
  
      subcategoryDropdown.addEventListener("change", function () {
        const category = categoryDropdown.value;
        const subcategory = this.value;
  
        tagDropdown.value = "";
        resetDropdown(tagDropdown);
  
        loadTags(category, subcategory);
      });
    });
  })();
  