
    function fetchDetails() {
        const id = document.getElementById("inputId").value;

        alert("hiii")

        fetch("/bin/fetch-details", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "id=" + encodeURIComponent(id)
        })
        .then(response => response.json())
        .then(data => {
            document.getElementById("resultArea").textContent = JSON.stringify(data, null, 2);
        })
        .catch(error => {
            document.getElementById("resultArea").textContent = "Error: " + error;
        });
    }
