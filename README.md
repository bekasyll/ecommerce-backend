# 🛒 eCommerce Backend

RESTful backend-приложение для интернет-магазина на **Spring Boot** с поддержкой:
- 🔐 JWT-аутентификации и авторизации
- 👥 Регистрации пользователей и подтверждения email
- 🛍️ Управления корзиной
- 📦 Управления заказами (для пользователей и админов)
- 💬 Комментариев к товарам
- 📸 Загрузки и хранения изображений для товаров
- 📧 Email-уведомлений (подтверждение email и подтверждение заказа)

---

## 🚀 Технологии
- **Java 17+**
- **Spring Boot 3 (Web, Security, Data JPA, Validation)**
- **JWT (Java JWT by Auth0)**
- **Hibernate / JPA**
- **PostgreSQL**
- **MapStruct** (DTO ↔ Entity маппинг)
- **Lombok**
- **Spring Mail**
- **Maven**

---

## ⚙️ Запуск проекта

### 1. Клонировать репозиторий
```bash
git clone https://github.com/bekasyll/ecommerce-backend.git
cd eCommerceApp
```

### 2. Настроить `application.properties`
Пример:
```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

jwt.secret=your-secret-key
jwt.issuer=issuer-name

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 3. Запустить приложение
```bash
./mvnw spring-boot:run
```

---

## 📚 API эндпоинты

### 🔑 Аутентификация
- `POST /api/auth/registration` – регистрация нового пользователя
- `POST /api/auth/login` – вход (возвращает JWT)
- `POST /api/auth/change-password` – смена пароля (требует аутентификации)
- `POST /api/auth/confirm-email` – подтверждение email

### 🛒 Корзина
- `GET /api/cart` – получить корзину текущего пользователя
- `POST /api/cart/add?productId={id}&quantity={q}` – добавить товар
- `DELETE /api/cart` – очистить корзину
- `DELETE /api/cart/{productId}` – удалить товар из корзины

### 📦 Заказы
- `GET /api/orders/user` – заказы пользователя
- `POST /api/orders` – создать заказ (только с подтверждённым email)
- `GET /api/orders` – список всех заказов (**только ADMIN**)
- `PUT /api/orders/{id}/status?status=DELIVERED` – обновить статус заказа (**ADMIN**)

### 💬 Комментарии
- `GET /api/comments/product/{id}` – комментарии к товару
- `POST /api/comments/product/{id}` – добавить комментарий (требует авторизации)

### 🛍️ Продукты
- `GET /api/products` – список товаров
- `GET /api/products/{id}` – товар по id
- `POST /api/products` – добавить продукт (**ADMIN**, multipart/form-data)
- `PUT /api/products/{id}` – обновить продукт (**ADMIN**, multipart/form-data)
- `DELETE /api/products/{id}` – удалить продукт (**ADMIN**)

---

## 🔐 Безопасность
- Авторизация по **JWT** (`Authorization: Bearer <token>`)
- Разграничение доступа:  
  - Обычные пользователи (**ROLE_USER**)  
  - Администраторы (**ROLE_ADMIN**)  
- Stateless-сессии
