DROP DATABASE IF EXISTS online_shopping_app;
CREATE DATABASE online_shopping_app;
USE online_shopping_app;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role INT NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS permission;
CREATE TABLE permission (
    permission_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    value VARCHAR(255) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS product;
CREATE TABLE product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    retail_price DOUBLE NOT NULL,
    wholesale_price DOUBLE
);

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_placed DATETIME(6) NOT NULL,
    order_status VARCHAR(255) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchased_price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    wholesale_price DOUBLE,
    order_id BIGINT,
    product_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES `order`(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS watchlist;
CREATE TABLE watchlist (
    user_id BIGINT,
    product_id BIGINT,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);
