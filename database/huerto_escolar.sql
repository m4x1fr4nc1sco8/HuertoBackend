-- =====================================================================
-- HuertoEscolar - Base de datos del microservicio (MySQL / MariaDB de XAMPP)
-- Modelo normalizado en 3FN: los estados y roles viven en tablas de
-- catálogo y las demás tablas los referencian por clave foránea.
--
-- Cómo ejecutarlo:
--   1) Abre XAMPP y arranca Apache y MySQL.
--   2) Entra a http://localhost/phpmyadmin  -> pestaña "SQL".
--   3) Pega todo este archivo y presiona "Continuar"
--      (o usa la pestaña "Importar" y elige este archivo).
-- =====================================================================

DROP DATABASE IF EXISTS huerto_escolar;
CREATE DATABASE huerto_escolar
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE huerto_escolar;

-- ---------------------------------------------------------------------
-- Catálogos
-- ---------------------------------------------------------------------
CREATE TABLE rol (
                     id_rol  TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
                     nombre  VARCHAR(20)      NOT NULL,
                     PRIMARY KEY (id_rol),
                     UNIQUE KEY uq_rol_nombre (nombre)
) ENGINE = InnoDB;

CREATE TABLE estado_sector (
                               id_estado_sector TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
                               nombre           VARCHAR(20)      NOT NULL,
                               PRIMARY KEY (id_estado_sector),
                               UNIQUE KEY uq_estado_sector_nombre (nombre)
) ENGINE = InnoDB;

CREATE TABLE estado_cultivo (
                                id_estado_cultivo TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                nombre            VARCHAR(25)      NOT NULL,
                                PRIMARY KEY (id_estado_cultivo),
                                UNIQUE KEY uq_estado_cultivo_nombre (nombre)
) ENGINE = InnoDB;

CREATE TABLE estado_tarea (
                              id_estado_tarea TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
                              nombre          VARCHAR(25)      NOT NULL,
                              PRIMARY KEY (id_estado_tarea),
                              UNIQUE KEY uq_estado_tarea_nombre (nombre)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
-- Entidades principales
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
                         id_usuario       INT UNSIGNED     NOT NULL AUTO_INCREMENT,
                         nombre           VARCHAR(50)      NOT NULL,
                         apellido         VARCHAR(50)      NOT NULL,
                         correo           VARCHAR(100)     NOT NULL,
                         fecha_nacimiento DATE             NOT NULL,
                         clave            VARCHAR(100)     NOT NULL,
                         id_rol           TINYINT UNSIGNED NOT NULL,
                         PRIMARY KEY (id_usuario),
                         UNIQUE KEY uq_usuario_correo (correo),
                         KEY ix_usuario_rol (id_rol),
                         CONSTRAINT fk_usuario_rol
                             FOREIGN KEY (id_rol) REFERENCES rol (id_rol)
) ENGINE = InnoDB;

CREATE TABLE sector (
                        id_sector        INT UNSIGNED     NOT NULL AUTO_INCREMENT,
                        codigo           VARCHAR(20)      NOT NULL,
                        nombre           VARCHAR(60)      NOT NULL,
                        ubicacion        VARCHAR(100)     NOT NULL,
                        id_estado_sector TINYINT UNSIGNED NOT NULL,
                        fecha_creacion   DATE             NOT NULL,
                        foto_url         VARCHAR(255)     NULL,
                        PRIMARY KEY (id_sector),
                        UNIQUE KEY uq_sector_codigo (codigo),
                        KEY ix_sector_estado (id_estado_sector),
                        CONSTRAINT fk_sector_estado
                            FOREIGN KEY (id_estado_sector) REFERENCES estado_sector (id_estado_sector)
) ENGINE = InnoDB;

-- Un cultivo pertenece a un sector y se asigna (opcionalmente) a un estudiante.
-- Los datos de la tarea (estado, foto, comentario, motivo de rechazo) dependen
-- solo del cultivo, por eso quedan en la misma tabla sin romper la 3FN.
CREATE TABLE cultivo (
                         id_cultivo            INT UNSIGNED     NOT NULL AUTO_INCREMENT,
                         nombre                VARCHAR(60)      NOT NULL,
                         id_sector             INT UNSIGNED     NOT NULL,
                         id_estudiante         INT UNSIGNED     NULL,
                         fecha_siembra         DATE             NOT NULL,
                         fecha_cosecha         DATE             NOT NULL,
                         fecha_limite          DATE             NULL,
                         frecuencia_riego_dias TINYINT UNSIGNED NOT NULL,
                         id_estado_cultivo     TINYINT UNSIGNED NOT NULL,
                         id_estado_tarea       TINYINT UNSIGNED NOT NULL,
                         foto_url              VARCHAR(255)     NULL,
                         comentario_estudiante VARCHAR(500)     NULL,
                         motivo_rechazo        VARCHAR(255)     NULL,
                         PRIMARY KEY (id_cultivo),
                         KEY ix_cultivo_sector (id_sector),
                         KEY ix_cultivo_estudiante (id_estudiante),
                         KEY ix_cultivo_estado (id_estado_cultivo),
                         KEY ix_cultivo_estado_tarea (id_estado_tarea),
                         CONSTRAINT fk_cultivo_sector
                             FOREIGN KEY (id_sector) REFERENCES sector (id_sector),
                         CONSTRAINT fk_cultivo_estudiante
                             FOREIGN KEY (id_estudiante) REFERENCES usuario (id_usuario),
                         CONSTRAINT fk_cultivo_estado
                             FOREIGN KEY (id_estado_cultivo) REFERENCES estado_cultivo (id_estado_cultivo),
                         CONSTRAINT fk_cultivo_estado_tarea
                             FOREIGN KEY (id_estado_tarea) REFERENCES estado_tarea (id_estado_tarea),
                         CONSTRAINT ck_cultivo_riego
                             CHECK (frecuencia_riego_dias BETWEEN 1 AND 30),
                         CONSTRAINT ck_cultivo_fechas
                             CHECK (fecha_cosecha > fecha_siembra)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
-- Datos de los catálogos (mismos valores que los enums de la app Kotlin)
-- ---------------------------------------------------------------------
INSERT INTO rol (nombre) VALUES
                             ('ADMINISTRADOR'),
                             ('ESTUDIANTE');

INSERT INTO estado_sector (nombre) VALUES
                                       ('DISPONIBLE'),
                                       ('EN_USO'),
                                       ('EN_MANTENIMIENTO'),
                                       ('FUERA_DE_USO');

INSERT INTO estado_cultivo (nombre) VALUES
                                        ('PLANIFICADO'),
                                        ('SEMBRADO'),
                                        ('EN_CRECIMIENTO'),
                                        ('LISTO_PARA_COSECHAR'),
                                        ('COSECHADO'),
                                        ('CON_PROBLEMAS'),
                                        ('FINALIZADO');

INSERT INTO estado_tarea (nombre) VALUES
                                      ('PENDIENTE'),
                                      ('EN_DESARROLLO'),
                                      ('REALIZADA'),
                                      ('PENDIENTE_DE_VALIDACION'),
                                      ('VALIDADA'),
                                      ('RECHAZADA'),
                                      ('ATRASADA');

-- ---------------------------------------------------------------------
-- Datos de prueba (ficticios)
-- Las cuentas coinciden con las de prueba de la app: clave huerto1234
-- ---------------------------------------------------------------------
INSERT INTO usuario (nombre, apellido, correo, fecha_nacimiento, clave, id_rol) VALUES
                                                                                    ('Camila', 'Torres', 'admin@huerto.cl',      '1988-03-12', 'huerto1234',
                                                                                     (SELECT id_rol FROM rol WHERE nombre = 'ADMINISTRADOR')),
                                                                                    ('Pedro',  'Saez',   'estudiante@huerto.cl', '2014-07-05', 'huerto1234',
                                                                                     (SELECT id_rol FROM rol WHERE nombre = 'ESTUDIANTE')),
                                                                                    ('Ana',    'Soto',   'ana@huerto.cl',        '2012-01-20', 'huerto1234',
                                                                                     (SELECT id_rol FROM rol WHERE nombre = 'ESTUDIANTE'));

INSERT INTO sector (codigo, nombre, ubicacion, id_estado_sector, fecha_creacion) VALUES
                                                                                     ('S1', 'Huerta Norte',  'Patio central',
                                                                                      (SELECT id_estado_sector FROM estado_sector WHERE nombre = 'EN_USO'),      '2026-03-01'),
                                                                                     ('S2', 'Invernadero',   'Costado sur',
                                                                                      (SELECT id_estado_sector FROM estado_sector WHERE nombre = 'DISPONIBLE'),  '2026-03-01'),
                                                                                     ('S3', 'Jardinera Sur', 'Entrada del colegio',
                                                                                      (SELECT id_estado_sector FROM estado_sector WHERE nombre = 'EN_MANTENIMIENTO'), '2026-03-15');

INSERT INTO cultivo (nombre, id_sector, id_estudiante, fecha_siembra, fecha_cosecha,
                     fecha_limite, frecuencia_riego_dias, id_estado_cultivo, id_estado_tarea) VALUES
                                                                                                  ('Tomates',
                                                                                                   (SELECT id_sector FROM sector WHERE codigo = 'S1'),
                                                                                                   (SELECT id_usuario FROM usuario WHERE correo = 'estudiante@huerto.cl'),
                                                                                                   '2026-10-01', '2027-01-15', '2026-12-20', 3,
                                                                                                   (SELECT id_estado_cultivo FROM estado_cultivo WHERE nombre = 'EN_CRECIMIENTO'),
                                                                                                   (SELECT id_estado_tarea FROM estado_tarea WHERE nombre = 'PENDIENTE')),
                                                                                                  ('Lechugas',
                                                                                                   (SELECT id_sector FROM sector WHERE codigo = 'S1'),
                                                                                                   (SELECT id_usuario FROM usuario WHERE correo = 'ana@huerto.cl'),
                                                                                                   '2026-10-20', '2026-12-10', '2026-12-05', 2,
                                                                                                   (SELECT id_estado_cultivo FROM estado_cultivo WHERE nombre = 'SEMBRADO'),
                                                                                                   (SELECT id_estado_tarea FROM estado_tarea WHERE nombre = 'PENDIENTE')),
                                                                                                  ('Albahaca',
                                                                                                   (SELECT id_sector FROM sector WHERE codigo = 'S2'),
                                                                                                   NULL,
                                                                                                   '2026-11-10', '2027-01-30', NULL, 4,
                                                                                                   (SELECT id_estado_cultivo FROM estado_cultivo WHERE nombre = 'PLANIFICADO'),
                                                                                                   (SELECT id_estado_tarea FROM estado_tarea WHERE nombre = 'PENDIENTE'));
