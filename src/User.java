import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;

enum Outcome {ERROR, ACCOUNT_DELETED, ACCOUNT_CREATED, ACCOUNT_EDITED, ACCESS_GRANTED, ACCESS_DENIED,  INVALID_USERNAME, USER_NAME_USED, INVALID_PASSWORD, EMAIL_USED}
public class User {
    private static String name;
    private static char[] password;
    private static String email;
    private static LinkedList<Expense> expenseLinkedList = null;
    private static Statement stat;
    private static ResultSet rs;

    public User(String userName, char[] userPassword){
        name = userName;
        password = userPassword;
    }
    public User(String userName, char[] userPassword, String e){
        name = userName;
        password = userPassword;
        email = e;
    }
    /*
     *  Loguje uzytkownika do programu sprawdzajac czy uzytkownik znajduje sie w bazie danych
     *  oraz poprawnosc hasla
     *  @return Outcome wartosc powodzenia operacji
     */
    public Outcome logIn(){
        try{
            String command = "SELECT password FROM users WHERE users.name = " + "'" + name + "'";
            stat = Main.getStatement();
            ResultSet rs = stat.executeQuery(command);
            if(rs.next()){
                var dbPass = rs.getString(1);
                char[] pass = dbPass.toCharArray();
                if(Arrays.equals(getPassword(), pass)) {
                    command = "SELECT email FROM users WHERE users.name = " + "'" + name  + "'";
                    rs = stat.executeQuery(command);
                    rs.next();
                    setEmail(rs.getString(1));
                    getUserExpenses();
                    return Outcome.ACCESS_GRANTED;
                }
                else return Outcome.INVALID_PASSWORD;
            }
            else return Outcome.INVALID_USERNAME;
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return Outcome.ACCESS_DENIED;
    }
    /*
    * */
    public void getUserExpenses(){
        String message = "SELECT e.name, e.amount, ec.name AS category, e.expense_date " +
                "FROM expenses e " +
                "JOIN users u using(user_id) " +
                "JOIN expense_categories ec USING(category_id) " +
                "WHERE u.name = '" + name + "'";
        try{
            stat = Main.getStatement();
            rs = stat.executeQuery(message);
            if(rs.next()){
                String name = rs.getString(1);
                double amount = rs.getDouble(2);
                String category = rs.getString(3);
                String date = rs.getString(4);
                expenseLinkedList = new LinkedList<>();
                var newExpense = new Expense(name, amount, category, date);
                System.out.println(newExpense.toString());
                expenseLinkedList.add(newExpense);
            }
            while(rs.next()){
                String name = rs.getString(1);
                double amount = rs.getDouble(2);
                String category = rs.getString(3);
                String date = rs.getString(4);
                var newExpense = new Expense(name, amount, category, date);
                System.out.println(newExpense.toString());
                expenseLinkedList.add(newExpense);
            }
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
    /*
    * Wylogowuje uzytkownika z aplikacji, ustawiajac wartosc null polu loggedUser w klasie Main
    * */
    public void logOut(){
        Main.setLoggedUser(null);
    }
    /*
     * Tworzy nowe rekord tabeli persons i wstawia go do bazy danych
     * @return Outcome wartosc powodzenia operacji
     * */
    public static Outcome createAccount(){
        if(!checkUserName()){
            if(!checkUserEmail()){
                String name = "'" +getName()+ "'";
                String password = "'" + getStringPassword() + "'";
                String email = "'" +getEmail()+ "'";
                String insert = "INSERT INTO users (name, password, email) VALUES ("
                        + name + ", "
                        + password + ", "
                        + email + ");";
                try{
                    stat = Main.getStatement();
                    System.out.println(stat.executeUpdate(insert));
                    return Outcome.ACCOUNT_CREATED;
                }catch(SQLException e){
                    System.out.println("SQLException: " + e.getMessage());
                    System.out.println("SQLState: " + e.getSQLState());
                    System.out.println("VendorError: " + e.getErrorCode());
                }
            }
            else return Outcome.EMAIL_USED;
        }
        return Outcome.USER_NAME_USED;
    }
    /*
     * Pobiera od uzytkownika nowe dane konta
     * i aktualizuje dany rekord w bazie danych
     * @return Outcome wartosc powodzenia operacji
     * */
    public static Outcome editAccount(String newUserName, char[] newPassword, String newEmail){
        if(newUserName.compareTo(name) != 0)
            //uzytkownik zmienil nazwe
            if(checkUserName(newUserName) ) return Outcome.USER_NAME_USED;
        if(newEmail.compareTo(email) != 0)
            //Uzytkownik zmienil e-mail
            if(checkUserEmail(newEmail)) return Outcome.EMAIL_USED;

        String password = "";
        for (char letter: newPassword) {
            password+=letter;
        }

        String update = "UPDATE users SET " +
                "name = '" + newUserName +"', " +
                "password = '"+ password + "', " +
                "email =  '" + newEmail + "' " +
                "WHERE name = '" + name + "';";
        System.out.println(update);
        try{
            stat = Main.getStatement();
            if(stat.executeUpdate(update) != 0){
                setName(newUserName);
                setPassword(newPassword);
                setEmail(newEmail);
                return Outcome.ACCOUNT_EDITED;
            }
            else return Outcome.ERROR;
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return Outcome.ERROR;
    }
    /*
     *  Usuwa konto z bazy danych, usuwajac
     *  @return Outcome wartosc powodzenia operacji
     * */
    public static Outcome deleteAccount(){
        String deleteUser = "DELETE FROM users WHERE name = '" + name + "'";
        String deleteExpenses = "DELETE FROM expenses WHERE user_id = " +
                "(SELECT user_id FROM users WHERE name = '" + name + "')";
        try{
            stat = Main.getStatement();
            if(stat.executeUpdate(deleteExpenses) != 0)
                //usuniecie wytatkow uzytkownika udalo sie
                if(stat.executeUpdate(deleteUser) != 0)
                    //usuniecie uzytkownika udalo sie
                    return Outcome.ACCOUNT_DELETED;
            else return Outcome.ERROR;
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return Outcome.ERROR;
    }
    /*
     * Sprawdza czy uzytkownik o danej nazwie jest juz w bazie danych
     * @param conn obiekt przechowujacy polaczenie z baza danych
     * @return boolean.true jesli jest już uzytkownik o takiej nazwie
     *         boolean.false w przeciwnym przypadku
     * */
    public static boolean checkUserName(){
        try{
            stat = Main.getStatement();
            String command = "SELECT name FROM users WHERE name = '" + name + "';";
            rs = stat.executeQuery(command);
            return rs.next();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }
    /*
     * Sprawdza czy uzytkownik o danej nazwie jest juz w bazie danych
     * @param conn obiekt przechowujacy polaczenie z baza danych
     * @param name nazwa uzytkownika do sprawdzenia w DB
     * @return boolean.true jesli jest już uzytkownik o takiej nazwie
     *         boolean.false w przeciwnym przypadku
     * */
    public static boolean checkUserName(String name){
        try{
            stat = Main.getStatement();
            String command = "SELECT name FROM users WHERE name = '" + name + "';";
            rs = stat.executeQuery(command);
            return rs.next();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }
    /*
     * Sprawdza czy uzytkownik o danym adresie e-mail jest juz w bazie danych
     * @param conn obiekt przechowujacy polaczenie z baza danych
     * @return boolean.true jesli jest już uzytkownik o danym adresie e-mail
     *         boolean.false w przeciwnym przypadku
     * */
    public static boolean checkUserEmail() {
        try {
            stat = Main.getStatement();
            String command = "SELECT email FROM users WHERE email = '" + email + "';";
            rs = stat.executeQuery(command);
            return rs.next();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }
    /*
     * Sprawdza czy uzytkownik o danym adresie e-mail jest juz w bazie danych
     * @param conn obiekt przechowujacy polaczenie z baza danych
     * @param email nazwa email do sprawdznia
     * @return boolean.true jesli jest już uzytkownik o danym adresie e-mail
     *         boolean.false w przeciwnym przypadku
     * */
    public static boolean checkUserEmail(String email) {
        try {
            stat = Main.getStatement();
            String command = "SELECT email FROM users WHERE email = '" + email + "';";
            rs = stat.executeQuery(command);
            return rs.next();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }
    //GETERY i SETERY
    public static String getName() { return name; }
    public static void setName(String newName){name = newName; }
    public static String getEmail() {
        return email;
    }
    public static void setEmail(String newEmail){
        email = newEmail;
    }
    public static char[] getPassword() {
        return password;
    }
    public static void setPassword(char[] newPassword){password = newPassword;}
    /*
     * Zwraca haslo uzytkownika w typie String
     * @return haslo uzytkownika
     * */
    public static String getStringPassword() {
        String out = "";
        for (char c : password) {
            out += c;
        }
        return out;
    }
    @Override
    public String toString(){
        return this.getClass().getName() + " name: " + name + " email: " + email;
    }
}
