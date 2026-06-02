CREATE DATABASE bd_multi;
USE bd_multi;

CREATE TABLE personaje (
    id_personaje INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre       VARCHAR(20)  NOT NULL,
    rol          VARCHAR(10)  NOT NULL,
    nivel        SMALLINT     NOT NULL DEFAULT 1,   
    vida         SMALLINT     NOT NULL,
    daño         SMALLINT     NOT NULL,
    exp          SMALLINT     NOT NULL DEFAULT 0   
);

-- Datos de prueba para la demo del parcial
INSERT INTO personaje (nombre, rol, nivel, vida, daño, exp) VALUES
('Carlos',     'Guerrero', 1, 120, 25, 0),
('Ana',        'Mago',     1,  80, 35, 0),
('Luis',       'Arquero',  1, 100, 20, 0);



