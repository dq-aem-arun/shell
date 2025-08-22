
(function () {
  async function fetchCFs(parentPath, modelPath) {
    let url = `/bin/cfList?parentPath=${encodeURIComponent(parentPath)}&modelPath=${encodeURIComponent(modelPath)}`;
    let res = await fetch(url);
    return res.json();
  }

  // Helper to safely get and decode data attributes
  function getDataAttr(el, name) {
    return decodeURIComponent(el.dataset[name] || "");
  }

  function render(items, container, popup) {
    
    container.innerHTML = items.map(item => `
      <div class="cf-card"
           data-image="${encodeURIComponent(item.productImage || '')}"
           data-title="${encodeURIComponent(item.title || '')}"
           data-tilename="${encodeURIComponent(item.tileName || '')}"
           data-subtitle="${encodeURIComponent(item.subTitle || '')}"
           data-description="${encodeURIComponent(item.description || '')}"
           data-link1="${encodeURIComponent(item.linkPath1 || '')}"
           data-name1="${encodeURIComponent(item.pathName1 || '')}"
           data-linkimageright1="${encodeURIComponent(item.linkImageRight1 || '')}"
           data-linkimageleft1="${encodeURIComponent(item.linkImageLeft1 || '')}"
           data-link2="${encodeURIComponent(item.linkPath2 || '')}"
           data-name2="${encodeURIComponent(item.pathName2 || '')}"
           data-linkimageright2="${encodeURIComponent(item.linkImageRight2 || '')}"
           data-linkimageleft2="${encodeURIComponent(item.linkImageLeft2 || '')}"
      >
        <div class="product_img">
          <img src="${item.productImage || ''}" alt="${item.title || ''}" />
        </div>
        <div class="product_des">
          <h2>${item.title || ''}</h2>
          <div class="sec_line">
            <h3>${item.tileName || ''}</h3>
            <p>${item.subTitle || ''}</p>
          </div>
          <div class="main_link_path">
              <a href="${item.mainLinkPath || '#'}" class="main-link">${item.mainLinkName || "Main Link"}</a>
              <img src="${item.mainLinkImage || ''}" class="main-link-img" alt="img" />
          </div>
        </div>
      </div>
    `).join("");

    // Add click → open popup
    container.querySelectorAll(".cf-card").forEach(card => {
      card.addEventListener("click", () => {
        let data = {
          productImage: getDataAttr(card, "image"),
          title: getDataAttr(card, "title"),
          tileName: getDataAttr(card, "tilename"),
          subTitle: getDataAttr(card, "subtitle"),
          description: getDataAttr(card, "description"),
          linkPath1: getDataAttr(card, "link1"),
          pathName1: getDataAttr(card, "name1"),
          linkImageRight1: getDataAttr(card, "linkimageright1"),
          linkImageLeft1: getDataAttr(card, "linkimageleft1"),
          linkPath2: getDataAttr(card, "link2"),
          pathName2: getDataAttr(card, "name2"),
          linkImageRight2: getDataAttr(card, "linkimageright2"),
          linkImageLeft2: getDataAttr(card, "linkimageleft2"),
        };
        popup.querySelector(".popup-img img").src = data.productImage || "";
        popup.querySelector(".popup-title").innerHTML = data.title || "";
        popup.querySelector(".popup-tilename").innerHTML = data.tileName || "";
        popup.querySelector(".popup-subtitle").innerHTML = data.subTitle || "";
        popup.querySelector(".popup-description").innerHTML = data.description || "";

        let links = popup.querySelector(".popup-links");
        links.innerHTML = "";
        if (data.linkPath1) links.innerHTML += ` <div>
                    <a href="${data.linkPath1}" target="_blank" onclick="return confirm('Are you sure you want to go to this link?')" >
                        <img src="${data.linkImageLeft1 || ''}" class="leftImage1"  />
                        ${data.pathName1 || "Link 1"}
                        <img src="${data.linkImageRight1 || ''}" class="rigthImage1"  />
                    </a>  </div>`;
        if (data.linkPath2) links.innerHTML += ` <div>
                    <a href="${data.linkPath2}" target="_blank" onclick="return confirm('are you sure?')" >
                        <img src="${data.linkImageLeft2 }" class="leftImage2"  />
                        ${data.pathName2 || "Link 2"}
                        <img src="${data.linkImageRight2 }" class="rigthImage2"  />
                    </a>   </div>`;

        popup.style.display = "flex";
        document.documentElement.classList.add("no-scroll"); 

      });
    });
  }

  function populateDropdown(dropdown, items, key) {
    let uniqueValues = [...new Set(items.map(i => i[key]).filter(v => v))];
    uniqueValues.forEach(val => {
      let opt = document.createElement("option");
      opt.value = val;
      opt.textContent = val;
      dropdown.appendChild(opt);
    });
  }

  document.addEventListener("DOMContentLoaded", async () => {
    

    document.querySelectorAll(".cf-list").forEach(async root => {
      let parent = root.dataset.parentPath;
      let model = root.dataset.modelPath;
      let container = root.querySelector("#cf-container");
      let popup = document.getElementById("cf-popup");

      // Fetch data
      let items = await fetchCFs(parent, model);
      let allItems = [...items];

      // Dropdown filter
      let subTitleFilter = root.querySelector("#filter-subtitle");
      populateDropdown(subTitleFilter, items, "subTitle");

      function applyFilter() {
        let val = subTitleFilter.value;
        let filtered = allItems.filter(i => val === "" || i.subTitle === val);
        render(filtered, container, popup);
      }
      subTitleFilter.addEventListener("change", applyFilter);

      // Initial render
      render(allItems, container, popup);

      // Close popup
      popup.querySelector(".close-popup").addEventListener("click", () => {
        popup.style.display = "none";
        document.documentElement.classList.remove("no-scroll"); 

      });
      
    });

  });
})();

