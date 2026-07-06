package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.LoginFrame;

/**
 * 程式進入點
 * 啟動後顯示登入畫面，使用者可選擇登入 / 註冊 / 以訪客身分使用
 */
public class App {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // 若取得系統 Look and Feel 失敗，使用預設樣式即可，不影響程式運作
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
