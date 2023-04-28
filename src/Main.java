import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Jest to program umozliwiajacy zarzadzanie wydatkami uzytkownika,
* korzystajac z odpowiedniej bazy danych
* @version 1.0 2023-04-28
* @author Bartosz Surma*/

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
    /*
    * Zwraca obiekt sluzacy do tworzenia zapytan do bazy danych w jezyku SQL
    * @return Statement stat obiekt sluzacy do tworzenia zapytan do bazy danych*/
    public static Statement getStatement(){
        Statement stat;
        try {
             stat = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stat;
    }
    /*
    * Zamykaa polaczenie z baza danych*/
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
    /*Zwraca wartosc logiczna zalezna od tego czy uzytkownik jest zalogowany
    * @return (1) boolean true gdy uzytkownik jest zalogowany, (2) boolean false w przeciwnym wypadkuy*/
    public static boolean isLogged(){return loggedUser != null; }
}
