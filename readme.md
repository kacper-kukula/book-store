<p align="center">
<img src="https://i.imgur.com/kKAJrLA_d.webp?maxwidth=660&fidelity=grand" alt="Book Store Logo"/>
</p>

[Unique Functionalities](#unique-functionalities) • [Database Structure](#database-structure) • [How To Use](#getting-started) • [Video Example](#loom-video) • [Obstacles and Strategies](#obstacles-and-strategies) • [Conclusion](#conclusion)


# Bookstore Management System

Welcome to the Bookstore Management System! This project aims to provide a seamless experience for managing a bookstore, from user authentication to order processing. Whether you're an administrator adding new books or a customer browsing through categories, this system has got you covered.

## Inspiration

The idea for this project stemmed from the need for a modern and efficient solution to manage a bookstore's operations. By leveraging the power of Java Spring Boot and related technologies, I aimed to create a robust and user-friendly system that simplifies book management tasks.

## Technologies and Tools

<p align="center">
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" title="Maven"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207493-49665200-adf4-11eb-808e-a9c0fcc2a0a0.png" alt="Hibernate" title="Hibernate"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192107858-fe19f043-c502-4009-8c47-476fc89718ad.png" alt="REST" title="REST"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117533873-484d4480-afef-11eb-9fad-67c8605e3592.png" alt="JUnit" title="JUnit"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183892181-ad32b69e-3603-418c-b8e7-99e976c2a784.png" alt="mockito" title="mockito"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183891673-32824908-bc5d-44f8-8f72-f0415822404a.png" alt="Liquibase" title="Liquibase"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183896128-ec99105a-ec1a-4d85-b08b-1aa1620b2046.png" alt="MySQL" title="MySQL"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png" alt="Docker" title="Docker"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/186711335-a3729606-5a78-4496-9a36-06efcc74f800.png" alt="Swagger" title="Swagger"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192108372-f71d70ac-7ae6-4c0d-8395-51d8870c2ef0.png" alt="Git" title="Git"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192108890-200809d1-439c-4e23-90d3-b090cf9a4eea.png" alt="IntelliJ" title="IntelliJ"/></code>
</p>
<br />

- **As well as**: JWT, Jackson, Lombok, MapStruct, Spring Security, Spring Data JPA

## Unique Functionalities

### Authentication Controller

- **<span style="color:green">[public] </span>```POST /auth/registration```**: Allows new users to register securely.
- **<span style="color:green">[public] </span>```POST /auth/login```**: Enables existing users to authenticate securely using JWT.

### Book Controller

- **<span style="color:orange">[user] </span>```GET /books```**: Retrieve all available books.
- **<span style="color:orange">[user] </span>```GET /books/{id}```**: Retrieve a book by its ID.
- **<span style="color:red">[admin] </span>```POST /books```**: Create a new book.
- **<span style="color:red">[admin] </span>```DELETE /books/{id}```**: Soft delete a book by its ID.
- **<span style="color:red">[admin] </span>```PUT /books/{id}```**: Update a book by its ID.
- **<span style="color:orange">[user] </span>```GET /books/search```**: Search books by criteria.

### Category Controller

- **<span style="color:red">[admin] </span>```POST /categories```**: Create a new category.
- **<span style="color:orange">[user] </span>```GET /categories```**: Retrieve all available categories.
- **<span style="color:orange">[user] </span>```GET /categories/{id}```**: Retrieve a category by its ID.
- **<span style="color:red">[admin] </span>```PUT /categories/{id}```**: Update a category by its ID.
- **<span style="color:red">[admin] </span>```DELETE /categories/{id}```**: Soft delete a category by its ID.
- **<span style="color:orange">[user] </span>```GET /categories/{id}/books```**: Find all books belonging to a specific category.

### Order Controller

- **<span style="color:orange">[user] </span>```POST /orders```**: Place an order.
- **<span style="color:orange">[user] </span>```GET /orders```**: Retrieve order history.
- **<span style="color:red">[admin] </span>```PATCH /orders/{orderId}```**: Update order status.
- **<span style="color:orange">[user] </span>```GET /orders/{orderId}/items```**: Retrieve items from an order.
- **<span style="color:orange">[user] </span>```GET /orders/{orderId}/items/{itemId}```**: Retrieve a single item from an order.

### Shopping Cart Controller

- **<span style="color:orange">[user] </span>```GET /cart```**: Retrieve the current shopping cart.
- **<span style="color:orange">[user] </span>```POST /cart```**: Add items to the shopping cart.
- **<span style="color:orange">[user] </span>```PUT /cart/cart-items/{id}```**: Update the shopping cart.
- **<span style="color:orange">[user] </span>```DELETE /cart/cart-items/{id}```**: Delete an item from the shopping cart.

## Database Structure

I've included a diagram illustrating the structure of the MySQL database which can be found below.
<p align="center">
<img src="https://i.imgur.com/q07pBhQ_d.webp?maxwidth=760&fidelity=grand" alt="Database Diagram"/>
</p>

## Getting Started

1. Make sure to install [IDE](https://www.jetbrains.com/idea/), [Maven](https://maven.apache.org/download.cgi), [Docker](https://www.docker.com/products/docker-desktop/), [JDK 17+](https://www.oracle.com/pl/java/technologies/downloads/)
2. Clone the repository.
3. Configure the .env file with your database credentials and ports. Example:
```
MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=root
MYSQLDB_DATABASE=book_store

MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```
4. Ensure Docker Desktop is running.
5. Build and run the application using Docker: `docker-compose up --build`
6. Access the API documentation at Swagger UI: `http://localhost:[PORT]/swagger-ui.html`

You can now access the endpoints using `Swagger` or `Postman`. To access the functionality, you must first register, and you will be granted `User` role. To access admin endpoints (covers `user` endpoints as well), feel free to use already pre-defined credentials:
```
{
    "email": "admin@book-store.com",
    "password": "safePassword"
}
```
After logging in, you receive a `Bearer Token` which you must then provide as authorization to access the endpoints.

## Loom Video

To get a comprehensive overview of the project and its functionalities, watch my short Loom video [here](https://www.loom.com/share/0511e7427e014309a97ee3c01f0cbaaa?sid=48d30d17-3f21-44fc-8d73-1ae9c1ffb499).

## Obstacles and Strategies

Throughout the development process, I've encountered challenges such as optimizing database queries for performance and implementing secure authentication mechanisms. One particular challenge arose when I changed the fetch type from `eager` to `lazy` loading for certain relationships in my entities. This change led to `lazy initialization exception` when accessing these relationships outside the transactional boundaries.

To address this issue, I applied the `@Transactional` annotation to the relevant methods, ensuring that the necessary data is fetched within the same transaction context. This approach resolved the lazy initialization exceptions and maintained the integrity of the data retrieval process. Additionally, I leveraged Spring Boot's built-in features and sought guidance from the community to refine my solution and enhance the overall performance of the application.

## Conclusion

Thank you for taking the time to explore my Bookstore Management System! As my first Java project, this journey has been both challenging and rewarding. Through overcoming obstacles, I've gained valuable experience and insights that will undoubtedly benefit future projects.

While I'm proud of the system's current functionality, I recognize that there is always room for improvement. In particular, I see potential for implementing the payment processing system to provide a seamless checkout experience for users.

I hope you find the Bookstore Management System useful and intuitive to use. If you have any questions, feedback, or ideas for improvement, please don't hesitate to reach out. Your input is valuable as I continue to learn and grow as a developer.
