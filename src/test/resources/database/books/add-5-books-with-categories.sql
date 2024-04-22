INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Harry Potter', 'some author', 0000000001, 4.99, 'description', 'image', false),
       (2, 'Odyssey', 'some author', 0000000002, 10.99, 'description', 'image', false),
       (3, 'LOTR', 'some author', 0000000003, 14.99, 'description', 'image', false),
       (4, 'Hobbit', 'some author', 0000000004, 49.99, 'description', 'image', false),
       (5, 'Da Vinci Code', 'some author', 0000000005, 249.99, 'description', 'image', false);

INSERT INTO categories (id, name, description, is_deleted)
VALUES (1, 'Fantasy', 'Books that feature magic, mythical creatures, and imaginary worlds.', false),
       (2, 'Epic Poetry', 'Narratives that tell grand stories often involving heroes and gods.', false),
       (3, 'Adventure', 'Books filled with thrilling journeys and exciting escapades.', false),
       (4, 'Mystery', 'Stories that involve solving puzzles or uncovering secrets.', false),
       (5, 'Cooking', 'Books with recipes and tips for preparing delicious dishes.', false);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1), -- Harry Potter - Fantasy
       (1, 3), -- Harry Potter - Adventure
       (2, 2), -- Odyssey - Epic Poetry
       (3, 1), -- LOTR - Fantasy
       (3, 3), -- LOTR - Adventure
       (4, 1), -- Hobbit - Fantasy
       (4, 3), -- Hobbit - Adventure
       (5, 4); -- Da Vinci Code - Mystery
