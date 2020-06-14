/*
 * Implemented using the "MVC" architectural pattern.
 * As you can see, i chose not to abstract the concept of "book"
 * since this is a purely event driven approach.
 *
 * Ντεβές Χαράλαμπος - 15766 -
 *
 * (Όσο για τα comments στα αγγλικά, δεν μπορώ να καταλάβω γιατί, άλλα με βολεύει πολύ περισσότερο έτσι.)
 */

import model.Controller;
import model.Model;
import ui.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

//pretty self explanatory:
public class MainClass {

    private static MainGUI gui;

    public static void main(String[] args) {

        try {
            //Γερασιμος Ελευθεριοτης - 15692 - suggested this one:
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException |
                IllegalAccessException |
                UnsupportedLookAndFeelException |
                InstantiationException e) {
            e.printStackTrace();
        }

        try {
            //dispatching the GUI in a Thread safe way
            //using the "invokeAndWait" ensures the gui will be created before passed to the Controller.
            EventQueue.invokeAndWait(() -> gui = new MainGUI());
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        new Controller(gui, new Model());

    }

}
