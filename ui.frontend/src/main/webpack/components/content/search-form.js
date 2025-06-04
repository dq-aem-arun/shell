document.addEventListener('DOMContentLoaded', function () {

  const dropdownMenu = document.getElementById('dropdownMenu');
  let currentPage = 1;
  const pageSize = 25;

  // Toggle dropdown and fetch tags
  window.toggleDropdown = function () {
    dropdownMenu.classList.toggle('show');
  
    // Save currently selected tags
    const previouslySelected = new Set();
    const checkboxes = dropdownMenu.querySelectorAll('input[type="checkbox"]:checked');
    checkboxes.forEach(cb => previouslySelected.add(cb.value));
  
    dropdownMenu.innerHTML = ''; // Clears the checkboxes
  
    fetch("/bin/tags/list")
      .then(response => response.json())
      .then(data => {
        const tagsArray = data.map(tag => tag.title);
  
        tagsArray.forEach((tag, index) => {
          const item = document.createElement('div');
          item.className = 'dropdown-item';
  
          // If tag was previously selected, keep it checked
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
        resultDiv.innerHTML = ''; // Clear previous content
      
        // Create and insert result count paragraph
        const resultCountPara = document.createElement('p');
        resultCountPara.textContent = `${filtered.length} result${filtered.length !== 1 ? 's' : ''} for selected filters`;
        resultCountPara.style.fontWeight = 'bold';
        resultCountPara.style.marginBottom = '10px';
        resultDiv.appendChild(resultCountPara); // Append above results
      
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
      
          // Pagination controls
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
