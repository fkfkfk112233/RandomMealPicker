package service;

import java.util.List;

import model.User;

/**
 * User 業務邏輯介面
 * 涵蓋一般使用者的註冊/登入，以及管理員的會員管理（CRUD）
 */
public interface UserService {

    /**
     * 註冊新帳號，固定建立為「會員」身分
     * @throws IllegalArgumentException 帳號已存在，或欄位不合法
     */
    User register(String username, String plainPassword);

    /**
     * 登入驗證
     * @return 驗證成功回傳 User；帳號不存在或密碼錯誤回傳 null
     */
    User login(String username, String plainPassword);

    /**
     * 管理員新增使用者（可指定角色 MEMBER / ADMIN）
     */
    void addUser(String username, String plainPassword, String role);

    /**
     * 管理員更新使用者資料
     * @param newPlainPassword 若為 null 或空字串，代表不變更密碼
     */
    void updateUser(int id, String username, String newPlainPassword, String role);

    /** 刪除使用者 */
    void deleteUser(int id);

    /** 取得全部使用者清單 */
    List<User> getAllUsers();

    /** 依 id 取得使用者 */
    User getUserById(int id);

    /** 檢查帳號是否已存在 */
    boolean existsByUsername(String username);
}
