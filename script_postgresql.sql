-- Se deben eliminar las tablas si existen para asegurar un inicio limpio antes de que Spring las cree.
DROP TABLE IF EXISTS postulaciones CASCADE;
DROP TABLE IF EXISTS electivos CASCADE;
DROP TABLE IF EXISTS estudiantes CASCADE;
DROP TABLE IF EXISTS profesores CASCADE;
DROP TABLE IF EXISTS administradores CASCADE;
DROP TABLE IF EXISTS USUARIO CASCADE;
DROP TABLE IF EXISTS ESTADO CASCADE;

-- Creamos la tabla ESTADO, ya que con las modificaciones en application properties
-- no se crea por si sola.
CREATE TABLE ESTADO (
    ID SERIAL PRIMARY KEY,
    NOMBRE VARCHAR(50) NOT NULL UNIQUE
);

-- Se deben insertar los valores del ENUM de Java
INSERT INTO ESTADO (nombre) VALUES
('PENDIENTE'),-- ID 1
('ACEPTADA'),-- ID 2
('RECHAZADA');-- ID 3

SELECT * FROM POSTULACIONES