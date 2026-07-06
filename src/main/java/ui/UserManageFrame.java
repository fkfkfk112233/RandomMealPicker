package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.UserController;
import model.Role;
import model.User;

/**
 * 會員管理畫面（管理員專用）
 * 提供帳號的新增 / 修改 / 刪除 / 清空欄位
 * 密碼欄位在「修改」時若留空，代表不變更密碼；「新增」時則必填。
 */
public class UserManageFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String[] ROLE_OPTIONS = { Role.MEMBER.name(), Role.ADMIN.name() };

    private final UserController userController = new UserController();

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JTable table;
    private DefaultTableModel tableModel;

    private int selectedId = 0;

    public UserManageFrame() {
        setTitle("會員管理");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(150, 150, 640, 480);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        initInputArea();
        initTableArea();

        loadTableData();
    }

    private void initInputArea() {
        JLabel lblUsername = new JLabel("帳號:");
        lblUsername.setBounds(20, 20, 50, 25);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(70, 20, 140, 25);
        contentPane.add(txtUsername);

        JLabel lblPassword = new JLabel("密碼:");
        lblPassword.setBounds(220, 20, 50, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(270, 20, 140, 25);
        contentPane.add(txtPassword);

        JLabel lblRole = new JLabel("角色:");
        lblRole.setBounds(20, 55, 50, 25);
        contentPane.add(lblRole);

        cbRole = new JComboBox<>(ROLE_OPTIONS);
        cbRole.setBounds(70, 55, 140, 25);
        contentPane.add(cbRole);

        JLabel lblHint = new JLabel("（修改時密碼留空 = 不變更密碼）");
        lblHint.setBounds(220, 55, 250, 25);
        contentPane.add(lblHint);

        JButton btnAdd = new JButton("新增");
        btnAdd.setBounds(430, 20, 80, 25);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });
        contentPane.add(btnAdd);

        JButton btnUpdate = new JButton("修改");
        btnUpdate.setBounds(520, 20, 80, 25);
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUpdate();
            }
        });
        contentPane.add(btnUpdate);

        JButton btnDelete = new JButton("刪除");
        btnDelete.setBounds(430, 55, 80, 25);
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDelete();
            }
        });
        contentPane.add(btnDelete);

        JButton btnClear = new JButton("清除欄位");
        btnClear.setBounds(520, 55, 80, 25);
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        contentPane.add(btnClear);
    }

    private void initTableArea() {
        tableModel = new DefaultTableModel(new Object[] { "ID", "帳號", "角色" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onRowSelected();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 95, 580, 330);
        contentPane.add(scrollPane);
    }

    // ================== 事件處理 ==================

    private void onAdd() {
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String role = (String) cbRole.getSelectedItem();

            userController.addUser(username, password, role);

            JOptionPane.showMessageDialog(this, "新增成功！");
            clearFields();
            loadTableData();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onUpdate() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this, "請先從表格中選取要修改的資料列", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()); // 留空 = 不變更密碼
            String role = (String) cbRole.getSelectedItem();

            userController.updateUser(selectedId, username, password, role);

            JOptionPane.showMessageDialog(this, "修改成功！");
            clearFields();
            loadTableData();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onDelete() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this, "請先從表格中選取要刪除的資料列", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this, "確定要刪除這個使用者嗎？此動作無法復原。", "確認刪除", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            userController.deleteUser(selectedId);
            JOptionPane.showMessageDialog(this, "刪除成功！");
            clearFields();
            loadTableData();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onRowSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }
        selectedId = (int) tableModel.getValueAt(viewRow, 0);
        txtUsername.setText((String) tableModel.getValueAt(viewRow, 1));
        cbRole.setSelectedItem(tableModel.getValueAt(viewRow, 2));
        txtPassword.setText(""); // 密碼永遠不顯示，留空代表修改時不變更
    }

    // ================== 輔助方法 ==================

    private void loadTableData() {
        try {
            tableModel.setRowCount(0);
            List<User> users = userController.getAllUsers();
            for (User user : users) {
                tableModel.addRow(new Object[] { user.getId(), user.getUsername(), user.getRole() });
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void clearFields() {
        selectedId = 0;
        txtUsername.setText("");
        txtPassword.setText("");
        cbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "發生錯誤", JOptionPane.ERROR_MESSAGE);
    }
}
