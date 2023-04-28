import javax.swing.*;
import java.awt.*;

/*Klasa reprezentuje pasek menu, znajdujący się na górze okna*/
public class MainMenuBar extends JMenuBar {
    private static final JMenu accountMenu = new JMenu("Konto");
    private static final JMenuItem createAccount = new JMenuItem("Utwórz", new ImageIcon("icons/create_account.png"));
    private static final JMenuItem logIn = new JMenuItem("Zaloguj",new ImageIcon("icons/log_in.png"));
    private static final JMenuItem logOut = new JMenuItem("Wyloguj", new ImageIcon("icons/log_out.png"));
    private static final JMenuItem editAccount= new JMenuItem("Edytuj", new ImageIcon("icons/edit_account.png"));
    private static final JMenuItem exit = new JMenuItem("Wyjdź", new ImageIcon("icons/exit.png"));
    private static final JMenu expenseMenu = new JMenu("Wydatek");
    private static final JMenuItem addExpense = new JMenuItem("Dodaj", new ImageIcon("icons/add_expense.png"));
    private static final JMenuItem editExpense = new JMenuItem("Edytuj", new ImageIcon("icons/edit_expense.png"));
    private static JDialog dialog = null;
    public MainMenuBar(MainFrame parent){        //ACCOUNT BAR
        setBorder(BorderFactory.createEmptyBorder());
        setPreferredSize(new Dimension(MainFrame.getScreenSizeWidth(), 30));
        accountMenu.add(logIn);
        accountMenu.add(createAccount);
        accountMenu.addSeparator();
        accountMenu.add(logOut);
        accountMenu.add(editAccount);
        accountMenu.addSeparator();
        accountMenu.add(exit);

        accountMenu.setFont(MainFrame.getBasicFont());
        createAccount.setFont(MainFrame.getBasicFont());
        logIn.setFont(MainFrame.getBasicFont());
        logOut.setFont(MainFrame.getBasicFont());
        editAccount.setFont(MainFrame.getBasicFont());
        exit.setFont(MainFrame.getBasicFont());

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
            if(Main.isLogged()) Main.setLoggedUser(null);
            Main.closeConnection();
            System.exit(0);
        });

        //EXPENSE BAR
        expenseMenu.add(addExpense);
        expenseMenu.add(editExpense);

        expenseMenu.setFont(MainFrame.getBasicFont());
        addExpense.setFont(MainFrame.getBasicFont());
        editExpense.setFont(MainFrame.getBasicFont());
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
    /*
    * Po sprawdzeniu stanu zalogowaania uzytkownika ustawia dostepnosc
    * elementow menu*/
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
        editExpense.setEnabled(User.getExpenseLinkedList() != null && User.getExpenseLinkedList().size() != 0);
    }
}
