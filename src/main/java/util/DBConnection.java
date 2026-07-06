package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 資料庫連線工具類別
 *
 * 請依照自己的 MySQL 環境修改下列常數：
 *   URL      - 資料庫連線位址（預設 localhost:3306，資料庫名稱 food_selector_db）
 *   USER     - 帳號（預設 root）
 *   PASSWORD - 密碼（預設空字串，請改成你自己的 MySQL 密碼）
 */
public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/food_selector_db"
            + "?useSSL=false"
            + "&serverTimezone=Asia/Taipei"
            + "&characterEncoding=UTF-8"
            + "&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASSWORD = "1234"; // TODO: 請改成你自己的 MySQL 密碼

    static {
        try {
            // MySQL 8.0 Connector/J 的 Driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到 MySQL JDBC Driver，請確認 pom.xml 的 mysql-connector-j 依賴已正確下載", e);
        }
    }

    private DBConnection() {
        // 工具類別，不可實例化
    }

    /**
     * 取得一個新的資料庫連線
     * 呼叫端使用完畢後請自行 close（建議搭配 try-with-resources）
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
