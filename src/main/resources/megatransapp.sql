-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-02-2025 a las 01:03:32
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `megatransapp`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_encomienda`
--

CREATE TABLE `detalle_encomienda` (
  `id` int(11) NOT NULL,
  `apellido_d` varchar(255) DEFAULT NULL,
  `correo_d` varchar(255) DEFAULT NULL,
  `dir_remitente` varchar(255) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `identificacion_d` varchar(255) DEFAULT NULL,
  `nombre_d` varchar(255) DEFAULT NULL,
  `num_guia` varchar(255) NOT NULL,
  `referencia_d` varchar(255) DEFAULT NULL,
  `ruta` varchar(255) DEFAULT NULL,
  `telf_beneficiario` varchar(255) DEFAULT NULL,
  `telf_encargado` varchar(255) DEFAULT NULL,
  `tipo_entrega` varchar(255) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_transporte`
--

CREATE TABLE `detalle_transporte` (
  `id` bigint(20) NOT NULL,
  `cantidad_estibaje` int(11) DEFAULT NULL,
  `descripcion_producto` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `estibaje` bit(1) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `num_orden` varchar(255) DEFAULT NULL,
  `pago` varchar(255) DEFAULT NULL,
  `id_dir_destino` bigint(20) DEFAULT NULL,
  `id_dir_origen` bigint(20) DEFAULT NULL,
  `unidad_id` int(11) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  `tipo_servicio` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detalle_transporte`
--

INSERT INTO `detalle_transporte` (`id`, `cantidad_estibaje`, `descripcion_producto`, `estado`, `estibaje`, `fecha`, `num_orden`, `pago`, `id_dir_destino`, `id_dir_origen`, `unidad_id`, `usuario_id`, `tipo_servicio`) VALUES
(1, 2, 'Pollos', 'FINALIZADO', b'1', '2025-02-02', 'TM00001', 'EFECTIVO', 2, 1, 3, 1, 'MERCADERIA'),
(2, 2, 'Pollos', 'PENDIENTE', b'1', '2025-02-04', 'TM00002', 'EFECTIVO', 4, 3, 3, 1, 'MERCADERIA'),
(9, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00003', 'EFECTIVO', 22, 21, 3, 1, 'MERCADERIA'),
(10, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00004', 'EFECTIVO', 34, 33, 3, 1, 'MERCADERIA'),
(11, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00005', 'EFECTIVO', 36, 35, 3, 1, 'MERCADERIA'),
(12, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00006', 'EFECTIVO', 38, 37, 3, 1, 'MERCADERIA'),
(13, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00007', 'EFECTIVO', 40, 39, 3, 1, 'MERCADERIA'),
(14, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00008', 'EFECTIVO', 42, 41, 3, 1, 'MERCADERIA'),
(15, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00009', 'EFECTIVO', 44, 43, 3, 1, 'MERCADERIA'),
(16, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00010', 'EFECTIVO', 46, 45, 3, 1, 'MERCADERIA'),
(17, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00011', 'EFECTIVO', 48, 47, 3, 1, 'MERCADERIA'),
(18, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-11', 'TM00012', 'EFECTIVO', 50, 49, 3, 1, 'MERCADERIA'),
(19, 1, '1', 'PENDIENTE', b'1', '2025-02-12', 'TM00013', 'EFECTIVO', 52, 51, 1, 9, 'MUDANZA'),
(20, 0, 'carnes', 'PENDIENTE', b'0', '2025-02-12', 'TM00014', 'EFECTIVO', 54, 53, 3, 1, 'MERCADERIA'),
(21, 2, '2', 'PENDIENTE', b'1', '2025-02-13', 'TM00015', 'TRANSFERENCIA', 56, 55, 1, 9, 'DISTRIBUCION'),
(22, 1, 'Saco de papas', 'PENDIENTE', b'1', '2025-02-14', 'TM00016', 'EFECTIVO', 58, 57, 1, 1, 'DISTRIBUCION'),
(23, NULL, 'Muebles ', 'PENDIENTE', b'0', '2025-02-16', 'TM00017', 'EFECTIVO', 60, 59, 1, 1, 'DISTRIBUCION');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `direccion`
--

CREATE TABLE `direccion` (
  `id` bigint(20) NOT NULL,
  `barrio` varchar(255) NOT NULL,
  `calle_principal` varchar(255) NOT NULL,
  `calle_secundaria` varchar(255) DEFAULT NULL,
  `ciudad` varchar(255) NOT NULL,
  `latitud` double NOT NULL,
  `longitud` double NOT NULL,
  `referencia` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `direccion`
--

INSERT INTO `direccion` (`id`, `barrio`, `calle_principal`, `calle_secundaria`, `ciudad`, `latitud`, `longitud`, `referencia`, `telefono`) VALUES
(1, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(2, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(3, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(4, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(5, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(6, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(7, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(8, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(9, 'asd', 'gjgyh', 'ygyug', 'yguygyu', 89798, 7987987, 'guyguy', '7987987'),
(10, 'gyugyu', 'uygyug', 'yuguyg', 'uyguyg', 8979879, 897987, 'uygyug', 'yuguygu'),
(11, 'gjhg', 'jghjgjh', 'hjgjhgjh', 'jhgjhg', 7987987, 987987, 'gjhgjhg', '897789798'),
(12, 'sdfsdsfd', 'dfsdfs', 'fdsfdsfd', 'sfdsfds', 56354354, 45354345, 'sdsfdsfdsdf', '35354354'),
(13, 'asd', 'asdASD', 'ASD', 'Quinta Chica', 123213, 123213, '1231231', '0983724721'),
(14, 'hiuhui', 'jhgjhg', 'ytuytdtydytjd', 'jhgjhg', 1232131, 12313, 'gjhgjhg', '12312313'),
(15, 'asd', 'asdASD', 'ASD', 'Quinta Chica', 123213, 123213, '1231231', '0983724721'),
(16, 'hiuhui', 'jhgjhg', 'ytuytdtydytjd', 'jhgjhg', 1232131, 12313, 'gjhgjhg', '12312313'),
(17, 'molleasda', 'oaoa', 'ostias', 'cuenca', 12, 12, 'casa', '012931238'),
(18, 'asogues', 'asogues', 'asigues', 'asogues', 12319, 12319, 'casauutau', '29123813098'),
(19, 'aaa', 'aaa', 'aaa', 'aaa', 222, 222, 'aaaa', '22222222'),
(20, 'bbbb', 'bbbb', 'bbbb', 'bbbbb', 7777, 7777, 'bbbb', '999999999'),
(21, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(22, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(23, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(24, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(25, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(26, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(27, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(28, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(29, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(30, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(31, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(32, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(33, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(34, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(35, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(36, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(37, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(38, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(39, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(40, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(41, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(42, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(43, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(44, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(45, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(46, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(47, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(48, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(49, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(50, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(51, '1', '1', '1', '1', 1, 1, '1', '1'),
(52, '1', '1', '1', '1', 1, 1, '1', '1'),
(53, 'Quinta Chica Alta', 'San Pablo de Lago', 'Cubilche', 'Cuenca', 10.1234, -10.1234, 'Frente a Ideal Alambreak', '0987654321'),
(54, 'Quinta Chica Baja', 'LLanaurco', 'Cerezoa', 'Cuenca', 11.5678, -11.5678, 'Frente al colegio Mario Rizini', '0987654321'),
(55, '2', '2', '2', '2', 2, 2, '2', '2'),
(56, '2', '2', '2', '2', 2, 2, '2', '2'),
(57, 'Quinta Chica', 'San Pablo De Lago', 'Quinta Chica Alta', 'Cuenca', -2.883669, -78.970617, 'Frente a ideal alambrek', '0983724721'),
(58, 'Norte Cuenca nose', 'Abraham Sarmiento', 'Urbanización Del Molino del Arco', 'Cuenca', -2.890066, -79.014273, 'casa blanca techo rojo', '0987654321'),
(59, 'Quinta Chica Alta', 'San Pablo De Lago', 'cubilche', 'Cuenca', -2.883809, -78.970413, 'ideal alambrak', '0983724721'),
(60, 'CONDOMINIO SANTA CLARA', 'Avenida Luis Monsalve Pozo', 'cualkiera', 'Azogues', -2.738672, -78.852997, 'casa balcna techo rojo', '0987654321');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `encomienda_producto`
--

CREATE TABLE `encomienda_producto` (
  `id` int(11) NOT NULL,
  `id_detalle_encomienda` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `id` int(11) NOT NULL,
  `alto` double DEFAULT NULL,
  `ancho` double DEFAULT NULL,
  `fragil` bit(1) NOT NULL,
  `largo` double DEFAULT NULL,
  `peso` double DEFAULT NULL,
  `tipo_producto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id` int(11) NOT NULL,
  `rol_nombre` enum('ROLE_ADMIN','ROLE_EMPL','ROLE_USER') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id`, `rol_nombre`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_EMPL');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `unidad`
--

CREATE TABLE `unidad` (
  `id` int(11) NOT NULL,
  `altura` double DEFAULT NULL,
  `ancho` double DEFAULT NULL,
  `imagen_url` varchar(255) DEFAULT NULL,
  `largo` double DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `tipo_cajon` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `unidad`
--

INSERT INTO `unidad` (`id`, `altura`, `ancho`, `imagen_url`, `largo`, `tipo`, `tipo_cajon`) VALUES
(1, 3.41, 3.21, '/uploads/1738556859202_b8638456-5cf9-4c39-9285-686b985da6f2.jpg', 6.21, 'Tipo 3', 'furgon'),
(3, 1.5, 2.3, '/uploads/1738638525738_d4129e93-9802-4256-a648-ded58c898ca0.jpg', 2.2, 'Tipo 2', 'Paila'),
(4, 89, 78, '/uploads/1739055345264_cancelar.png', 78, '7878', '87'),
(5, 3.5, 2.5, '/uploads/1739643564963_50404033-7cc2-49ee-94b8-4e75ac8b261e.jpg', 7, 'Tipo ', 'Cajon');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `identificacion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `nombre_usuario` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `apellido`, `identificacion`, `nombre`, `nombre_usuario`, `password`, `telefono`) VALUES
(1, 'Tapia', '0106133291', 'Franklin', 'franxxdmsk@admin.com', '$2a$10$qgjzo4k2Al3VwmMueTADu.DYBHgXJtUtMrZzGNpGs4eyr0MIENxlC', '0983724721'),
(2, 'user', '0105922447', 'user', 'user@user.com', '$2a$10$G6yPB.Y8rfyiTyC5i7Zv1uRAixOqB6ytPvdOG0ylF2KOo265tWcdW', '0987654322'),
(3, 'Leon', '0106229255', 'Carla', 'ani1210', '$2a$10$iy3jLvsRzfnr4CcRMXlxUuxDV.O3FJ5.FDytXrwrPGB4YXGLS0BXm', '0987654321'),
(4, 'nic', '1827465738', 'ion', 'ionic@ionic.com', '$2a$10$3QC7RR0qeZUTQWns4l1Z6uu4WzXMt3zvsETQ7/8nrPHHA7OosqcyO', '0192378462'),
(5, 'lar', '0192849230', 'angu', 'angular', '$2a$10$Q273bdeaaZTdXpj1TJxTXegFaQExMV8ts4C7EyuSxL33Zne5fpWES', '0192847382'),
(6, 'angular', '0987654321', 'material', 'material', '$2a$10$G6XNu8AUwBd0KsalFQj60OdKBYrl63RC7IlY/00YHWPd1SBeSN1BS', '0987654321'),
(7, 'eado', '0107677876', 'empl', 'empleado', '$2a$10$R82i/OQx/wxGKeCjUw8RkOgBRJP4nmN2c/pYLPCskLLbrQlNcANei', '0987654322'),
(8, 'a', 'a', 'a', 'a', '$2a$10$eqyMWbvJXvSOm03kf.PCbe6stx6nwRvrJAk/KN/dVd0a7HLoilASO', 'a'),
(9, 'Moscoso', '0109877656', 'Fabian', 'fabian@gmail.com', '$2a$10$Xln5oKyyjIX1g73Va1Zg0ucO/bL9zPzRyXkEfWT3TXvJe0n8rIW3O', '0987654321');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_rol`
--

CREATE TABLE `usuario_rol` (
  `usuario_id` int(11) NOT NULL,
  `rol_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario_rol`
--

INSERT INTO `usuario_rol` (`usuario_id`, `rol_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 2),
(5, 2),
(6, 2),
(7, 3),
(8, 3),
(9, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `detalle_encomienda`
--
ALTER TABLE `detalle_encomienda`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKlmavdmkssl3otaht4u2v9xrci` (`num_guia`),
  ADD KEY `FKj1rvgi2a30vtrqvxl3cn2ret3` (`usuario_id`);

--
-- Indices de la tabla `detalle_transporte`
--
ALTER TABLE `detalle_transporte`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqumd2wyhbkq5yyngeyx1uhadm` (`id_dir_destino`),
  ADD KEY `FKgonxg6lc0mwaq4kpejvs01ppv` (`id_dir_origen`),
  ADD KEY `FK8ho3xkg3l3usd07mys85e6nbr` (`unidad_id`),
  ADD KEY `FKok5xw86yws0m42ik1f5m02hjm` (`usuario_id`);

--
-- Indices de la tabla `direccion`
--
ALTER TABLE `direccion`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `encomienda_producto`
--
ALTER TABLE `encomienda_producto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5vws5jmmy6jdnmub3ibyj8348` (`id_detalle_encomienda`),
  ADD KEY `FK2fdf2snxoq0fwq3psmr0fjewx` (`id_producto`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `unidad`
--
ALTER TABLE `unidad`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKpuhr3k3l7bj71hb7hk7ktpxn0` (`nombre_usuario`);

--
-- Indices de la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD PRIMARY KEY (`usuario_id`,`rol_id`),
  ADD KEY `FK610kvhkwcqk2pxeewur4l7bd1` (`rol_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `detalle_encomienda`
--
ALTER TABLE `detalle_encomienda`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_transporte`
--
ALTER TABLE `detalle_transporte`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `direccion`
--
ALTER TABLE `direccion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT de la tabla `encomienda_producto`
--
ALTER TABLE `encomienda_producto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `unidad`
--
ALTER TABLE `unidad`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalle_encomienda`
--
ALTER TABLE `detalle_encomienda`
  ADD CONSTRAINT `FKj1rvgi2a30vtrqvxl3cn2ret3` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Filtros para la tabla `detalle_transporte`
--
ALTER TABLE `detalle_transporte`
  ADD CONSTRAINT `FK8ho3xkg3l3usd07mys85e6nbr` FOREIGN KEY (`unidad_id`) REFERENCES `unidad` (`id`),
  ADD CONSTRAINT `FKgonxg6lc0mwaq4kpejvs01ppv` FOREIGN KEY (`id_dir_origen`) REFERENCES `direccion` (`id`),
  ADD CONSTRAINT `FKok5xw86yws0m42ik1f5m02hjm` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `FKqumd2wyhbkq5yyngeyx1uhadm` FOREIGN KEY (`id_dir_destino`) REFERENCES `direccion` (`id`);

--
-- Filtros para la tabla `encomienda_producto`
--
ALTER TABLE `encomienda_producto`
  ADD CONSTRAINT `FK2fdf2snxoq0fwq3psmr0fjewx` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`),
  ADD CONSTRAINT `FK5vws5jmmy6jdnmub3ibyj8348` FOREIGN KEY (`id_detalle_encomienda`) REFERENCES `detalle_encomienda` (`id`);

--
-- Filtros para la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD CONSTRAINT `FK610kvhkwcqk2pxeewur4l7bd1` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`),
  ADD CONSTRAINT `FKbyfgloj439r9wr9smrms9u33r` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
