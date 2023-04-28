import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
* Klasa reprezentuje pojedynczy wydatek dokonany przez uzytkownika
* Posiada pola:
* String name - nazwa wydatku
* double amount - wydana kwota
* String category - kategoria wydatku
* String date - data wydatku w formacie yyyy-MM-dd
* int expense_id unikalny identyfikator wydatku
* Posiada metody, które pozwalają na dodawanie, usuwanie oraz edycję
* konkretnego wydatku w bazie danych*/
public class Expense {
        private String name;
        private double amount;
        private String category;
        private String date;
        private int expense_id;

    public Expense(String expenseName, double expenseAmount, String expenseCategory, String expenseDate){
        name = expenseName;
        amount = expenseAmount;
        category = expenseCategory;
        date = expenseDate;
    }
    public Expense(String expenseName, double expenseAmount, String expenseCategory, String expenseDate, int id){
        name = expenseName;
        amount = expenseAmount;
        category = expenseCategory;
        date = expenseDate;
        expense_id = id;
    }
    /*
    * Dodaje obiekt do bazy danych
    * @return (1) true jesli opoeracja sie powiodla (2) false w przeciwnym przypadku*/
    public boolean addToDatabase(){
        Statement stat = Main.getStatement();
        String userName = User.getName();
        String insert = "INSERT INTO expenses(name, category_id, amount, expense_date, user_id)" +
                " VALUES('" + name+"', " +
                //ustalenie id kategorium
                "(SELECT category_id FROM expense_categories WHERE name = '" + category + "'), "+
                amount + ", '"
                + date + "', " +
                //ustalenie id uzytkownika
                "(SELECT user_id FROM users WHERE name = '" + userName +"'))";

        try{
            if(stat.executeUpdate(insert) != 0 ){
                String select = "SELECT expense_id FROM expenses WHERE name = '" + name + "';";
                ResultSet rs = stat.executeQuery(select);
                if(rs.next()) expense_id = rs.getInt(1);
                User.addExpense(this);
                return true;
            }
            else return false;
        }
        catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }
    /*
    * Akutalizuje rekord zwiazany z danym obiektem w bazie danych
    * @return (1) true jezeli operacja sie poweidzie (2) false w przeciwnym wypadku*/
    public boolean editExpense(String newName, double newAmount, String newCategory, String newDate){
        Statement stat = Main.getStatement();
        String update = "UPDATE expenses\n" +
                "SET\n" +
                "name = '" + newName + "',\n" +
                "category_id = (SELECT category_id FROM expense_categories WHERE name = '" + newCategory + "'),\n"+
                "amount = " +  newAmount +",\n" +
                "expense_date = '"+  newDate +"'\n" +
                "WHERE expense_id = " + expense_id +";";
        try{
            if(stat.executeUpdate(update) != 0) {
                name = newName;
                amount = newAmount;
                category = newCategory;
                date = newDate;
            }
            return true;
        }
        catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }
    /*
    * Usuwa z bazy danych rekord zwiazany z danym obiektem
    * @return (1) true jezeli operacja sie powiedzie (2) false w przeciwnym wypadku*/
    public boolean deleteExpense(){
        String delete = "DELETE FROM expenses WHERE expense_id = " + expense_id;
        Statement stat = Main.getStatement();
        try{
            if(stat.executeUpdate(delete) != 0) {
                User.removeExpense(this);
                return true;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }
    public String getName() {return name;}

    public void setName(String newName) { name = newName; }

    public double getAmount() {return amount;}

    public void setAmount(double amount) {this.amount = amount;}

    public String getCategory() {return category;}
    /*
    * Pobiera z bazy danych pole category_id dla rekordu odpowiadajacemu kategori wydatku danego obiektu
    * @return indeks kategorium wydatku*/
    public int getCategoryIndex(){
        String select = "SELECT category_id FROM expense_categories WHERE name = '" + category + "';";
        Statement stat = Main.getStatement();
        int index = -1;
        try{
            ResultSet rs = stat.executeQuery(select);
            if(rs.next()) index = rs.getInt(1);
        } catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return index-1;
    }
    public void setCategory(String category) {this.category = category;}

    public String getDate() {return date;}

    public void setDate(String date) {this.date = date;}

    @Override
    public String toString(){
        return this.getClass().getName() + ":[name: " + name + " amount: " + amount + " category: " + category + " date: " + date +"]";

    }
}
