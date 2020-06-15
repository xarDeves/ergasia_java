package model;

import ui.InsertGUI;
import ui.MainGUI;
import utilities.CheckerSingleton;
import utilities.IOManagerSingleton;

import javax.naming.InvalidNameException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private final IOManagerSingleton manager;
    private final CheckerSingleton checker;
    private boolean insertFormExists = false;
    private SwingWorker<Void, Void> worker;
    private InsertGUI insertGUI;

    public Controller(MainGUI mainGUI, Model model) {
        this.mainGUI = mainGUI;
        this.model = model;
        this.manager = IOManagerSingleton.getInstance();
        this.checker = CheckerSingleton.getInstance();

        this.mainGUI.addShowBooksListener(new ShowBooksListener());
        this.mainGUI.addInsertListener(new InsertListener());
        this.mainGUI.addQuitListener(new QuitListener());
        this.mainGUI.addSearchListener(new SearchListener());

        try {
            manager.readFromFile(model);
        } catch (IOException e) {
            e.printStackTrace();
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

            System.out.println(e.getActionCommand() + "deleteListener");
            model.deleteBook(e.getActionCommand());
            manager.writeToFile(model.getAllBooks());
            renderAllBooks(model.getAllBooks());


        }
    }

    private void renderAllBooks(List<LinkedHashMap<String, String>> books) {

        mainGUI.clearDisplay();

        int i = 0;
        for (Map<String, String> bookMap : books) {

            BookForm book = new BookForm(bookMap, i);

            JButton deleteButton = book.getDeleteBtn();
            deleteButton.addActionListener(new DeleteBooksListener());

            mainGUI.displayBook(book.getBookPanel(), book.getDeleteBtn());

            i++;
        }
    }

    private void renderSearchedBooks(List<LinkedHashMap<String, String>> books) {

        mainGUI.clearDisplay();

        for (Map<String, String> bookMap : books) {

            BookForm book = new BookForm(bookMap);
            mainGUI.displayBook(book.getBookPanel(), book.getDeleteBtn());

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

                    renderAllBooks(model.getAllBooks());
                    return null;
                }
            };

            worker.execute();

        }
    }

    class InsertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //in order for the insert form to appear only once.
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
            manager.handleData(model);

        }
    }


    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (worker != null) {
                worker.cancel(true);
            }

            worker = new SwingWorker<>() {

                @Override
                protected Void doInBackground() {
                    try {

                        //my eyes hurt
                        renderSearchedBooks(
                                model.getBookSearched(
                                        checker.removePunctuation(
                                                mainGUI.getFieldText())));

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
