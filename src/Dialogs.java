import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/*
* Klasa reprezentuje okno dialogowe sluzace do logowania do programu*/
class LoggingDialog extends JDialog {
    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 150;
    private static final JTextField usernameField = new JTextField("");
    private static final JPasswordField passwordField = new JPasswordField("");
    public LoggingDialog(MainFrame owner) {
        super(owner, "Zaloguj");
        setLayout(new BorderLayout());

        int scrn_w = MainFrame.getScreenSizeWidth();
        int scrn_h = MainFrame.getScreenSizeHeight();
        setBounds((scrn_w - DEFAULT_WIDTH) / 2, (scrn_h - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);

        var loggingPanel = new JPanel();
        loggingPanel.setLayout(new GridLayout(2, 2));

        loggingPanel.add(new JLabel("Nazwa użytkownika:"));
        loggingPanel.add(usernameField);

        loggingPanel.add(new JLabel("Hasło:"));
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
    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 200;
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
        createAccountPanel.setLayout(new GridLayout(4, 2));
        createAccountPanel.add(new JLabel("Nazwa użytkownika:"));
        createAccountPanel.add(userNameField );
        createAccountPanel.add(new JLabel("Adres e-mail:"));
        createAccountPanel.add(emailField);
        createAccountPanel.add(new JLabel("Hasło:"));
        createAccountPanel.add(passwordField);
        createAccountPanel.add(new JLabel("Powtórz hasło:"));
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
                        setVisible(false);
                    }
                    default -> {
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
        cancelButton.addActionListener(e -> setVisible(false));
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
    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 200;
    private static final JTextField userNameField = new JTextField();
    private static final JTextField emailField = new JTextField();
    private static final JPasswordField passwordField = new JPasswordField();
    public EditAccountDialog(MainFrame owner){
        super(owner, "Edytuj konto");
        setLayout(new BorderLayout());
        int scrn_w = MainFrame.getScreenSizeWidth();
        int scrn_h = MainFrame.getScreenSizeHeight();
        setBounds((scrn_w  - DEFAULT_WIDTH) / 2, (scrn_h - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);
        setLayout(new BorderLayout());

        var editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(4, 2));
        editPanel.add(new JLabel("Nazwa użytkownika"));
        userNameField.setText(User.getName());
        editPanel.add(userNameField);
        editPanel.add(new JLabel("Adres e-mail"));
        emailField.setText(User.getEmail());
        editPanel.add(emailField);
        editPanel.add(new JLabel("Hasło"));
        passwordField.setText(User.getStringPassword());
        editPanel.add(passwordField);

        add(editPanel, BorderLayout.CENTER);

        var buttonsPanel = new JPanel();
        var acceptButton = new JButton("Zatwierdź");
        acceptButton.addActionListener(e->{
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
        });
        var cancelButton = new JButton("Anuluj");
        var deleteButton = new JButton("Usuń konto");

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
