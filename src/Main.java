import ui.LoginFrame;

public class Main {

  public static void main(String[] args) {

    javax.swing.SwingUtilities.invokeLater(() -> {
      try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      new LoginFrame().setVisible(true);
    });

  }
}