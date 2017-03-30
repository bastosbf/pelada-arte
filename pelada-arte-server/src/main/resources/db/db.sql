DROP TABLE IF EXISTS `rate`;
DROP TABLE IF EXISTS `pelada_player`;
DROP TABLE IF EXISTS `pelada`;
DROP TABLE IF EXISTS `player`;
--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(500) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table structure for table `pelada`
--

CREATE TABLE `pelada` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `day_of_the_week` varchar(1) NOT NULL,
  `time` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_owner_idx` (`owner`),
  CONSTRAINT `fk_owner` FOREIGN KEY (`owner`) REFERENCES `player` (`uid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table structure for table `pelada_player`
--

CREATE TABLE `pelada_player` (
  `player` int(11) NOT NULL,
  `pelada` int(11) NOT NULL,
  PRIMARY KEY (`player`,`pelada`),
  KEY `fk_pelada_idx` (`pelada`),
  CONSTRAINT `fk_pelada` FOREIGN KEY (`pelada`) REFERENCES `pelada` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_player` FOREIGN KEY (`player`) REFERENCES `player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `rate`
--

CREATE TABLE `rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pelada` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `rate_from` int(11) NOT NULL,
  `rate_to` int(11) NOT NULL,
  `rate` float NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `rate_UNIQUE` (`pelada`,`date`,`rate_from`,`rate_to`),
  KEY `fk_rate_from_idx` (`rate_from`),
  KEY `fk_pelada_idx` (`pelada`),
  KEY `fk_rate_to_idx` (`rate_to`),
  CONSTRAINT `fk_rate_from` FOREIGN KEY (`rate_from`) REFERENCES `player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rate_pelada` FOREIGN KEY (`pelada`) REFERENCES `pelada` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rate_to` FOREIGN KEY (`rate_to`) REFERENCES `player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;