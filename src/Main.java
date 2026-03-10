import ui.LoginFrame;

public class Main {

    public static void main(String[] args) {
        // Start application on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Apply Nimbus Look and Feel for modern UI
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Launch login screen
            new LoginFrame().setVisible(true);
        });
    }
}