-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- ホスト: localhost:3306
-- 生成日時: 2025-04-01 01:47:57
-- サーバのバージョン： 5.7.24
-- PHP のバージョン: 7.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- データベース: `todojava_db`
--

-- --------------------------------------------------------

--
-- テーブルの構造 `list`
--

CREATE TABLE `list` (
  `id` int(11) NOT NULL COMMENT 'リストid',
  `list_name` varchar(255) NOT NULL COMMENT 'リスト名',
  `user_id` int(255) NOT NULL COMMENT 'ユーザid'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- テーブルのデータのダンプ `list`
--

INSERT INTO `list` (`id`, `list_name`, `user_id`) VALUES
(1, '転職', 3),
(2, '私用', 1),
(3, '３月２４日', 1),
(4, '私用', 2),
(5, '忘年会', 2),
(6, '神様天気予報', 2),
(7, '涙の訳', 1);

-- --------------------------------------------------------

--
-- テーブルの構造 `secret`
--

CREATE TABLE `secret` (
  `id` int(11) NOT NULL,
  `secret_tips_text` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- テーブルのデータのダンプ `secret`
--

INSERT INTO `secret` (`id`, `secret_tips_text`) VALUES
(1, '小学校の名前'),
(2, '中学校の名前'),
(3, '両親の旧姓');

-- --------------------------------------------------------

--
-- テーブルの構造 `task`
--

CREATE TABLE `task` (
  `id` int(11) NOT NULL COMMENT 'タスクid',
  `task_text` varchar(255) NOT NULL COMMENT 'タスク内容',
  `start_date` datetime DEFAULT NULL COMMENT '開始年月日',
  `end_date` datetime DEFAULT NULL COMMENT '終了年月日',
  `progress` int(255) DEFAULT '0' COMMENT '進捗率',
  `task_status` tinyint(1) DEFAULT '0' COMMENT 'タスクの状態',
  `user_id` int(255) NOT NULL COMMENT 'ユーザid',
  `list_id` int(255) NOT NULL COMMENT 'リストid'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- テーブルのデータのダンプ `task`
--

INSERT INTO `task` (`id`, `task_text`, `start_date`, `end_date`, `progress`, `task_status`, `user_id`, `list_id`) VALUES
(1, 'Todoリスト作成', '2025-03-24 16:48:45', '2025-03-25 16:48:45', 0, 0, 1, 1),
(2, 'タスク管理\r\n', '2025-03-23 20:48:47', '2025-03-24 20:48:47', NULL, NULL, 1, 1),
(3, '長髪', NULL, NULL, NULL, 0, 1, 4),
(4, '朝８時半に起きる', NULL, NULL, NULL, 0, 1, 3),
(5, '読書', NULL, NULL, 0, 0, 2, 4),
(6, 'お店探し', NULL, NULL, 0, 0, 2, 5),
(7, '車検費用を払いに行く', '2025-03-24 12:12:12', '2025-03-30 00:00:00', 0, 0, 1, 1);

-- --------------------------------------------------------

--
-- テーブルの構造 `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL COMMENT 'ユーザid',
  `email` varchar(255) NOT NULL COMMENT 'メールアドレス',
  `password` varchar(255) NOT NULL COMMENT 'パスワード',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日時',
  `secret_tips_id` int(255) NOT NULL COMMENT '秘密のパスワードのヒント',
  `secret_password` varchar(255) NOT NULL COMMENT '秘密のパスワード'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- テーブルのデータのダンプ `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `create_at`, `updated_at`, `secret_tips_id`, `secret_password`) VALUES
(1, 'akagi@gmail.com', 'aaaaaaaa', '2025-03-22 05:19:24', '2025-03-22 05:19:24', 1, '川永'),
(2, 'ryohei@gmail.com', 'aaaaaaaa', '2025-03-29 05:16:58', '2025-03-29 05:16:58', 1, '高積');

--
-- ダンプしたテーブルのインデックス
--

--
-- テーブルのインデックス `list`
--
ALTER TABLE `list`
  ADD PRIMARY KEY (`id`);

--
-- テーブルのインデックス `secret`
--
ALTER TABLE `secret`
  ADD PRIMARY KEY (`id`);

--
-- テーブルのインデックス `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id`);

--
-- テーブルのインデックス `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `email_2` (`email`),
  ADD UNIQUE KEY `email_3` (`email`);

--
-- ダンプしたテーブルの AUTO_INCREMENT
--

--
-- テーブルの AUTO_INCREMENT `list`
--
ALTER TABLE `list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'リストid', AUTO_INCREMENT=8;

--
-- テーブルの AUTO_INCREMENT `secret`
--
ALTER TABLE `secret`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- テーブルの AUTO_INCREMENT `task`
--
ALTER TABLE `task`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'タスクid', AUTO_INCREMENT=9;

--
-- テーブルの AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ユーザid', AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
