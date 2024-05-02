INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Harry Potter', 'some author', 123, 4.99, 'description', 'image', false),
       (2, 'Odyssey', 'some author', 1234, 10.99, 'description', 'image', false);

INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'admin@book-store.com', 'password', 'user', 'user', 'address', 0);

INSERT INTO roles (id, name)
VALUES (1, 'USER');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);

INSERT INTO shopping_carts (id, user_id, is_deleted)
VALUES (1, 1, 0);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 5);
