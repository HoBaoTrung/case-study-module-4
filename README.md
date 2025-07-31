
# 📱 Mobile Store Web Application

A feature-rich web application for a mobile phone store, built using **Spring MVC**, **Spring Security**, **Thymeleaf**, and **Hibernate/JPA**, with full CRUD functionality, product filtering, OAuth2 login, multilingual support, file upload support, session-based shopping cart, and AJAX-based UI interactions.

---

## 🛠 Technologies Used

* **Java 8+**
* **Spring MVC**
* **Spring Security**
* **Spring Data JPA**
* **Hibernate ORM**
* **Thymeleaf**
* **Bootstrap 5**
* **MySQL**
* **jQuery & AJAX**

---

## 🌟 Key Features

### ✅ Product Management

* Add, edit, delete, and view product details
* Product image upload using `multipart/form-data`
* Price formatting with localized currency display
* Enum fields replaced with lookup tables for flexibility

### 🔎 Product Filtering (AJAX)

* Search by name (case-insensitive)
* Filter by category, brand, and price range
* Dynamic pagination with real-time updates

### 🛒 Shopping Cart

* Add to cart using AJAX
* View cart and quantities in session
* Update or remove items
* Validate product availability before adding

### 👥 User Management

#### 🔐 Traditional Login

- Admin and Customer login with form-based authentication
- CSRF protection (custom-configured for file upload & AJAX)
- Field validation (server and client-side)

#### 📝 User Registration

- Register new users with form
- Validate for unique username and email
- Secure password encryption using BCrypt
- New users automatically assigned the `CUSTOMER` role

#### 🌐 OAuth2 Login (Google & Facebook)

- Sign in with Google or Facebook using OAuth2
- Auto-register social users if email does not exist
- OAuth2 users are assigned `CUSTOMER` role by default
- Social login buttons in the login form

### 🌍 Internationalization (i18n)

- Full support for:
  - 🇺🇸 English
  - 🇻🇳 Vietnamese
  - 🇯🇵 Japanese
- Change language via `?lang=en`, `?lang=vi`, `?lang=ja`
- Multi-language support in UI and validation messages
- Message files in `resources`

### 🛡️Role

* ADMIN 
  * username: admin
  * password: 12345
* CUSTOMER
  * username: user1
  * password: 12345
* Or login with Google/Facebook


---

## 📁 Project Structure

```
src/
└── main/
    ├── java/com/codegym/mobilestore/
    │   ├── component/           # Custom beans, utilities
    │   ├── configuration/       # Spring configuration (WebConfig, SecurityConfig, etc.)
    │   ├── controller/          # Web controllers (handle requests)
    │   ├── dto/                 # Data Transfer Objects
    │   ├── model/               # JPA entities
    │   ├── repository/          # Spring Data JPA interfaces
    │   ├── service/             # Business logic layer
    │   ├── specification/       # Custom search filters and queries
    │   └── HelloServlet.java    # Example Servlet (optional/test)
    │
    ├── resources/
    │   ├── static/              # Static files (CSS, JS, images)
    |   ├── messages_*.properties # i18n message files
    │   └── secret.properties    # Sensitive configurations (excluded from Git)
    │
    └── webapp/
        ├── index.jsp
        └── WEB-INF/
            └── views/
                ├── cart/        # Cart-related views
                ├── checkout/    # Checkout page
                ├── fragments/   # Common layout fragments
                └── product/     # Product list, details, add/edit pages

```

---

## 📷 Screenshots

> * Product listing with filters
![coccoc_screenshot_localhost.jpg](image/coccoc_screenshot_localhost.jpg)
> * Add product form
![coccoc_screenshot_localhost (1).jpg](image/coccoc_screenshot_localhost%20%281%29.jpg)
> * Cart session demo![Recording2025-07-15111044-ezgif.com-video-to-gif-converter.gif](image/Recording2025-07-15111044-ezgif.com-video-to-gif-converter.gif)
> * AJAX pagination in action![Recording2025-07-15111432-ezgif.com-video-to-gif-converter.gif](image/Recording2025-07-15111432-ezgif.com-video-to-gif-converter.gif)
---

## 🚀 How to Run

1. Clone the repository
2. Configure `secret.properties`
3. Run the application using your preferred IDE or Gradle
4. Insert data and procedure (with sql files)
5. Access the app at `http://localhost:8080/mobilestore`

---

## 📌 TODOs / Next Steps
* Admin dashboard
* PDF/Excel export
* Voucher/discount integration
