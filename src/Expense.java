public class Expense {
    private String name;
    private double amount;
    private String category;
    private String date;

    public Expense(String expenseName, double expenseAmount, String expenseCategory, String expenseDate){
        name = expenseName;
        amount = expenseAmount;
        category = expenseCategory;
        date = expenseDate;
    }

    public String getName() {return name;}

    public void setName(String newName) {name = newName;
    }

    public double getAmount() {return amount;}

    public void setAmount(double amount) {this.amount = amount;}

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public String getDate() {return date;}

    public void setDate(String date) {this.date = date;}

    @Override
    public String toString(){
        return this.getClass().getName() + ":[name: " + name + " amount: " + amount + " category: " + category + " date: " + date +"]";

    }
}
