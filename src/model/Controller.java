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
 * (Arguably over engineered and unnecessary complex for such assignment)
 * (i wasn't able to test whether IO is actually thread safe, Although it should be)
 *
 * Note on SwingWorkers:
 * As per the documentation the correct usage is for the "DoInBackground" to compute
 * and "done" to post the results but "done" takes a list of chunks as an argument
 * meaning i would need to abstract the panel which displays the info from each book
 * in a separate class (which would be better in anyway but i tried it, and it didn't work out).
 * I understand it is a sloppy approach but frankly i don't really see a point to use "done" in this case.
 *
 * also the:
 *
 *          if (worker != null) {
                worker.cancel(true);
            }
 *
 * does not work for some reason.
 */

public class Controller {

    private final MainGUI mainGUI;
    private final Model model;
    private final IOManager manager;
    private final Checker checker;
    private InsertGUI insertGUI;
    private boolean insertFormExists = false;
    private SwingWorker worker;

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

            if (worker != null) {
                worker.cancel(true);
            }

            worker = new SwingWorker<>() {
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

            if (worker != null) {
                worker.cancel(true);
            }

            worker = new SwingWorker<>() {
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

/*    public class ShowAllWorker extends SwingWorker {

        @Override
        protected Void doInBackground() throws Exception {
            return null;
        }
    }

    public class SearchWorker extends SwingWorker {
        @Override
        protected Void doInBackground() throws Exception {
            return null;
        }
    }*/

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (worker != null) {
                worker.cancel(true);
            }

            worker = new SwingWorker<>() {

                @Override
                protected Void doInBackground() {
                    System.out.println(mainGUI.getFieldText());
                    try {
                        //my eyes hurt
                        mainGUI.showBooksGui(
                                model.getBookSearched(
                                        checker.removePunctuation(
                                                mainGUI.getFieldText())), false);
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
