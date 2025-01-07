USE online_shopping_app;

INSERT INTO user (email, password, role, username)
VALUES ('admin@gmail.com', '$2a$10$0yM/hZ/xpRnY/giVx61tIeCpG99nqJI5lFSvm94zJQ2KSuaoODeOO', 2, 'admin');

INSERT INTO product (description, name, quantity, retail_price, wholesale_price) 
VALUES 
    ('High-quality smartphone', 'Smartphone X', 50, 699.99, 500.00),
    ('Noise-canceling headphones', 'Headphones Y', 30, 199.99, 120.00),
    ('Smart fitness watch', 'Fitness Watch Z', 100, 149.99, 90.00),
    ('Portable Bluetooth speaker', 'Speaker W', 75, 89.99, 60.00),
    ('Wireless charging pad', 'Charging Pad V', 200, 29.99, 15.00);
   
-- Insert 4 orders for each user (user_id: 2, 3, 4, 5)
INSERT INTO `order` (date_placed, order_status, user_id) VALUES 
(NOW(), 'Processing', 2),
(NOW(), 'Canceled', 2),
(NOW(), 'Completed', 2),
(NOW(), 'Completed', 2),
(NOW(), 'Processing', 3),
(NOW(), 'Canceled', 3),
(NOW(), 'Completed', 3),
(NOW(), 'Completed', 3),
(NOW(), 'Processing', 4),
(NOW(), 'Canceled', 4),
(NOW(), 'Completed', 4),
(NOW(), 'Completed', 4),
(NOW(), 'Processing', 5),
(NOW(), 'Canceled', 5),
(NOW(), 'Completed', 5),
(NOW(), 'Completed', 5);

-- Insert order items for the orders
-- Assuming the IDs of the orders just created start from 1 and increment sequentially

-- Order items for orders of user_id 2
INSERT INTO order_item (purchased_price, quantity, wholesale_price, order_id, product_id) VALUES 
(699.99, 1, 500.00, 1, 1),
(199.99, 2, 120.00, 1, 2),
(149.99, 1, 90.00, 2, 3),
(89.99, 1, 60.00, 2, 4),
(29.99, 3, 15.00, 3, 5),
(699.99, 1, 500.00, 4, 1);

-- Order items for orders of user_id 3
INSERT INTO order_item (purchased_price, quantity, wholesale_price, order_id, product_id) VALUES 
(199.99, 1, 120.00, 5, 2),
(149.99, 2, 90.00, 5, 3),
(89.99, 1, 60.00, 6, 4),
(29.99, 2, 15.00, 6, 5),
(699.99, 1, 500.00, 7, 1),
(199.99, 1, 120.00, 8, 2);

-- Order items for orders of user_id 4
INSERT INTO order_item (purchased_price, quantity, wholesale_price, order_id, product_id) VALUES 
(149.99, 1, 90.00, 9, 3),
(89.99, 2, 60.00, 9, 4),
(29.99, 1, 15.00, 10, 5),
(699.99, 1, 500.00, 10, 1),
(199.99, 2, 120.00, 11, 2),
(149.99, 1, 90.00, 12, 3);

-- Order items for orders of user_id 5
INSERT INTO order_item (purchased_price, quantity, wholesale_price, order_id, product_id) VALUES 
(89.99, 1, 60.00, 13, 4),
(29.99, 2, 15.00, 13, 5),
(699.99, 1, 500.00, 14, 1),
(149.99, 1, 90.00, 14, 3),
(199.99, 1, 120.00, 15, 2),
(89.99, 2, 60.00, 16, 4);

