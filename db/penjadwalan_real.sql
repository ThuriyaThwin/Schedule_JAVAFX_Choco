/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 10.1.21-MariaDB : Database - penjadwalan_real
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`penjadwalan_real` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `penjadwalan_real`;

/*Table structure for table `dosen` */

DROP TABLE IF EXISTS `dosen`;

CREATE TABLE `dosen` (
  `no` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `dosen` */

insert  into `dosen`(`no`,`nama`) values 
(1,'YHP'),
(2,'RDT'),
(3,'RIS'),
(4,'TNT');

/*Table structure for table `dosen_matkul` */

DROP TABLE IF EXISTS `dosen_matkul`;

CREATE TABLE `dosen_matkul` (
  `no_dosen` int(20) DEFAULT NULL,
  `no_matkul` int(20) DEFAULT NULL,
  `nilai` int(20) DEFAULT NULL,
  KEY `fk_dosen` (`no_dosen`),
  KEY `fk_matkul2` (`no_matkul`),
  CONSTRAINT `fk_dosen` FOREIGN KEY (`no_dosen`) REFERENCES `dosen` (`no`),
  CONSTRAINT `fk_matkul2` FOREIGN KEY (`no_matkul`) REFERENCES `matkul` (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `dosen_matkul` */

insert  into `dosen_matkul`(`no_dosen`,`no_matkul`,`nilai`) values 
(1,1,0),
(1,2,1),
(1,3,0),
(1,4,1),
(1,5,0),
(1,6,1),
(1,7,0),
(1,8,0),
(2,1,0),
(2,2,0),
(2,3,1),
(2,4,0),
(2,5,0),
(2,6,0),
(2,7,0),
(2,8,1),
(3,1,0),
(3,2,0),
(3,3,0),
(3,4,1),
(3,5,1),
(3,6,0),
(3,7,0),
(3,8,0),
(4,1,1),
(4,2,0),
(4,3,0),
(4,4,0),
(4,5,0),
(4,6,0),
(4,7,1),
(4,8,0);

/*Table structure for table `hari` */

DROP TABLE IF EXISTS `hari`;

CREATE TABLE `hari` (
  `no` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `hari` */

insert  into `hari`(`no`,`nama`) values 
(1,'Jumat'),
(2,'Kamis'),
(3,'Rabu'),
(4,'Selasa'),
(5,'Senin');

/*Table structure for table `jadwal` */

DROP TABLE IF EXISTS `jadwal`;

CREATE TABLE `jadwal` (
  `id_jadwal` int(20) NOT NULL AUTO_INCREMENT,
  `no_dosen` int(20) DEFAULT NULL,
  `no_matkul` int(20) DEFAULT NULL,
  `no_kelas` int(20) DEFAULT NULL,
  `no_ruangan` int(20) DEFAULT NULL,
  `no_hari` int(20) DEFAULT NULL,
  `no_sesi` int(20) DEFAULT NULL,
  `kategori` int(20) DEFAULT NULL,
  `tot_ruangan` int(20) DEFAULT NULL,
  PRIMARY KEY (`id_jadwal`),
  KEY `fk_jadwaldosen` (`no_dosen`),
  KEY `fk_jadwalhari` (`no_hari`),
  KEY `fk_jadwalsesi` (`no_sesi`),
  KEY `fk_jadwalmatkul` (`no_matkul`),
  KEY `fk_jadwalkelas` (`no_kelas`),
  KEY `fk_jadwalruangan` (`no_ruangan`),
  KEY `fk_jadwalkategori` (`kategori`),
  CONSTRAINT `fk_jadwaldosen` FOREIGN KEY (`no_dosen`) REFERENCES `dosen` (`no`),
  CONSTRAINT `fk_jadwalhari` FOREIGN KEY (`no_hari`) REFERENCES `hari` (`no`),
  CONSTRAINT `fk_jadwalkategori` FOREIGN KEY (`kategori`) REFERENCES `kategori` (`no`),
  CONSTRAINT `fk_jadwalkelas` FOREIGN KEY (`no_kelas`) REFERENCES `kelas` (`no`),
  CONSTRAINT `fk_jadwalmatkul` FOREIGN KEY (`no_matkul`) REFERENCES `matkul` (`no`),
  CONSTRAINT `fk_jadwalruangan` FOREIGN KEY (`no_ruangan`) REFERENCES `ruangan` (`no`),
  CONSTRAINT `fk_jadwalsesi` FOREIGN KEY (`no_sesi`) REFERENCES `sesi` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

/*Data for the table `jadwal` */

insert  into `jadwal`(`id_jadwal`,`no_dosen`,`no_matkul`,`no_kelas`,`no_ruangan`,`no_hari`,`no_sesi`,`kategori`,`tot_ruangan`) values 
(1,1,2,2,5,4,3,1,1),
(2,1,2,2,5,4,4,1,1),
(3,1,2,2,5,5,3,1,1),
(4,1,2,2,5,5,4,1,1),
(5,1,6,3,5,4,2,1,3),
(6,1,6,3,5,5,1,1,3),
(7,1,6,3,5,5,2,1,3),
(8,2,3,3,5,4,4,1,4),
(9,2,3,3,5,5,3,1,4),
(10,2,3,3,5,5,4,1,4),
(11,2,8,1,5,4,2,1,3),
(12,2,8,1,5,4,3,1,3),
(13,2,8,1,5,5,1,1,3),
(14,2,8,1,5,5,2,1,3),
(15,3,4,2,5,4,2,1,3),
(16,3,4,2,5,5,1,1,3),
(17,3,4,2,5,5,2,1,3),
(18,3,5,1,5,4,4,1,3),
(19,3,5,1,5,5,3,1,3),
(20,3,5,1,5,5,4,1,3),
(21,4,1,1,5,3,3,1,1),
(22,4,1,1,5,3,4,1,1),
(23,4,1,1,5,4,1,1,1),
(24,4,7,3,5,2,4,1,3),
(25,4,7,3,5,3,1,1,3),
(26,4,7,3,5,3,2,1,3),
(27,4,7,3,5,4,3,1,3);

/*Table structure for table `kategori` */

DROP TABLE IF EXISTS `kategori`;

CREATE TABLE `kategori` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `kategori` */

insert  into `kategori`(`no`,`nama`) values 
(1,'Teori'),
(2,'Praktikum');

/*Table structure for table `kelas` */

DROP TABLE IF EXISTS `kelas`;

CREATE TABLE `kelas` (
  `no` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  `jumlah` int(20) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `kelas` */

insert  into `kelas`(`no`,`nama`,`jumlah`) values 
(1,'33TI',30),
(2,'43TI',30),
(3,'12TI',30);

/*Table structure for table `matkul` */

DROP TABLE IF EXISTS `matkul`;

CREATE TABLE `matkul` (
  `no` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  `sks` int(20) DEFAULT NULL,
  `jumlah` int(20) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Data for the table `matkul` */

insert  into `matkul`(`no`,`nama`,`sks`,`jumlah`) values 
(1,'PSW1',3,180),
(2,'MATDIS',4,150),
(3,'PAM',3,15),
(4,'PAP',3,30),
(5,'KREN',3,30),
(6,'CERTAN',3,30),
(7,'KEPAL',4,30),
(8,'OOSD',4,30);

/*Table structure for table `matkul_kategori` */

DROP TABLE IF EXISTS `matkul_kategori`;

CREATE TABLE `matkul_kategori` (
  `no_matkul` int(11) DEFAULT NULL,
  `no_kategori` int(11) DEFAULT NULL,
  `nilai` int(1) DEFAULT NULL,
  KEY `fk_mk1` (`no_matkul`),
  KEY `fk_mk2` (`no_kategori`),
  CONSTRAINT `fk_mk1` FOREIGN KEY (`no_matkul`) REFERENCES `matkul` (`no`),
  CONSTRAINT `fk_mk2` FOREIGN KEY (`no_kategori`) REFERENCES `kategori` (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `matkul_kategori` */

insert  into `matkul_kategori`(`no_matkul`,`no_kategori`,`nilai`) values 
(1,1,1),
(1,2,0),
(2,1,1),
(2,2,0),
(3,1,1),
(3,2,0),
(4,1,1),
(4,2,0),
(5,1,1),
(5,2,0),
(6,1,1),
(6,2,0),
(7,1,1),
(7,2,0),
(8,1,1),
(8,2,0);

/*Table structure for table `matkul_kelas` */

DROP TABLE IF EXISTS `matkul_kelas`;

CREATE TABLE `matkul_kelas` (
  `no_matkul` int(20) DEFAULT NULL,
  `no_kelas` int(20) DEFAULT NULL,
  `nilai` int(20) DEFAULT NULL,
  KEY `fk_matkul` (`no_matkul`),
  KEY `fk_kelas` (`no_kelas`),
  CONSTRAINT `fk_kelas` FOREIGN KEY (`no_kelas`) REFERENCES `kelas` (`no`),
  CONSTRAINT `fk_matkul` FOREIGN KEY (`no_matkul`) REFERENCES `matkul` (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `matkul_kelas` */

insert  into `matkul_kelas`(`no_matkul`,`no_kelas`,`nilai`) values 
(1,1,1),
(1,2,0),
(1,3,0),
(2,1,0),
(2,2,1),
(2,3,0),
(3,1,0),
(3,2,0),
(3,3,1),
(4,1,0),
(4,2,1),
(4,3,0),
(5,1,1),
(5,2,0),
(5,3,0),
(6,1,0),
(6,2,0),
(6,3,1),
(7,1,0),
(7,2,0),
(7,3,1),
(8,1,1),
(8,2,0),
(8,3,0);

/*Table structure for table `ruangan` */

DROP TABLE IF EXISTS `ruangan`;

CREATE TABLE `ruangan` (
  `no` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  `kapasitas` int(20) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `ruangan` */

insert  into `ruangan`(`no`,`nama`,`kapasitas`) values 
(1,'GD511',30),
(2,'GD512',20),
(3,'GD513',200),
(4,'GD514',30),
(5,'Belum di-set',0);

/*Table structure for table `sesi` */

DROP TABLE IF EXISTS `sesi`;

CREATE TABLE `sesi` (
  `no` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `sesi` */

insert  into `sesi`(`no`,`nama`) values 
(1,'Sesi 4'),
(2,'Sesi 3'),
(3,'Sesi 2'),
(4,'Sesi 1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
