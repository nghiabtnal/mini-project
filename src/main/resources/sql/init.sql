SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `demo`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
                         `id` bigint UNSIGNED NOT NULL,
                         `name` varchar(255) CHARACTER SET NOT NULL,
                         `email` varchar(255) CHARACTER SET NOT NULL,
) ENGINE=InnoDB;