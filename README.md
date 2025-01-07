# Shopping App

## Overview
This repository contains a **Shopping Application** that supports both frontend and backend implementations. The application provides e-commerce features for two types of users—**Buyers** and **Sellers**—with secure authentication and authorization mechanisms. 

## Features
### User (Buyer)
1. **Registration & Login**
   - Register with a unique username, email, and encrypted password.
   - Login with authentication and authorization using JWT.
2. **Shopping**
   - View all available products.
   - View product details without seeing stock quantity.
   - Add/remove products to/from a watchlist.
3. **Orders**
   - Place orders with specified quantities, deducting stock automatically.
   - View order details, including status (Processing, Completed, Canceled).
   - Cancel orders, which restores stock if applicable.
4. **Analytics**
   - View top 3 most frequently purchased items.
   - View top 3 most recently purchased items.

### Admin (Seller)
1. **Product Management**
   - Add, view, and update product details (description, prices, stock).
2. **Order Management**
   - View and update order statuses.
3. **Analytics**
   - View the most profitable product.
   - Track the top 3 most sold products.
   - Calculate the total number of successfully sold items.

### Security
- Implements **Spring Security** and **JWT** for authentication and authorization.
- Users cannot access unauthorized resources.

### Exception Handling
- Custom exceptions are implemented and handled using **Spring AOP**.
- Provides meaningful error messages for debugging.

## Tech Stack
### Backend
- Spring Boot
- Hibernate (HQL, Criteria)
- MySQL
- Spring Security + JWT
- Spring AOP
- Spring Validation
- Spring Cache

### Frontend
- Angular

### Tools
- Postman (API Testing)
- GitHub (Version Control)

## Architecture
- Follows layered architecture:
  - **Controller**: REST endpoints
  - **Service**: Business logic
  - **Repository**: Data access layer

## Database Design
- MySQL database with normalized tables.
- Prevents updates to prices affecting past orders.

## Setup Instructions
1. **Clone the repository:**
```bash
  git clone <repository-url>
```
2. **Backend Setup:**
   - Navigate to the backend directory.
   - Configure `application.properties` with database connection details.
   - Build and run the Spring Boot application.
3. **Frontend Setup:**
   - Navigate to the frontend directory.
   - Install dependencies and start the Angular app.
```bash
  npm install
  ng serve
```
4. **Access the Application:**
   - Open the browser and navigate to `http://localhost:4200`.

## Additional Notes
- All endpoints are protected, requiring authentication except registration and login.
- Error handling and validations are implemented for robustness.
- Demonstrations can be performed using Postman to test API endpoints.

## Contributor
- Jiaqi Chen

