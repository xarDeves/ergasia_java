package model;

import ui.BookForm;
import ui.InsertGUI;
import ui.MainGUI;
import utilities.CheckerSingleton;
import utilities.IOManagerSingleton;

import javax.imageio.ImageIO;
import javax.naming.InvalidNameException;
import javax.swing.*;
import java.awt.*;
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
 * Using Synchronized Threads to handle IO, ensures collision and data loss avoidance.
 * (Arguably over engineered and unnecessary complex for such assignment)
 * (i wasn't able to test whether IO is actually thread safe, Although it should be)
 */

public class Controller {

    private final MainGUI mainGUI;
    private final Model model;
    private final IOManagerSingleton ioManager;
    private final CheckerSingleton checker;
    private boolean insertFormExists = false;
    private InsertGUI insertGUI;
    private volatile Thread displayThread;

    public Controller(MainGUI mainGUI, Model model) {

        this.mainGUI = mainGUI;
        this.model = model;
        this.ioManager = IOManagerSingleton.getInstance();
        this.checker = CheckerSingleton.getInstance();

        this.mainGUI.addShowBooksListener(new ShowBooksListener());
        this.mainGUI.addInsertListener(new InsertListener());
        this.mainGUI.addQuitListener(new QuitListener());
        this.mainGUI.addSearchListener(new SearchListener());

        try {
            ioManager.readFromFile(model);
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

    private void renderBooks(List<LinkedHashMap<String, String>> books) {

        mainGUI.clearDisplay();

        ImageIcon icon = null;

        //fetch the image and convert it to an icon for the button
        try {
            Image sourceImage = ImageIO.read(getClass().getResource("/res/bin.png"));
            icon = new ImageIcon(sourceImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //classic instead of advanced for loop had to be used here
        //else "ConcurrentModificationException" occurs
        for (int i = 0; i < books.size(); i++) {
            LinkedHashMap<String, String> bookMap = books.get(i);

            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            BookForm book = new BookForm(bookMap, i, icon);

            JButton deleteButton = book.getDeleteBtn();
            deleteButton.addActionListener(new DeleteBooksListener());

            mainGUI.displayBook(book.getBookPanel());
        }

    }

    class DeleteBooksListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int confirm = JOptionPane.showConfirmDialog(
                    null, "Are you sure you want to delete this book?");

            if (confirm == 0) {

                if (displayThread != null) {
                    displayThread.interrupt();
                }

                Thread deleteThread = new Thread(() -> {

                    model.deleteBook(e.getActionCommand());
                    ioManager.writeToFile(model.getAllBooks());

                });

                deleteThread.start();
                try {
                    deleteThread.join();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                mainGUI.SuccessPopUp();

                displayThread = new Thread(() -> {

                    renderBooks(model.getAllBooks());

                });

                displayThread.start();

            }
        }
    }

    class ShowBooksListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (displayThread != null) {
                displayThread.interrupt();
            }

            displayThread = new Thread(() -> renderBooks(model.getAllBooks()));

            displayThread.start();

        }
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (displayThread != null) {
                displayThread.interrupt();
            }

            displayThread = new Thread(() -> {

                try {

                    //my eyes hurt
                    List<LinkedHashMap<String, String>> booksFound =
                            model.getBookSearched(
                                    checker.removePunctuation(
                                            mainGUI.getFieldText()));

                    if (booksFound.isEmpty()) {
                        mainGUI.NoBooksFoundPopUp();
                    } else {
                        renderBooks(booksFound);
                    }


                } catch (InvalidNameException ex) {
                    mainGUI.ErrorPopUp(ex.getMessage());
                }
            });

            displayThread.start();

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

            ioManager.setGui(insertGUI);
            ioManager.handleData(model);

        }
    }

    class QuitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            System.exit(0);

        }
    }

}
