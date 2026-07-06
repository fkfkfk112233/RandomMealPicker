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

import controller.AuthController;

/**
 * 註冊畫面
 * 註冊成功後一律建立為「會員」身分（管理員帳號僅能由資料庫種子資料建立）
 */
public class RegisterFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final AuthController authController = new AuthController();

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    public RegisterFrame() {
        setTitle("隨機選餐系統 - 註冊");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 340);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("註冊新會員", JLabel.CENTER);
        lblTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        lblTitle.setBounds(20, 20, 340, 30);
        contentPane.add(lblTitle);

        JLabel lblUsername = new JLabel("帳號:");
        lblUsername.setBounds(50, 70, 90, 25);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(140, 70, 190, 25);
        contentPane.add(txtUsername);

        JLabel lblPassword = new JLabel("密碼:");
        lblPassword.setBounds(50, 105, 90, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 105, 190, 25);
        contentPane.add(txtPassword);

        JLabel lblConfirm = new JLabel("確認密碼:");
        lblConfirm.setBounds(50, 140, 90, 25);
        contentPane.add(lblConfirm);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(140, 140, 190, 25);
        contentPane.add(txtConfirmPassword);

        JButton btnRegister = new JButton("送出註冊");
        btnRegister.setBounds(50, 185, 280, 30);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRegister();
            }
        });
        contentPane.add(btnRegister);

        JButton btnBack = new JButton("返回登入");
        btnBack.setBounds(50, 225, 280, 30);
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnBack);
    }

    private void onRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "兩次輸入的密碼不一致", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            authController.register(username, password);
            JOptionPane.showMessageDialog(this, "註冊成功！請重新登入");
            new LoginFrame().setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "註冊失敗", JOptionPane.ERROR_MESSAGE);
        }
    }
}
