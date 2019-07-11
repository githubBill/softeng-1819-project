
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema GG_spot2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema GG_spot2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `GG_spot2` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `GG_spot2` ;

-- -----------------------------------------------------
-- Table `GG_spot2`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GG_spot2`.`Customer` (
  `customerId` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(255) NOT NULL,
  `lastName` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL,
  `username` VARCHAR(255) NOT NULL,
  `hashPassword` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`customerId`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GG_spot2`.`Product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GG_spot2`.`Product` (
  `productId` INT NOT NULL AUTO_INCREMENT,
  `productName` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `category` VARCHAR(255) NULL,
  `withdrawn` TINYINT(1) NOT NULL,
  PRIMARY KEY (`productId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GG_spot2`.`Shop`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GG_spot2`.`Shop` (
  `shopId` INT NOT NULL AUTO_INCREMENT,
  `shopName` VARCHAR(255) NULL,
  `address` VARCHAR(255) NULL,
  `lng` DOUBLE NULL,
  `lat` DOUBLE NULL,
  `withdrawn` TINYINT(1) NOT NULL,
  PRIMARY KEY (`shopId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GG_spot2`.`Price`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GG_spot2`.`Price` (
  `price` DOUBLE NOT NULL,
  `date` DATE NOT NULL,
  `ProductId` INT NOT NULL,
  `ShopId` INT NOT NULL,
  PRIMARY KEY (`date`, `ProductId`, `ShopId`),
  INDEX `ProductId_idx` (`ProductId` ASC),
  INDEX `ShopId_idx` (`ShopId` ASC),
  CONSTRAINT `ProductId`
    FOREIGN KEY (`ProductId`)
    REFERENCES `GG_spot2`.`Product` (`productId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ShopId`
    FOREIGN KEY (`ShopId`)
    REFERENCES `GG_spot2`.`Shop` (`shopId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GG_spot2`.`ProductTags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GG_spot2`.`ProductTags` (
  `pid` INT NOT NULL,
  `tag` VARCHAR(255) NOT NULL,
  INDEX `productId_idx` (`pid` ASC),
  PRIMARY KEY (`pid`, `tag`),
  CONSTRAINT `productId2`
    FOREIGN KEY (`pid`)
    REFERENCES `GG_spot2`.`Product` (`productId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GG_spot2`.`ShopTags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GG_spot2`.`ShopTags` (
  `sid` INT NOT NULL,
  `tag` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`sid`, `tag`),
  CONSTRAINT `shopIDConstraint`
    FOREIGN KEY (`sid`)
    REFERENCES `GG_spot2`.`Shop` (`shopId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- --------------------------------------------------------------------

/*
 * Calculates the straight line distance between two GPS coordinates
 *
 * @author    Jason Kruse <jason@jasonkruse.com> or @mnisjk
 * @copyright 2014
 * @license   BSD (see LICENSE)
 *
 * @param lat1  (double)   latitude of origin
 * @param lon1  (double)   longitude of origin
 * @param lat2  (double)   latitude of destination
 * @param lon2  (double)   longitude of destination
 * @param unit  (enum)     MILE, MI for miles or KILOMETER, KM for kilometers
 * @return      (double)   Distance between the two points in the units specified.
 *
 * This function is very helpful for ordering a list of results by nearst first with:
 *
 * ORDER BY DISTANCE( userInput.lat, userInput.lon, locations.lat, locations.lon ) ASC;
 */
DROP FUNCTION IF EXISTS DISTANCEGPS;
DELIMITER $$
CREATE FUNCTION DISTANCEGPS( lat1 DOUBLE, lon1 DOUBLE, lat2 DOUBLE, lon2 DOUBLE, unit ENUM( 'MILE', 'KILOMETER', 'MI', 'KM' ) )
RETURNS DOUBLE
BEGIN
  DECLARE dist    DOUBLE;
  DECLARE latDist DOUBLE;
  DECLARE lonDist DOUBLE;
  DECLARE a,c,r   DOUBLE;

  # earth's radius
  IF unit = 'MILE' OR unit = 'MI' THEN SET r = 3959;
  ELSE SET r = 6371;
  END IF;

  # Haversine formula <http://en.wikipedia.org/wiki/Haversine_formula>
  SET latDist = RADIANS( lat2 - lat1 );
  SET lonDist = RADIANS( lon2 - lon1 );
  SET a = POW( SIN( latDist/2 ), 2 ) + COS( RADIANS( lat1 ) ) * COS( RADIANS( lat2 ) ) * POW( SIN( lonDist / 2 ), 2 );
  SET c = 2 * ATAN2( SQRT( a ), SQRT( 1 - a ) );
  SET dist = r * c;

  RETURN dist;
END$$
DELIMITER ;
