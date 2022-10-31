import java.util.List;

public class Main {
    public static void main(String[] args) {
        VisitsDAO dao = new VisitsDAO();

        // get an item
        int count = dao.getVisitCount("matt", "guatemala");
        System.out.println("(Just getting) Matt has visited Guatemala " + count + " time(s)");






        // update an item
        dao.recordVisit("matt", "provo");
        dao.recordVisit("matt", "guatemala");

        int countGuatemala = dao.getVisitCount("matt", "guatemala");
        System.out.println("(After recording) Matt has visited Guatemala " + countGuatemala + " time(s)");

        int countProvo = dao.getVisitCount("matt", "provo");
        System.out.println("(After recording) Matt has visited Provo " + countProvo + " time(s)");






        // delete an item
        dao.deleteVisit("matt", "provo");
        int countProvo2 = dao.getVisitCount("matt", "provo");
        System.out.println("(After deletion) Matt has visited Provo " + countProvo2 + " time(s)");






        // get a page of items not having seen one before
        List<Visit> visits = dao.getVisitedLocations("matt", 2, null);
        System.out.println("Matt has visited: " + visits);



        // get a page of items, the last one we saw was guatemala having seen one before
        List<Visit> visits2 = dao.getVisitedLocations("matt", 2, "guatemala");
        System.out.println("Matt has visited: " + visits2);

    }
}
