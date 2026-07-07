# 隨機選餐系統

依照 MVC + DAO 架構設計的隨機選餐系統。用來解決「一餐要吃什麼」的世紀難題。
Maven 專案，包含登入 / 註冊 / 三種角色權限、選餐歷史紀錄、隨機選餐排除功能。

## 角色與權限

| 角色 | 說明 | 功能 |
|---|---|---|
| 一般使用者（訪客） | 不需登入，不記錄任何資料 | 隨機選餐（不可篩選類型、不可排除）、註冊 |
| 會員 | 需登入，操作會記錄到資料庫 | 新增 / 修改 / 刪除 / 清空欄位 / 隨機選餐（可篩選類型、可排除）/ 查詢自己的選餐歷史 |
| 管理員 | 需登入 | 會員的全部功能 + 會員管理（CRUD 帳號）+ 查詢所有人的選餐歷史 |

程式啟動後會先顯示登入畫面，可選擇「登入」「註冊」或「訪客使用」。

## 專案架構（Maven 標準結構）

```
RandomMealPicker/
├── pom.xml
├── sql/
│   ├── schema.sql                    全新環境：建立資料庫、全部資料表與種子資料
│   └── migration_meal_history.sql    既有環境：只新增 meal_history 資料表
├── src/main/java/
│   ├── app/App.java                  程式進入點
│   ├── model/                        Food, User, Role, MealHistory
│   ├── ui/
│   │   ├── LoginFrame                登入入口
│   │   ├── RegisterFrame             註冊（固定建立會員身分）
│   │   ├── GuestFrame                訪客（僅隨機選餐 + 註冊）
│   │   ├── MemberFrame               會員完整功能（CRUD + 隨機選餐 + 排除 + 選餐歷史）
│   │   ├── AdminFrame                繼承 MemberFrame，加上會員管理
│   │   ├── UserManageFrame           會員管理 CRUD（管理員專用）
│   │   └── MealHistoryFrame          選餐歷史查詢（會員看自己 / 管理員看全部）
│   ├── controller/                   FoodController, AuthController,
│   │                                 UserController, MealHistoryController
│   ├── service/                      FoodService, UserService, MealHistoryService
│   ├── service/impl/                 對應的 ServiceImpl（密碼雜湊、隨機邏輯、歷史記錄都在這層）
│   ├── dao/                          FoodDAO, UserDAO, MealHistoryDAO
│   ├── dao/impl/                     對應的 DAOImpl（JDBC 實作）
│   └── util/                         DBConnection, PasswordUtil（BCrypt）
```

## 資料庫欄位

### food 資料表（不變）
| 欄位 | 型態 | 說明 |
|---|---|---|
| id | INT (PK, AUTO_INCREMENT) | 主鍵 |
| name | VARCHAR(100) | 名稱 |
| type | VARCHAR(20) | 類型，由 Java 端 JComboBox 限制選項（目前含：中式/日式/西式/速食/其他） |

### user 資料表（不變）
| 欄位 | 型態 | 說明 |
|---|---|---|
| id | INT (PK, AUTO_INCREMENT) | 主鍵 |
| username | VARCHAR(50) UNIQUE | 帳號 |
| password | VARCHAR(100) | BCrypt 雜湊後的密碼 |
| role | VARCHAR(20) | MEMBER 或 ADMIN |

### meal_history 資料表（新增）
| 欄位 | 型態 | 說明 |
|---|---|---|
| id | INT (PK, AUTO_INCREMENT) | 主鍵 |
| user_id | INT (FK → user.id, ON DELETE CASCADE) | 選餐的會員/管理員 id；該帳號被刪除時，歷史也會一併刪除 |
| food_id | INT (FK → food.id, 可 NULL, ON DELETE SET NULL) | 對應餐點；餐點被刪除後這欄變 NULL，但不影響下面兩個快照欄位 |
| food_name | VARCHAR(100) | **選餐當下的名稱快照**，確保餐點被刪除/改名後歷史紀錄依然準確 |
| food_type | VARCHAR(20) | **選餐當下的類型快照** |
| picked_at | DATETIME (預設 CURRENT_TIMESTAMP) | 選餐時間 |

刪除皆採真刪除。密碼一律使用 **BCrypt** 加密。

## 建置步驟

### 1. 建立 / 更新資料庫

**全新環境**：執行完整的 `sql/schema.sql`
```
mysql -u root -p < sql/schema.sql
```

**已有既有資料庫**
```
mysql -u root -p < sql/migration_meal_history.sql
```

預設管理員帳號：`admin` / `admin123`（僅能由 SQL 種子資料建立，註冊功能固定只會建立「會員」身分）

### 2. 匯入 Eclipse

`File` → `Import` → `Existing Maven Projects`，選擇 `RandomMealPicker` 資料夾，Maven 會自動下載 `mysql-connector-j` 與 `jbcrypt` 依賴。

### 3. 確認資料庫連線資訊

`src/main/java/util/DBConnection.java` 已設定好連線資訊，如果你的 MySQL 環境有變動再自行調整。

### 4. 執行

執行 `src/main/java/app/App.java`，或指令：
```
mvn compile exec:java
```

## 新增功能說明

### 選餐歷史
- 會員 / 管理員每次成功「隨機選餐」，結果都會自動寫入 `meal_history`
- 在 MemberFrame（會員/管理員都適用）新增「選餐歷史」按鈕，開啟查詢畫面：
  - **會員**登入查詢：只看得到自己的歷史（時間、名稱、類型）
  - **管理員**登入查詢：看得到所有人的歷史，多一欄「帳號」，可知道是誰選的
- 訪客不會產生任何歷史紀錄（訪客本來就不需登入，也沒有 user_id 可以記錄）

### 隨機選餐排除功能
- 在餐點表格中選取一列 → 按「排除」鈕 → 加入排除清單，畫面上的「已排除」會即時顯示目前排除了哪些餐點
- 可以排除多筆，按「清空排除」可一次重置
- 按下「隨機選餐！」時，會從「符合目前類型篩選 **且** 不在排除清單中」的餐點裡挑選
- 排除清單只存在於這次視窗開啟期間（記憶體中），**不會寫入資料庫**，關閉視窗重新登入就會重置
- 此功能僅適用於會員 / 管理員畫面（訪客畫面沒有餐點表格可供選取，維持原本「不可篩選、不可排除」的簡化設計）

## 可能的擴充方向（供未來參考）

- 加入權重隨機（例如常吃的降低權重）
- 加入評分欄位
- 選餐歷史增加統計圖表（例如最常吃的類型）

