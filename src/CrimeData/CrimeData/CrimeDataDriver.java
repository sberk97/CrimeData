package CrimeData;

import javax.swing.SwingUtilities;

/**
 * This class is a Launcher of whole project
 * @author Sebastian Berk UB:
 */
final class CrimeDataDriver {
    
    private CrimeDataDriver() {
        //not called
    }
    /**
     * Creates new GUIHandler object and calls a setup method to create JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIHandler GUIHandler = new GUIHandler();
            GUIHandler.setup();
        });
        }
}