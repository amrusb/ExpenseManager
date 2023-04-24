import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {
    private static final MainFrame frame = new MainFrame();
    private static final Connection conn = new DataBaseConnector().startConnection();
    private static User loggedUser = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(() ->{
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
    public static Statement getStatement(){
        Statement stat;
        try {
             stat = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stat;
    }
    public static void closeConnection(){
        try{
            if(!conn.isClosed()){
                conn.close();
            }
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
    public static void setLoggedUser(User user){loggedUser = user;}
    public static User getLoggedUser(){ return loggedUser; }
    public static boolean isLogged(){return loggedUser != null; }
}
