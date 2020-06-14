package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * All the data are stored here.
 * Honestly this class seems redundant
 * since data persistence is ensured by the text file,
 * i could just parse everything on the spot,
 * Ram usage on large files is obviously high.
 * I could still get rid of the data structure by leveraging the GC,
 * and just keep it for search and delete operations
 * but that would mean that every time a search/delete is performed,
 * the data structure would be re-created.
 *
 * Also it would be cleaner to use "data" classes instead of a hashmap,
 * but i was unsure about performance in search operations.
 */

public class Model {

    private final List<LinkedHashMap<String, String>> bookList;

    public Model() {
        bookList = new ArrayList<>();
    }

    //jesus christ my eyes
    public List<LinkedHashMap<String, String>> getBookSearched(String value) {

        List<LinkedHashMap<String, String>> booksFound = new ArrayList<>();

        for (Map<String, String> bookMap : bookList) {
            for (Map.Entry<String, String> entry : bookMap.entrySet()) {

                if (entry.getKey().equals("title") || entry.getKey().equals("author")) {
                    if (entry.getValue().equals(value)) {
                        booksFound.add((LinkedHashMap<String, String>) bookMap);
                    }

                }
            }
        }

        return booksFound;
    }

    public List<LinkedHashMap<String, String>> getAllBooks() {
        return bookList;
    }

    public void deleteBook(String pos) {
        bookList.remove(Integer.parseInt(pos));
    }

    public void setNewBook(LinkedHashMap<String, String> data) {
        bookList.add(data);
    }


}
