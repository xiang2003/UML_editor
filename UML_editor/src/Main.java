
import ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // 使用 SwingUtilities.invokeLater 確保 UI 在事件調度執行緒 (EDT) 中啟動
        // 這是 Swing 程式開發的標準規範，避免執行緒衝突
        SwingUtilities.invokeLater(() -> {
            try {
                // 選擇性：設定為系統原生的外觀 (Look and Feel)
                // 這樣視窗在 Windows 就會像 Windows 程式，在 Mac 就會像 Mac 程式
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // 如果設定失敗，會使用預設的 Java 外觀，不影響執行
                e.printStackTrace();
            }

            // 初始化主視窗
            MainFrame frame = new MainFrame();

            // 將視窗置中顯示
            frame.setLocationRelativeTo(null);

            // 顯示視窗
            frame.setVisible(true);
        });
    }
}