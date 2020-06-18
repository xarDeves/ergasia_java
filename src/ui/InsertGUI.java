package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.Enumeration;

public class InsertGUI implements ActionListener {

    private JFrame frame;
    private JTextField titleField;
    private JTextField authField;
    private JTextField isbnField;
    private JTextField releaseField;
    private JTextField branchField;
    private JLabel titleLabel;
    private JLabel authLabel;
    private JLabel isbnLabel;
    private JLabel releaseLabel;
    private JLabel typeLabel;
    private JLabel branchLabel;
    private JRadioButton scientificRadio;
    private JRadioButton literatureRadio;
    private JButton saveBtn;
    private JPanel mainPanel;
    private JRadioButton fictionRadioButton;
    private JRadioButton novelRadioButton;
    private JRadioButton storyRadioButton;
    private JRadioButton poetryRadioButton;
    private JRadioButton magazineRadioButton;
    private JRadioButton bookRadioButton;
    private JRadioButton conferenceRecordsRadioButton;

    ButtonGroup superTypeRadioGroup;
    ButtonGroup scientificGroup;
    ButtonGroup literatureGroup;

    private String superTypeSelected = "Scientific";
    private ButtonGroup radioGroupSelected;

    public InsertGUI() {

        frame = new JFrame("Insert New Book");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        superTypeRadioGroup = new ButtonGroup();
        scientificGroup = new ButtonGroup();
        literatureGroup = new ButtonGroup();

        superTypeRadioGroup.add(scientificRadio);
        superTypeRadioGroup.add(literatureRadio);

        scientificGroup.add(magazineRadioButton);
        scientificGroup.add(bookRadioButton);
        scientificGroup.add(conferenceRecordsRadioButton);

        literatureGroup.add(fictionRadioButton);
        literatureGroup.add(novelRadioButton);
        literatureGroup.add(storyRadioButton);
        literatureGroup.add(poetryRadioButton);

        radioTypeConfig(scientificGroup, literatureGroup);
        radioGroupSelected = scientificGroup;
        getType();

        scientificRadio.addActionListener(this);
        literatureRadio.addActionListener(this);

    }

    public void showPopUpInsert(String error) {

        JOptionPane.showMessageDialog(
                new JFrame(),
                error,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void addSaveBookListener(ActionListener saveBookListener) {
        saveBtn.addActionListener(saveBookListener);

    }

    public void addWindowClosedListener(WindowAdapter WindowClosedListener) {
        frame.addWindowListener(WindowClosedListener);
    }

    //https://stackoverflow.com/questions/24980758/disable-group-of-radio-buttons
    //this method goes though each radio button in a group and disables/enables them one by one:
    void radioTypeConfig(ButtonGroup groupEnable, ButtonGroup groupDisable) {

        Enumeration<AbstractButton> enumEnable;
        Enumeration<AbstractButton> enumDisable;

        enumEnable = groupEnable.getElements();
        enumDisable = groupDisable.getElements();

        groupDisable.clearSelection();

        groupEnable.getElements().nextElement().setSelected(true);

        while (enumEnable.hasMoreElements()) {
            enumEnable.nextElement().setEnabled(true);
        }

        while (enumDisable.hasMoreElements()) {
            enumDisable.nextElement().setEnabled(false);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {

            case "Scientific":
                branchField.setEditable(true);
                radioTypeConfig(scientificGroup, literatureGroup);
                radioGroupSelected = scientificGroup;
                superTypeSelected = "Scientific";
                break;

            case "Literature":
                branchField.setEditable(false);
                radioTypeConfig(literatureGroup, scientificGroup);
                radioGroupSelected = literatureGroup;
                superTypeSelected = "Literature";
                branchField.setText("");
                break;

        }
    }

    public String getSuperType() {
        return superTypeSelected.toUpperCase();
    }

    public String getType() {

        String selected = "";

        Enumeration<AbstractButton> enumGroup;
        enumGroup = radioGroupSelected.getElements();

        while (enumGroup.hasMoreElements()) {

            AbstractButton element = enumGroup.nextElement();
            if (element.isSelected()) {
                selected = element.getText();
            }
        }

        return selected.toUpperCase();

    }

    public String getTitle() {
        return titleField.getText().toUpperCase();
    }

    public String getIsbn() {
        return isbnField.getText();
    }

    public String getAuthor() {
        return authField.getText().toUpperCase();
    }

    public String getRelease() {
        return releaseField.getText().toUpperCase();
    }

    public String getBranch() {
        return branchField.getText().toUpperCase();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
        titleLabel = new JLabel();
        titleLabel.setText("Title");
        mainPanel.add(titleLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        authLabel = new JLabel();
        authLabel.setText("Author");
        mainPanel.add(authLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        isbnLabel = new JLabel();
        isbnLabel.setText("ISBN");
        mainPanel.add(isbnLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        releaseLabel = new JLabel();
        releaseLabel.setText("Release Year");
        mainPanel.add(releaseLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        typeLabel = new JLabel();
        typeLabel.setText("Type");
        mainPanel.add(typeLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        branchLabel = new JLabel();
        branchLabel.setText("Science Branch");
        mainPanel.add(branchLabel, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titleField = new JTextField();
        mainPanel.add(titleField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        authField = new JTextField();
        mainPanel.add(authField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        isbnField = new JTextField();
        mainPanel.add(isbnField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        releaseField = new JTextField();
        mainPanel.add(releaseField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        branchField = new JTextField();
        mainPanel.add(branchField, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        scientificRadio = new JRadioButton();
        scientificRadio.setSelected(true);
        scientificRadio.setText("Scientific");
        mainPanel.add(scientificRadio, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        literatureRadio = new JRadioButton();
        literatureRadio.setText("Literature");
        mainPanel.add(literatureRadio, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveBtn = new JButton();
        saveBtn.setText("save");
        mainPanel.add(saveBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fictionRadioButton = new JRadioButton();
        fictionRadioButton.setSelected(true);
        fictionRadioButton.setText("Fiction");
        panel2.add(fictionRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        novelRadioButton = new JRadioButton();
        novelRadioButton.setText("Novel");
        panel2.add(novelRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        storyRadioButton = new JRadioButton();
        storyRadioButton.setText("Story");
        panel2.add(storyRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        poetryRadioButton = new JRadioButton();
        poetryRadioButton.setText("Poetry");
        panel2.add(poetryRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        magazineRadioButton = new JRadioButton();
        magazineRadioButton.setSelected(true);
        magazineRadioButton.setText("Magazine");
        panel3.add(magazineRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bookRadioButton = new JRadioButton();
        bookRadioButton.setText("Book");
        panel3.add(bookRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        conferenceRecordsRadioButton = new JRadioButton();
        conferenceRecordsRadioButton.setSelected(false);
        conferenceRecordsRadioButton.setText("Confrence Records");
        panel3.add(conferenceRecordsRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
