package dao;

import java.util.List;

import model.User;

/**
 * User 資料存取介面（僅定義方法，不含實作）
 */
public interface UserDAO {

    /** 新增一筆使用者資料（password 需已是雜湊後的字串） */
    void insert(User user);

    /** 更新使用者資料（password 需已是雜湊後的字串） */
    void update(User user);

    /** 依 id 刪除使用者（真刪除） */
    void delete(int id);

    /** 依 id 查詢單筆資料，查無資料回傳 null */
    User findById(int id);

    /** 依帳號查詢單筆資料，查無資料回傳 null */
    User findByUsername(String username);

    /** 查詢全部使用者資料 */
    List<User> findAll();

    /** 檢查帳號是否已存在 */
    boolean existsByUsername(String username);
}
