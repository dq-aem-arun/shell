document.addEventListener('DOMContentLoaded', function () {

  const dropdownMenu = document.getElementById('dropdownMenu');
  let currentPage = 1;
  const pageSize = 25;
  let isFirstLoad = true; // ✅ Track initial load

  window.toggleDropdown = function () {
    dropdownMenu.classList.toggle('show');

    // Save previously selected tags
    const previouslySelected = new Set();
    const checkboxes = dropdownMenu.querySelectorAll('input[type="checkbox"]:checked');
    checkboxes.forEach(cb => previouslySelected.add(cb.value));

    dropdownMenu.innerHTML = '';

    fetch("/bin/tags/list")
      .then(response => response.json())
      .then(data => {
        const tagsArray = data.map(tag => tag.title);

        tagsArray.forEach((tag, index) => {
          const item = document.createElement('div');
          item.className = 'dropdown-item';

          const isChecked = previouslySelected.has(tag) ? 'checked' : '';

          item.innerHTML = `
            <input type="checkbox" id="tag${index}" name="tags" value="${tag}" ${isChecked}>
            <label for="tag${index}">${tag}</label>
          `;
          dropdownMenu.appendChild(item);
        });
      })
      .catch(error => {
        console.error("Error fetching tags:", error);
        dropdownMenu.innerHTML = '<p style="color:red;">Failed to load tags.</p>';
      });
  };

  window.onclick = function (event) {
    const button = document.querySelector('.dropdown-button');
    if (!dropdownMenu.contains(event.target) && !button.contains(event.target)) {
      dropdownMenu.classList.remove('show');
    }
  };

  function fetchFilteredArticles() {
    const selectedTags = [];
    const checkboxes = document.querySelectorAll('input[name="tags"]:checked');
    checkboxes.forEach(cb => selectedTags.push(cb.value));

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    const isInitialLoad = selectedTags.length === 0 && !startDate && !endDate;

    if (!isInitialLoad && ((startDate && !endDate) || (!startDate && endDate))) {
      alert('Please select both Start Date and End Date.');
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
        resultDiv.innerHTML = '';

        // ✅ Show result count only after first load
        if (!isFirstLoad) {
          const resultCountPara = document.createElement('p');
          resultCountPara.textContent = `${filtered.length} result${filtered.length !== 1 ? 's' : ''} for selected filters`;
          resultCountPara.style.fontWeight = 'bold';
          resultCountPara.style.marginBottom = '10px';
          resultDiv.appendChild(resultCountPara);
        }

        if (filtered.length > 0) {
          const ul = document.createElement('ul');
          filtered.forEach(p => {
            const li = document.createElement('li');
            li.style.marginBottom = '10px';
            li.innerHTML = `
              <a href="${p.url}.html" target="_blank" style="font-weight: bold; color: #007bff;">${p.title}</a><br/>
              <span style="font-size: 0.9em; color: #555;">${p.url}</span>`;
            ul.appendChild(li);
          });
          resultDiv.appendChild(ul);

          const paginationDiv = document.createElement('div');
          paginationDiv.style.marginTop = '10px';
          paginationDiv.innerHTML = `
            <button id="prevPage" ${currentPage === 1 ? 'disabled' : ''}>Previous</button>
            <button id="nextPage" ${filtered.length < pageSize ? 'disabled' : ''}>Next</button>`;
          resultDiv.appendChild(paginationDiv);

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

        } else {
          const noResults = document.createElement('p');
          noResults.textContent = 'No pages found for selected filters.';
          resultDiv.appendChild(noResults);
        }

        isFirstLoad = false; // ✅ Turn off initial load after first fetch
      })
      .catch(error => {
        console.error('Error:', error);
        document.getElementById('results').innerHTML = '<p style="color:red;">An error occurred while fetching pages.</p>';
      });
  }

  document.getElementById('filterForm').addEventListener('submit', function (event) {
    event.preventDefault();
    currentPage = 1;
    isFirstLoad = false; // ✅ Ensure filter count shows after search
    fetchFilteredArticles();
  });

  // 🔄 Initial fetch when page loads — shows all articles
  fetchFilteredArticles();

});
