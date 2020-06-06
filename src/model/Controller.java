package model;

import ui.InsertGUI;
import ui.MainGUI;
import utilities.Checker;
import utilities.IOManager;

import javax.naming.InvalidNameException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/*
 * Main logic is here.
 * All the listeners are "custom made" (see inner classes bellow)
 * as the controller is instantiated in main, they get attached to the ui elements.
 * Using Synchronized Threads to handle IO, ensures collision avoidance and data loss.
 * As for the swing workers, i am a bit hesitant as i am not very accustomed to java's concurrency,
 * although it seems to function as expected.
 * (Arguably over engineered and unnecessary complex for such assignment)
 * (i wasn't able to test whether IO is actually thread safe, at least it works)
 */

public class Controller {

    private final MainGUI mainGUI;
    private final Model model;
    private final IOManager manager;
    private final Checker checker;
    private InsertGUI insertGUI;
    private boolean insertFormExists = false;

    public Controller(MainGUI mainGUI, Model model, IOManager manager, Checker checker) {
        this.mainGUI = mainGUI;
        this.model = model;
        this.manager = manager;
        this.checker = checker;

        this.mainGUI.addShowBooksListener(new ShowBooksListener());
        this.mainGUI.addInsertListener(new InsertListener());
        this.mainGUI.addQuitListener(new QuitListener());
        this.mainGUI.addSearchListener(new SearchListener());

        try {
            manager.readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addDeleteListeners() {
        //I chose to use a list of buttons to avoid coupling MainGUI with the model.Controller.
        //(although the button list is stored in MainGUI instead of the model.Model)
        for (JButton button : mainGUI.getDeleteButtons()) {
            button.addActionListener(new DeleteBooksListener());
        }
    }

    class WindowClosedListener extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent e) {
            insertFormExists = false;
            super.windowClosed(e);
        }
    }

    class DeleteBooksListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {

                    model.deleteBook(e.getActionCommand());
                    manager.writeToFile();
                    mainGUI.showBooksGui(model.getAllBooks(), true);
                    addDeleteListeners();

                    return null;

                }
            };

            worker.execute();
        }
    }

    class ShowBooksListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    mainGUI.showBooksGui(model.getAllBooks(), true);
                    addDeleteListeners();
                    return null;
                }
            };

            worker.execute();

        }
    }

    class InsertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //in order for the form to appear only once.
            //(i managed to implement a system where multiple forms could be present
            //and the data would still be fetched correctly (using list of insertGUIs to distinguish between them)
            //but it didn't make sense so i scraped it).
            if (!insertFormExists) {
                insertGUI = new InsertGUI();
                insertGUI.addSaveBookListener(new SaveBookListener());
                insertGUI.addWindowClosedListener(new WindowClosedListener());
                insertFormExists = true;
            }

        }
    }

    class SaveBookListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            manager.setGui(insertGUI);
            manager.handleData();

        }
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            SwingWorker<Void, Void> worker = new SwingWorker<>() {

                @Override
                protected Void doInBackground() {
                    System.out.println(mainGUI.getFieldText());
                    try {
                        //my eyes hurt
                        mainGUI.showBooksGui(
                                model.getBookSearched(
                                        checker.removePunctuation(mainGUI.getFieldText())), false);
                    } catch (InvalidNameException ex) {
                        mainGUI.showPopUpMain(ex.getMessage());
                    }
                    return null;
                }
            };

            worker.execute();

        }
    }

    class QuitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
