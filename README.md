# 隨機選餐系統

依照 MVC + DAO 架構設計的隨機選餐系統。用來解決「一餐要吃什麼」的世紀難題。
本版為 **Maven 專案**，並加入登入 / 註冊 / 三種角色權限功能。

## 角色與權限

| 角色 | 說明 | 功能 |
|---|---|---|
| 一般使用者（訪客） | 不需登入，不記錄任何資料 | 隨機選餐（不可篩選類型）、註冊 |
| 會員 | 需登入，操作會記錄到資料庫 | 新增 / 修改 / 刪除 / 清空欄位 / 隨機選餐（可篩選類型） |
| 管理員 | 需登入 | 會員的全部功能 + 會員管理（CRUD 帳號） |

程式啟動後會先顯示登入畫面，可選擇「登入」「註冊」或「訪客使用」。

## 專案架構（Maven 標準結構）

```
RandomMealPicker/
├── pom.xml
├── sql/schema.sql              建立資料庫、資料表與管理員種子帳號
├── src/main/java/
│   ├── app/App.java             程式進入點
│   ├── model/                   Food.java, User.java, Role.java
│   ├── ui/                      LoginFrame, RegisterFrame, GuestFrame,
│   │                            MemberFrame, AdminFrame, UserManageFrame
│   ├── controller/              FoodController, AuthController, UserController
│   ├── service/                 FoodService, UserService
│   ├── serviceimpl/             FoodServiceImpl, UserServiceImpl（含密碼驗證邏輯）
│   ├── dao/                     FoodDAO, UserDAO
│   ├── daoimpl/                 FoodDAOImpl, UserDAOImpl（JDBC 實作）
│   └── util/                    DBConnection.java, PasswordUtil.java（BCrypt）
```

## 資料庫欄位

### food 資料表
| 欄位 | 型態 | 說明 |
|---|---|---|
| id | INT (PK, AUTO_INCREMENT) | 主鍵 |
| name | VARCHAR(100) | 名稱 |
| type | VARCHAR(20) | 類型（中式/日式/速食/其他），由 Java 端 JComboBox 限制選項 |

### user 資料表
| 欄位 | 型態 | 說明 |
|---|---|---|
| id | INT (PK, AUTO_INCREMENT) | 主鍵 |
| username | VARCHAR(50) UNIQUE | 帳號 |
| password | VARCHAR(100) | BCrypt 雜湊後的密碼（絕不儲存明碼） |
| role | VARCHAR(20) | MEMBER 或 ADMIN |

刪除皆採真刪除。密碼一律使用 **BCrypt** 加鹽雜湊（`org.mindrot:jbcrypt`），Service 層以外看不到明碼密碼。

## 建置步驟

### 1. 建立資料庫

在 MySQL 8.0 中執行 `sql/schema.sql`：

```
mysql -u root -p < sql/schema.sql
```

會建立 `food_selector_db` 資料庫、`food` / `user` 資料表，並附上範例餐點資料，以及一組預設管理員帳號：

```
帳號：admin
密碼：admin123
```

> 註冊功能只會建立「會員」身分，管理員帳號僅能透過此 SQL 種子資料建立（或由管理員在「會員管理」畫面手動新增）。

### 2. 匯入 Eclipse

`File` → `Import` → `Existing Maven Projects`，選擇 `RandomMealPicker` 資料夾。
Eclipse（搭配 m2e 外掛）會自動下載 `pom.xml` 中定義的依賴：
- `com.mysql:mysql-connector-j:8.4.0`
- `org.mindrot:jbcrypt:0.4`

不需要再手動下載 jar 或設定 Build Path。

### 3. 設定資料庫連線資訊

打開 `src/main/java/util/DBConnection.java`，依照你自己的 MySQL 環境修改：

```java
private static final String USER = "root";
private static final String PASSWORD = ""; // 改成你自己的 MySQL 密碼
```

預設連線位址為 `localhost:3306`，資料庫名稱為 `food_selector_db`，如果你的環境不同也請一併修改 URL。

### 4. 執行

執行 `src/main/java/app/App.java`（右鍵 → Run As → Java Application）。

也可以在專案根目錄用指令執行：

```
mvn compile exec:java
```

或打包成可直接執行的 fat-jar：

```
mvn clean package
java -jar target/RandomMealPicker.jar
```

## 功能說明

### 訪客（免登入）
- 隨機選餐：從全部餐點中隨機抽選一筆（不提供類型篩選）
- 註冊：前往註冊畫面，成為會員

### 會員（登入後）
- 新增 / 修改 / 刪除 / 清空欄位：管理餐點資料
- 隨機選餐：可先選擇類型（或選「全部」）再抽選
- 登出

### 管理員（登入後）
- 擁有會員的全部功能
- 會員管理：可新增 / 修改 / 刪除會員或管理員帳號
  - 新增時密碼必填
  - 修改時密碼欄位留空 = 不變更密碼

## 關於 WindowBuilder

各個 Frame（`LoginFrame`、`RegisterFrame`、`GuestFrame`、`MemberFrame`、`UserManageFrame`）皆使用絕對座標（`setBounds`）建立版面，可直接在 Eclipse 中安裝 WindowBuilder 外掛後，以視覺化編輯器開啟繼續調整版面配置與元件外觀。

`AdminFrame` 繼承自 `MemberFrame`，僅新增「會員管理」按鈕，本身沒有額外版面配置，因此不適合用 WindowBuilder 開啟（繼承關係的視覺化編輯支援有限），如需調整請直接修改程式碼。

## 已實際編譯驗證

所有 Java 原始碼已在本機以 JDK 21（相容 Java 11 語法）搭配對應的 `mysql-connector-j` / `jbcrypt` 原始碼實際編譯過，並額外驗證過 `admin123` 的 BCrypt 雜湊值可被正確驗證，確保交付的程式碼沒有語法或型別錯誤。實際的 MySQL 資料庫連線與 UI 互動行為，仍請在你自己的環境中測試。

## 可能的擴充方向（供未來參考）

- 加入權重隨機（例如常吃的降低權重）
- 加入評分欄位
- 加入「排除清單」功能（例如今天不想吃的先排除再抽）
- 會員個人化記錄（例如記錄每個會員的選餐歷史）
- 加入飲料隨機
  
