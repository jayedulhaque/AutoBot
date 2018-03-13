-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 15, 2013 at 07:04 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `irobot`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE IF NOT EXISTS `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_pass` varchar(255) DEFAULT NULL,
  `cl_pass` varchar(255) DEFAULT NULL,
  `fw_email` varchar(255) DEFAULT NULL,
  `fw_email_pass` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `proxy` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `last_posted_date` datetime DEFAULT NULL,
  `working` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=45 ;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `user_id`, `country_id`, `email`, `email_pass`, `cl_pass`, `fw_email`, `fw_email_pass`, `phone`, `state`, `city`, `link`, `proxy`, `remarks`, `last_posted_date`, `working`) VALUES
(1, 1, 1, 'korinthas@gmail.com', 'acer123456', 'we0123456789', 'f@yahoo.com', 'f', '', '', 'sandiego', 'http://post.craigslist.org/c/sfo/P/cas?mix=w4m', '66.90.104.75', '', '2013-03-23 14:57:52', 0),
(2, 1, 1, 'protikshawj@gmail.com', 'acer123456', 'er0123456789', '', '', '', '', 'sandiego', 'http://post.craigslist.org/c/bzn/J/ofc/none/x', '66.90.104.75', '', '2013-03-23 15:01:10', 0),
(25, 1, 1, 'shahinur.ism@gmail.com', 'gmail@@7', '', 'a', 'b', '', '', '', 'http://post.craigslist.org/c/sfo/P/cas?mix=w4m', '193', '', '2013-03-30 17:07:32', 0),
(26, 1, 1, 'shahinur.ism@gmail.com', 'gmail@@7', '', 'a', 'b', '', '', '', 'google.com', '192', '', '2013-03-30 17:07:32', NULL),
(27, 2, 1, 'prohm@yahoo.com', '', '', 'a', 'b', '', '', '', 'yahoo.com', '192', '', '2013-03-30 17:07:32', NULL),
(28, 2, 1, 'vhxsd@yahoo.com', '', '', 'a', 'b', '', '', '', 'yahoo.com', '193', '', '2013-03-30 17:07:32', NULL),
(29, 2, 1, 'dfkhb@yahoo.com', '', '', 'a', 'b', '', '', '', 'yahoo.com', '192', '', '2013-03-30 17:07:32', NULL),
(30, 2, 1, 'xwjrd@yahoo.com', '', '', 'a', 'b', '', '', '', 'yahoo.com', '193', '', '2013-03-30 17:07:32', NULL),
(31, 2, 1, 'miocq@yahoo.com', '', '', 'a', 'b', '', '', '', 'yahoo.com', '192', '', '2013-03-30 17:07:32', NULL),
(32, 2, 1, 'madyh@yahoo.com', '', '', 'a', 'b', '', '', '', 'yahoo.com', '193', '', '2013-03-30 17:08:07', NULL),
(33, 2, 1, 'nbvfl@yahoo.com', '', '', 'a', 'b', '', '', '', 'm.com', '192', '', '2013-03-30 17:08:07', NULL),
(34, 2, 1, 'ucklc@yahoo.com', '', '', 'a', 'b', '', '', '', 'google.com', '192', '', '2013-03-30 17:08:07', NULL),
(35, 2, 1, 'sablj@yahoo.com', '', '', 'a', 'b', '', '', '', 'm.com', '192', '', '2013-03-30 17:08:07', NULL),
(36, 2, 1, 'flldr@yahoo.com', '', '', 'a', 'b', '', '', '', 'google.com', '192', '', '2013-03-30 17:08:07', NULL),
(37, 2, 1, 'xipro@yahoo.com', '', '', 'a', 'b', '', '', '', 'google.com', '193', '', '2013-03-30 17:08:07', NULL),
(38, 2, 1, 'waiqp@yahoo.com', '', '', 'a', 'b', '', '', '', 'google.com', '193', '', '2013-03-30 17:08:08', NULL),
(39, 1, 1, 'desc', 'remarks', 'compensation', '', '', '', '', '', '', '', '', '2013-04-01 19:04:52', 0),
(40, 1, 1, 'desc', 'remarks', 'compensation', '', '', '', '', '', '', '', '', '2013-04-01 19:05:21', 0),
(41, 1, 1, 'B', 'abc', 'no com', '', '', '', '', '', '', '', '', '2013-04-01 19:05:21', 0),
(42, 1, 1, 'C', 'defff', 'yes compen', '', '', '', '', '', '', '', '', '2013-04-01 19:05:21', 0),
(43, 1, 1, 'emp', 'clp', 'fe', 'fp', 'ph', 'state', 'city', 'link', 'proxy', 'remarks', '', '2013-04-15 16:03:28', NULL),
(44, 1, 1, 'email', 'emp', 'clp', 'fe', 'fp', 'ph', 'state', 'city', 'link', 'proxy', 'remarks', '2013-04-15 16:06:33', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `advertisement`
--

CREATE TABLE IF NOT EXISTS `advertisement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `title` varchar(1024) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `remarks` varchar(512) DEFAULT NULL,
  `last_posted_on` datetime DEFAULT NULL,
  `compensation` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `advertisement`
--

INSERT INTO `advertisement` (`id`, `user_id`, `country_id`, `title`, `description`, `remarks`, `last_posted_on`, `compensation`) VALUES
(1, 1, 1, 'Always involved in SEX', 'I''m 5''9. Medium length brunette, green eyes, long legs. I''m a cute bbw. Sexy when I need to be.I''m intelligent and I work hard for myself and my family. I''m a great homemaker as well.Reply if interested', '', '2013-03-23 15:03:13', 'A'),
(2, 1, 1, 'Always involved in SEX', 'I''m 5''9. Medium length brunette, green eyes, long legs. I''m a cute bbw. Sexy when I need to be.\nI''m intelligent and I work hard for myself and my family. I''m a great homemaker as well.\nReply if interested', '', '2013-03-23 16:21:59', 'B'),
(3, 1, 1, 'Job For USA', 'This is test job for usa', 'Official', '2013-03-30 18:53:43', 'Only usa people can apply'),
(16, 1, 1, 'A', 'B', 'abc', '2013-04-01 19:13:48', 'no com'),
(17, 1, 1, 'B', 'C', 'defff', '2013-04-01 19:13:48', 'yes compen');

-- --------------------------------------------------------

--
-- Table structure for table `country`
--

CREATE TABLE IF NOT EXISTS `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`id`, `code`, `name`) VALUES
(1, 'US', 'US'),
(2, 'UK', 'UK');

-- --------------------------------------------------------

--
-- Table structure for table `ngfxaccount`
--

CREATE TABLE IF NOT EXISTS `ngfxaccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_pass` varchar(255) DEFAULT NULL,
  `cl_pass` varchar(255) DEFAULT NULL,
  `fw_email` varchar(255) DEFAULT NULL,
  `fw_email_pass` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `proxy` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `last_posted_date` datetime DEFAULT NULL,
  `date_of_birth` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `verify_code1` varchar(255) DEFAULT NULL,
  `verify_code2` varchar(255) DEFAULT NULL,
  `working` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=24 ;

--
-- Dumping data for table `ngfxaccount`
--

INSERT INTO `ngfxaccount` (`id`, `user_id`, `country_id`, `email`, `email_pass`, `cl_pass`, `fw_email`, `fw_email_pass`, `phone`, `state`, `city`, `link`, `proxy`, `remarks`, `last_posted_date`, `date_of_birth`, `username`, `password`, `verify_code1`, `verify_code2`, `working`) VALUES
(22, 1, NULL, 'emp', 'foe', NULL, 'fop', 'un', NULL, NULL, NULL, NULL, NULL, NULL, '2013-04-15 15:55:56', '0000-00-00 00:00:00', 'up', 'vc1', 'vc2', 'dab', NULL),
(23, 1, NULL, 'email', 'emp', NULL, 'foe', 'fop', NULL, NULL, NULL, NULL, NULL, NULL, '2013-04-15 16:08:13', '0000-00-00 00:00:00', 'un', 'up', 'vc1', 'vc2', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `ngfxadvertisement`
--

CREATE TABLE IF NOT EXISTS `ngfxadvertisement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `title` varchar(1024) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `remarks` varchar(512) DEFAULT NULL,
  `last_posted_on` datetime DEFAULT NULL,
  `compensation` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `ngfxuser`
--

CREATE TABLE IF NOT EXISTS `ngfxuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `exp_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_login` tinyint(1) DEFAULT NULL,
  `is_master` tinyint(1) DEFAULT NULL,
  `account_type` tinyint(1) DEFAULT NULL,
  `number_of_account` int(10) unsigned DEFAULT NULL,
  `is_pva` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `ngfxuser`
--

INSERT INTO `ngfxuser` (`id`, `parent_id`, `country_id`, `username`, `password`, `status`, `exp_date`, `create_date`, `is_login`, `is_master`, `account_type`, `number_of_account`, `is_pva`) VALUES
(1, NULL, NULL, 'mithun', 'mithun', 1, NULL, NULL, 0, NULL, 2, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `post_info`
--

CREATE TABLE IF NOT EXISTS `post_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `uri` varchar(512) DEFAULT NULL,
  `udvalue1` varchar(512) DEFAULT NULL,
  `udvalue2` varchar(512) DEFAULT NULL,
  `udvalue3` varchar(512) DEFAULT NULL,
  `post_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `post_info`
--

INSERT INTO `post_info` (`id`, `user_id`, `country_id`, `user_name`, `uri`, `udvalue1`, `udvalue2`, `udvalue3`, `post_date`) VALUES
(1, 1, 1, 'mithun', 'http://springfieldil.craigslist.org/cas/3710286209.html', NULL, NULL, NULL, '2013-03-29 05:23:00');

-- --------------------------------------------------------

--
-- Table structure for table `script`
--

CREATE TABLE IF NOT EXISTS `script` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `email_domain` varchar(255) DEFAULT NULL,
  `fw_email` varchar(255) DEFAULT NULL,
  `fw_email_pass` varchar(255) DEFAULT NULL,
  `links` varchar(1024) DEFAULT NULL,
  `number_of_account` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `setting`
--

CREATE TABLE IF NOT EXISTS `setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `post_per_ip` int(11) DEFAULT NULL,
  `post_interval` int(11) DEFAULT NULL,
  `upper_age` int(11) DEFAULT NULL,
  `lower_age` int(11) DEFAULT NULL,
  `login_cl` tinyint(1) DEFAULT NULL,
  `use_location` tinyint(1) DEFAULT NULL,
  `random_subarea` tinyint(1) DEFAULT NULL,
  `use_age` tinyint(1) DEFAULT NULL,
  `use_fw_email` tinyint(1) DEFAULT NULL,
  `max_wait_time` int(11) DEFAULT NULL,
  `log_auto_scroll` tinyint(1) DEFAULT NULL,
  `post_type` varchar(255) DEFAULT NULL,
  `auto_post` tinyint(1) DEFAULT NULL,
  `change_ip` tinyint(1) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `use_proxy` tinyint(1) DEFAULT NULL,
  `postfix` varchar(255) DEFAULT NULL,
  `prefix` varchar(255) DEFAULT NULL,
  `vpn_name` varchar(255) DEFAULT NULL,
  `max_try` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Dumping data for table `setting`
--

INSERT INTO `setting` (`id`, `user_id`, `post_per_ip`, `post_interval`, `upper_age`, `lower_age`, `login_cl`, `use_location`, `random_subarea`, `use_age`, `use_fw_email`, `max_wait_time`, `log_auto_scroll`, `post_type`, `auto_post`, `change_ip`, `user_name`, `password`, `country_id`, `use_proxy`, `postfix`, `prefix`, `vpn_name`, `max_try`) VALUES
(8, 1, 1, 5, 23, 23, 0, 1, 1, 1, 0, 5, 0, 'cas', 0, 0, 'omar298', '6HnKyk56', 1, 0, 'B', 'A', '', 2),
(15, 2, 1, 0, 23, 23, 0, 0, 0, 0, 0, 0, 0, 'cas', 0, 0, '', '', 1, 0, '', '', '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user_account`
--

CREATE TABLE IF NOT EXISTS `user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `exp_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_login` tinyint(1) DEFAULT NULL,
  `is_master` tinyint(1) DEFAULT NULL,
  `account_type` tinyint(1) DEFAULT NULL,
  `number_of_account` int(10) unsigned DEFAULT NULL,
  `is_pva` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `user_account`
--

INSERT INTO `user_account` (`id`, `parent_id`, `country_id`, `username`, `password`, `status`, `exp_date`, `create_date`, `is_login`, `is_master`, `account_type`, `number_of_account`, `is_pva`) VALUES
(1, NULL, 1, 'mithun', 'mithun', 1, NULL, NULL, 0, 0, 1, 100, 1),
(2, 1, 1, 'prantoor', 'prantoor', 1, NULL, NULL, 0, NULL, 2, 2, 0),
(8, NULL, 1, 'admin', '@dm!n123', 1, NULL, NULL, 1, 1, 0, 10, NULL),
(9, 8, 1, 'robot', 'r@b@t', 1, NULL, NULL, 0, 1, 1, NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
