-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 13-07-2018 a las 00:06:59
-- Versión del servidor: 10.1.32-MariaDB
-- Versión de PHP: 5.6.36

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `db_service`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coordenadas`
--

CREATE TABLE `coordenadas` (
  `Id` int(11) NOT NULL,
  `Longitud` varchar(100) NOT NULL,
  `Latitud` varchar(100) NOT NULL,
  `Mac` varchar(100) NOT NULL,
  `FechaHora` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `coordenadas`
--

INSERT INTO `coordenadas` (`Id`, `Longitud`, `Latitud`, `Mac`, `FechaHora`) VALUES
(69, '-75.5584474', '6.196245', '0001', '2018-07-12 02:07:00');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `coordenadas`
--
ALTER TABLE `coordenadas`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `coordenadas`
--
ALTER TABLE `coordenadas`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
