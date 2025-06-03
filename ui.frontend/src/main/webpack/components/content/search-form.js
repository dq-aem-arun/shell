document.addEventListener('DOMContentLoaded', function () {

  const dropdownMenu = document.getElementById('dropdownMenu');
  let currentPage = 1;
  const pageSize = 25;

  // Toggle dropdown and fetch tags
  window.toggleDropdown = function () {
    dropdownMenu.classList.toggle('show');
    dropdownMenu.innerHTML = '';

    fetch("/bin/tags/list", {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    })
      .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
      })
      .then(data => {
        const tagsArray = data.map(tag => tag.title);
        tagsArray.forEach((tag, index) => {
          const item = document.createElement('div');
          item.className = 'dropdown-item';
          item.innerHTML = `<input type="checkbox" id="tag${index}" name="tags" value="${tag}">
                            <label for="tag${index}">${tag}</label>`;
          dropdownMenu.appendChild(item);
        });
      })
      .catch(error => {
        console.error("Error fetching tags:", error);
        dropdownMenu.innerHTML = '<p style="color:red;">Failed to load tags.</p>';
      });
  };

  // Close dropdown on outside click
  window.onclick = function (event) {
    const button = document.querySelector('.dropdown-button');
    if (!dropdownMenu.contains(event.target) && !button.contains(event.target)) {
      dropdownMenu.classList.remove('show');
    }
  };

  
  // Collect form data and fetch filtered results
  function fetchFilteredArticles() {
    const selectedTags = [];
    const checkboxes = document.querySelectorAll('input[name="tags"]:checked');
    for (let i = 0; i < checkboxes.length; i++) {
      selectedTags.push(checkboxes[i].value);
    }

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (
      (startDate && !endDate) || (!startDate && endDate) ||
      (selectedTags.length === 0 && (!startDate || !endDate))
    ) {
      alert('Please select at least one tag or enter both Start Date and End Date.');
      return;
    }

    const formData = {};
    if (selectedTags.length > 0) formData.tags = selectedTags;
    if (startDate) formData.startDate = startDate;
    if (endDate) formData.endDate = endDate;

    const offset = (currentPage - 1) * pageSize;

    fetch(`/bin/filter-articles?limit=${pageSize}&offset=${offset}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData)
    })
      .then(response => response.json())
      .then(filtered => {
        const resultDiv = document.getElementById('results');
        var totalResults = filtered.length;
        if (filtered.length > 0) {
          resultDiv.innerHTML = '<strong>Filtered Pages:</strong><ul>' +
            filtered.map(p => `
              <li style="margin-bottom: 10px;">
                <a href="${p.url}.html" target="_blank" style="font-weight: bold; color: #007bff;">${p.title}</a><br/>
                <span style="font-size: 0.9em; color: #555;">${p.url}</span>
              </li>
            `).join('') +
            '</ul>' +
            `<div style="margin-top: 10px;">
              <button id="prevPage" ${currentPage === 1 ? 'disabled' : ''}>Previous</button>
              <button id="nextPage" ${filtered.length < pageSize ? 'disabled' : ''}>Next</button>
            </div>`;
        } else {
          resultDiv.innerHTML = '<p>No pages found for selected filters.</p>';
        }

        // Pagination events
        document.getElementById('prevPage')?.addEventListener('click', () => {
          if (currentPage > 1) {
            currentPage--;
            fetchFilteredArticles();
          }
        });

        document.getElementById('nextPage')?.addEventListener('click', () => {
          currentPage++;
          fetchFilteredArticles();
        });
      })
      .catch(error => {
        console.error('Error:', error);
        document.getElementById('results').innerHTML = '<p style="color:red;">An error occurred while fetching pages.</p>';
      });
  }

  // Submit handler
  document.getElementById('filterForm').addEventListener('submit', function (event) {
    event.preventDefault();
    currentPage = 1; // Reset to first page on new filter
    fetchFilteredArticles();
  });

});
