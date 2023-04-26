import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final int SCREEN_SIZE_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int SCREEN_SIZE_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int DEFAULT_WIDTH = 2 * 1920 / 3;
    private static final int DEFAULT_HEIGHT = 2 *1080 / 3;
    private static final String PROGRAM_NAME = "Expense Manager";

    public MainFrame(){
        setTitle(PROGRAM_NAME);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        int x = (SCREEN_SIZE_WIDTH - DEFAULT_WIDTH) /2;
        int y = (SCREEN_SIZE_HEIGHT - DEFAULT_HEIGHT) / 2;
        setLocation(x, y);
        setResizable(false);

        setLayout(new BorderLayout());
        add(new MainMenuBar(this), BorderLayout.NORTH);
        add(new MainPanel(), BorderLayout.CENTER);
        MainMenuBar.reload();
    }
    /* Metoda zwraca szerokosc ekranu
    *  @return int szerokosc ekranu*/
    public static int getScreenSizeWidth() {return SCREEN_SIZE_WIDTH;}
    /* Metoda zwraca wysokosc ekranu
     *  @return int wysokosc ekranu*/
    public static int getScreenSizeHeight() {return SCREEN_SIZE_HEIGHT;}
}


