CREATE DATABASE clinicdb2;
USE clinicdb2; 
-- Admin Table
CREATE TABLE admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50)
);

-- Doctor Table
CREATE TABLE doctor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    specialization VARCHAR(100),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50)
);

-- Patient Table
CREATE TABLE patient (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    age INT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50)
);

-- (You can add appointment table later)
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    appointment_date DATE,
    appointment_time TIME,
    reason VARCHAR(255),
    status VARCHAR(50) DEFAULT 'Scheduled',
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);
UPDATE appointments
SET patient_id = 1, reason = 'Consultation'
WHERE id = 2;
ALTER TABLE appointments MODIFY doctor_id INT NULL;


CREATE TABLE appointment (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  doctor_id INT NOT NULL,
  appointment_date DATE,
  appointment_time TIME,
  reason VARCHAR(255),
  status VARCHAR(50) DEFAULT 'Scheduled'
);




INSERT INTO admin (username, password) VALUES ('admin', 'admin123');
INSERT INTO doctor (name, specialization, username, password) VALUES ('Dr. Smith', 'Cardiologist', 'drsmith', 'doc123');
INSERT INTO patient (name, age, username, password) VALUES ('john ', 14, 'john', 'john123');
INSERT INTO patient (name, age, username, password) VALUES ('del', 06, 'del', 'del123');
INSERT INTO patient (name, age, username, password) VALUES ('jeswin', 15, 'jeswin', 'jeswin123');
INSERT INTO patient (name, age, username, password) VALUES ('glory', 40, 'glory', 'glory123');
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, reason)
VALUES (1, 1, '2025-05-01', '10:00:00', 'Routine Checkup');

SELECT * FROM doctor;
SELECT * FROM patient;
SELECT * FROM admin;
SELECT * FROM appointments;

SELECT * FROM patient WHERE id = 2;
