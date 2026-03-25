-- Table Users
CREATE TABLE IF NOT EXISTS Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Products
CREATE TABLE IF NOT EXISTS Products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    description TEXT
);

-- Table Orders
CREATE TABLE IF NOT EXISTS Orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Table Order_Details
CREATE TABLE IF NOT EXISTS Order_Details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

-- Stored Procedure: Get Top 5 Buyers
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS SP_GetTopBuyers()
BEGIN
    SELECT u.username, SUM(o.total_amount) as total_spent
    FROM Users u
    JOIN Orders o ON u.id = o.user_id
    GROUP BY u.id, u.username
    ORDER BY total_spent DESC
    LIMIT 5;
END //
DELIMITER ;

-- Function: Calculate Category Revenue (Simplified as Product Revenue for this schema)
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS SP_GetProductRevenue()
BEGIN
    SELECT p.name, SUM(od.quantity * od.unit_price) as revenue
    FROM Products p
    JOIN Order_Details od ON p.id = od.product_id
    GROUP BY p.id, p.name;
END //
DELIMITER ;
