package client.gui;

import client.proxy.CustomerProxy;
import server.Assignment;
import client.clientEnumerations.CalendarState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.lang.*;



public class GUIDailyAssignments extends JFrame {

    final int WIDTH = 512;
    final int HEIGHT = 512;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    protected JPanel p = new JPanel();//pannello esterno
    protected JPanel panelButtons = new JPanel();
    protected GridLayout gridLayout = new GridLayout(1, 1);
    protected JButton button[] = new JButton[SwingConstants.RIGHT];
    protected JButton buttonInfo[] = new JButton[SwingConstants.RIGHT];
    protected JLabel[] labelDescription = new JLabel[SwingConstants.LEFT];
    protected JPanel[] infoPanel;
    protected JLabel lb = new JLabel();
    protected JScrollPane scroll = new JScrollPane(p);
    protected HashMap<Integer, Assignment> listAssigment;
    protected CustomerProxy proxy;
    protected String email;
    protected Date todayDate;
    public GUIDailyAssignments guiDailyAssignments;



    /**
     * Constructor
     *
     * @param cs   identifies the menu from which this interface is called
     * @param email       of the customer
     * @param todayDate  identifies the days in the calendar

     */

    public GUIDailyAssignments(CalendarState cs, String email, Date todayDate, GUIHome guiHome) {

        setTitle("Daily assignments");
        setSize(WIDTH, HEIGHT);
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setVisible(true);
        this.email = email;
        this.todayDate = todayDate;
        guiDailyAssignments = this;
        initComponents(cs);

        guiHome.setEnabled(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                guiHome.setEnabled(true);
            }
        });

    }

    public GUIDailyAssignments(CalendarState cs, String email, Date todayDate, GUICustomer guiCustomer) {

        setTitle("Daily assignments");
        setSize(WIDTH, HEIGHT);
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setVisible(true);
        this.email = email;
        this.todayDate = todayDate;
        guiDailyAssignments = this;
        initComponents(cs);

        guiCustomer.setEnabled(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                guiCustomer.setEnabled(true);
            }
        });
    }

    public GUIDailyAssignments(CalendarState cs, String email, Date todayDate) {

        setTitle("Daily assignments");
        setSize(WIDTH, HEIGHT);
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setVisible(true);
        this.email = email;
        this.todayDate = todayDate;
        guiDailyAssignments = this;
        initComponents(cs);

    }

    /**
     * Method that initalizes graphic components of the GUI
     *
     * @param cs identifies the menu from which this interface is called

     */

    protected void initComponents(CalendarState cs) {
        proxy = new CustomerProxy(email);
        this.listAssigment = proxy.getAssignmentList();

        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setLayout(new GridLayout(9, 1, 20, 20));


        if ((cs.equals(CalendarState.REMOVING)) || (cs.equals((CalendarState.NORMAL)))) {

            setTitle("Daily assignment");

            HashMap<Integer, Assignment>todayAssigment = new HashMap<>();

            int n = 0;

            for (Integer i : listAssigment.keySet()) {


                Assignment a = null;
                a = listAssigment.get(i);
                Date dateStart = a.getDateStart();
                Date dateEnd = a.getDateEnd();
                SimpleDateFormat date1 = new SimpleDateFormat("dd/MM/yyyy");
                String dateString1 = date1.format(dateStart);
                String dateStringEnd1 = date1.format(dateEnd);
                SimpleDateFormat date2 = new SimpleDateFormat("dd/MM/yyyy");
                String dateString2 = date1.format(todayDate);
                String dateStringEnd2 = date1.format(todayDate);
                dateString1.equals(dateString2);
                dateStringEnd1.equals(dateStringEnd2);


                if (dateString1.equals(dateString2) || (dateStringEnd1.equals(dateStringEnd2))) {

                    todayAssigment.put(n, a);
                }

                n++;

            }


            System.out.println(todayAssigment.size());
            labelDescription = new JLabel[todayAssigment.size()];
            button = new JButton[todayAssigment.size()];
            buttonInfo = new JButton[todayAssigment.size()];
            infoPanel = new JPanel[todayAssigment.size()];



            if ((cs.equals(CalendarState.REMOVING))) {

                if (todayAssigment.isEmpty()) {

                    lb = new JLabel(" There aren't assignments today to be cancel ", SwingConstants.CENTER);
                    p.add(lb);


                } else {

                    int j = 0;

                    for (Integer i : todayAssigment.keySet()) {
                        Assignment a = null;
                        String labelString = "";
                        a = todayAssigment.get(i);
                        String nameDogSitter = proxy.getDogSitterNameOfAssignment(a.getCode());
                        String surnameDogSitter = proxy.getDogSitterSurnameOfAssignment(a.getCode());


                        labelString = "<html>" + "Assignment with " + nameDogSitter + " " + surnameDogSitter + "</html>";



                        labelDescription[j] = new JLabel(labelString);
                        buttonInfo[j] = new JButton("More info");
                        button[j] = new JButton("Delete");


                        button[j].addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                int action = JOptionPane.showConfirmDialog(null, "Are you sure to cancel ?", "Conferm Actions", JOptionPane.YES_NO_OPTION);
                                if (action == JOptionPane.YES_OPTION) {
                                    proxy.removeAssignment(todayAssigment.get(i).getCode()); //TODO funzionante e verificato
                                    dispose();
                                }

                            }
                        });

                        ActionListener showInfo = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                GUIAssignmentInformationCustomer assignmentInfo = new GUIAssignmentInformationCustomer(todayAssigment.get(i), email, guiDailyAssignments);
                                assignmentInfo.setVisible(true);

                            }
                        };


                        buttonInfo[j].addActionListener(showInfo);

                        createPanelOrderDelete(j);

                        j++;

                    }

                }

                scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                getContentPane().add(scroll);

            }



            else{

                if (todayAssigment.isEmpty()) {

                        lb = new JLabel(" There aren't assignments today ", SwingConstants.CENTER);

                        p.add(lb);

                    } else {

                        int j = 0;

                        for (Integer i : todayAssigment.keySet()) {
                            Assignment a = null;
                            String labelString = "";
                            a = todayAssigment.get(i);
                            String nameDogSitter = proxy.getDogSitterNameOfAssignment(a.getCode());
                            String surnameDogSitter = proxy.getDogSitterSurnameOfAssignment(a.getCode());
                            labelString = "<html>" + "Assignment with " + nameDogSitter + " " + surnameDogSitter + "</html>";


                            labelDescription[j] = new JLabel(labelString);
                            button[j] = new JButton("More info");

                            ActionListener showInfo = new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    GUIAssignmentInformationCustomer assignmentInfo = new GUIAssignmentInformationCustomer(todayAssigment.get(i), email, guiDailyAssignments);
                                    assignmentInfo.setVisible(true);

                                }
                            };


                            button[j].addActionListener(showInfo);


                            createPanelOrder(j);
                            j++;


                        }
                    }


                    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    getContentPane().add(scroll);


            }
        }
    }

        /**
         * method who create a panel that contains the assignment information: status, description and  button
         * @param i panel index

         */

        protected void createPanelOrder ( int i){

            infoPanel[i] = new JPanel();


            infoPanel[i].setLayout(new BorderLayout());
            infoPanel[i].setMaximumSize(new Dimension(450, 150));

            labelDescription[i].setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 50));

            infoPanel[i].add(labelDescription[i], BorderLayout.CENTER);
            infoPanel[i].add(button[i], BorderLayout.EAST);

            infoPanel[i].setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 15)); //5,5,5,5

            p.add(infoPanel[i]);

        }

         /**
         * method who create a panel that contains the assignment information: status, description and  button
         * @param i panel index

        */

        protected void createPanelOrderDelete(int i) {

        infoPanel[i] = new JPanel();
        panelButtons = new JPanel();

        panelButtons.setBorder(BorderFactory.createEmptyBorder(5,0,5, 150));


        infoPanel[i].setLayout(new BorderLayout());
        infoPanel[i].setMaximumSize(new Dimension(450,150));

        labelDescription[i].setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 100));

        infoPanel[i].add(labelDescription[i], BorderLayout.CENTER);
        panelButtons.add(button[i]);
        panelButtons.add(buttonInfo[i]);

        infoPanel[i].add(panelButtons, BorderLayout.EAST);

        infoPanel[i].setBorder(BorderFactory.createEmptyBorder(5,10,5,150));

        p.add(infoPanel[i]);
        }


}


