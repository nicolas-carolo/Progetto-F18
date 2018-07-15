package client.gui;

import client.Calendar;
import client.proxy.CustomerProxy;
import client.proxy.DogSitterProxy;
import enumeration.CalendarState;
import server.Assignment;
import server.Review;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GUIShowDogsitterAssignment extends GUIListAssignments {

    private DogSitterProxy dogSitterProxy;
    public  GUIShowDogsitterAssignment guiShowDogsitterAssignment; // serve per disattivare la finestra madre @Riccardo


    public GUIShowDogsitterAssignment(CalendarState cs, HashMap<Integer, Assignment> listAssignment, String email, GUIHome guiDogsitter){
        super(cs,listAssignment, email, guiDogsitter);
        guiShowDogsitterAssignment = this;

        initComponents(cs, guiDogsitter);

    }


    @Override
    protected void initComponents(CalendarState cs, GUIHome guiDogsitter) {


        dogSitterProxy = new DogSitterProxy(email);
        listReview = dogSitterProxy.getReviewList();
        assignmentNumber = listAssignment.size();
        reviewNumber = listReview.size();

        gridLayout = new GridLayout(1,1);

        initArray(cs);

        gridLayout = new GridLayout(1,1);
        contentPanel = new JPanel();
        panelOut = new JPanel();
        scrollPanel = new JScrollPane(panelOut);

        contentPanel.setLayout(gridLayout);
        panelOut.setLayout(new BorderLayout());

        if(cs.equals(CalendarState.NORMAL)){
            int j = 0;

            for(Integer i : listAssignment.keySet()){
                Assignment a = null;

                a = listAssignment.get(i);

                ActionListener showInfo = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                            //TODO aggiungere GUIAssignmentInformationDogsitter



                    }
                };


                setComponents(setLabelString(cs, a, null), "Info", j);
                buttonAction[j].addActionListener(showInfo);


                createPanelAssignment(a,j);

                gridLayout.setRows(gridLayout.getRows() + 1);
                j++;

            }
        }
        else if(cs.equals(CalendarState.SHOW_REVIEWS)){
            setTitle("Your reviews");

            int j = 0;
            for(Integer i: listReview.keySet()){
                Review r = null;
                r = listReview.get(i);

                setComponents(setLabelString(cs, null, r), "Show more", j);
                buttonAction[j].addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        GUIShowDogsitterReview guiShowDogsitterReview = new GUIShowDogsitterReview(listReview.get(i), email);
                        guiShowDogsitterReview.setVisible(true);
                        dispose();


                    }

                });

                createPanelReview(j);
                gridLayout.setRows(gridLayout.getRows() + 1);
                j++;

            }
        }





        panelOut.add(contentPanel, BorderLayout.NORTH);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPanel);


        this.addWindowListener (new WindowAdapter() {
            public void windowClosing (WindowEvent we) {
                guiDogsitter.setCalendarState(CalendarState.NORMAL);
            }
        });



    }

    @Override
    protected void setLabelStringMap(Assignment a, Review r) {
        strLabel = new HashMap<>();

        String nameCustomer = "";
        String surnameCustomer = "";

        String dateStringReview ="";

        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date.setLenient(false);
        if(r!=null){
            Date reviewDate = r.getDate();
            dateStringReview = date.format(reviewDate);

            strLabel.put(CalendarState.SHOW_REVIEWS, "<html>" + "Customer: " + dogSitterProxy.getCustomerNameOfAssignment(r.getCode()) + " " + dogSitterProxy.getCustomerSurnameOfAssignment(r.getCode()) +"<br/>"+ dateStringReview +"<br/>" + r.getTitle() +"<br/>" + "Vote: " + r.starsRating() + "</html>");
        }


        if(a!=null){
            nameCustomer = dogSitterProxy.getCustomerNameOfAssignment(a.getCode());
            surnameCustomer = dogSitterProxy.getCustomerSurnameOfAssignment(a.getCode());
            strLabel.put(CalendarState.NORMAL, "Assignment with " + nameCustomer + " " + surnameCustomer);
        }
    }



}
