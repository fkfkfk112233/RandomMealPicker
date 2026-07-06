package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.AuthController;
import model.Role;
import model.User;

/**
 * 登入畫面（程式進入點畫面）
 * 提供：登入 / 註冊 / 以訪客身分使用
 */
public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final AuthController authController = new AuthController();

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("隨機選餐系統 - 登入");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 320);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("隨機選餐系統", JLabel.CENTER);
        lblTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 20));
        lblTitle.setBounds(20, 20, 340, 35);
        contentPane.add(lblTitle);

        JLabel lblUsername = new JLabel("帳號:");
        lblUsername.setBounds(50, 80, 60, 25);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(120, 80, 200, 25);
        contentPane.add(txtUsername);

        JLabel lblPassword = new JLabel("密碼:");
        lblPassword.setBounds(50, 115, 60, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(120, 115, 200, 25);
        contentPane.add(txtPassword);

        JButton btnLogin = new JButton("登入");
        btnLogin.setBounds(50, 160, 270, 30);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });
        contentPane.add(btnLogin);

        JButton btnRegister = new JButton("註冊");
        btnRegister.setBounds(50, 200, 130, 30);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnRegister);

        JButton btnGuest = new JButton("訪客使用");
        btnGuest.setBounds(190, 200, 130, 30);
        btnGuest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GuestFrame().setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnGuest);

        JLabel lblHint = new JLabel("預設管理員帳號 admin / admin123", JLabel.CENTER);
        lblHint.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 11));
        lblHint.setBounds(20, 250, 340, 20);
        contentPane.add(lblHint);
    }

    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請輸入帳號與密碼", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = authController.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "帳號或密碼錯誤", "登入失敗", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Role.ADMIN.name().equals(user.getRole())) {
                new AdminFrame(user).setVisible(true);
            } else {
                new MemberFrame(user).setVisible(true);
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "發生錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
