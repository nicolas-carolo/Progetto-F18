import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class TestCustomer {
    public static void main(String[] args) throws ParseException {
        //test addAssignment(...)
        HashSet<String> selectedDogs = new HashSet<String>(2);
        selectedDogs.add("Fuffi");
        selectedDogs.add("Scooby");
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date.setLenient(false);
        String strBirthC = "01/01/1970 00:00";
        Date birthC = date.parse(strBirthC);
        String strBirthDS = "03/04/1980 00:00";
        Date birthDS = date.parse(strBirthC);
        String strDateStartAssignment = "30/04/2018 11:00";
        Date dateStartAssignment = date.parse(strDateStartAssignment);
        String strDateEndAssignment = "30/05/2018 18:00";
        Date dateEndAssignment = date.parse(strDateEndAssignment);
        Customer c = new Customer("pippo@email.it", "Pippo", "Baudo", "password", "1234567890", birthC);
        DogSitter ds = new DogSitter("paperino@email.it", "Paperino", "Paperini", "pass", "0987654321", birthDS);
        c.addAssignment(ds, dateStartAssignment, dateEndAssignment, selectedDogs);

        //test removeAssignment(...)
        c.removeAssignment("30/04/2018 11:00_paperino@email.it_pippo@email.it");
        //c.removeAssignment("30/04/2018 11:00_paperino@email.it_pippo@email.it");

        //test addReview(...)
        String strDateReview = "10/05/2018 06:32";
        Date dateReview = date.parse(strDateReview);
        int rating = 5;
        String title = "Very good!";
        String comment = "This dog sitter is very good";
        c.addReview(ds, dateReview, rating, title, comment);

        //test removeReview
        c.removeReview("10/05/2018 06:32_paperino@email.it_pippo@email.it");
        //c.removeReview("10/05/2018 06:32_paperino@email.it_pippo@email.it");

        //test listAssignment(...)
        c.listAssignment();

        //test listReview(...)
        c.listReview();
    }
}
