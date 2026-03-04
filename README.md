# 🛒 CommerceFlow - E-Commerce Backend API

CommerceFlow is a production-ready Spring Boot backend application for an e-commerce platform.

It includes:

* User Authentication (JWT based)
* Product Management
* Cart Management
* Order Processing
* Dockerized Deployment Setup

---

# 🚀 Tech Stack

* Java 17
* Spring Boot
* Spring Security (JWT Authentication)
* Spring Data JPA
* MySQL
* Docker & Docker Compose
* Maven

---

# 🏗 Architecture Overview

Client → Spring Boot API → MySQL Database
↓
JWT Security

---

# 🔐 Authentication Module

## 🔹 Register User

**POST** `/api/auth/register`

### Request

```json
{
  "name": "Gowri",
  "email": "gowri@example.com",
  "password": "password123",
  "role": "USER"
}
```

### Response

```json
{
  "message": "User registered successfully"
}
```

---

## 🔹 Login

**POST** `/api/auth/login`

### Request

```json
{
  "email": "gowri@example.com",
  "password": "password123"
}
```

### Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

# 📦 Product Module

## 🔹 Create Product (Admin)

**POST** `/api/products`

### Request

```json
{
  "name": "iPhone 15",
  "description": "Latest Apple smartphone",
  "price": 80000,
  "stock": 10
}
```

### Response

```json
{
  "id": 1,
  "name": "iPhone 15",
  "price": 80000,
  "stock": 10,
  "createdAt": "2026-03-03T10:30:00"
}
```

---

## 🔹 Get All Products

**GET** `/api/products`

### Response

```json
[
  {
    "id": 1,
    "name": "iPhone 15",
    "price": 80000,
    "stock": 10
  }
]
```

---

# 🛒 Cart Module

## 🔹 Add Item to Cart

**POST** `/api/cart/add`

### Request

```json
{
  "productId": 1,
  "quantity": 2
}
```

### Response

```json
{
  "message": "Product added to cart"
}
```

---

## 🔹 Get Cart

**GET** `/api/cart`

### Response

```json
{
  "items": [
    {
      "productId": 1,
      "productName": "iPhone 15",
      "quantity": 2,
      "price": 80000,
      "total": 160000
    }
  ],
  "totalAmount": 160000
}
```

---

## 🔹 Remove Item

**DELETE** `/api/cart/remove/{productId}`

### Response

```json
{
  "message": "Item removed successfully"
}
```

---

# 📦 Order Module

## 🔹 Checkout

**POST** `/api/orders/checkout`

### Response

```json
{
  "orderId": 1001,
  "status": "PLACED",
  "totalAmount": 160000,
  "createdAt": "2026-03-03T11:00:00"
}
```

---

## 🔹 Cancel Order

**POST** `/api/orders/cancel/{orderId}`

### Response

```json
{
  "message": "Order cancelled successfully"
}
```

---

# 🐳 Docker Setup

## Build JAR

```
mvn clean package
```

## Run with Docker Compose

```
docker-compose up --build
```

Application runs on:

```
http://localhost:8080
```

---

# 🔒 Security

* JWT-based authentication
* Role-based access (USER / ADMIN)
* Protected endpoints

---

# 📈 Features Implemented

* Soft delete for products
* Search & filtering
* Role-based authorization
* Transactional order processing
* Cart total calculation
* Proper entity relationships

---

# 🧠 Future Improvements

* Redis caching
* Payment gateway integration
* CI/CD pipeline
* AWS deployment
* Kubernetes orchestration

---

# 👨‍💻 Author

Gowri Shankar
Backend Developer | Spring Boot | Docker | AWS
