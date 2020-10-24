DROP TABLE IF EXISTS billionaires;

CREATE TABLE `patreonkeys` (
  `idpatreonKeys` int NOT NULL AUTO_INCREMENT,
  `accessToken` varchar(45) NOT NULL,
  `expires` int NOT NULL,
  `refreshToken` varchar(45) NOT NULL,
  `patreonId` int NOT NULL,
  PRIMARY KEY (`idpatreonKeys`)
);