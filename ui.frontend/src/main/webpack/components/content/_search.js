// search-results.js

function renderResults(results) {
  const container = document.getElementById("results");
  container.innerHTML = ""; // clear previous results

  if (!results || results.length === 0) {
    container.innerHTML = "<p>No results found.</p>";
    return;
  }

  results.forEach(item => {
    const div = document.createElement("div");
    div.className = "search-result-item"; // Add this line
    div.style.marginBottom = "20px";

    // Build breadcrumb HTML from array
    let breadcrumbHtml = "";
    if (Array.isArray(item.breadcrumb)) {
      breadcrumbHtml = item.breadcrumb.map((crumb, index) => {
        const link = `<a href="${crumb.path}.html">${crumb.title}</a>`;
        return index < item.breadcrumb.length - 1 ? link + " &gt; " : link;
      }).join("");
    } else {
      breadcrumbHtml = item.breadcrumb; // fallback if breadcrumb is plain string
    }

    div.innerHTML = `
      <h3><a href="${item.path}.html">${item.title}</a></h3>
      <p>${breadcrumbHtml}</p>
      <p>${item.excerpt}</p>
      <hr />
    `;
    container.appendChild(div);
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
    event.preventDefault(); // prevent normal submit

    const searchTerm = form.querySelector('input[name="searchTerm"]').value;
    const componentPath = form.querySelector('input[name="componentPath"]').value;

    const params = new URLSearchParams({
      searchTerm: searchTerm,
      componentPath: componentPath
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
  });
});
