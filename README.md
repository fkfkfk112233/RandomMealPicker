# 隨機選餐系統

依照 MVC + DAO 架構設計的隨機選餐系統。用來解決「一餐要吃什麼」的世紀難題。

## 專案架構

```
RandomMealPicker/
├── src/
│   ├── model/          Food.java                資料模型
│   ├── ui/              MainFrame.java           JFrame 主畫面
│   ├── controller/      FoodController.java      銜接 UI 與 Service
│   ├── service/         FoodService.java         業務邏輯介面
│   ├── serviceimpl/     FoodServiceImpl.java     業務邏輯實作（含隨機選餐邏輯）
│   ├── dao/             FoodDAO.java             資料存取介面
│   ├── daoimpl/         FoodDAOImpl.java         JDBC CRUD 實作
│   └── util/            DBConnection.java        資料庫連線工具
├── sql/schema.sql        建立資料庫與資料表的 SQL 腳本
├── lib/                  請將 mysql-connector-j 的 jar 放在這裡
├── .project / .classpath Eclipse 專案設定檔（可直接匯入）
```

## 資料庫欄位（food 資料表）

| 欄位 | 型態 | 說明 |
|---|---|---|
| id | INT (PK, AUTO_INCREMENT) | 主鍵 |
| name | VARCHAR(100) | 名稱 |
| type | VARCHAR(20) | 類型（中式/日式/西式/速食/其他），由 Java 端 JComboBox 限制選項，資料庫不做強制約束，方便未來擴充類型 |

刪除採真刪除（`DELETE FROM food WHERE id = ?`）。

## 建置步驟

### 1. 建立資料庫

在 MySQL 8.0 中執行 `sql/schema.sql`：

```
mysql -u root -p < sql/schema.sql
```

會建立 `food_selector_db` 資料庫、`food` 資料表，並附上幾筆範例資料。

### 2. 下載 MySQL Connector/J

前往 https://dev.mysql.com/downloads/connector/j/ 下載 **Platform Independent** 版本的 jar 檔（例如 `mysql-connector-j-8.4.0.jar`），放入專案的 `lib/` 資料夾。

> 若你下載的版本號不同，請修改 `.classpath` 中對應的檔名，或直接在 Eclipse 專案上按右鍵 → Build Path → Configure Build Path → Libraries → Add JARs 重新加入。

### 3. 匯入 Eclipse

`File` → `Import` → `Existing Projects into Workspace`，選擇 `RandomMealPicker` 資料夾即可（已內建 `.project` / `.classpath`）。

### 4. 設定資料庫連線資訊

打開 `src/util/DBConnection.java`，依照你自己的 MySQL 環境修改：

```java
private static final String USER = "root";
private static final String PASSWORD = ""; // 改成你自己的 MySQL 密碼
```

預設連線位址為 `localhost:3306`，資料庫名稱為 `food_selector_db`，如果你的環境不同也請一併修改 URL。

### 5. 執行

執行 `src/ui/MainFrame.java`（右鍵 → Run As → Java Application）。

## 功能說明

- **新增 / 修改 / 刪除**：在上方欄位輸入名稱、選擇類型後按下對應按鈕；點選表格中的一列可將資料帶入欄位方便修改或刪除。
- **隨機選餐**：可從下拉選單選擇要篩選的類型（或選「全部」不篩選），按下「隨機選餐！」即可從符合條件的清單中隨機挑選一筆結果顯示。

## 關於 WindowBuilder

`MainFrame.java` 使用絕對座標（`setBounds`）建立版面，可直接在 Eclipse 中安裝 WindowBuilder 外掛後，以視覺化編輯器開啟此檔案繼續調整版面配置與元件外觀。

## 可能的擴充方向（供未來參考）

- 加入權重隨機（例如常吃的降低權重）
- 加入評分欄位
- 加入「排除清單」功能（例如今天不想吃的先排除再抽）