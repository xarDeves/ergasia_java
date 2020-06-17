package utilities;

import javax.naming.InvalidNameException;
import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//This could be a static class (?)
//but i recon a singleton is cleaner.
public class CheckerSingleton {

    private static final char[] punc = {'Ά', 'Έ', 'Ή', 'Ί', 'Ό', 'Ύ', 'Ώ'};
    private static final char[] noPunc = {'Α', 'Ε', 'Η', 'Ι', 'Ο', 'Υ', 'Ω'};

    private static CheckerSingleton instance = null;

    private CheckerSingleton() {
    }

    public static CheckerSingleton getInstance() {
        if (instance == null)
            instance = new CheckerSingleton();

        return instance;
    }

    public void checkListEmpty(List<LinkedHashMap<String, String>> booksFound) throws InvalidNameException {

        if (booksFound.isEmpty()) throw new InvalidNameException("No such book");

    }

    public void checkEmpty(String value) throws InvalidNameException {

        if (value.equals("")) throw new InvalidNameException("A field is empty");

    }

    //i read something about normalization after the implementation of this method,
    //plus i could use regex which i guess it would be way faster, but i absolutely hate regex.
    public String removePunctuation(String value) throws InvalidNameException {

        checkEmpty(value);

        char letter;
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {

            letter = value.charAt(i);
            for (int j = 0; j < punc.length; j++) {
                if (letter == punc[j]) {
                    letter = noPunc[j];
                }
            }
            formatted.append(letter);
        }

        return formatted.toString();
    }

    public String checkIsbn(String value, List<LinkedHashMap<String, String>> books) throws InvalidNameException {

        checkEmpty(value);

        try {
            long isbnNum = Long.parseLong(value);

            if (isbnNum < 9780000000000L || isbnNum > 9790000000000L) {
                throw new InvalidParameterException("ISBN must be a number between 978 and 979");
            }
        } catch (Exception e) {
            throw new InvalidParameterException("ISBN must be a number between 978 and 979");
        }

        //check for isbn uniqueness.
        for (Map<String, String> bookMap : books) {

            if (bookMap.containsValue(value)) {
                throw new InvalidParameterException("ISBN must be unique");
            }
        }

        return value;
    }

    public String checkRelease(String value) {

        int valueLen = value.length();
        char letter;

        if (valueLen != 4) {
            throw new InvalidParameterException("RELEASE YEAR must be a 4 digit integer");

        } else {

            for (int i = 0; i < valueLen; i++) {
                letter = value.charAt(i);

                if (letter == '.') {
                    throw new InvalidParameterException("RELEASE YEAR must be a 4 digit integer");
                }
            }
        }

        return value;
    }

}
