package client.gui;

import client.proxy.CustomerProxy;
//import org.omg.CORBA.CustomMarshal;
import server.Assignment;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

public class GUIWriteReview extends JFrame {
    final int WIDTH = 600;
    final int HEIGHT = 580;

    final int MAX_CHAR_TITLE = 150;
    final int MAX_CHAR_COMMENT = 65535;


    private Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );


    private JPanel outPanel;
    private JPanel contentPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel votePanel;
    private JPanel buttonPanel;
    private JPanel descriptionPanel;

    private JScrollPane descriptionFieldScroll;
    private JScrollPane titleFieldScroll;

    private JLabel labelTitle;
    private JLabel labelVote;
    private JLabel labelDescription;

    private JTextArea titleField;
    private JTextArea descriptionField;
    private JButton sendButton, cancelButton;


    private JComboBox<String> voteBox;
    private String[] grade = new String[]{"★", "★★", "★★★", "★★★★", "★★★★★"};

    private Assignment assignmentToReview;
    private String email;
    private CustomerProxy proxy;

    private DefaultStyledDocument docTitle;
    private DefaultStyledDocument docComment;
    private GUIWriteReview guiWriteReview;
    private GUIListAssignments guiListAssignments;
    private JFrame success = new JFrame();


    /**
     *
     * @param a appuntamento riferito alla recensione che devo scrivere
     * @param email riferimento all'utente
     */

    public GUIWriteReview(Assignment a, String email, GUIListAssignments guiListAssignments)
    {
        assignmentToReview = a;
        guiWriteReview = this;
        this.guiListAssignments = guiListAssignments;
        guiListAssignments.setEnabled(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                guiListAssignments.setEnabled(true);
            }
        });

        this.email = email;
        proxy = new CustomerProxy(this.email);
        docTitle = new DefaultStyledDocument();
        docComment = new DefaultStyledDocument();

        docTitle.setDocumentFilter(new DocumentSizeFilter(MAX_CHAR_TITLE));
        docTitle.addDocumentListener(new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCountTitle();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCountTitle();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCountTitle();
            }
        });

        docComment.setDocumentFilter(new DocumentSizeFilter(MAX_CHAR_COMMENT));
        docComment.addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCountComment();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCountComment();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCountComment();
            }

        });

        initComponent();
    }


    /**
     * inizializza le componenti dell'interfaccia
     */
    private void initComponent(){
        setTitle("Write a review");
        setSize(WIDTH, HEIGHT);
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        outPanel = new JPanel();
        contentPanel = new JPanel();

        topPanel = new JPanel();
        votePanel = new JPanel();
        bottomPanel = new JPanel();
        buttonPanel = new JPanel(new GridLayout(1,2));
        descriptionPanel = new JPanel(new GridLayout(1,1));


        labelTitle = new JLabel("Title: ");
        labelVote = new JLabel("Vote: ");
        labelDescription = new JLabel("<html><br/><br/><br/><br/><br/>Comment: </html>");

        titleField = new JTextArea(2,1);
        titleField.setDocument(docTitle);


        /* //secondo metodo, da provare
        KeyEvent e = new KeyEvent(titleField, );
        keyTyped(KeyEvent.KEY_TYPED , MAX_CHAR, titleField);
        */

        if(updateCountTitle() > MAX_CHAR_TITLE){
            Toolkit.getDefaultToolkit().beep();
        }



        descriptionField = new JTextArea( 7,1);
        descriptionField.setDocument(docComment);

        if(updateCountComment()> MAX_CHAR_COMMENT){
            Toolkit.getDefaultToolkit().beep();
        }

        sendButton = new JButton("Send");
        cancelButton = new JButton("Cancel");

        descriptionFieldScroll = new JScrollPane(descriptionField);
        titleFieldScroll = new JScrollPane(titleField);
        voteBox = new JComboBox<>(grade);


        topPanel.setLayout(new GridLayout(2,1,5,5));
        labelTitle.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        topPanel.add(labelTitle);
        titleField.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        titleField.setLineWrap(true);
        titleField.setWrapStyleWord(true);
        //titleField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,5, 5,5));
        topPanel.add(titleFieldScroll);

        votePanel.setLayout(new GridLayout(2,1));
        votePanel.add(labelVote);
        votePanel.add(voteBox);
        votePanel.setBorder(BorderFactory.createEmptyBorder(40,10,20,70));

        bottomPanel.setLayout(new GridLayout(1,2));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        bottomPanel.add(labelDescription);
        bottomPanel.add(votePanel);

        descriptionField.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
        descriptionPanel.add(descriptionFieldScroll);

        ActionListener post = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent registrationAe) {

                if (registrationAe.getActionCommand().equals("Send")) {
                    boolean strError = false;
                    String error = "";

                    int rating = starsRating(voteBox.getSelectedItem().toString());
                    String title = titleField.getText();
                    String comment = descriptionField.getText();
                    int code = assignmentToReview.getCode();
                    Date date = assignmentToReview.getDateEnd();

                    if(title.equals("")|| comment.equals("")){
                            strError = true;
                            error = "Please fill in all fields!";
                    }
                    if(title.contains("#")|| comment.contains("#")){
                            strError = true;
                            error = "# invalid character";
                    }



                    if(strError){
                        JOptionPane.showMessageDialog(new JFrame(), error, "Error", JOptionPane.ERROR_MESSAGE);
                        titleField.setText("");
                        descriptionField.setText("");
                    } else if(proxy.addReview(code, email, rating ,title.toUpperCase() ,comment.toUpperCase())){
                        JOptionPane.showMessageDialog(success = new JFrame(), "Review added!", "Review", JOptionPane.INFORMATION_MESSAGE);
                        if (!success.isActive()) {
                            guiWriteReview.dispatchEvent(new WindowEvent(guiWriteReview, WindowEvent.WINDOW_CLOSING));
                            guiListAssignments.dispatchEvent(new WindowEvent(guiListAssignments, WindowEvent.WINDOW_CLOSING));
                        }
                    }
                }
            }
        };
        sendButton.addActionListener(post);
        cancelButton.addActionListener(e -> guiWriteReview.dispatchEvent(new WindowEvent(guiWriteReview, WindowEvent.WINDOW_CLOSING)));
        buttonPanel.setLayout(new GridLayout(1, 2,5,5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 90, 10, 90));
        buttonPanel.add(cancelButton, BorderLayout.SOUTH);
        buttonPanel.add(sendButton, BorderLayout.SOUTH);


        contentPanel.setLayout(new GridLayout(3,1, 5, 5));

        contentPanel.setBorder(BorderFactory.createTitledBorder("Review fields: "));

        contentPanel.add(topPanel);
        contentPanel.add(bottomPanel);
        contentPanel.add(descriptionPanel);


        outPanel.setLayout(new BoxLayout(outPanel, BoxLayout.Y_AXIS));
        outPanel.setBorder(BorderFactory.createEmptyBorder(15,20,5,20));
        outPanel.add(contentPanel);
        outPanel.add(buttonPanel);

        add(outPanel);



    }


    /**
     *
     * @param rating stringa che indica il voto della recensione
     * @return rating della recensione convertito in int
     */
    private int starsRating(String rating){
        switch (rating){
            case "★":
                return 1;
            case "★★":
                return 2;
            case "★★★":
                return 3;
            case "★★★★":
                return 4;
            case "★★★★★":
                return 5;
            default:
                return 0;
        }
    }



    /**
     * aggiorna il conteggio di caratteri nel titolo
     * @return numero di caratteri ancora ammessi
     */
    private int updateCountTitle()
    {
        return MAX_CHAR_TITLE - docTitle.getLength();
    }

    /**
     * aggiorna il conteggio di caratteri nel titolo
     * @return numero di caratteri ancora ammessi
     */
    private int updateCountComment(){
        return MAX_CHAR_COMMENT - docComment.getLength();
    }

    
}


/**
 * classe predefinita per il conteggio di caratteri in un documento (JTextArea)
 */

class DocumentSizeFilter extends DocumentFilter {
    int maxCharacters;

    //boolean DEBUG = false;

    public DocumentSizeFilter(int maxChars) {
        maxCharacters = maxChars;
    }

    public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {

        /* if (DEBUG) {
            System.out.println("in DocumentSizeFilter's insertString method");
        }*/


        //This rejects the entire insertion if it would make the contents too long.
        if ((fb.getDocument().getLength() + str.length()) <= maxCharacters)
            super.insertString(fb, offs, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }

   public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {

        /*if (DEBUG) {
           System.out.println("in DocumentSizeFilter's replace method");
       }*/


        //This rejects the entire replacement if it would make the contents too long.
        if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters)
            super.replace(fb, offs, length, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }

}
