package ui;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.MealHistoryController;
import model.MealHistory;
import model.User;

/**
 * 選餐歷史查詢畫面
 * 會員登入時查詢：只顯示自己的歷史
 * 管理員登入時查詢：顯示所有人的歷史（多一欄「帳號」）
 */
public class MealHistoryFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final MealHistoryController mealHistoryController = new MealHistoryController();
    private final User currentUser;

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    public MealHistoryFrame(User currentUser) {
        this.currentUser = currentUser;

        boolean isAdmin = currentUser.isAdmin();
        setTitle(isAdmin ? "選餐歷史 - 全部會員" : "選餐歷史 - 我的紀錄");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(150, 150, 560, 480);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel(isAdmin ? "全部會員的選餐歷史" : "我的選餐歷史", JLabel.CENTER);
        lblTitle.setBounds(20, 15, 500, 25);
        contentPane.add(lblTitle);

        initTableArea(isAdmin);

        JButton btnRefresh = new JButton("重新整理");
        btnRefresh.setBounds(20, 405, 500, 30);
        btnRefresh.addActionListener(e -> loadData());
        contentPane.add(btnRefresh);

        loadData();
    }

    private void initTableArea(boolean isAdmin) {
        String[] columns = isAdmin
                ? new String[] { "帳號", "選餐時間", "名稱", "類型" }
                : new String[] { "選餐時間", "名稱", "類型" };

        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 50, 500, 345);
        contentPane.add(scrollPane);
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);

            List<MealHistory> historyList = currentUser.isAdmin()
                    ? mealHistoryController.getAllHistory()
                    : mealHistoryController.getMyHistory(currentUser.getId());

            for (MealHistory history : historyList) {
                String time = history.getPickedAt() != null ? history.getPickedAt().format(TIME_FORMAT) : "";

                if (currentUser.isAdmin()) {
                    tableModel.addRow(new Object[] {
                            history.getUsername(), time, history.getFoodName(), history.getFoodType()
                    });
                } else {
                    tableModel.addRow(new Object[] {
                            time, history.getFoodName(), history.getFoodType()
                    });
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "發生錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }
}
