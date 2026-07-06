-- ======================================================
-- 隨機選餐系統 - 資料庫建置腳本
-- MySQL 8.0
-- ======================================================

CREATE DATABASE IF NOT EXISTS food_selector_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE food_selector_db;

CREATE TABLE IF NOT EXISTS food (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20)  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 範例資料（可自行刪除）
INSERT INTO food (name, type) VALUES
('滷肉飯', '中式'),
('拉麵', '日式'),
('麥當勞', '速食'),
('牛肉麵', '中式'),
('壽司', '日式'),
('肯德基', '速食'),
('鹽酥雞', '其他');
