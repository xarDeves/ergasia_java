package utilities;

import model.Model;
import ui.InsertGUI;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//Synchronized methods ensure thread safety (just in case).
//The fact that this is actually harder to implement than a database makes me cry.
public class IOManager {

    private InsertGUI insertGUI;
    private final Model model;
    private final Checker checker;

    public IOManager(Model model, Checker checker) {

        this.model = model;
        this.checker = checker;

    }

    public synchronized void writeToFile() {

        //try-with-resources statement, basically auto closes the writer:
        try (FileWriter fw = new FileWriter("Books.txt")) {

            List<LinkedHashMap<String, String>> books = model.getAllBooks();
            for (Map<String, String> bookMap : books) {
                for (Map.Entry<String, String> entry : bookMap.entrySet()) {
                    fw.write(entry.getValue() + "\n");
                }
                fw.write("\n");
            }

        } catch (FileNotFoundException fe) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void readFromFile() throws IOException {

        //try-with-resources statement, basically auto closes the reader:
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {

            String line = br.readLine();

            while (line != null) {
                LinkedHashMap<String, String> book = new LinkedHashMap<>();

                if (line.equals("SCIENTIFIC")) {

                    book.put("superType", line);
                    book.put("title", br.readLine());
                    book.put("author", br.readLine());
                    book.put("isbn", br.readLine());
                    book.put("release", br.readLine());
                    book.put("type", br.readLine());
                    book.put("branch", br.readLine());

                    model.setNewBook(book);

                } else if (line.equals("LITERATURE")) {

                    book.put("superType", line);
                    book.put("title", br.readLine());
                    book.put("author", br.readLine());
                    book.put("isbn", br.readLine());
                    book.put("release", br.readLine());
                    book.put("type", br.readLine());

                    model.setNewBook(book);
                }

                line = br.readLine();
            }
        }


    }

    //TODO check isbn uniqueness
    public synchronized void handleData() {

        LinkedHashMap<String, String> book = new LinkedHashMap<>();

        try {

            //temp variables for checks:
            String superType = insertGUI.getSuperType();
            String title = checker.removePunctuation(insertGUI.getTitle());
            String author = checker.removePunctuation(insertGUI.getAuthor());
            String isbn = checker.checkIsbn(insertGUI.getIsbn());
            String release = checker.checkRelease(insertGUI.getRelease());
            String type = insertGUI.getType();
            String branch = insertGUI.getBranch();

            book.put("superType", superType);
            book.put("title", title);
            book.put("author", author);
            book.put("isbn", isbn);
            book.put("release", release);
            book.put("type", type);

            if (branch.equals("")) {
                if (!superType.equals("LITERATURE")) {
                    book.put("branch", "NO BRANCH SPECIFIED");
                }
            } else {
                book.put("branch", checker.removePunctuation(branch));
            }

            model.setNewBook(book);
            writeToFile();

        } catch (Exception e) {
            insertGUI.showPopUpInsert(e.getMessage());
        }

    }

    public void setGui(InsertGUI insertGUI) {
        this.insertGUI = insertGUI;
    }

}
