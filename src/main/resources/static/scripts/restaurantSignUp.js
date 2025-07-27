document.addEventListener("DOMContentLoaded", function() {
    let itemIndex = 1;

    document.getElementById("addMenuItem").addEventListener("click", function() {
        const menuItemsContainer = document.getElementById("menuItems");
        const newItem = document.createElement("div");
        newItem.classList.add("menu-item");
        newItem.innerHTML = `
            <div class="form-group">
                <label for="itemName${itemIndex}">Item Name</label>
                <input type="text" id="itemName${itemIndex}" name="restaurantItems[${itemIndex}].item.name" required>
            </div>
            <div class="form-group">
                <label for="itemCategory${itemIndex}">Category</label>
                <input type="text" id="itemCategory${itemIndex}" name="restaurantItems[${itemIndex}].item.category.name" required>
            </div>
            <div class="form-group">
                <label for="itemCost${itemIndex}">Cost</label>
                <input type="number" id="itemCost${itemIndex}" name="restaurantItems[${itemIndex}].cost" required>
            </div>
            <div class="form-group">
                <label for="itemQuantity${itemIndex}">Quantity</label>
                <input type="number" id="itemQuantity${itemIndex}" name="restaurantItems[${itemIndex}].quantity" required>
            </div>
			<div class="form-group">
                <label for="itemImage${itemIndex}">ItemImage</label>
                <input type="file" class="itemImages" name="itemImages[${itemIndex}]" required>
            </div>
            <button type="button" class="remove-menu-item">Remove Item</button>
        `;
        menuItemsContainer.appendChild(newItem);
        itemIndex++;
    });

    document.getElementById("menuItems").addEventListener("click", function(event) {
        if (event.target.classList.contains("remove-menu-item")) {
            event.target.parentElement.remove();
        }
    });

    document.getElementById('restaurantForm').addEventListener('submit', registerRestaurant);
});

async function registerRestaurant(event) {
    event.preventDefault(); // Prevent the default form submission

    const form = document.getElementById("restaurantForm");
    const formData = new FormData(form);

    console.log(formData);
	
	let imageInputs = document.getElementsByClassName('itemImages');

	// Convert the HTMLCollection to an array
	let imageInputArray = Array.from(imageInputs); // File input elements

	imageInputArray.forEach((input, index) => {
	    // Append each file with an index
	    formData.append(`itemImages[]`, input.files[0]); // input.files[0] gets the actual file
	});
	
	for (let [key, value] of formData.entries()) {
        console.log(key, value);
    }

    try {
        const response = await fetch("/register/restaurant", {
            method: "POST",
            body: formData,
        });

        if (response.ok) {
            const message = await response.text();
            alert(message); // Display success message

            // Redirect to index page
            window.location.href = "/"; // Adjust URL as needed
        } else if (response.status === 409) {
            const error = await response.text();
            alert(`Error: ${error}`); // Display conflict message
        } else {
            const error = await response.text();
            alert(`Error: ${error}`); // Display general failure message
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred while processing the registration. Please try again later.");
    }
}