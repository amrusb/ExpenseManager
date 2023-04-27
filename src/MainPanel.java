import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setBackground(new Color(227, 227, 227));

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
    private static final JTextField userName = new JTextField("Zaloguj się, aby korzytać z aplikacji");
    private static final JFormattedTextField  averageExpense = new JFormattedTextField(new NumberFormatter(new DecimalFormat("")));
    private static final JFormattedTextField  monthAverageExpense = new JFormattedTextField(new NumberFormatter(new DecimalFormat("")));
    private static final JTextField mostCommonCategory = new JTextField("");
    private static final Color MAIN_COLOR = new Color(246, 246, 246);
    private static final Color HEADER_COLOR = new Color(40,40,40);
    private static final Color TEXT_COLOR = new Color(64, 64, 64);
    public InfoPanel(){
        var GBlayout = new GridBagLayout();
        setLayout(GBlayout);
        setBackground(MAIN_COLOR);

        var con = new GridBagConstraints();
        con.weightx = 100;
        con.weighty = 100;
        con.insets.set(20, 20, 5, 20);
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 2;
        con.gridheight = 1;
        con.fill = GridBagConstraints.HORIZONTAL;
        var loggedLabel = new JLabel("Zalogowany jako:");
        loggedLabel.setForeground(HEADER_COLOR);
        loggedLabel.setFont(MainFrame.getHeader2Font());
        add(loggedLabel, con);

        con.gridy = 1;
        con.insets.top = 5;
        userName.setEditable(false);
        userName.setFont(new Font("SansSerif", Font.ITALIC, 13));
        userName.setBackground(MAIN_COLOR);
        userName.setForeground(TEXT_COLOR);
        userName.setBorder(BorderFactory.createEmptyBorder());
        add(userName, con);

        con.insets.top = 20;
        con.insets.bottom = 10;
        con.gridy = 2;
        var sumLabel = new JLabel("Podsumowanie:");
        sumLabel.setForeground(HEADER_COLOR);
        sumLabel.setFont(MainFrame.getHeaderFont());
        add(sumLabel, con);

        con.insets.top = 5;
        con.insets.bottom = 5;
        con.gridy = 3;
        con.gridwidth = 1;
        var avgExpLabel = new JLabel("Śrenia wydatków:");
        avgExpLabel.setFont(MainFrame.getHeader2Font());
        avgExpLabel.setForeground(TEXT_COLOR);
        add(avgExpLabel, con);

        con.gridx = 1;
        averageExpense.setEditable(false);
        averageExpense.setFont(MainFrame.getBasicFont());
        averageExpense.setBackground(MAIN_COLOR);
        averageExpense.setForeground(TEXT_COLOR);
        averageExpense.setBorder(BorderFactory.createEmptyBorder());
        add(averageExpense, con);

        con.gridy = 4;
        con.gridx = 0;
        var currentMonthLabel = new JLabel("Bierzący miesiąc:");
        currentMonthLabel.setFont(MainFrame.getHeader2Font());
        currentMonthLabel.setForeground(TEXT_COLOR);
        add(currentMonthLabel, con);

        con.gridx = 1;
        String[] months = {"Styczeń", "Luty", "Marzec",
                "Kwiecień", "Maj", "Czerwiec",
                "Lipiec", "Sierpień", "Wrzesień",
                "Październik", "Listopad", "Grudzień"};
        var monthTextField = new JTextField(months[LocalDate.now().getMonthValue() - 1]);
        monthTextField.setForeground(TEXT_COLOR);
        monthTextField.setFont(MainFrame.getBasicFont());
        monthTextField.setEditable(false);
        monthTextField.setBackground(MAIN_COLOR);
        monthTextField.setBorder(BorderFactory.createEmptyBorder());
        add(monthTextField, con);

        con.gridy = 5;
        con.gridx = 0;
        var monthAvgExpLabel = new JLabel("Średnia wydatków:");
        monthAvgExpLabel.setFont(MainFrame.getHeader2Font());
        monthAvgExpLabel.setForeground(TEXT_COLOR);
        add(monthAvgExpLabel, con);

        con.gridx = 1;
        monthAverageExpense.setEditable(false);
        monthAverageExpense.setBorder(BorderFactory.createEmptyBorder());
        monthAverageExpense.setFont(MainFrame.getBasicFont());
        monthAverageExpense.setBackground(MAIN_COLOR);
        monthAverageExpense.setForeground(TEXT_COLOR);
        add(monthAverageExpense, con);

        con.gridy = 6;
        con.gridx = 0;
        con.insets.bottom = 30;
        var mostCommonLabel = new JLabel("Najwięcej wydatków w kategorii:");
        mostCommonLabel.setFont(MainFrame.getHeader2Font());
        mostCommonLabel.setForeground(TEXT_COLOR);
        add(mostCommonLabel, con);

        con.gridx = 1;
        mostCommonCategory.setEditable(false);
        mostCommonCategory.setBorder(BorderFactory.createEmptyBorder());
        mostCommonCategory.setFont(MainFrame.getBasicFont());
        mostCommonCategory.setBackground(MAIN_COLOR);
        mostCommonCategory.setForeground(TEXT_COLOR);
        add(mostCommonCategory, con);
    }
    public static void reload(){
        if(!Main.isLogged()){
            userName.setText("Zaloguj się, aby korzytać z aplikacji");
            userName.setFont(new Font("SansSerif", Font.ITALIC, 13));
            averageExpense.setText("");
            monthAverageExpense.setText("");
            mostCommonCategory.setText("");
        }
        else{
            userName.setText(User.getName());
            userName.setFont(MainFrame.getBasicFont());
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
    private static final JTextField mostCommonCategory = new JTextField("");
    private static final Color MAIN_COLOR = new Color(246, 246, 246);
    private static final Color HEADER_COLOR = new Color(40,40,40);
    private static final Color TEXT_COLOR = new Color(64, 64, 64);
    public ExpensePanel(){
        var GBlayout = new GridBagLayout();
        setLayout(GBlayout);
        setBackground(MAIN_COLOR);

        var con = new GridBagConstraints();

        con.weightx = 100;
        con.weighty = 100;
        con.insets.set(20, 20, 10, 20);
        con.anchor = GridBagConstraints.WEST;
        con.fill = GridBagConstraints.HORIZONTAL;

        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 6;
        con.gridheight = 1;
        var yourExpensesLabel = new JLabel("Twoje wydatki:");
        yourExpensesLabel.setFont(MainFrame.getHeaderFont());
        yourExpensesLabel.setForeground(HEADER_COLOR);
        add(yourExpensesLabel, con);

        con.gridy = 1;
        con.gridwidth = 1;
        con.insets.top = 5;
        con.insets.bottom = 5;
        con.insets.right = 2;
        var monthLabel = new JLabel("Miesiąc:", JLabel.RIGHT);
        monthLabel.setFont(MainFrame.getBasicFont());
        monthLabel.setForeground(TEXT_COLOR);
        add(monthLabel, con);
        con.insets.right = 20;
        con.gridwidth = 2;
        con.gridx = 1;
        add(monthBox, con);

        con.gridy = 2;
        con.gridx = 0;
        con.gridwidth = 1;
        con.insets.top = 5;
        con.insets.bottom = 5;
        con.insets.right = 2;
        var yearLabel = new JLabel("Rok:", JLabel.RIGHT);
        yearLabel.setForeground(TEXT_COLOR);
        yearLabel.setFont(MainFrame.getBasicFont());
        add(yearLabel, con);

        con.gridx = 1;
        con.gridwidth = 2;
        con.insets.right = 20;
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
        con.gridheight = 4;
        con.gridx = 0;
        con.gridy = 3;
        con.insets.top = 5;
        con.insets.bottom = 30;
        con.fill = GridBagConstraints.BOTH;
        add(expenseTablePanel, con);

    }
    public static void reload(){
        ExpenseTablePanel.clear();
        if(Main.isLogged()){
            fillYearBox();
            //fillMonthBox();
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
        for (int i = year; i >= 2018; i--) {
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
    private static final JTextField mostCommonCategory = new JTextField("");
    private static final Color MAIN_COLOR = new Color(246, 246, 246);
    private static final Color HEADER_COLOR = new Color(40,40,40);
    private static final Color TEXT_COLOR = new Color(64, 64, 64);

    public ExpenseTablePanel(){
        super(table);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        model.setColumnIdentifiers(columnNames);
        table.setEnabled(false);

        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setForeground(TEXT_COLOR);
        table.setBackground(MAIN_COLOR);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        tableHeader.setForeground(HEADER_COLOR);
        tableHeader.setBackground((new Color(226, 232, 238)));
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

    }

    public static void clear(){
        model.setRowCount(0);
    }
    public static void centerValues(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        int columnCount = model.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        }

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
        centerValues();
    }
}