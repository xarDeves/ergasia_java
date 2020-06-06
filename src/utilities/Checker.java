package utilities;
import javax.naming.InvalidNameException;
import java.security.InvalidParameterException;

public class Checker {

    private static final char[] punc = {'Ά', 'Έ', 'Ή', 'Ί', 'Ό', 'Ύ', 'Ώ'};
    private static final char[] noPunc = {'Α', 'Ε', 'Η', 'Ι', 'Ο', 'Υ', 'Ω'};

    public void checkEmpty(String value) throws InvalidNameException {

        if (value.equals("")) throw new InvalidNameException("A field is empty");

    }

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

    public String checkIsbn(String value) throws InvalidNameException {

        checkEmpty(value);

        try {
            long isbnNum = Long.parseLong(value);

            if (isbnNum < 9780000000000L || isbnNum > 9790000000000L) {
                throw new InvalidParameterException("ISBN must be a number between 978 and 979");
            }
        } catch (Exception e) {
            throw new InvalidParameterException("ISBN must be a number between 978 and 979");
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
