import javax.swing.*;
import java.awt.*;

/*
* Klasa reprezentuje główną ramkę programu*/
public class MainFrame extends JFrame {
    private static final int SCREEN_SIZE_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int SCREEN_SIZE_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int DEFAULT_WIDTH = 2 * 1920 / 3;
    private static final int DEFAULT_HEIGHT = 2 *1080 / 3;
    private static final String PROGRAM_NAME = "Expense Manager";
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 20);
    private static final Font HEADER_2_FONT = new Font("SansSerif", Font.BOLD, 13);
    private static final Font BASIC_FONT = new Font("SansSerif", Font.PLAIN, 13);
    private static MainMenuBar menuBar;
    private static MainPanel mainPanel;

    public MainFrame(){
        setTitle(PROGRAM_NAME);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        int x = (SCREEN_SIZE_WIDTH - DEFAULT_WIDTH) /2;
        int y = (SCREEN_SIZE_HEIGHT - DEFAULT_HEIGHT) / 2;
        setLocation(x, y);
        setResizable(false);
        ImageIcon icon = new ImageIcon("icons/main_icon.png");
        setIconImage(icon.getImage());
        setBackground(new Color(64, 64, 64));
        setLayout(new BorderLayout());

        menuBar = new MainMenuBar(this);
        add(menuBar, BorderLayout.NORTH);

        mainPanel = new MainPanel();
        add(mainPanel, BorderLayout.CENTER);

        MainMenuBar.reload();
    }
    /* Metoda zwraca szerokosc ekranu
    *  @return int szerokosc ekranu*/
    public static int getScreenSizeWidth() {return SCREEN_SIZE_WIDTH;}
    /* Metoda zwraca wysokosc ekranu
     *  @return int wysokosc ekranu*/
    public static int getScreenSizeHeight() {return SCREEN_SIZE_HEIGHT;}
    public static Font getHeaderFont(){return HEADER_FONT; }

    public static Font getHeader2Font() {return HEADER_2_FONT;}

    public static Font getBasicFont() {return BASIC_FONT;}
}


