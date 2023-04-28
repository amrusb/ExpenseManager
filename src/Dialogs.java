import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/*
* Klasa reprezentuje okno dialogowe sluzace do logowania do programu*/
class LoggingDialog extends JDialog {
    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 150;
    private static final JTextField usernameField = new JTextField("");
    private static final JPasswordField passwordField = new JPasswordField("");
    private static final Color MAIN_COLOR = new Color(246, 246, 246);
    public LoggingDialog(MainFrame owner) {
        super(owner, "Zaloguj");
        setLayout(new BorderLayout());
        setBackground(MAIN_COLOR);

        int scrn_w = MainFrame.getScreenSizeWidth();
        int scrn_h = MainFrame.getScreenSizeHeight();
        setBounds((scrn_w - DEFAULT_WIDTH) / 2, (scrn_h - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);

        var loggingPanel = new JPanel();
        var layout = new GridLayout(2, 2);
        layout.setVgap(10);
        layout.setHgap(10);
        loggingPanel.setLayout(layout);

        var userNameLabel = new JLabel("Nazwa użytkownika:");
        userNameLabel.setFont(MainFrame.getHeader2Font());
        userNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        loggingPanel.add(userNameLabel);
        loggingPanel.add(usernameField);

        var passwordLabel = new JLabel("Hasło:");
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        passwordLabel.setFont(MainFrame.getHeader2Font());
        loggingPanel.add(passwordLabel);

        loggingPanel.add(passwordField);

        add(loggingPanel, BorderLayout.CENTER);

        var buttonPanel = new JPanel();
        var loginButton = new JButton("Zaloguj");

        loginButton.addActionListener(e -> {
            String userName;
            char[] userPassword;
            userName = usernameField.getText();
            userPassword = passwordField.getPassword();
            var user = new User(userName, userPassword);

            switch (user.logIn()) {
                case INVALID_PASSWORD -> {
                    String message = "Nieprawidłowe hasło";
                    JOptionPane.showConfirmDialog(this, message, "Błąd",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
                case INVALID_USERNAME -> {
                    String message = "Nieprawidłowa nazwa użytkownika";
                    JOptionPane.showConfirmDialog(this, message, "Błąd",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
                case ACCESS_GRANTED -> {
                    Main.setLoggedUser(user);
                    MainMenuBar.reload();
                    MainPanel.reload();
                    setVisible(false);
                }
                default -> {
                    String message = "Wystąpił nieoczekiwany błąd";
                    JOptionPane.showConfirmDialog(this, message, "Błąd",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(loginButton);
        var cancelButton = new JButton("Anuluj");
        cancelButton.addActionListener((e) -> setVisible(false));
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    /*
    * Usuwa wartosci z pola usernameField i passwordField
    * ustawiajac je na pusty String
    * */
    public static void clear(){
        usernameField.setText("");
        passwordField.setText("");
    }
}
/*
* Klasa reprezentuje okno dialogowe sluzace do tworzenia konta
* */
class CreateAccountDialog extends JDialog {
    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 220;
    private static final JTextField userNameField = new JTextField();
    private static final JTextField emailField = new JTextField();
    private static final JPasswordField passwordField = new JPasswordField();
    private static final JPasswordField repeatPasswordField = new JPasswordField();


    public CreateAccountDialog(MainFrame owner) {
        super(owner, "Utwórz konto");
        setLayout(new BorderLayout());
        int scrn_w = MainFrame.getScreenSizeWidth();
        int scrn_h = MainFrame.getScreenSizeHeight();
        setBounds((scrn_w  - DEFAULT_WIDTH) / 2, (scrn_h - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);

        var createAccountPanel = new JPanel();
        var layout = new GridLayout(4,2);
        createAccountPanel.setLayout(layout);
        layout.setVgap(10);
        layout.setHgap(10);

        var userNameLabel = new JLabel("Nazwa użytkownika:");
        userNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        userNameLabel.setFont(MainFrame.getHeader2Font());
        createAccountPanel.add(userNameLabel);
        createAccountPanel.add(userNameField );

        var emailLabel = new JLabel("Adres e-mail:");
        emailLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        emailLabel.setFont(MainFrame.getHeader2Font());
        createAccountPanel.add(emailLabel);
        createAccountPanel.add(emailField);

        var passwordLabel = new JLabel("Hasło:");
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        passwordLabel.setFont(MainFrame.getHeader2Font());
        createAccountPanel.add(passwordLabel);
        createAccountPanel.add(passwordField);

        var repeatPasswordLabel = new JLabel("Powtórz hasło:");
        repeatPasswordLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        repeatPasswordLabel.setFont(MainFrame.getHeader2Font());
        createAccountPanel.add(repeatPasswordLabel);
        createAccountPanel.add(repeatPasswordField);

        add(createAccountPanel, BorderLayout.CENTER);

        var createButton = new JButton("Utwórz");
        createButton.addActionListener(e -> {
            if (Arrays.equals(passwordField.getPassword(), repeatPasswordField.getPassword())) {
                var newUser = new User(
                        userNameField.getText(),
                        passwordField.getPassword(),
                        emailField.getText());
                String message;
                switch(newUser.verifyEmail()){
                    case EMAIL_INCORRECT -> {
                        message = "Błędy adres e-mail";
                        JOptionPane.showConfirmDialog(this, message, "Błąd",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE);
                    }
                    case EMAIL_CORRECT -> {
                        switch (newUser.createAccount()) {
                            case USER_NAME_USED -> {
                                message = "Nazwa użytkownika została już użyta";
                                JOptionPane.showConfirmDialog(this, message, "Błąd",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            case EMAIL_USED -> {
                                message = "Adres e-mail został już użyty";
                                JOptionPane.showConfirmDialog(this, message, "Błąd",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            case ACCOUNT_CREATED -> {
                                message = "Konto utworzono pomyślnie\nZaloguj się, aby kontynuować.";
                                JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.PLAIN_MESSAGE);
                                clear();
                                setVisible(false);
                            }
                            default -> {
                                message = "Wystąpił nieoczekiwany błąd";
                                JOptionPane.showConfirmDialog(this, message, "Błąd",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    default->{
                        message = "Wystąpił nieoczekiwany błąd";
                        JOptionPane.showConfirmDialog(this, message, "Błąd",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else {
                String message = "Hasła się różnią";
                JOptionPane.showConfirmDialog(this, message, "Błąd",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }

        });
        var cancelButton = new JButton("Anuluj");
        cancelButton.addActionListener(e -> {
            clear();
            setVisible(false);
        });
        var buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    /*
     * Usuwa wartosci z pola usernameField, emailField, passwordField i repeatPasswordField
     * ustawiajac je na pusty String
     * */
    public static void clear(){
        userNameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        repeatPasswordField.setText("");
    }
}
/*
 * Klasa reprezentuje okno dialogowe sluzace do edytowania danych konta uzytkownika
 * */
class EditAccountDialog extends JDialog{
    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 200;
    private static final JTextField userNameField = new JTextField();
    private static final JTextField emailField = new JTextField();
    private static final JPasswordField passwordField = new JPasswordField();
    private static final Color MAIN_COLOR = new Color(246, 246, 246);
    public EditAccountDialog(MainFrame owner){
        super(owner, "Edytuj konto");
        setLayout(new BorderLayout());
        setBackground(MAIN_COLOR);
        int scrn_w = MainFrame.getScreenSizeWidth();
        int scrn_h = MainFrame.getScreenSizeHeight();
        setBounds((scrn_w  - DEFAULT_WIDTH) / 2, (scrn_h - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);
        setLayout(new BorderLayout());

        var editPanel = new JPanel();
        var layout = new GridLayout(3,2);
        layout.setVgap(10);
        layout.setHgap(10);
        editPanel.setLayout(layout);

        var userNameLabel = new JLabel("Nazwa użytkownika:");
        userNameLabel.setFont(MainFrame.getHeader2Font());
        userNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        editPanel.add(userNameLabel);

        userNameField.setText(User.getName());
        editPanel.add(userNameField);

        var emailLabel = new JLabel("Adres e-mail:");
        emailLabel.setFont(MainFrame.getHeader2Font());
        emailLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        editPanel.add(emailLabel);

        emailField.setText(User.getEmail());
        editPanel.add(emailField);

        var passwordLabel = new JLabel("Hasło:");
        passwordLabel.setFont(MainFrame.getHeader2Font());
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        editPanel.add( passwordLabel);

        passwordField.setText(User.getStringPassword());
        editPanel.add(passwordField);

        add(editPanel, BorderLayout.CENTER);

        var buttonsPanel = new JPanel();
        var acceptButton = new JButton("Zatwierdź");
        acceptButton.addActionListener(e->{
            switch (User.verifyEmail()){
                case EMAIL_INCORRECT -> {
                    String message = "Błędy adres e-mail";
                    JOptionPane.showConfirmDialog(this, message, "Błąd",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
                case EMAIL_CORRECT -> {
                    switch(User.editAccount(
                            userNameField.getText(),
                            passwordField.getPassword(),
                            emailField.getText())){
                        case USER_NAME_USED -> {
                            String message = "Konto o takiej nazwie już istnieje. Wprowadź inny.";
                            JOptionPane.showConfirmDialog(this, message, "Błąd",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE);

                        }
                        case EMAIL_USED -> {
                            String message = "Konto o takim adresie e-mail już istnieje. Wprowadź inny.";
                            JOptionPane.showConfirmDialog(this, message, "Błąd",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE);

                        }
                        case ACCOUNT_EDITED -> {
                            String message = "Zmiany zostały wprowadzone";
                            JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
                        }
                        case ERROR -> {
                            String message = "Wystąpił nieoczekiwany błąd";
                            JOptionPane.showConfirmDialog(this, message, "Błąd",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE);

                        }
                    }
                }
                default -> {
                    String message = "Wystąpił nieoczekiwany błąd";
                    JOptionPane.showConfirmDialog(this, message, "Błąd",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        var cancelButton = new JButton("Anuluj");
        var deleteButton = new JButton("Usuń");

        buttonsPanel.add(acceptButton);
        buttonsPanel.add(deleteButton);
        deleteButton.addActionListener(e->{
            //SHOW CONFIRMATION
            String message = "Czy na pewno chcesz usunąć konto?\nUsunięcie konta nie ulega cofnięciu.";
            int choice = JOptionPane.showConfirmDialog(this, message, "Ostrzeżenie",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if(choice == JOptionPane.OK_OPTION){
                switch(User.deleteAccount()){
                    case ACCOUNT_DELETED -> {
                        Main.getLoggedUser().logOut();
                        MainMenuBar.reload();
                        MainPanel.reload();

                        message = "Konto zostało usunięte";
                        JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                    }
                    case ERROR -> {
                        message = "Wystąpił nieoczekiwany błąd";
                        JOptionPane.showConfirmDialog(this, message, "Błąd",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE);
                    }
                }


            }
        });
        buttonsPanel.add(cancelButton);
        cancelButton.addActionListener( e-> setVisible(false));
        add(buttonsPanel, BorderLayout.SOUTH);
    }
    /*
     * Usuwa wartosci z pola usernameField, emailField i passwordField
     * ustawiajac je na pusty String
     * */
    public static void clear(){
        userNameField.setText("");
        passwordField.setText("");
        emailField.setText("");
    }
}
/*
 * Klasa reprezentuje okno dialogowe sluzace do dodania nowego wydatku
 * przez uzytkownika
 * */
class AddNewExpenseDialog extends JDialog{
    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 220;
    private static int userExpenseCounter = User.getExpenseCount();
    private static String DEFAULT_NAME;
    private static final JTextField expenseNameField = new JTextField();
    private static final JFormattedTextField amountField = new JFormattedTextField(new NumberFormatter(new DecimalFormat(".00")));
    private static final JFormattedTextField dateField = new JFormattedTextField();
    private static final JComboBox<String> categoryComboBox = new JComboBox<>();

    public AddNewExpenseDialog(MainFrame owner){
        super(owner, "Dodaj nowy wydatek");
        int scrn_w = MainFrame.getScreenSizeWidth();
        int scrn_h = MainFrame.getScreenSizeHeight();
        setLayout(new BorderLayout());
        setBounds((scrn_w - DEFAULT_WIDTH )/ 2, (scrn_h - DEFAULT_HEIGHT )/ 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        var addExpensePanel = new JPanel();
        var layout = new GridLayout(4, 2);
        layout.setVgap(10);
        layout.setHgap(10);
        addExpensePanel.setLayout(layout);
        var expenseNameLabel = new JLabel("Nazwa wydatku:");
        expenseNameLabel.setFont(MainFrame.getHeader2Font());
        expenseNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        addExpensePanel.add(expenseNameLabel);
        setDefaultName();
        expenseNameField.setText(DEFAULT_NAME);
        addExpensePanel.add(expenseNameField);

        var amountLabel = new JLabel("Kwota:");
        amountLabel.setFont(MainFrame.getHeader2Font());
        amountLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        addExpensePanel.add(amountLabel);
        amountField.setText("0,00");
        addExpensePanel.add(amountField);

        var dateLabel = new JLabel("Data:");
        dateLabel.setFont(MainFrame.getHeader2Font());
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        addExpensePanel.add(dateLabel);
        MaskFormatter dataMaskFormatter = null;
        try{
            dataMaskFormatter = new MaskFormatter("####-##-##");
            dataMaskFormatter.setPlaceholderCharacter('_');
        }
        catch(ParseException e){
            System.out.println(e.getMessage());
        }
        dateField.setFormatterFactory(new DefaultFormatterFactory(dataMaskFormatter));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        dateField.setText(dtf.format(now));
        addExpensePanel.add(dateField);

        var categoryLabel = new JLabel("Kategoria:");
        categoryLabel.setFont(MainFrame.getHeader2Font());
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        addExpensePanel.add(categoryLabel);
        fillCategoriesBox();
        addExpensePanel.add(categoryComboBox);

        add(addExpensePanel, BorderLayout.CENTER);

        var buttonPanel = new JPanel();
        var addButton = new JButton("Dodaj");
        var cancelButton = new JButton("Anuluj");
        //ADD BUTTON ACTION LISTENER
        addButton.addActionListener(e->{
            if(amountField.getText().compareTo("") == 0) amountField.setText("0,00");
            if(checkDate(dateField.getText())){
                var categoryIndex = categoryComboBox.getSelectedIndex();
                String categoryName = getCategoryName(++categoryIndex);
                String expenseName = expenseNameField.getText();
                double expenseAmount = parseDecimal(amountField.getText());
                String expenseDate = dateField.getText();
                var newExpense = new Expense(expenseName, expenseAmount, categoryName, expenseDate);

                if(newExpense.addToDatabase()){
                    String message = "Dodano wydatek: " + expenseName;
                    JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    userExpenseCounter++;
                    clear();
                    MainMenuBar.reload();
                    MainPanel.reload();

                    setVisible(false);
                }
            }
        });
        //CANCEL BUTTON ACTION LISTENER
        cancelButton.addActionListener(e->{
            setVisible(false);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    /*
    * Ustawia domyslna nazwe wydatku na 'Wydatek (liczba dotychczasowych wydatkow + 1)'
    * */
    private void setDefaultName(){
         DEFAULT_NAME = "Wydatek " + (userExpenseCounter + 1);
    }
    /*
    * Czysci okno dialogowe ustawiajac domyslne wartosci
    * */
    public static void clear(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        dateField.setText(dtf.format(now));
        amountField.setText("0,00");
        expenseNameField.setText(DEFAULT_NAME);

    }
    /*
    * Przeksztalca liczbe zapisana w obiekcie typu String na wartosc double
    * */
    public double parseDecimal(String value){
        String decimal_str = "";
        for (int i = 0; i < value.length(); i++) {
            if(value.charAt(i) == ',') decimal_str+='.';
            else decimal_str += value.charAt(i);
        }
        return Double.parseDouble(decimal_str);
    }
    /*
    * Pobiera i zwraca z bazy danych nazwe kategorii o zadanym ineksie
    * @param index indeks kategorii wydatku w bazie danych
    * @return String nazwa kategori
    * */
    public String getCategoryName(int index){
        Statement stat = Main.getStatement();
        String select = "SELECT name FROM expense_categories WHERE category_id = " + index + ";";
        String name = null;
        try{
            ResultSet rs = stat.executeQuery(select);
            if(rs.next()) name = rs.getString(1);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return name;
    }
    /*
    * Wypelnia liste rozwijalna wartosciami kategorii wydatkow z bazy danych*/
    public void fillCategoriesBox(){
        Statement stat = Main.getStatement();
        String select = "SELECT name FROM expense_categories ORDER BY category_id;";
        try{
            ResultSet rs = stat.executeQuery(select);
            while(rs.next()){
                String category = rs.getString(1);
                categoryComboBox.addItem(category);
            }
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
    /*
    * Sprawdza poprawnosc wprowadzonej przez uzytkownika daty
    * i wyswietla odpowiedni komunikat
    * @return (1) false jesli wproawdzono niepoprawny format daty
    * lub data jest wieksza od terazniejszej
    * (2) true w przeciwnym wypadku*/
    public boolean checkDate(String date){
        LocalDate enteredDate;
        try {
            enteredDate = LocalDate.parse(date);
        }
        catch (DateTimeParseException e){
            String message = "Niepoprawny format daty.";
            JOptionPane.showConfirmDialog(this, message, "Błąd",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(enteredDate.isAfter(LocalDate.now())){
            String message = "Nie wybiegaj w przyszłość.";
            JOptionPane.showConfirmDialog(this, message, "Błąd",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else return true;
    }
}
/*
 * Klasa reprezentuje okno dialogowe sluzace do edytowania wydatkow przez uzytkownika
 * */
class EditExpenseDialog extends JDialog{
    private static final int DEFAULT_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 300;
    private static JComboBox<String> expenseNameComboBox = new JComboBox<>();
    private static final JTextField expenseNameField = new JTextField();
    private static final JFormattedTextField amountField = new JFormattedTextField(new NumberFormatter(new DecimalFormat("0.00")));
    private static final JFormattedTextField dateField = new JFormattedTextField();
    private static final JComboBox<String> categoryComboBox = new JComboBox<>();
    private static Expense currentExpense = User.getExpense(0);
    public EditExpenseDialog(MainFrame owner){
        super(owner, "Edytuj wydatek");
        int scrn_w = MainFrame.getScreenSizeWidth();
        int scrn_h = MainFrame.getScreenSizeHeight();
        setBounds((scrn_w - DEFAULT_WIDTH) / 2, (scrn_h - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout());

        clear();
        currentExpense = User.getExpense(0);
        fillExpenseNameBox(User.getExpenseLinkedList());
        expenseNameComboBox.addActionListener(e->{
            int index = expenseNameComboBox.getSelectedIndex();
            currentExpense = User.getExpense(index);
            reload();
        });
        expenseNameComboBox.setBorder(BorderFactory.createTitledBorder("Wybierz wydatek"));
        add(expenseNameComboBox, BorderLayout.NORTH);

        var editPanel = new JPanel();

        var layout = new GridLayout(4, 2);
        editPanel.setLayout(layout);
        layout.setVgap(10);
        layout.setHgap(10);
        editPanel.setLayout(layout);

        var expenseNameLabel = new JLabel("Nazwa wydatku:");
        expenseNameLabel.setFont(MainFrame.getHeader2Font());
        expenseNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        editPanel.add(expenseNameLabel);
        editPanel.add(expenseNameField);

        var amountLabel = new JLabel("Kwota:");
        amountLabel.setFont(MainFrame.getHeader2Font());
        amountLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        editPanel.add(amountLabel);
        editPanel.add(amountField);

        var dateLabel = new JLabel("Data:");
        dateLabel.setFont(MainFrame.getHeader2Font());
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        editPanel.add(dateLabel);
        MaskFormatter dataMaskFormatter = null;
        try{
            dataMaskFormatter = new MaskFormatter("####-##-##");
            dataMaskFormatter.setPlaceholderCharacter('_');
        }
        catch(ParseException e){
            System.out.println(e.getMessage());
        }
        dateField.setFormatterFactory(new DefaultFormatterFactory(dataMaskFormatter));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        dateField.setText(dtf.format(now));
        editPanel.add(dateField);

        var categoryLabel = new JLabel("Kategoria:");
        categoryLabel.setFont(MainFrame.getHeader2Font());
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        editPanel.add(categoryLabel);
        fillCategoriesBox();
        editPanel.add(categoryComboBox);
        reload();
        add(editPanel, BorderLayout.CENTER);

        var buttonsPanel = new JPanel();
        var acceptButton = new JButton("Zatwierdź");
        var cancelButton = new JButton("Anuluj");
        var deleteButton = new JButton("Usuń");

        buttonsPanel.add(acceptButton);
        //ACCEPT BUTTON ACTION LISTENER
        acceptButton.addActionListener(e->{
            var categoryIndex = categoryComboBox.getSelectedIndex();
            String newCategoryName = getCategoryName(++categoryIndex);
            String newName = expenseNameField.getText();
            double newAmount = parseDecimal(amountField.getText());
            String newDate = dateField.getText();
            if(currentExpense.editExpense(newName, newAmount, newCategoryName, newDate)){
                String message = "Zmiany dokonane pomyślnie";
                JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
                MainMenuBar.reload();
                MainPanel.reload();
            }
        });
        buttonsPanel.add(deleteButton);
        //DELETE BUTTON ACTION LISTENER
        deleteButton.addActionListener(e->{
            String message = "Czy na pewno chcesz usunąć ten wydatek?\nJego usunięcie nie ulega cofnięciu.";
            int choice = JOptionPane.showConfirmDialog(this, message, "Ostrzeżenie",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if(choice == JOptionPane.OK_OPTION){
                if(currentExpense.deleteExpense()){
                    message = "Wydatek został usunięty";
                    JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    MainMenuBar.reload();
                    MainPanel.reload();
                    clear();
                    setVisible(false);
                    }
                else{
                    message = "Wystąpił nieoczekiwany błąd";
                    JOptionPane.showConfirmDialog(this, message, "Błąd",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(cancelButton);
        cancelButton.addActionListener( e-> setVisible(false));
        add(buttonsPanel, BorderLayout.SOUTH);
    }/*
     * Ustawia wartosci domslne w oknie dialogowym
     * */
    public static void reload(){
        if(currentExpense != null){
            expenseNameField.setText(currentExpense.getName());
            amountField.setText(currentExpense.getAmount()+ "");
            dateField.setText(currentExpense.getDate());

            int categoryIndex = currentExpense.getCategoryIndex();
            categoryComboBox.setSelectedIndex(categoryIndex);
        }
        else{
            expenseNameField.setText("");
            amountField.setText("");
            dateField.setText("");
            categoryComboBox.setSelectedIndex(0);
        }
    }
    /*
     * Czysci okno dialogowe ustawiajac domyslne wartosci
     * */
    public static void clear(){
        dateField.setText("");
        amountField.setText("0,00");
        expenseNameField.setText("");
        expenseNameComboBox = new JComboBox<>();
        currentExpense = null;
    }
    /* Metoda pobiera nazwy kategorii wydatków z bazy danych i dodaje je do obiektu JComboBox*/
    public void fillCategoriesBox(){
        Statement stat = Main.getStatement();
        String select = "SELECT name FROM expense_categories ORDER BY category_id;";
        try{
            ResultSet rs = stat.executeQuery(select);
            while(rs.next()){
                String category = rs.getString(1);
                categoryComboBox.addItem(category);
            }
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
    /* Dodaje do obiektu JComboBox nazwy wszystkich wydatkow uzytkownika
    * @param expenseLingedList lista zawierajaca wydatki uzytkownika*/
    private static void fillExpenseNameBox(LinkedList<Expense> expenseLinkedList){
        if(expenseLinkedList != null){
            Iterator<Expense> expenseIterator = expenseLinkedList.iterator();
            String name;
            while(expenseIterator.hasNext()){
                name = expenseIterator.next().getName();
                expenseNameComboBox.addItem(name);
            }
        }
    }
    /*
     * Przeksztalca liczbe zapisana w obiekcie klasy String na wartosc double
     * @param value liczba zmiennoprzecinkowa zapisana w obiekcie klasy String
     * @return liczba w zapisie zmiennoprzecinkowym
     * */
    public double parseDecimal(String value){
        String decimal_str = "";
        for (int i = 0; i < value.length(); i++) {
            if(value.charAt(i) == ',') decimal_str+='.';
            else decimal_str += value.charAt(i);
        }
        return Double.parseDouble(decimal_str);
    }
    /*
     * Pobiera i zwraca z bazy danych nazwe kategorii o zadanym ineksie
     * @param index indeks kategorii wydatku w bazie danych
     * @return nazwa kategori
     * */
    public String getCategoryName(int index){
        Statement stat = Main.getStatement();
        String select = "SELECT name FROM expense_categories WHERE category_id = " + index + ";";
        String name = null;
        try{
            ResultSet rs = stat.executeQuery(select);
            if(rs.next()) name = rs.getString(1);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return name;
    }
}