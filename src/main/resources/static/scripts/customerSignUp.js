document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");

  form.addEventListener("submit", async (event) => {
    event.preventDefault(); // Prevent the default form submission

    // Collect form data
    const customerData = {
      firstName: document.getElementById("firstName").value,
      lastname: document.getElementById("lastName").value,
      age: parseInt(document.getElementById("age").value, 10),
      gender: document.getElementById("gender").value,
      email: document.getElementById("email").value,
      mobileNumber: document.getElementById("mobileNumber").value,
      address: {
        buildingName: document.getElementById("buildingName").value,
        streetNo: document.getElementById("streetNo").value,
        area: document.getElementById("area").value,
        city: document.getElementById("city").value,
        state: document.getElementById("state").value,
        country: document.getElementById("country").value,
        pinCode: document.getElementById("pincode").value,
      },
      password: document.getElementById("password").value,
    };

    try {
      // Make the POST request
      const response = await fetch("/register/customer", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(customerData),
      });

      if (response.ok) {
        const message = await response.text();
		window.location.href="/";
        alert(message); // Display success message
      } else {
        const message = await response.text();
        alert(`Error: ${message}`); // Display error message
      }
    } catch (error) {
      console.error("An error occurred:", error);
      alert("An unexpected error occurred. Please try again.");
    }
  });
});
