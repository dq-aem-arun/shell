function renderResults(data) {
  const container = document.getElementById("results");
  const countContainer = document.getElementById("result-count");
  const paginationContainer = document.getElementById("pagination");

  // Clear previous
  container.innerHTML = "";
  countContainer.textContent = "";
  paginationContainer.innerHTML = "";
  container.style.display = "none";
  countContainer.style.display = "none";
  paginationContainer.style.display = "none";

  if (!data || !data.results || data.results.length === 0) {
    countContainer.textContent = "No results found.";
    countContainer.style.display = "block";
    return;
  }

  // ✅ Show total results count
  countContainer.textContent = `${data.totalResults} results found`;
  countContainer.style.display = "block";

  data.results.forEach(item => {
    const div = document.createElement("div");
    div.className = "search-result-item";
    div.style.marginBottom = "40px";

    // Breadcrumb build
    let breadcrumbHtml = "";
    if (Array.isArray(item.breadcrumb)) {
      breadcrumbHtml = item.breadcrumb.map((crumb, index) => {
        const link = `<a href="${crumb.path}.html">${crumb.title}</a>`;
        return index < item.breadcrumb.length - 1 ? link + " &gt; " : link;
      }).join("");
    } else {
      breadcrumbHtml = item.breadcrumb;
    }

    div.innerHTML = `
      <h3><a href="${item.path}.html">${item.title}</a></h3>
      <p>${breadcrumbHtml}</p>
      <p class="excerpt">${item.excerpt}</p>
    `;
    container.appendChild(div);
  });

  container.style.display = "block";

// ✅ Render pagination
if (data.totalPages > 1) {
  paginationContainer.style.display = "block";

  // Previous link (always show)
  const prev = document.createElement("span");
  prev.textContent = "< Previous ";
  if (data.currentPage > 1) {
    prev.classList.add("pagination-link");
    prev.addEventListener("click", () => fetchResults(data.currentPage - 1));
  } else {
    prev.classList.add("disabled-link"); // ✅ dull when first page
  }
  paginationContainer.appendChild(prev);

  // Page numbers
  for (let i = 1; i <= data.totalPages; i++) {
    const page = document.createElement("span");
    page.textContent = ` ${i} `;
    if (i === data.currentPage) {
      page.classList.add("active-page");
    } else {
      page.classList.add("pagination-link");
      page.addEventListener("click", () => fetchResults(i));
    }
    paginationContainer.appendChild(page);
  }

  // Next link (always show)
  const next = document.createElement("span");
  next.textContent = " Next >";
  if (data.currentPage < data.totalPages) {
    next.classList.add("pagination-link");
    next.addEventListener("click", () => fetchResults(data.currentPage + 1));
  } else {
    next.classList.add("disabled-link"); // ✅ dull when last page
  }
  paginationContainer.appendChild(next);
}

}

// ✅ Function to fetch with pagination
function fetchResults(page = 1) {
  const form = document.querySelector(".search-form");
  const searchTerm = form.querySelector('input[name="searchTerm"]').value;
  const componentPath = form.querySelector('input[name="componentPath"]').value;
  const pageSize = 2; // match servlet's limit, or make configurable
  const offset = (page - 1) * pageSize;

  const params = new URLSearchParams({
    searchTerm: searchTerm,
    componentPath: componentPath,
    limit: pageSize,
    offset: offset
  });

  fetch(`/bin/search?${params.toString()}`)
    .then(response => {
      if (!response.ok) throw new Error("Network response not OK");
      return response.json();
    })
    .then(data => renderResults(data))
    .catch(error => {
      document.getElementById("results").innerHTML = `<p>Error: ${error.message}</p>`;
    });
}

window.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector(".search-form");
  const input = form.querySelector(".search-input");
  const clearBtn = form.querySelector(".clear-input");

  input.addEventListener("input", () => {
    clearBtn.style.display = input.value ? "block" : "none";
  });

  clearBtn.addEventListener("click", () => {
    input.value = "";
    clearBtn.style.display = "none";
    input.focus();
  });

  form.addEventListener("submit", event => {
    event.preventDefault();
    fetchResults(1); // always start from page 1 on new search
  });
});
