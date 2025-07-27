function openModal(action, id = null) {
    const modal = document.getElementById('itemModal');
    const form = document.getElementById('itemForm');
	
	console.log("ID: " + id);
    
    if (action === 'update' && id) {
        // Get the item data directly from the DOM
        const itemElement = document.querySelector(`#item-${id}`);
		
		console.log(itemElement);
        
        if (itemElement) {
            const itemName = itemElement.querySelector('h3').innerText.trim();
            const category = itemElement.querySelector('.details .category').innerText.trim();
            const quantity = itemElement.querySelector('.details .quantity').innerText.trim();
            const price = itemElement.querySelector('.details .price').innerText.trim();
            
            // Populate the form fields
            document.getElementById('itemId').value = id;
            document.getElementById('name').value = itemName;
            document.getElementById('category').value = category;
            document.getElementById('quantity').value = quantity;
            document.getElementById('price').value = price;
        }
    } else {
        form.reset();  // Clear the form for 'add' action
    }
    
    modal.style.display = 'block';
}


// Function to close the modal
function closeModal() {
    const modal = document.getElementById('itemModal');
    modal.style.display = 'none';
}

// Event listener for form submission
document.getElementById('itemForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    const formData = {
        id: document.getElementById('itemId').value,
        name: document.getElementById('name').value,
        category: document.getElementById('category').value,
        quantity: document.getElementById('quantity').value,
        price: document.getElementById('price').value
    };

    fetch('/addOrUpdateMenuItem', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to save item');
        return response.json();
    })
    .then(data => {
        alert('Item saved successfully!');
        closeModal();   // Close the modal
        location.reload(); // Reload the page to reflect the changes
    })
    .catch(error => {
        alert(`Error: ${error.message}`);
    });
});

// Close modal when clicking outside of it
window.onclick = function(event) {
    const modal = document.getElementById('itemModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};