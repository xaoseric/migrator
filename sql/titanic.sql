CREATE TABLE IF NOT EXISTS `titanic_analytics` (
    `passenger_id` int(11) NOT NULL,
    `survived` int(11) DEFAULT NULL,
    `pclass` int(11) DEFAULT NULL,
    `name` varchar(500) DEFAULT NULL,
    `sex` varchar(255) DEFAULT NULL,
    `age` float DEFAULT NULL,
    `sibsp` int(11) DEFAULT NULL,
    `parch` int(11) DEFAULT NULL,
    `ticket` varchar(255) DEFAULT NULL,
    `fare` decimal(10,2) DEFAULT NULL,
    `cabin` varchar(255) DEFAULT NULL,
    `embarked` varchar(10) DEFAULT NULL,
    PRIMARY KEY (`passenger_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;