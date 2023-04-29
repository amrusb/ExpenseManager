import javax.swing.*;

/*Typ wyliczeniowy używany w klasie DialogFactory jako argument metody createDialog()
do wyboru odpowiedniego typu dialogu, który zostanie utworzony*/
enum DialogType{LOGIN, CREATE_ACCOUNT, EDIT_ACCOUNT, ADD_EXPENSE, EDIT_EXPENSE}

/* Klasa implementuje wzorzec projektory *factory*, udostepniajac metode
* tworzaca okno dialogowe zalezna od przekazanego typu */
public class DialogFactory {
    /*Zwraca okno dialogowe okreslonego typu
    * @param DialogType type typ okna dialogowego
    * @param MainFrame parent obiekt glownej ramki
    * @return MyDialog klasa dziedziczaca po klasie MyDialog*/
    public static MyDialog createDialog(DialogType type, MainFrame parent){
        switch(type){
            case LOGIN ->{
                return new LoggingDialog(parent);
            }
            case CREATE_ACCOUNT ->{
                return  new CreateAccountDialog(parent);
            }
            case EDIT_ACCOUNT->{
                return new EditAccountDialog(parent);
            }
            case ADD_EXPENSE ->{
                return  new AddNewExpenseDialog(parent);
            }
            case EDIT_EXPENSE->{
                return new EditExpenseDialog(parent);
            }
        }
        return new MyDialog();
    }
}
