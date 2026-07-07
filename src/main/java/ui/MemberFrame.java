package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import controller.FoodController;
import controller.MealHistoryController;
import model.Food;
import model.User;

/**
 * 會員畫面：新增 / 修改 / 刪除 / 清空欄位 / 隨機選餐
 * 版面使用絕對座標 (setBounds)，可直接在 Eclipse WindowBuilder 中開啟繼續視覺化編輯。
 *
 * 本類別設計為可被 AdminFrame 繼承，並在既有版面下方的空白區域擴充「會員管理」按鈕，
 * 因此關鍵欄位（contentPane 等）宣告為 protected。
 */
public class MemberFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    /** 類型選項（下拉選單固定清單，之後要擴充類型只需改這裡） */
    protected static final String[] TYPE_OPTIONS = { "中式", "日式","西式", "速食", "其他" };
    protected static final String[] FILTER_OPTIONS = { "全部", "中式", "日式","西式", "速食", "其他" };

    protected final FoodController foodController = new FoodController();
    protected final MealHistoryController mealHistoryController = new MealHistoryController();
    protected final User currentUser;

    protected JPanel contentPane;
    protected JTextField txtName;
    protected JComboBox<String> cbType;
    protected JComboBox<String> cbFilterType;
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JLabel lblResult;
    protected JLabel lblExcludedList;

    /** 目前選取列的 id，0 代表尚未選取任何列（新增模式） */
    protected int selectedId = 0;

    /** 排除清單：id -> 名稱，僅存在於本次視窗開啟期間，不會寫入資料庫 */
    protected final Map<Integer, String> excludedFoods = new LinkedHashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    public MemberFrame(User currentUser) {
        this.currentUser = currentUser;

        setTitle("隨機選餐系統 - 會員（" + currentUser.getUsername() + "）");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 560);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        initInputArea();
        initTableArea();
        initRandomArea();
        initLogoutButton();

        loadTableData();
    }

    /** 上方：名稱 / 類型輸入欄位 + CRUD 按鈕 */
    protected void initInputArea() {
        JLabel lblName = new JLabel("名稱:");
        lblName.setBounds(20, 20, 50, 25);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setBounds(70, 20, 160, 25);
        contentPane.add(txtName);

        JLabel lblType = new JLabel("類型:");
        lblType.setBounds(250, 20, 50, 25);
        contentPane.add(lblType);

        cbType = new JComboBox<>(TYPE_OPTIONS);
        cbType.setBounds(300, 20, 100, 25);
        contentPane.add(cbType);

        JButton btnAdd = new JButton("新增");
        btnAdd.setBounds(420, 20, 80, 25);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });
        contentPane.add(btnAdd);

        JButton btnUpdate = new JButton("修改");
        btnUpdate.setBounds(510, 20, 80, 25);
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUpdate();
            }
        });
        contentPane.add(btnUpdate);

        JButton btnDelete = new JButton("刪除");
        btnDelete.setBounds(420, 55, 80, 25);
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDelete();
            }
        });
        contentPane.add(btnDelete);

        JButton btnClear = new JButton("清除欄位");
        btnClear.setBounds(510, 55, 80, 25);
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        contentPane.add(btnClear);
    }

    /** 中間：資料表格 */
    protected void initTableArea() {
        tableModel = new DefaultTableModel(new Object[] { "ID", "名稱", "類型" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格僅供顯示與選取，不可直接編輯儲存格
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
        scrollPane.setBounds(20, 95, 640, 230);
        contentPane.add(scrollPane);
    }

    /** 下方：隨機選餐區塊（含排除功能、登出按鈕的空間） */
    protected void initRandomArea() {
        JLabel lblFilter = new JLabel("篩選類型:");
        lblFilter.setBounds(20, 340, 70, 25);
        contentPane.add(lblFilter);

        cbFilterType = new JComboBox<>(FILTER_OPTIONS);
        cbFilterType.setBounds(95, 340, 100, 25);
        contentPane.add(cbFilterType);

        JButton btnRandom = new JButton("隨機選餐！");
        btnRandom.setBounds(210, 340, 120, 25);
        btnRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRandomPick();
            }
        });
        contentPane.add(btnRandom);

        JButton btnExclude = new JButton("排除");
        btnExclude.setBounds(335, 340, 75, 25);
        btnExclude.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExclude();
            }
        });
        contentPane.add(btnExclude);

        JButton btnClearExclude = new JButton("清空排除");
        btnClearExclude.setBounds(415, 340, 90, 25);
        btnClearExclude.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClearExclude();
            }
        });
        contentPane.add(btnClearExclude);

        JLabel lblExcludedTitle = new JLabel("已排除:");
        lblExcludedTitle.setBounds(20, 372, 60, 20);
        contentPane.add(lblExcludedTitle);

        lblExcludedList = new JLabel("（無）");
        lblExcludedList.setBounds(85, 372, 575, 20);
        contentPane.add(lblExcludedList);

        lblResult = new JLabel("今天吃：尚未選擇", JLabel.CENTER);
        lblResult.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        lblResult.setBounds(20, 398, 640, 55);
        contentPane.add(lblResult);

        JButton btnHistory = new JButton("選餐歷史");
        btnHistory.setBounds(20, 465, 150, 30);
        btnHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOpenHistory();
            }
        });
        contentPane.add(btnHistory);
    }

    /** 登出按鈕（放在隨機選餐同一列的右側空白處） */
    protected void initLogoutButton() {
        JButton btnLogout = new JButton("登出");
        btnLogout.setBounds(560, 340, 100, 25);
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLogout();
            }
        });
        contentPane.add(btnLogout);
    }

    // ================== 事件處理 ==================

    protected void onAdd() {
        try {
            String name = txtName.getText().trim();
            String type = (String) cbType.getSelectedItem();

            foodController.addFood(name, type);

            JOptionPane.showMessageDialog(this, "新增成功！");
            clearFields();
            loadTableData();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    protected void onUpdate() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this, "請先從表格中選取要修改的資料列", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String name = txtName.getText().trim();
            String type = (String) cbType.getSelectedItem();

            foodController.updateFood(selectedId, name, type);

            JOptionPane.showMessageDialog(this, "修改成功！");
            clearFields();
            loadTableData();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    protected void onDelete() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this, "請先從表格中選取要刪除的資料列", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this, "確定要刪除這筆資料嗎？此動作無法復原。", "確認刪除", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            foodController.deleteFood(selectedId);
            JOptionPane.showMessageDialog(this, "刪除成功！");
            clearFields();
            loadTableData();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    protected void onRowSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }
        selectedId = (int) tableModel.getValueAt(viewRow, 0);
        txtName.setText((String) tableModel.getValueAt(viewRow, 1));
        cbType.setSelectedItem(tableModel.getValueAt(viewRow, 2));
    }

    protected void onRandomPick() {
        try {
            String filterType = (String) cbFilterType.getSelectedItem();
            Food food = foodController.getRandomFood(filterType, excludedFoods.keySet());

            if (food == null) {
                lblResult.setText("查無符合條件的餐點（可能都被篩選或排除了）");
            } else {
                lblResult.setText("今天吃：" + food.getName() + "（" + food.getType() + "）");
                // 會員 / 管理員的選餐結果記錄到歷史（訪客不會使用這個畫面，故不需另外判斷）
                mealHistoryController.recordPick(currentUser.getId(), food);
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    /** 將表格中目前選取的一列加入排除清單 */
    protected void onExclude() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "請先從表格中選取要排除的餐點", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(viewRow, 0);
        String name = (String) tableModel.getValueAt(viewRow, 1);

        excludedFoods.put(id, name);
        updateExcludedLabel();
    }

    /** 清空排除清單 */
    protected void onClearExclude() {
        excludedFoods.clear();
        updateExcludedLabel();
    }

    /** 開啟選餐歷史查詢畫面 */
    protected void onOpenHistory() {
        new MealHistoryFrame(currentUser).setVisible(true);
    }

    /** 更新畫面上「已排除」清單的顯示文字 */
    protected void updateExcludedLabel() {
        if (excludedFoods.isEmpty()) {
            lblExcludedList.setText("（無）");
        } else {
            lblExcludedList.setText(String.join("、", excludedFoods.values()));
        }
    }

    protected void onLogout() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    // ================== 輔助方法 ==================

    /** 重新從資料庫載入表格資料 */
    protected void loadTableData() {
        try {
            tableModel.setRowCount(0);
            List<Food> foods = foodController.getAllFoods();
            for (Food food : foods) {
                tableModel.addRow(new Object[] { food.getId(), food.getName(), food.getType() });
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    protected void clearFields() {
        selectedId = 0;
        txtName.setText("");
        cbType.setSelectedIndex(0);
        table.clearSelection();
    }

    protected void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "發生錯誤", JOptionPane.ERROR_MESSAGE);
    }
}
