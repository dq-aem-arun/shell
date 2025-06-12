(function () {
  "use strict";

  // Wait until the AEM dialog is fully loaded
  $(document).on("foundation-contentloaded", function () {
    const dialog = document.querySelector(".cq-dialog");
    if (!dialog) return;

    // Grab the Coral dropdown fields by their name
    const categoryDropdown = dialog.querySelector("coral-select[name='./category']");
    const subcategoryDropdown = dialog.querySelector("coral-select[name='./subcategory']");
    const tagDropdown = dialog.querySelector("coral-select[name='./tag']");

    // Get the wrappers used to show/hide or display warnings
    const categoryWrapper = dialog.querySelector(".category-wrapper");
    const subcategoryWrapper = dialog.querySelector(".subcategory-wrapper");
    const tagWrapper = dialog.querySelector(".tags-wrapper");

    if (!categoryDropdown || !subcategoryDropdown || !tagDropdown) return;

    function clearWrapperMessage(wrapper) {
      const warnings = wrapper.querySelectorAll(".dialog-warning");
      warnings.forEach(p => p.remove());
    }

    function showWarning(wrapper, message) {
      clearWrapperMessage(wrapper); // Clean up any previous messages

      const warning = document.createElement("p");
      warning.classList.add("dialog-warning");
      warning.style.color = "red";
      warning.style.fontSize = "13px";
      warning.style.marginTop = "5px";
      warning.textContent = message;

      wrapper.appendChild(warning);
      wrapper.style.display = ""; // Ensure the wrapper is visible
    }

    
      //Removes all dropdown options and resets the placeholder
    function resetDropdown(dropdown, placeholder = "Select") {
      while (dropdown.items.length) {
        dropdown.items.remove(dropdown.items.first());
      }
      dropdown.placeholder = placeholder;
    }

    
     //Populates a dropdown with a list of options
    function populateDropdown(dropdown, dataArray, selectedValue) {
      resetDropdown(dropdown);

      const defaultItem = new Coral.Select.Item();
      defaultItem.setAttribute("value", "");
      defaultItem.textContent = "-- Select --";
      dropdown.items.add(defaultItem);

      dataArray.forEach(item => {
        const option = new Coral.Select.Item();
        option.setAttribute("value", item.value);
        option.textContent = item.text;

        if (selectedValue && item.value === selectedValue) {
          option.setAttribute("selected", true);
        }

        dropdown.items.add(option);
      });
    }

     //Show or hide any wrapper element (like dropdown wrappers)
    function toggleWrapper(wrapper, show) {
      wrapper.style.display = show ? "" : "none";
    }

     //Fetch and display category options from servlet
    function loadCategories() {
      clearWrapperMessage(categoryWrapper);

      fetch(`/bin/shell/dynamicdropdown/categories`)
        .then(res => res.json())
        .then(data => {
          if (!Array.isArray(data) || data.length === 0) {
            showWarning(categoryWrapper, "No categories found. Please check the JCR structure.");
            toggleWrapper(subcategoryWrapper, false);
            toggleWrapper(tagWrapper, false);
            return;
          }

          populateDropdown(categoryDropdown, data, categoryDropdown.value);
          toggleWrapper(categoryWrapper, true);
          toggleWrapper(subcategoryWrapper, false);
          toggleWrapper(tagWrapper, false);
        })
        .catch(err => {
          console.error("Error loading categories:", err);
          showWarning(categoryWrapper, "Failed to load categories. Contact the administrator.");
        });
    }

     //Fetch and display subcategories based on selected category
    function loadSubcategories(category) {
      clearWrapperMessage(subcategoryWrapper);
      toggleWrapper(tagWrapper, false);

      if (!category) {
        toggleWrapper(subcategoryWrapper, false);
        return;
      }

      fetch(`/bin/shell/dynamicdropdown/subcategories?category=${category}`)
        .then(res => res.json())
        .then(data => {
          if (!Array.isArray(data) || data.length === 0) {
            showWarning(subcategoryWrapper, " No subcategories found for this category.");
            toggleWrapper(subcategoryWrapper, true);
            return;
          }

          populateDropdown(subcategoryDropdown, data, "");
          toggleWrapper(subcategoryWrapper, true);
        })
        .catch(err => {
          console.error("Error loading subcategories:", err);
          showWarning(subcategoryWrapper, "Failed to load subcategories.");
        });
    }

     //Fetch and display tags based on selected subcategory
    function loadTags(category, subcategory) {
      clearWrapperMessage(tagWrapper);

      if (!category || !subcategory) {
        toggleWrapper(tagWrapper, false);
        return;
      }

      fetch(`/bin/shell/dynamicdropdown/tags?category=${category}&subcategory=${subcategory}`)
        .then(res => res.json())
        .then(data => {
          if (!Array.isArray(data) || data.length === 0) {
            showWarning(tagWrapper, " No tags found for the selected subcategory.");
            toggleWrapper(tagWrapper, true); // Keep it visible to show warning
            return;
          }

          populateDropdown(tagDropdown, data, "");
          toggleWrapper(tagWrapper, true);
        })
        .catch(err => {
          console.error("Error loading tags:", err);
          showWarning(tagWrapper, " Failed to load tags.");
        });
    }

    // On dialog load: initialize dropdowns
    Coral.commons.ready(categoryDropdown, () => {
      toggleWrapper(categoryWrapper, true);
      toggleWrapper(subcategoryWrapper, false);
      toggleWrapper(tagWrapper, false);
      loadCategories();
    });

    // When category changes reset & load subcategories
    categoryDropdown.addEventListener("change", function () {
      const selectedCategory = this.value;

      resetDropdown(subcategoryDropdown);
      resetDropdown(tagDropdown);
      subcategoryDropdown.value = "";
      tagDropdown.value = "";

      toggleWrapper(tagWrapper, false);
      loadSubcategories(selectedCategory);
    });

    // When subcategory changes → reset & load tags
    subcategoryDropdown.addEventListener("change", function () {
      const category = categoryDropdown.value;
      const subcategory = this.value;

      resetDropdown(tagDropdown);
      tagDropdown.value = "";

      loadTags(category, subcategory);
    });
  });
})();
