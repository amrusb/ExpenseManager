import com.mysql.cj.log.Log;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {
    private static final JMenu accountMenu = new JMenu("Konto");
    private static final JMenuItem createAccount = new JMenuItem("Utwórz");
    private static final JMenuItem logIn = new JMenuItem("Zaloguj");
    private static final JMenuItem logOut = new JMenuItem("Wyloguj");
    private static final JMenuItem editAccount= new JMenuItem("Edytuj");
    private static final JMenuItem exit = new JMenuItem("Wyjdź");
    private static final JMenu expenseMenu = new JMenu("Wydatek");
    private static final JMenuItem addExpense = new JMenuItem("Dodaj");
    private static final JMenuItem editExpense = new JMenuItem("Edytuj");
    private static JDialog dialog = null;
    public MainMenuBar(MainFrame parent){
        //ACCOUNT BAR
        accountMenu.add(createAccount);
        accountMenu.add(logIn);
        accountMenu.addSeparator();
        accountMenu.add(logOut);
        accountMenu.add(editAccount);
        accountMenu.addSeparator();
        accountMenu.add(exit);
        //CREATE ACCOUNT
        createAccount.addActionListener(e->{
            dialog = new CreateAccountDialog(parent);
            dialog.setVisible(true);
        });
        //LOGIN
        logIn.addActionListener(e->{
            dialog = new LoggingDialog(parent);
            dialog.setVisible(true);
        });
        //LOGOUT
        logOut.addActionListener(e->{
            Main.getLoggedUser().logOut();
            String message = "Wylogowano pomyślnie";
            JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            LoggingDialog.clear();
            EditAccountDialog.clear();
            CreateAccountDialog.clear();
            reload();
        });
        //EDIT ACCOUNT
        editAccount.addActionListener(e->{
            dialog = new EditAccountDialog(parent);
            dialog.setVisible(true);
        });
        //EXIT
        exit.addActionListener( e-> {
            Main.closeConnection();
            System.exit(0);
        });

        //EXPENSE BAR
        expenseMenu.add(addExpense);
        expenseMenu.add(editExpense);

        add(accountMenu);
        add(expenseMenu);
    }

    public static void reload(){
        if(Main.isLogged()){
            createAccount.setEnabled(false);
            logIn.setEnabled(false);
            logOut.setEnabled(true);
            editAccount.setEnabled(true);

            expenseMenu.setEnabled(true);
        }
        else{
            createAccount.setEnabled(true);
            logIn.setEnabled(true);
            logOut.setEnabled(false);
            editAccount.setEnabled(false);

            expenseMenu.setEnabled(false);
        }
    }
}
