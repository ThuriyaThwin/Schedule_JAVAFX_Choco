/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 10.1.21-MariaDB : Database - penjadwalan_mhs
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`penjadwalan_mhs`/*!40100 DEFAULT CHARACTER SET latin1 */;

USE `penjadwalan_mhs`;

/*Table structure for table `dosen` */

DROP TABLE IF EXISTS `dosen`;

CREATE TABLE `dosen` (
  `nip` int(20) NOT NULL,
  `nama` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`nip`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `dosen` */

insert  into `dosen`(`nip`,`nama`) values 
(111101,'MMS'),
(111102,'IL'),
(111103,'IHT');

/*Table structure for table `fakultas` */

DROP TABLE IF EXISTS `fakultas`;

CREATE TABLE `fakultas` (
  `id_fakultas` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`id_fakultas`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `fakultas` */

insert  into `fakultas`(`id_fakultas`,`nama`) values 
(1,'FTIE'),
(2,'FTB'),
(3,'FTI');

/*Table structure for table `jenis_ruangan` */

DROP TABLE IF EXISTS `jenis_ruangan`;

CREATE TABLE `jenis_ruangan` (
  `id_jenis_ruangan` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`id_jenis_ruangan`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `jenis_ruangan` */

insert  into `jenis_ruangan`(`id_jenis_ruangan`,`nama`) values 
(1,'Ruang'),
(2,'Lab');


/*Table structure for table `prodi` */

DROP TABLE IF EXISTS `prodi`;

CREATE TABLE `prodi` (
  `id_prodi` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  `fakultas` int(20) DEFAULT NULL,
  PRIMARY KEY (`id_prodi`),
  KEY `fk_prodi_fakultas` (`fakultas`),
  CONSTRAINT `fk_prodi_fakultas` FOREIGN KEY (`fakultas`) REFERENCES `fakultas` (`id_fakultas`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Data for the table `prodi` */

insert  into `prodi`(`id_prodi`,`nama`,`fakultas`) values
(1,'D3TI',1),
(2,'D3TK',1),
(3,'D4TI',1),
(4,'S1TI',1),
(5,'S1SI',1),
(6,'SITE',1),
(7,'S1BP',2),
(8,'S1MR',3);

/*Table structure for table `kelas` */

DROP TABLE IF EXISTS `kelas`;

CREATE TABLE `kelas` (
  `id_kelas` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  `jumlah` int(20) DEFAULT NULL,
  `prodi` int(20) DEFAULT NULL,
  PRIMARY KEY (`id_kelas`),
  KEY `fk_kelas_prodi` (`prodi`),
  CONSTRAINT `fk_kelas_prodi` FOREIGN KEY (`prodi`) REFERENCES `prodi` (`id_prodi`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `kelas` */

insert  into `kelas`(`id_kelas`,`nama`,`jumlah`,`prodi`) values 
(1,'11BP1',30,7),
(2,'21BP1',28,7),
(3,'31BP1',20,7),
(4,'41BP1',35,7);

/*Table structure for table `mahasiswa` */

DROP TABLE IF EXISTS `mahasiswa`;

CREATE TABLE `mahasiswa` (
  `nim` int(20) NOT NULL,
  `nama` varchar(225) DEFAULT NULL,
  `jurusan` varchar(225) DEFAULT NULL,
  `alamat` varchar(225) DEFAULT NULL,
  `hp` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`nim`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `mahasiswa` */

insert  into `mahasiswa`(`nim`,`nama`,`jurusan`,`alamat`,`hp`) values 
(11316010,'Franciskus Napitupulu','D3TI','Siantar','082311239012'),
(11316020,'Sehat Samosir','D3TI','Hinalang','082367894080');

/*Table structure for table `mata_kuliah` */

DROP TABLE IF EXISTS `mata_kuliah`;

CREATE TABLE `mata_kuliah` (
  `id_mata_kuliah` int(20) NOT NULL AUTO_INCREMENT,
  `kode` varchar(20) DEFAULT NULL,
  `nama` varchar(225) DEFAULT NULL,
  `sks` int(20) DEFAULT NULL,
  `kelas` int(20) DEFAULT NULL,
  PRIMARY KEY (`id_mata_kuliah`),
  KEY `fk_mata_kuliah_dosen` (`nama`),
  KEY `fk_mata_kuliah_kelas` (`kelas`),
  CONSTRAINT `fk_mata_kuliah_kelas` FOREIGN KEY (`kelas`) REFERENCES `kelas` (`id_kelas`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `mata_kuliah` */

insert  into `mata_kuliah`(`id_mata_kuliah`,`kode`,`nama`,`sks`,`kelas`) values 
(1,'MAS1101','Matematika Dasar I',4,1),
(2,'FIS1101','Fisika Dasar I (+P)',4,1),
(3,'KUS1102','Bahasa Inggris I',2,2);

/*Table structure for table `ruangan` */

DROP TABLE IF EXISTS `ruangan`;

CREATE TABLE `ruangan` (
  `id_ruangan` int(20) NOT NULL AUTO_INCREMENT,
  `nama` varchar(225) DEFAULT NULL,
  `jenis` int(20) DEFAULT NULL,
  `kapasitas` int(20) DEFAULT NULL,
  PRIMARY KEY (`id_ruangan`),
  KEY `fk_jenis_ruangan` (`jenis`),
  CONSTRAINT `fk_jenis_ruangan` FOREIGN KEY (`jenis`) REFERENCES `ruangan` (`id_ruangan`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `ruangan` */

insert  into `ruangan`(`id_ruangan`,`nama`,`jenis`,`kapasitas`) values 
(1,'GD811',1,1),
(2,'GD812',1,1),
(3,'GD813',1,1),
(4,'GD814',1,1);


/*Table structure for table `jadwal` */

DROP TABLE IF EXISTS `jadwal`;

CREATE TABLE `jadwal` (
  `id_jadwal` int(20) NOT NULL AUTO_INCREMENT,
  `dosen` int(20) DEFAULT NULL,
  `kelas` int(20) DEFAULT NULL,
  `mata_kuliah` int(20) DEFAULT NULL,
  PRIMARY KEY (`id_jadwal`),
  KEY `fk_jadwal_dosen` (`dosen`),
  KEY `fk_jadwal_mata_kuliah` (`mata_kuliah`),
  KEY `fk_jadwal_kelas` (`kelas`),
  CONSTRAINT `fk_jadwal_dosen` FOREIGN KEY (`dosen`) REFERENCES `dosen` (`nip`),
  CONSTRAINT `fk_jadwal_kelas` FOREIGN KEY (`kelas`) REFERENCES `kelas` (`id_kelas`),
  CONSTRAINT `fk_jadwal_mata_kuliah` FOREIGN KEY (`mata_kuliah`) REFERENCES `mata_kuliah` (`id_mata_kuliah`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `jadwal` */

insert  into `jadwal`(`id_jadwal`,`dosen`,`kelas`,`mata_kuliah`) values
(1,111101,1,1),
(2,111102,1,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
