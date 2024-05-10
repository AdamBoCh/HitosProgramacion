CREATE DATABASE hito4;
USE hito4;


CREATE TABLE Fotografias (
    IdFoto INT AUTO_INCREMENT PRIMARY KEY,
    Titulo VARCHAR(255),
    Fecha DATE,
    Archivo VARCHAR(255), 
    Visitas INT,
    IdFotografo INT,
    FOREIGN KEY (IdFotografo) REFERENCES Fotografos(IdFotografo)
);

CREATE TABLE Fotografos (
    IdFotografo INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100), 
    Premiado BOOLEAN
);

INSERT INTO Fotografos (Nombre, Premiado) VALUES
('Juan Pérez', 1),
('María López', 0),
('Carlos Martínez', 1);


INSERT INTO Fotografias (Titulo, Fecha, Archivo, Visitas, IdFotografo) VALUES
('Atardecer en la playa', '2023-07-15', 'playa.jpg', 1500, 1),
('Retrato en blanco y negro', '2023-08-20', 'retrato.jpg', 2300, 2),
('Paisaje montañoso', '2023-09-05', 'montaña.jpg', 800, 3);