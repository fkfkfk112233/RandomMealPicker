package controller;

import model.User;
import service.UserService;
import serviceimpl.UserServiceImpl;

/**
 * 負責銜接登入 / 註冊畫面與 Service 層
 */
public class AuthController {

    private final UserService userService;

    public AuthController() {
        this.userService = new UserServiceImpl();
    }

    /**
     * 登入
     * @return 成功回傳 User；帳號或密碼錯誤回傳 null
     */
    public User login(String username, String password) {
        return userService.login(username, password);
    }

    /**
     * 註冊（固定建立為會員身分）
     * @throws IllegalArgumentException 帳號已存在或欄位不合法時拋出，訊息可直接顯示給使用者
     */
    public User register(String username, String password) {
        return userService.register(username, password);
    }
}
