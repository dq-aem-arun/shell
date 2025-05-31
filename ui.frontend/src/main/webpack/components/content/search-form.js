document.addEventListener('DOMContentLoaded', function () {

  const dropdownMenu = document.getElementById('dropdownMenu');

  // Dropdown toggle and fetch tags dynamically
  window.toggleDropdown = function () {
    dropdownMenu.classList.toggle('show');
    // Clear existing dropdown content
    dropdownMenu.innerHTML = '';

    fetch("/bin/tags/list", {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    })
    .then(response => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then(data => {
      const tagsArray = data.map(tag => tag.title); // Extract tag titles
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

  // Form submission to fetch filtered articles
  document.getElementById('filterForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const selectedTags = [];
    const checkboxes = document.querySelectorAll('input[name="tags"]:checked');
    for (let i = 0; i < checkboxes.length; i++) {
      selectedTags.push(checkboxes[i].value);
    }

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (selectedTags.length === 0 && (!startDate || !endDate )) {
      alert('Please select at least one tag or enter both start and end dates.');
      return;
    }

    const formData = {
      tags: selectedTags,
      startDate: startDate,
      endDate: endDate
    };
      
    fetch("/bin/filter-articles", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(filtered => {
      const resultDiv = document.getElementById('results');
      if (filtered.length > 0) {
        resultDiv.innerHTML = '<strong>Filtered Pages:</strong><ul>' +
          filtered.map(p => `<li>${p.title} (${p.date})</li>`).join('') +
          '</ul>';
      } else {
        resultDiv.innerHTML = '<p>No pages found for selected filters.</p>';
      }
    })
    .catch(error => {
      console.error('Error:', error);
      document.getElementById('results').innerHTML = '<p style="color:red;">An error occurred while fetching pages.</p>';
    });
  });
});
