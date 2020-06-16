package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

//could just create a separate class for each form,
//but since the change is not big between them (just a button) i stuck with overloading.
//I thought about using a JTable as well, but the api is a pain...
public class BookForm extends JPanel {

    JPanel bookPanel;
    JButton deleteBtn;

    public BookForm(Map<String, String> bookMap) {

        Box labelBox = Box.createVerticalBox();
        labelBox.setAlignmentX(LEFT_ALIGNMENT);

        bookPanel = new JPanel();
        bookPanel.setAlignmentX(LEFT_ALIGNMENT);
        bookPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        bookPanel.add(labelBox);

        for (Map.Entry<String, String> entry : bookMap.entrySet()) {
            JLabel info = new JLabel(entry.getValue());
            info.setAlignmentX(LEFT_ALIGNMENT);
            labelBox.add(info);
        }
    }

    //with delete button
    public BookForm(Map<String, String> bookMap, int pos, ImageIcon icon) {

        Box labelBox = Box.createVerticalBox();
        labelBox.setAlignmentX(LEFT_ALIGNMENT);

        bookPanel = new JPanel();
        bookPanel.setAlignmentX(LEFT_ALIGNMENT);
        bookPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        bookPanel.add(labelBox);

        deleteBtn = new JButton();
        deleteBtn.setIcon(icon);
        deleteBtn.setMargin(new Insets(0, 0, 0, 0));
        deleteBtn.setActionCommand(String.valueOf(pos));

        bookPanel.add(deleteBtn);

        for (Map.Entry<String, String> entry : bookMap.entrySet()) {
            JLabel info = new JLabel(entry.getValue());
            info.setAlignmentX(LEFT_ALIGNMENT);
            labelBox.add(info);
        }
    }

    public JPanel getBookPanel() {
        return bookPanel;
    }

    public JButton getDeleteBtn() {
        return deleteBtn;
    }

}
