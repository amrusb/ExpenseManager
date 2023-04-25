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
        accountMenu.add(logIn);
        accountMenu.add(createAccount);
        accountMenu.addSeparator();
        accountMenu.add(logOut);
        accountMenu.add(editAccount);
        accountMenu.addSeparator();
        accountMenu.add(exit);

        //LOGIN
        logIn.addActionListener(e->{
            if(dialog != null) dialog.setVisible(false);
            dialog = new LoggingDialog(parent);
            dialog.setVisible(true);
        });
        //CREATE ACCOUNT
        createAccount.addActionListener(e->{
            if(dialog != null) dialog.setVisible(false);
            dialog = new CreateAccountDialog(parent);
            dialog.setVisible(true);
        });
        //LOGOUT
        logOut.addActionListener(e->{
            Main.getLoggedUser().logOut();
            String message = "Wylogowano pomyślnie";
            JOptionPane.showConfirmDialog(this, message, "Potwierdzenie",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            reload();
        });
        //EDIT ACCOUNT
        editAccount.addActionListener(e->{
            if(dialog != null) dialog.setVisible(false);
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
        //ADD EXPENSE
        addExpense.addActionListener(e->{
            if(dialog != null) dialog.setVisible(false);
            dialog = new AddNewExpenseDialog(parent);
            dialog.setVisible(true);
        });
        //EDIT EXPENSE
        editExpense.addActionListener(e->{
            if(dialog != null) dialog.setVisible(false);
            dialog = new EditExpenseDialog(parent);
            dialog.setVisible(true);
        });
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
        if(User.getExpenseLinkedList() == null || User.getExpenseLinkedList().size() == 0){
            editExpense.setEnabled(false);
        }
        else{
            editExpense.setEnabled(true);
        }
    }
}
