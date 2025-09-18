# 🍴 FoodieExpress

**FoodieExpress** is an online food delivery web application that connects customers with multiple restaurants.  
The application allows users to browse restaurants, order food, manage carts, make payments, and track their orders — all in one place.

---

## 🚀 Features
- 👤 **User Management**: Customer registration & login
- 🍔 **Restaurant Management**: Multiple restaurants can register and manage their menus
- 📝 **Menu & Items**: Browse and order food items from restaurant menus
- 🛒 **Cart & Orders**: Add items to cart, place orders, view order history
- 💳 **Payments**: Secure checkout (Stripe integration planned)
- 🔐 **Authentication & Authorization**: JWT-based security with roles:
  - **Customer**: Can browse & order
  - **Restaurant**: Can manage menu
  - **Admin**: Can manage users & restaurants
- 📊 **Dashboard (Future)**: Analytics for restaurants and admins

---

## 🏗️ Tech Stack
### Backend
- Java 17+
- Spring Boot
- Spring Data JPA / Hibernate
- Spring Security + JWT
- MySQL
- Stripe API (planned)

### Frontend (planned)
- React.js
- HTML, CSS, JavaScript
- Tailwind CSS / Bootstrap

---

## ⚙️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/PrudhviKandipappu/FoodieExpress.git
cd FoodieExpress
