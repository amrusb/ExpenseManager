import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.LinkedList;

enum Outcome {ERROR, ACCOUNT_DELETED, ACCOUNT_CREATED, ACCOUNT_EDITED, ACCESS_GRANTED, ACCESS_DENIED,  INVALID_USERNAME, USER_NAME_USED, INVALID_PASSWORD, EMAIL_USED, EMAIL_INCORRECT, EMAIL_CORRECT}
/*
* Klasa reprezentuje konkretnego uzytkownika aplikacji
* posiada pola:
*       String name - nazwa uzytkownika
*       char[] password - haslo uzytkownika
*       String email - adres email uzytkownika
*       LinkedList<Expense> expenseLinkedList - lista wydatkow uzytkownika
* posiada metody, ktore umozliwiaja zarzadzenie kontem uzytkownika.*/
public class User {
    private static String name;
    private static char[] password;
    private static String email;
    private static LinkedList<Expense> expenseLinkedList = null;

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
     *          (1) Outcome.ACCESS_GRANTED jezeli uzytkownik zalogowal sie pomyslnie
     *          (2) Outcome.INVALID_USERNAME jezeli uzytkownik wprowadzil bledna nazwe uzytkownika
     *          (3) Outcome.INVALID_PASSWORD jezeli uzytkownik wprowadzil bledne haslo
     *          (4) Outcome.ACCESS_DENIED w przeciwnym wypadku
     */
    public Outcome logIn(){
        try{
            String command = "SELECT password FROM users WHERE users.name = " + "'" + name + "'";
            Statement stat = Main.getStatement();
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
            System.out.println(e.getMessage());
        }
        return Outcome.ACCESS_DENIED;
    }
    /*
    * Dodaje nowy wydatek do listy wydatkow LinkedList*/
    public static void addExpense(Expense newExpense){
        if(expenseLinkedList == null) expenseLinkedList = new LinkedList<>();
        expenseLinkedList.add(newExpense);
    }
    /*
    * Usuwa konkretny wydatek z listy wydatkoow LinkedList
    * */
    public static void removeExpense(Expense expense){
        expenseLinkedList.remove(expense);
    }
    /*
    * Pobiera z bazy danych wydatki uzytkownika i dodaje je do LinkedList
    * */
    public void getUserExpenses(){
        String message = "SELECT e.expense_id, e.name, e.amount, ec.name AS category, e.expense_date " +
                "FROM expenses e " +
                "JOIN users u using(user_id) " +
                "JOIN expense_categories ec USING(category_id) " +
                "WHERE u.name = '" + name + "'";
        try{
            Statement stat = Main.getStatement();
            ResultSet rs = stat.executeQuery(message);
            if(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double amount = rs.getDouble(3);
                String category = rs.getString(4);
                String date = rs.getString(5);
                expenseLinkedList = new LinkedList<>();
                var newExpense = new Expense(name, amount, category, date, id);
                expenseLinkedList.add(newExpense);
            }
            while(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double amount = rs.getDouble(3);
                String category = rs.getString(4);
                String date = rs.getString(5);
                var newExpense = new Expense(name, amount, category, date, id);
                expenseLinkedList.add(newExpense);
            }
        }catch(SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    /*
    * Wylogowuje uzytkownika z aplikacji, ustawiajac wartosc null polu loggedUser w klasie Main
    * */
    public void logOut(){
        Main.setLoggedUser(null);
        EditExpenseDialog.clear();
        AddNewExpenseDialog.clear();
        LoggingDialog.clear();
        CreateAccountDialog.clear();
        MainPanel.reload();
    }
    /*
     * Tworzy nowe rekord tabeli persons i wstawia go do bazy danych
     * @return wartosc powodzenia operacji
     *          (1) Outcome.ACCOUNT_CREATED jesli konto zostalo poprawnie utworzone
     *          (2) Outcome.USER_NAME_USED jezeli podana nazwa uzytkownika juz jest w bazie danych
     *          (3) Outcome.EMAIL_USED jezeli podany adres e-mail zotal juz jest w bazie danych
     *          (4) Outcome.ERROR w przeciwnym wypadku
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
                    Statement stat = Main.getStatement();
                    if(stat.executeUpdate(insert) == 1)
                        return Outcome.ACCOUNT_CREATED;
                    else return Outcome.ERROR;
                }catch(SQLException e){
                    throw new RuntimeException(e.getMessage());
                }
            }
            else return Outcome.EMAIL_USED;
        }
        return Outcome.USER_NAME_USED;
    }
    /*
     * Pobiera od uzytkownika nowe dane konta
     * i aktualizuje dany rekord w bazie danych
     * @return wartosc powodzenia operacji
     *          (1) Outcome.ACCOUNT_EDITED jesli konto zostalo pomyslnie zedytowane
     *          (2) Outcome.USER_NAME_USED jezeli podana nazwa uzytkownika juz jest w bazie danych
     *          (3) Outcome.EMAIL_USED jezeli podany adres e-mail zotal juz jest w bazie danych
     *          (4) Outcome.ERROR w przeciwnym wypadku
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
        try{
            Statement stat = Main.getStatement();
            if(stat.executeUpdate(update) != 0){
                setName(newUserName);
                setPassword(newPassword);
                setEmail(newEmail);
                return Outcome.ACCOUNT_EDITED;
            }
            else return Outcome.ERROR;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Outcome.ERROR;
    }
    /*
     *  Usuwa konto z bazy danych, usuwajac
     * @return wartosc powodzenia operacji
     *          (1) Outcome.ACCOUNT_DELETED jesli konto zostalo pomyslnie usuniete
     *          (2) Outcome.ERROR w przeciwnym wypadku
     * */
    public static Outcome deleteAccount(){
        String deleteUser = "DELETE FROM users WHERE name = '" + name + "'";
        String deleteExpenses = "DELETE FROM expenses WHERE user_id = " +
                "(SELECT user_id FROM users WHERE name = '" + name + "')";
        try{
            Statement stat = Main.getStatement();
            stat.executeUpdate(deleteExpenses);
            if(stat.executeUpdate(deleteUser) != 0)
                //usuniecie uzytkownika udalo sie
                return Outcome.ACCOUNT_DELETED;
            else return Outcome.ERROR;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Outcome.ERROR;
    }
    /*
     * Sprawdza czy uzytkownik o danej nazwie jest juz w bazie danych
     * @param conn obiekt przechowujacy polaczenie z baza danych
     * @return (1) true jesli jest już uzytkownik o takiej nazwie
     *         (2) false w przeciwnym przypadku
     * */
    public static boolean checkUserName(){
        try{
            Statement stat = Main.getStatement();
            String command = "SELECT name FROM users WHERE name = '" + name + "';";
            ResultSet rs = stat.executeQuery(command);
            return rs.next();
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    /*
     * Sprawdza czy uzytkownik o danej nazwie jest juz w bazie danych
     * @param conn obiekt przechowujacy polaczenie z baza danych
     * @param name nazwa uzytkownika do sprawdzenia w DB
     * @return (1) true jesli jest już uzytkownik o takiej nazwie
     *         (2) false w przeciwnym przypadku
     * */
    public static boolean checkUserName(String name){
        try{
            Statement stat = Main.getStatement();
            String command = "SELECT name FROM users WHERE name = '" + name + "';";
            ResultSet rs = stat.executeQuery(command);
            return rs.next();
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static Outcome verifyEmail(){
        String domain = "";

        for (int i = 0; i < email.length(); i++) {
            if(email.charAt(i) == '@'){
                for (int j = i+1; j < email.length(); j++) {
                    domain += email.charAt(j);
                }
                if(domain.contains(".") && domain.charAt(0) != '.') return Outcome.EMAIL_CORRECT;
                else return Outcome.EMAIL_INCORRECT;
            }
        }
        return Outcome.EMAIL_INCORRECT;
    }
    /*
     * Sprawdza czy uzytkownik o danym adresie e-mail jest juz w bazie danych
     * @param conn obiekt przechowujacy polaczenie z baza danych
     * @return boolean.true jesli jest już uzytkownik o danym adresie e-mail
     *         boolean.false w przeciwnym przypadku
     * */
    public static boolean checkUserEmail() {
        try {
            Statement stat = Main.getStatement();
            String command = "SELECT email FROM users WHERE email = '" + email + "';";
            ResultSet rs = stat.executeQuery(command);
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            Statement stat = Main.getStatement();
            String command = "SELECT email FROM users WHERE email = '" + email + "';";
            ResultSet rs = stat.executeQuery(command);
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    /*
    * Zwraca wydatek o konkretym indeksie w liscie wydatkow
    * @return (1) obiekt reprezentujacy konkretny wydatek (2) null jezeli lista jest puista*/
    public static Expense getExpense(int index){
        if(expenseLinkedList != null){
            return expenseLinkedList.get(index);
        }
        else return null;
    }
    /*
    * Zwraca srednia wydatków danego uzytkownika obliczona w bazie danych
    * @return double srenia wydatkow uzytkownika*/
    public static double getAverageExpense(){
        Statement stat = Main.getStatement();
        String select = "SELECT AVG(e.amount) FROM expenses e JOIN users u USING(user_id) \n" +
                "WHERE user_id = \n" +
                "(SELECT user_id FROM users WHERE name = '"+name+"')\n" +
                "GROUP BY user_id ;";
        ResultSet rs;
        double avg = 0.0;
        try{
            rs =stat.executeQuery(select);
            if(rs.next()) avg = rs.getDouble(1);
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return avg;
    }
    /*
    * Zwraca srednia wydatkow danego uzytkownika w bierzacym miesiacu, obliczona w bazie danych
    * @return double srednia wydatkow uzytkownika w bierzacym miesiacu*/
    public static double getMonthAverageExp(){
        Statement stat = Main.getStatement();

        ResultSet rs;
        LocalDate currentDate = LocalDate.now();
        String monthStart = currentDate.getYear() + "-" + currentDate.getMonthValue() + "-01";
        String monthEnd;
        if(currentDate.getMonth() == Month.DECEMBER) currentDate = currentDate.plusYears(1);
        currentDate = currentDate.plusMonths(1);
        monthEnd = currentDate.getYear() + "-" + currentDate.getMonthValue() + "-01";

        String select = "SELECT AVG(amount) FROM expenses e JOIN users u USING(user_id)\n" +
                "WHERE user_id = (SELECT user_id FROM users WHERE name = '"+name+"') \n" +
                "AND expense_date BETWEEN '"+monthStart+"' AND '"+monthEnd+"' AND expense_date != '"+monthEnd+"'\n" +
                "GROUP BY user_id\n" +
                "ORDER BY SUM(amount) DESC;";

        double avg = 0.0;
        try{
            rs =stat.executeQuery(select);
            if(rs.next()) avg= rs.getDouble(1);
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return avg;
    }
    /*
    * Zwraca nazwe kategorii, w ktorej uzytkownik zrobil najwiecej wydatkow w danym miesiacu
    * @return String naza kategorii pobrana z bazy danych*/
    public static String getMonthCategory(){
        Statement stat = Main.getStatement();

        ResultSet rs;
        LocalDate currentDate = LocalDate.now();
        String monthStart = currentDate.getYear() + "-" + currentDate.getMonthValue() + "-01";
        String monthEnd;
        if(currentDate.getMonth() == Month.DECEMBER) currentDate = currentDate.plusYears(1);
        currentDate = currentDate.plusMonths(1);
        monthEnd = currentDate.getYear() + "-" + currentDate.getMonthValue() + "-01";

        String select = "SELECT c.name, SUM(amount) FROM expenses e JOIN users u USING(user_id) JOIN expense_categories c USING (category_id)\n" +
                "WHERE user_id = (SELECT user_id FROM users WHERE name = '"+name+"') \n" +
                "AND expense_date BETWEEN '"+monthStart+"' AND '"+monthEnd+"' AND expense_date != '"+monthEnd+"'\n" +
                "GROUP BY category_id\n" +
                "ORDER BY SUM(amount) DESC;";

        String category = "Brak";
        try{
            rs =stat.executeQuery(select);
            if(rs.next()) category = rs.getString("name");
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return category;
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
    public static LinkedList<Expense> getExpenseLinkedList() {
        return expenseLinkedList;
    }
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
    /*
    * Zwraca aktualna ilosc wydatkow uzytkownika
    * @return rozmiar listy wydatkow
    * */
    public static int getExpenseCount(){
        if(expenseLinkedList == null) return 0;
        else return expenseLinkedList.size();
    }

    @Override
    public String toString(){
        return this.getClass().getName() + " name: " + name + " email: " + email;
    }
}
