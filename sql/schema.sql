-- ======================================================
-- 隨機選餐系統 - 資料庫建置腳本
-- MySQL 8.0
-- ======================================================

CREATE DATABASE IF NOT EXISTS food_selector_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE food_selector_db;

-- ------------------------------------------------------
-- 餐點資料表
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS food (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20)  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 範例餐點資料（可自行刪除）
INSERT INTO food (name, type) VALUES
('滷肉飯', '中式'),
('拉麵', '日式'),
('麥當勞', '速食'),
('牛肉麵', '中式'),
('壽司', '日式'),
('肯德基', '速食'),
('鹽酥雞', '其他');

-- ------------------------------------------------------
-- 使用者資料表（會員 / 管理員；訪客不記錄）
-- role 僅限 'MEMBER' 或 'ADMIN'
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,   -- BCrypt 雜湊後的字串
    role     VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 預設管理員帳號：帳號 admin / 密碼 admin123
-- 密碼已使用 BCrypt 雜湊（$2a$ 格式，與程式內 jBCrypt 相容）
INSERT INTO user (username, password, role) VALUES
('admin', '$2a$12$J5nVcpXobbXzD.4tovgjb.r5MZl6kY2d3E93722km3aD9vH5wU7bC', 'ADMIN');
