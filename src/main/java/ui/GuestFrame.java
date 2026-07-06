package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.FoodController;
import model.Food;

/**
 * 訪客（非會員）畫面
 * 依需求，訪客僅有兩個功能：
 *   1. 隨機選餐（不支援類型篩選，直接從全部餐點中抽選）
 *   2. 註冊（前往註冊畫面成為會員）
 */
public class GuestFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final FoodController foodController = new FoodController();

    private JPanel contentPane;
    private JLabel lblResult;

    public GuestFrame() {
        setTitle("隨機選餐系統 - 訪客模式");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 420, 300);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("訪客模式", JLabel.CENTER);
        lblTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        lblTitle.setBounds(20, 20, 360, 30);
        contentPane.add(lblTitle);

        JButton btnRandom = new JButton("隨機選餐");
        btnRandom.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        btnRandom.setBounds(60, 80, 280, 40);
        btnRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRandomPick();
            }
        });
        contentPane.add(btnRandom);

        lblResult = new JLabel("今天吃：尚未選擇", JLabel.CENTER);
        lblResult.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        lblResult.setBounds(20, 130, 360, 40);
        contentPane.add(lblResult);

        JButton btnRegister = new JButton("註冊");
        btnRegister.setBounds(60, 200, 280, 40);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnRegister);
    }

    private void onRandomPick() {
        try {
            // 訪客不提供類型篩選，一律從全部餐點中隨機挑選
            Food food = foodController.getRandomFood(null);

            if (food == null) {
                lblResult.setText("目前沒有任何餐點資料");
            } else {
                lblResult.setText("今天吃：" + food.getName() + "（" + food.getType() + "）");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "發生錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }
}
