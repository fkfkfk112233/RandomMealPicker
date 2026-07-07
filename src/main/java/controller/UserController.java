package controller;

import java.util.List;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

/**
 * 負責銜接會員管理畫面（管理員專用）與 Service 層
 */
public class UserController {

    private final UserService userService;

    public UserController() {
        this.userService = new UserServiceImpl();
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User getUserById(int id) {
        return userService.getUserById(id);
    }

    public void addUser(String username, String password, String role) {
        userService.addUser(username, password, role);
    }

    /**
     * @param newPassword 若為 null 或空字串，代表不變更密碼
     */
    public void updateUser(int id, String username, String newPassword, String role) {
        userService.updateUser(id, username, newPassword, role);
    }

    public void deleteUser(int id) {
        userService.deleteUser(id);
    }
}
