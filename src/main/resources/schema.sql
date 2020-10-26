DROP TABLE IF EXISTS billionaires;

CREATE TABLE `patreonkeys` (
  `idpatreonKeys` int NOT NULL AUTO_INCREMENT,
  `accessToken` varchar(255) NOT NULL,
  `expires` int NOT NULL,
  `refreshToken` varchar(255) NOT NULL,
  `patreonId` int NOT NULL,
  PRIMARY KEY (`idpatreonKeys`)
);