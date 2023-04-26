import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class MainPanel extends JPanel {
    public MainPanel(){
        var GBlayout = new GridBagLayout();
        setLayout(GBlayout);
        var con = new GridBagConstraints();

        JPanel infoPanel = new InfoPanel();
        JPanel expensePanel = new ExpensePanel();

        var border = BorderFactory.createEtchedBorder();
        expensePanel.setBorder(border);
        infoPanel.setBorder(border);

        con.weightx = 100;
        con.weighty = 100;
        con.insets.set(10, 10, 10, 5);
        con.anchor = GridBagConstraints.SOUTH;
        con.fill = GridBagConstraints.BOTH;

        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 2;
        con.gridheight = 1;

        add(expensePanel, con);
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 2;
        con.gridwidth = 1;
        con.insets.set(10, 5, 10, 10);
        add(infoPanel, con);

    }
    public static void reload(){
        InfoPanel.reload();
        ExpensePanel.reload();
    }
}
class InfoPanel extends JPanel{
    private static final JTextField userName = new JTextField("Nazwa użytkownika");
    private static final JFormattedTextField  averageExpense = new JFormattedTextField(new NumberFormatter(new DecimalFormat("0.00")));
    private static final JFormattedTextField  monthAverageExpense = new JFormattedTextField(new NumberFormatter(new DecimalFormat("0.00")));
    private static final JTextField mostCommonCategory = new JTextField("Brak");
    public InfoPanel(){
        var GBlayout = new GridBagLayout();
        setLayout(GBlayout);
        var con = new GridBagConstraints();
        con.weightx = 100;
        con.weighty = 100;
        con.insets.set(20, 20, 30, 20);
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 2;
        con.gridheight = 1;
        con.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Zalogowany jako:"), con);
        con.gridy = 1;
        userName.setEditable(false);
        userName.setBorder(BorderFactory.createEmptyBorder());
        add(userName, con);

        con.gridy = 2;
        add(new JLabel("Podsumowanie:"), con);
        con.gridy = 3;
        con.gridwidth = 1;
        add(new JLabel("Śrenia wydatków:"), con);
        con.gridx = 1;
        averageExpense.setEditable(false);
        averageExpense.setBorder(BorderFactory.createEmptyBorder());
        add(averageExpense, con);
        con.gridy = 4;
        con.gridx = 0;
        add(new JLabel("Bierzący miesiąc:"), con);
        con.gridx = 1;
        var monthTextField = new JTextField(LocalDate.now().getMonth().toString());
        monthTextField.setEditable(false);
        monthTextField.setBorder(BorderFactory.createEmptyBorder());
        add(monthTextField, con);
        con.gridy = 5;
        con.gridx = 0;
        add(new JLabel("Średnia wydatków w bierzącym miesiącu:"), con);
        con.gridx = 1;
        monthAverageExpense.setEditable(false);
        monthAverageExpense.setBorder(BorderFactory.createEmptyBorder());
        add(monthAverageExpense, con);
        con.gridy = 6;
        con.gridx = 0;
        add(new JLabel("Najwięcej wydatków w kategorii:"), con);
        con.gridx = 1;
        mostCommonCategory.setEditable(false);
        mostCommonCategory.setBorder(BorderFactory.createEmptyBorder());
        add(mostCommonCategory, con);
    }
    public static void reload(){
        if(!Main.isLogged()){
            userName.setText("Nazwa użytkownika");
            averageExpense.setText("0,00");
            monthAverageExpense.setText("0,00");
            mostCommonCategory.setText("Brak");
        }
        else{
            userName.setText(User.getName());
            String avgExpense = String.format("%.2f",User.getAverageExpense());
            averageExpense.setText(avgExpense);
            String monthAvg = String.format("%.2f",User.getMonthAverageExp());
            monthAverageExpense.setText(monthAvg);
            mostCommonCategory.setText(User.getMonthCategory());
        }
    }
}
class ExpensePanel extends JPanel{
    private static final JComboBox<Integer> yearBox = new JComboBox<>();
    private static final JComboBox<String> monthBox = new JComboBox<>();
    private static final JScrollPane expenseTablePanel = new ExpenseTablePanel();
    public ExpensePanel(){
        var GBlayout = new GridBagLayout();
        setLayout(GBlayout);
        var con = new GridBagConstraints();

        con.weightx = 100;
        con.weighty = 100;
        con.insets.set(20, 20, 30, 20);
        con.anchor = GridBagConstraints.NORTH;
        con.fill = GridBagConstraints.HORIZONTAL;

        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 6;
        con.gridheight = 1;

        add(new JLabel("Twoje wydatki:"), con);

        con.gridy = 1;
        con.gridwidth = 1;
        add(new JLabel("Miesiąc:"), con);
        con.gridwidth = 2;
        con.gridx = 1;
        add(monthBox, con);
        con.gridx = 3;
        con.gridwidth = 1;
        add(new JLabel("Rok:"), con);
        con.gridx = 5;
        con.gridwidth = 2;

        add(yearBox , con);
        yearBox.setEnabled(false);
        monthBox.setEnabled(false);

        yearBox.addActionListener(e->{
            monthBox.removeAllItems();
            fillMonthBox();
            if(yearBox.getSelectedItem() != null){
                int year = (int)yearBox.getSelectedItem();
                int month = monthBox.getSelectedIndex() + 1;
                ExpenseTablePanel.setValues(year, month);
            }

        });
        monthBox.addActionListener(e->{
            if(yearBox.getSelectedItem() != null){
                int year = (int)yearBox.getSelectedItem();
                int month = monthBox.getSelectedIndex() + 1;
                ExpenseTablePanel.setValues(year, month);
            }
        });
        //tabela
        con.gridwidth = 7;
        con.gridheight = 9;
        con.gridx = 0;
        con.gridy = 2;
        con.fill = GridBagConstraints.BOTH;
        add(expenseTablePanel, con);

    }
    public static void reload(){
        ExpenseTablePanel.clear();
        if(Main.isLogged()){
            fillYearBox();
            fillMonthBox();
            yearBox.setEnabled(true);
            monthBox.setEnabled(true);
            int year = (int)yearBox.getSelectedItem();
            int month = monthBox.getSelectedIndex() + 1;
            ExpenseTablePanel.setValues(year, month);
        }
        else{
            yearBox.removeAllItems();
            monthBox.removeAllItems();
            yearBox.setEnabled(false);
            monthBox.setEnabled(false);
        }
    }

    private static void fillYearBox(){
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        for (int i = year; i >= 2012; i--) {
            yearBox.addItem(i);
        }
    }
    private static void fillMonthBox(){
        String[] months = {"Styczeń", "Luty", "Marzec",
                "Kwiecień", "Maj", "Czerwiec",
                "Lipiec", "Sierpień", "Wrzesień",
                "Październik", "Listopad", "Grudzień"};

        if(yearBox.getSelectedIndex() == 0){
            //SELECTED CURRENT YEAR
            LocalDate currentDate = LocalDate.now();
            int currentMonth =  currentDate.getMonthValue();
            for (int i = 0; i < currentMonth ; i++) {
                monthBox.addItem(months[i]);
            }
            monthBox.setSelectedIndex(currentMonth-1);
        }
        else{
            for(int i = 0; i < 12; i++){
                monthBox.addItem(months[i]);
            }
        }
    }
}
class ExpenseTablePanel extends JScrollPane{
    private static final String[] columnNames = {"Nazwa", "Kwota", "Kategoria", "Data"};
    private static final DefaultTableModel model = new DefaultTableModel();
    private static final JTable table = new JTable();

    public ExpenseTablePanel(){
        super(table);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        model.setColumnIdentifiers(columnNames);
        table.setEnabled(false);
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

    }

    public static void clear(){
        model.setRowCount(0);
    }

    public static void setValues(int year, int month){
        clear();
        if(month == 0) month = 1;

        String startDate = year + "-" + month + "-01";
        if(month == 12){
            month = 0;
            year++;
        }
        String endDate = year + "-" + ++month + "-01";
        String select = "SELECT e.name, amount, c.name,  expense_date \n" +
                "FROM expenses e \n" +
                "JOIN expense_categories c \n" +
                "USING(category_id) \n" +
                "WHERE user_id = (SELECT user_id FROM users WHERE name = '"+User.getName()+"') \n" +
                "AND expense_date BETWEEN '"+startDate+"' AND '"+endDate+"' AND expense_date != '"+endDate+"' \n" +
                "ORDER BY expense_date DESC;";
        try{
            ResultSet rs = Main.getStatement().executeQuery(select);
            while(rs.next()){
                String name = rs.getString("e.name");
                double amount = rs.getDouble("amount");
                String category = rs.getString("c.name");
                String date = rs.getString("expense_date");
                model.addRow(new Object[]{name, amount, category, date});
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}