package model;

/**
 * 使用者角色
 *
 * GUEST  - 訪客（非會員），不會被儲存到資料庫，僅供程式內流程判斷使用
 * MEMBER - 會員
 * ADMIN  - 管理員
 */
public enum Role {
    GUEST,
    MEMBER,
    ADMIN
}
