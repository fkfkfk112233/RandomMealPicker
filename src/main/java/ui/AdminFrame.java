package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.User;

/**
 * 管理員畫面
 * 繼承 MemberFrame，因此擁有完整的新增/修改/刪除/清空欄位/隨機選餐功能，
 * 並在下方新增「會員管理」按鈕，開啟 UserManageFrame 進行會員 CRUD。
 */
public class AdminFrame extends MemberFrame {

    private static final long serialVersionUID = 1L;

    public AdminFrame(User currentUser) {
        super(currentUser);
        setTitle("隨機選餐系統 - 管理員（" + currentUser.getUsername() + "）");
        initAdminArea();
    }

    /** 在原本版面下方空白處新增管理員專用按鈕 */
    private void initAdminArea() {
        JButton btnUserManage = new JButton("會員管理");
        btnUserManage.setBounds(190, 465, 150, 30);
        btnUserManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserManageFrame().setVisible(true);
            }
        });
        contentPane.add(btnUserManage);
    }
}
