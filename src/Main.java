import controllers.MyController;
import view.Window;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            Window window = new Window();
            new MyController(window, window.getPanel());
            window.setVisible(true);
        });
    }
}
