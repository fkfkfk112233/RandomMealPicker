package serviceimpl;

import java.util.List;

import dao.UserDAO;
import daoimpl.UserDAOImpl;
import model.Role;
import model.User;
import service.UserService;
import util.PasswordUtil;

/**
 * UserService 的實作
 * 密碼一律以 BCrypt 雜湊後才寫入資料庫，服務層以外看不到明碼密碼。
 */
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public User register(String username, String plainPassword) {
        validateUsername(username);
        validatePassword(plainPassword);

        if (userDAO.existsByUsername(username)) {
            throw new IllegalArgumentException("此帳號已被註冊，請換一個帳號");
        }

        String hashed = PasswordUtil.hash(plainPassword);
        User user = new User(username, hashed, Role.MEMBER.name());
        userDAO.insert(user);

        // 回傳剛註冊的使用者資料（重新查詢以取得 id）
        return userDAO.findByUsername(username);
    }

    @Override
    public User login(String username, String plainPassword) {
        if (username == null || username.trim().isEmpty() || plainPassword == null || plainPassword.isEmpty()) {
            return null;
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null;
        }

        if (!PasswordUtil.matches(plainPassword, user.getPassword())) {
            return null;
        }

        return user;
    }

    @Override
    public void addUser(String username, String plainPassword, String role) {
        validateUsername(username);
        validatePassword(plainPassword);
        validateRole(role);

        if (userDAO.existsByUsername(username)) {
            throw new IllegalArgumentException("此帳號已存在");
        }

        String hashed = PasswordUtil.hash(plainPassword);
        userDAO.insert(new User(username, hashed, role));
    }

    @Override
    public void updateUser(int id, String username, String newPlainPassword, String role) {
        validateUsername(username);
        validateRole(role);

        User existing = userDAO.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("找不到這個使用者");
        }

        // 若帳號有變更，需確認新帳號沒有跟別人重複
        if (!existing.getUsername().equals(username) && userDAO.existsByUsername(username)) {
            throw new IllegalArgumentException("此帳號已被其他使用者使用");
        }

        String passwordToSave;
        if (newPlainPassword == null || newPlainPassword.isEmpty()) {
            passwordToSave = existing.getPassword(); // 不變更密碼
        } else {
            passwordToSave = PasswordUtil.hash(newPlainPassword);
        }

        User user = new User(id, username, passwordToSave, role);
        userDAO.update(user);
    }

    @Override
    public void deleteUser(int id) {
        userDAO.delete(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userDAO.existsByUsername(username);
    }

    // ================== 驗證 ==================

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("帳號不可為空");
        }
    }

    private void validatePassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("密碼不可為空");
        }
        if (plainPassword.length() < 4) {
            throw new IllegalArgumentException("密碼長度至少需要 4 個字元");
        }
    }

    private void validateRole(String role) {
        if (!Role.MEMBER.name().equals(role) && !Role.ADMIN.name().equals(role)) {
            throw new IllegalArgumentException("角色只能是 MEMBER 或 ADMIN");
        }
    }
}
