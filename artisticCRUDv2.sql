-- MySQL Script generated by MySQL Workbench
-- Sat Jun 10 06:27:47 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema artisticCRUD
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema artisticCRUD
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `artisticCRUD` DEFAULT CHARACTER SET utf8 ;
USE `artisticCRUD` ;

-- -----------------------------------------------------
-- Table `artisticCRUD`.`USUARIOS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `artisticCRUD`.`USUARIOS` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `tipoUsuario` INT NOT NULL,
  `correo` VARCHAR(320) NULL,
  `alias` VARCHAR(45) NULL,
  `password` VARCHAR(256) NULL,
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario` ASC) VISIBLE,
  PRIMARY KEY (`idUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `artisticCRUD`.`ARTISTAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `artisticCRUD`.`ARTISTAS` (
  `idUsuarioFK` INT NOT NULL,
  `tlf` VARCHAR(21) NULL,
  `instagram` VARCHAR(100) NULL,
  `web` VARCHAR(320) NULL,
  `biografia` VARCHAR(500) NULL,
  INDEX `artistasUsuarios_idx` (`idUsuarioFK` ASC) VISIBLE,
  CONSTRAINT `artistasUsuarios`
    FOREIGN KEY (`idUsuarioFK`)
    REFERENCES `artisticCRUD`.`USUARIOS` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `artisticCRUD`.`ESTILOS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `artisticCRUD`.`ESTILOS` (
  `idEstilos` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NULL,
  PRIMARY KEY (`idEstilos`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `artisticCRUD`.`ESTILOS_ARTISTAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `artisticCRUD`.`ESTILOS_ARTISTAS` (
  `idEstilosFK` INT NOT NULL,
  `idArtistasFK` INT NOT NULL,
  INDEX `artistasEstilos_idx` (`idArtistasFK` ASC) VISIBLE,
  INDEX `estilosArtistas_idx` (`idEstilosFK` ASC) VISIBLE,
  CONSTRAINT `artistasEstilos`
    FOREIGN KEY (`idArtistasFK`)
    REFERENCES `artisticCRUD`.`ARTISTAS` (`idUsuarioFK`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `estilosArtistas`
    FOREIGN KEY (`idEstilosFK`)
    REFERENCES `artisticCRUD`.`ESTILOS` (`idEstilos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `artisticCRUD`.`PUBLICACIONES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `artisticCRUD`.`PUBLICACIONES` (
  `idPublicacion` INT NOT NULL AUTO_INCREMENT,
  `idArtistaFK` INT NOT NULL,
  `idEstiloFK` INT NOT NULL,
  `fecha` DATE NOT NULL,
  `texto` VARCHAR(300) NULL,
  PRIMARY KEY (`idPublicacion`),
  INDEX `publicacionesArtistas_idx` (`idArtistaFK` ASC) VISIBLE,
  INDEX `publicacionesEstilos_idx` (`idEstiloFK` ASC) VISIBLE,
  CONSTRAINT `publicacionesArtistas`
    FOREIGN KEY (`idArtistaFK`)
    REFERENCES `artisticCRUD`.`ARTISTAS` (`idUsuarioFK`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `publicacionesEstilos`
    FOREIGN KEY (`idEstiloFK`)
    REFERENCES `artisticCRUD`.`ESTILOS` (`idEstilos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
-- CREAMOS EL USUARIO QUE VA A USAR LA APLICACIÓN.

CREATE USER 'adminArtisticCRUD'@'localhost' IDENTIFIED BY 'Studium2023;';
GRANT SELECT, INSERT, UPDATE, DELETE ON artisticcrud.* TO 'adminArtisticCRUD'@'localhost';

-- AÑADIRIREMOS A LA BASE DE DATOS EL USUARIOS ADMIN;
INSERT INTO usuarios VALUES (null,0,'admin@admin.com','admin',SHA2('admin',256));
-- AÑADIREMOS UN ESTILO A LA BASE DE DATOS;
INSERT INTO estilos VALUES(null,'OTROS');
