package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 密碼雜湊工具（使用 BCrypt 加鹽雜湊）
 * 資料庫中永遠只儲存雜湊後的字串，不儲存明碼密碼。
 */
public class PasswordUtil {

    private PasswordUtil() {
        // 工具類別，不可實例化
    }

    /** 將明碼密碼加鹽雜湊，回傳可儲存進資料庫的字串 */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /** 驗證明碼密碼是否與資料庫中的雜湊值相符 */
    public static boolean matches(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
