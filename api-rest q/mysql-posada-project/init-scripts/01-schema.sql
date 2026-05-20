CREATE DATABASE IF NOT EXISTS posada_db;
USE posada_db;

CREATE TABLE IF NOT EXISTS guests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone CHAR(10) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    number INT PRIMARY KEY,
    type ENUM('Sencilla', 'Doble', 'Doble con aire acondicionado') NOT NULL,
    price DECIMAL(10,2),
    status ENUM('Disponible', 'Ocupada', 'No disponible') DEFAULT 'Disponible'
);

CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guest_id INT NOT NULL,
    room_number INT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE,
    FOREIGN KEY (guest_id) REFERENCES guests(id),
    FOREIGN KEY (room_number) REFERENCES rooms(number)
);

CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    method ENUM('card', 'cash', 'transfer') NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);