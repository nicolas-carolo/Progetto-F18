package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import client.proxy.CustomerProxy;

public class GUILogin extends JFrame {
    //final int WIDTH = 600;
    //final int HEIGHT = 150;
   // private Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
    private JPanel panelLoginData = new JPanel();
    private JPanel panelBottom = new JPanel();
    private JLabel labelUser = new JLabel("Email", SwingConstants.LEFT);
    private JLabel labelPwd = new JLabel("Password", SwingConstants.LEFT);
    private JTextField textUser = new JTextField();
    private JPasswordField textPwd = new JPasswordField();
    private JButton buttonLogin = new JButton("Login as Costumer");
    private JButton buttonLoginSitter = new JButton("Login as Dogsitter");
    private JButton buttonNewAccount = new JButton("Create a new account");
    private JPanel cont1 = new JPanel();   //pannello contenitore
    private GridBagLayout layout = new GridBagLayout();

    private CustomerProxy proxy = new CustomerProxy();

    public GUILogin() {
        setTitle("Login");
        //setSize(WIDTH, HEIGHT);
        //setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setResizable(false);
        setLayout(new BorderLayout());
        //ImageIcon img = new ImageIcon("/Users/nicolas/Desktop/logo.png");
        //setIconImage(img.getImage());
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        initComponents();
    }

    private void initComponents(){

        cont1.setLayout(new GridLayout(2, 1, 100, 0)); // mi sposta il pannello dei dati tranne i bottoni
        cont1.setBorder(BorderFactory.createTitledBorder(" Main Fields: "));

        //login automatico per velocizzare il debug
        textUser.setText("RICCARDOGIURA@GMAIL.COM");
        textPwd.setText("PROVAPROVA123");

        panelLoginData.setLayout(new GridLayout(2,2));
        panelLoginData.add(labelUser);
        panelLoginData.add(textUser);
        panelLoginData.add(labelPwd);
        panelLoginData.add(textPwd);
        add(panelLoginData, BorderLayout.CENTER);

        panelBottom.setLayout(new GridLayout(3,1));
        panelBottom.add(buttonLogin);
        panelBottom.add(buttonLoginSitter);
        panelBottom.add(buttonNewAccount);
        add(panelBottom, BorderLayout.SOUTH);

        cont1.add(panelLoginData);
        cont1.add(panelBottom);


        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        //centra il frame nello schermo
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);


        Image img = getToolkit().getImage("new-logo.jpg");
        setIconImage(img);

        ImagePanel panel = new ImagePanel();
        panel.setBackground(SystemColor.window);
        Container contentPane = getContentPane();
        contentPane.add(panel);
        panel.add(cont1);
        panel.setLayout(layout);

        cont1.setOpaque(false);



           ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                /*if (ae.getActionCommand().equals("Login") && !(textUser.equals("")) && !(textPwd.equals(""))){
                    Login login = new Login();
                    try {
                        if(login.customerAccessDataVerifier(textUser.getText(), new String(textPwd.getPassword()))){
                            //open GUICustomer
                            GUICustomer guiCustomer = null;
                            try {
                                guiCustomer = new GUICustomer(textUser.getText());
                            } catch (ParseException e) {
                                //e.printStackTrace();
                                System.out.println("Error in parsing data");
                            }
                            guiCustomer.setVisible(true);
                            setVisible(false);
                        } else{
                            //show error message
                            JOptionPane.showMessageDialog(new JFrame(), "Incorrect user or password!", "Login error",
                                    JOptionPane.ERROR_MESSAGE);
                            textUser.setText("");
                            textPwd.setText("");
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/

                if (ae.getActionCommand().equals("Login as Costumer") && !(textUser.equals("")) && !(textPwd.equals(""))){
                    //String clientMsg = textUser.getText() + new String(textPwd.getPassword());
                    //proxy.getReply(clientMsg);
                    if (proxy.customerAccessDataVerifier(textUser.getText(), new String(textPwd.getPassword()))){
                        GUICustomer guiCustomer = null;
                        try {
                            //guiCustomer = new GUICustomer(textUser.getText());
                            guiCustomer = new GUICustomer(textUser.getText());
                        } catch (ParseException e) {
                            //e.printStackTrace();
                            System.out.println("Error in parsing data");
                        }
                        guiCustomer.setVisible(true);
                        setVisible(false);
                    } else {
                        //show error message
                        JOptionPane.showMessageDialog(new JFrame(), "Incorrect user or password!", "Login error",
                                JOptionPane.ERROR_MESSAGE);
                        textUser.setText("");
                        textPwd.setText("");
                    }
                }

                if (ae.getActionCommand().equals("Create a new account")){
                    GUISignUp guiSignUp = new GUISignUp();
                    guiSignUp.setVisible(true);
                    setVisible(false);
                }
            }
        };
        buttonLogin.addActionListener(al);
        buttonNewAccount.addActionListener(al);
    }


    private class ImagePanel extends JPanel {

            private Image image;


            public ImagePanel() {

                //acquisisco l'immagine
                image = Toolkit.getDefaultToolkit().getImage("images/new-logo.jpg");
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(image, 0);
                try {
                    tracker.waitForID(0);
                } catch (InterruptedException exception) {
                }

            }


            public void paintComponent(Graphics g) {

                super.paintComponent(g);

                //acquisisco le dimensioni dello schermo
                Toolkit kit = Toolkit.getDefaultToolkit();
                Dimension screenSize = kit.getScreenSize();
                int screenHeight = screenSize.height / 2;
                int screenWidth = screenSize.width / 2;

                //disegna l'immagine
                int centroAscissaImage = screenWidth - image.getWidth(null) / 2;
                int centroOrdinataImage = screenHeight - image.getHeight(null) / 2;
                g.drawImage(image, centroAscissaImage, centroOrdinataImage, null);



            }



    }
}
