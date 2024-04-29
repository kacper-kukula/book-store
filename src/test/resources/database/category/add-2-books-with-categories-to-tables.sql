INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Harry Potter', 'some author', 0000000001, 4.99, 'description', 'image', false),
       (2, 'Odyssey', 'some author', 0000000002, 10.99, 'description', 'image', false);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1), -- Harry Potter - Fantasy
       (1, 3), -- Harry Potter - Adventure
       (2, 1); -- Odyssey - Fantasy
