-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 05, 2017 at 06:23 AM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wineshop`
--

-- --------------------------------------------------------

--
-- Table structure for table `bill`
--

CREATE TABLE `bill` (
  `billno` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  `phno` varchar(50) NOT NULL DEFAULT 'NA',
  `ptype` tinyint(1) NOT NULL,
  `tno` varchar(200) NOT NULL DEFAULT '-1@',
  `bfrom` int(11) NOT NULL DEFAULT '0',
  `offamt` double NOT NULL DEFAULT '0',
  `onamt` double NOT NULL DEFAULT '0',
  `foamt` double NOT NULL DEFAULT '0',
  `tableno` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bill`
--

INSERT INTO `bill` (`billno`, `name`, `phno`, `ptype`, `tno`, `bfrom`, `offamt`, `onamt`, `foamt`, `tableno`) VALUES
(-1, 'Credit', '0000000000', 1, '-1@', 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `consignee`
--

CREATE TABLE `consignee` (
  `vat` float DEFAULT NULL,
  `Consig_name` varchar(1000) DEFAULT NULL,
  `Cosig_address` varchar(2000) DEFAULT NULL,
  `sl_no` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `consignee`
--

INSERT INTO `consignee` (`vat`, `Consig_name`, `Cosig_address`, `sl_no`) VALUES
(20, 'Swaraj Shop', 'Kolkata', 1);

-- --------------------------------------------------------

--
-- Table structure for table `constant`
--

CREATE TABLE `constant` (
  `consignee_n` varchar(1000) NOT NULL,
  `consignee_a` varchar(2000) NOT NULL,
  `vat` double NOT NULL,
  `ip` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ddesc`
--

CREATE TABLE `ddesc` (
  `dcode` varchar(200) NOT NULL,
  `kofl` varchar(200) NOT NULL,
  `cat` varchar(500) NOT NULL,
  `bname` varchar(2000) NOT NULL,
  `strng` varchar(200) NOT NULL,
  `mlpb` int(11) NOT NULL DEFAULT '0',
  `bpc` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ddesc`
--

INSERT INTO `ddesc` (`dcode`, `kofl`, `cat`, `bname`, `strng`, `mlpb`, `bpc`) VALUES
('1', 'IMFL', 'Beer', 'Kingfisher Strong Premium Beer', '8% V/V', 650, 12),
('10', 'IMFL', 'Rum', 'McDowells NO.1 Celebration Matured XXX Rum', '25 Under Proof (UP)', 180, 48),
('11', 'IMFL', 'Rum', 'McDowells NO.1 Celebration Matured XXX Rum', '25 Under Proof (UP)', 375, 26),
('2', 'IMFL', 'Vodka', 'Magic Moments Pure Gain Vodka', '25 Under Proof (UP)', 180, 48),
('3', 'IMFL', 'Vodka', 'Magic Moments Pure Gain Vodka', '25 Under Proof (UP)', 375, 24),
('4', 'IMFL', 'Whisky', 'McDowells NO.1 Luxury Premium Whisky', '25 Under Proof (UP)', 1000, 9),
('5', 'IMFL', 'Whisky', 'McDowells NO.1 Luxury Premium Whisky', '25 Under Proof (UP)', 180, 48),
('6', 'IMFL', 'Whisky', 'McDowells NO.1 Luxury Premium Whisky', '25 Under Proof (UP)', 375, 24),
('7', 'IMFL', 'Whisky', 'McDowells NO.1 Luxury Reserve Whisky', '25 Under Proof (UP)', 1000, 9),
('8', 'IMFL', 'Gin', 'McDowells Blue Riband Tangi Gin-N-Orange', '25 Under Proof (UP)', 750, 12),
('9', 'IMFL', 'Rum', 'McDowells NO.1 Celebration Matured XXX Rum [Pet Bottle]', '25 Under Proof (UP)', 1000, 9);

-- --------------------------------------------------------

--
-- Table structure for table `dealer`
--

CREATE TABLE `dealer` (
  `tpassno` varchar(500) NOT NULL,
  `date` date NOT NULL,
  `reqdate` varchar(500) NOT NULL,
  `tslno` varchar(500) NOT NULL,
  `name_conr` varchar(1000) NOT NULL,
  `add_conr` varchar(2000) NOT NULL,
  `make_vehicle` varchar(1000) NOT NULL,
  `model_vehicle` varchar(1000) NOT NULL,
  `reg_vehicle` varchar(1000) NOT NULL,
  `invoiceno` varchar(1000) NOT NULL,
  `tamt` double NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dealer`
--

INSERT INTO `dealer` (`tpassno`, `date`, `reqdate`, `tslno`, `name_conr`, `add_conr`, `make_vehicle`, `model_vehicle`, `reg_vehicle`, `invoiceno`, `tamt`) VALUES
('-1@', '1990-01-01', 'Debit', 'Debit', 'N/A', 'N/A', 'N/A', 'N/A', 'N/A', 'Debit', 0),
('tFLDR/2016-2017/00547578/P', '2016-11-16', 'tFLDR/2016-2017/00547578 & 16 Nov 2016', 'tFLDR/2016-2017/00547578/P', 'HIMANSU DUTTA F.L. TRADE', 'G.T. Road, Kodalia P.O. - Bandel Dist. - Hooghly 722136 Email - hduttafl@gmail.com', 'N/A', 'N/A', 'N/A', '513/16-17', 99680.56);

-- --------------------------------------------------------

--
-- Table structure for table `food`
--

CREATE TABLE `food` (
  `sl_no` int(11) NOT NULL,
  `fname` varchar(1000) NOT NULL,
  `ftype` varchar(255) DEFAULT NULL,
  `hprice` double DEFAULT NULL,
  `fprice` double DEFAULT NULL,
  `discount` float DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `food`
--

INSERT INTO `food` (`sl_no`, `fname`, `ftype`, `hprice`, `fprice`, `discount`) VALUES
(17, 'rice', 'indian', 40, 100, 12),
(20, 'bread', 'indian', 120, 240, 11),
(21, 'magi', 'chines', 40, 42, 120),
(22, 'kjjnj', 'japananis', 10, 20, 2),
(23, 'fffff', 'maxico', 120, 240, 10),
(24, 'errtt', 'spanish', 40, 80, 2),
(25, 'gggj', 'ppp', 2343, 4556, 10),
(26, 'yuyuu', 'fghg', 120, 200, 10),
(27, 'mnmn', 'bbb', 40, 10, 2),
(28, 'nnn', 'uuu', 120, 240, 5),
(29, 'ppp', 'vvvv', 120, 200, 0),
(30, 'jnj,mnc', 'cggjgdj', 100, 120, 2),
(32, 'aaaaaa', 'mmmmmmm', 100, 2000, 10);

-- --------------------------------------------------------

--
-- Table structure for table `mainstock`
--

CREATE TABLE `mainstock` (
  `slno` bigint(20) NOT NULL,
  `dcode` varchar(200) NOT NULL,
  `date` date NOT NULL,
  `ttype` tinyint(1) NOT NULL,
  `qty` int(11) NOT NULL DEFAULT '0',
  `instock` int(11) NOT NULL DEFAULT '0',
  `pricepb` double NOT NULL DEFAULT '0',
  `tpassno` varchar(500) NOT NULL DEFAULT '-1@',
  `shopt` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mainstock`
--

INSERT INTO `mainstock` (`slno`, `dcode`, `date`, `ttype`, `qty`, `instock`, `pricepb`, `tpassno`, `shopt`) VALUES
(7, '1', '2016-11-16', 1, 60, 60, 0, 'tFLDR/2016-2017/00547578/P', 1),
(8, '4', '2016-11-16', 1, 18, 18, 0, 'tFLDR/2016-2017/00547578/P', 1),
(9, '5', '2016-11-16', 1, 48, 48, 0, 'tFLDR/2016-2017/00547578/P', 1),
(10, '4', '2016-11-16', 1, 18, 36, 0, 'tFLDR/2016-2017/00547578/P', 1),
(11, '1', '2017-01-03', 0, 12, 6, 0, '-1@', 0),
(12, '4', '2017-01-03', 0, 9, 27, 15.65, '-1@', 1),
(13, '4', '2017-01-03', 0, 9, 18, 15.65, '-1@', 1),
(14, '1', '2017-01-03', 0, 2, 4, 0, '-1@', 0),
(15, '1', '2017-01-03', 0, 2, 2, 0, '-1@', 0);

-- --------------------------------------------------------

--
-- Table structure for table `offshopstock`
--

CREATE TABLE `offshopstock` (
  `slno` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `dcode` varchar(200) NOT NULL,
  `qty` int(11) NOT NULL DEFAULT '0',
  `instock` int(11) NOT NULL DEFAULT '0',
  `pricepb` double NOT NULL DEFAULT '0',
  `ttype` tinyint(1) NOT NULL,
  `billno` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `offshopstock`
--

INSERT INTO `offshopstock` (`slno`, `date`, `dcode`, `qty`, `instock`, `pricepb`, `ttype`, `billno`) VALUES
(1, '2017-01-03', '1', 2, 2, 0, 1, -1),
(2, '2017-01-03', '1', 2, 4, 0, 1, -1);

-- --------------------------------------------------------

--
-- Table structure for table `onshopstock`
--

CREATE TABLE `onshopstock` (
  `slno` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `dcode` varchar(200) NOT NULL,
  `qtyml` bigint(20) NOT NULL DEFAULT '0',
  `mlinstock` bigint(50) NOT NULL DEFAULT '0',
  `price30ml` double NOT NULL DEFAULT '0',
  `ttype` tinyint(1) NOT NULL,
  `billno` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `onshopstock`
--

INSERT INTO `onshopstock` (`slno`, `date`, `dcode`, `qtyml`, `mlinstock`, `price30ml`, `ttype`, `billno`) VALUES
(2, '2017-01-03', '4', 9000, 9000, 0, 1, -1),
(3, '2017-01-03', '4', 9000, 18000, 0, 1, -1),
(4, '2017-01-04', '4', 0, 18000, 16, 1, -1),
(5, '2017-01-04', '4', 0, 18000, 20, 1, -1),
(6, '2017-01-04', '4', 0, 18000, 25, 1, -1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `uname` varchar(200) NOT NULL,
  `pwd` varchar(200) NOT NULL,
  `atype` int(11) NOT NULL,
  `name` varchar(1000) NOT NULL,
  `phno` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uname`, `pwd`, `atype`, `name`, `phno`) VALUES
('food', '1234', 2, 'Food', '789456'),
('offshop', '1234', 3, 'Offshop', '1234789'),
('onshop', '1234', 4, 'Onshop', '456123'),
('satya', '1234', 3, 'Satya', '1234'),
('swaraj', '1234', 1, 'Swaraj', '9474013767');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`billno`);

--
-- Indexes for table `consignee`
--
ALTER TABLE `consignee`
  ADD PRIMARY KEY (`sl_no`);

--
-- Indexes for table `ddesc`
--
ALTER TABLE `ddesc`
  ADD PRIMARY KEY (`dcode`);

--
-- Indexes for table `dealer`
--
ALTER TABLE `dealer`
  ADD PRIMARY KEY (`tpassno`);

--
-- Indexes for table `food`
--
ALTER TABLE `food`
  ADD PRIMARY KEY (`sl_no`);

--
-- Indexes for table `mainstock`
--
ALTER TABLE `mainstock`
  ADD PRIMARY KEY (`slno`),
  ADD KEY `fk_dcode_mainstock` (`dcode`) USING BTREE,
  ADD KEY `fk_tpassno_mainstock` (`tpassno`) USING BTREE;

--
-- Indexes for table `offshopstock`
--
ALTER TABLE `offshopstock`
  ADD PRIMARY KEY (`slno`),
  ADD KEY `fk_dcode_offshopstock` (`dcode`) USING BTREE,
  ADD KEY `fk_billno_offshopbill` (`billno`) USING BTREE;

--
-- Indexes for table `onshopstock`
--
ALTER TABLE `onshopstock`
  ADD PRIMARY KEY (`slno`),
  ADD KEY `fk_dcode_onshopstock` (`dcode`) USING BTREE,
  ADD KEY `fk_billno_onshopbill` (`billno`) USING BTREE;

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uname`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bill`
--
ALTER TABLE `bill`
  MODIFY `billno` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `consignee`
--
ALTER TABLE `consignee`
  MODIFY `sl_no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `food`
--
ALTER TABLE `food`
  MODIFY `sl_no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
--
-- AUTO_INCREMENT for table `mainstock`
--
ALTER TABLE `mainstock`
  MODIFY `slno` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `offshopstock`
--
ALTER TABLE `offshopstock`
  MODIFY `slno` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `onshopstock`
--
ALTER TABLE `onshopstock`
  MODIFY `slno` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `mainstock`
--
ALTER TABLE `mainstock`
  ADD CONSTRAINT `fk_dcode` FOREIGN KEY (`dcode`) REFERENCES `ddesc` (`dcode`),
  ADD CONSTRAINT `fk_tpassno_mainstock` FOREIGN KEY (`tpassno`) REFERENCES `dealer` (`tpassno`);

--
-- Constraints for table `offshopstock`
--
ALTER TABLE `offshopstock`
  ADD CONSTRAINT `fk_billno_offshopbill` FOREIGN KEY (`billno`) REFERENCES `bill` (`billno`),
  ADD CONSTRAINT `fk_dcode_offshopstock` FOREIGN KEY (`dcode`) REFERENCES `ddesc` (`dcode`);

--
-- Constraints for table `onshopstock`
--
ALTER TABLE `onshopstock`
  ADD CONSTRAINT `fk_billno_onshopbill` FOREIGN KEY (`billno`) REFERENCES `bill` (`billno`),
  ADD CONSTRAINT `fk_dcode_onshopstock` FOREIGN KEY (`dcode`) REFERENCES `ddesc` (`dcode`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
