import java.util.List;

public class Main {
    public static void main(String[] args) {
        VisitsDAO dao = new VisitsDAO();

        /**
         * Get an Item
         */
        int count = dao.getVisitCount("matt", "guatemala");
        System.out.println("(Just getting) Matt has visited Guatemala " + count + " time(s)");






        /**
         * Update an item, one of which exists (guatemala), one of which is new (provo)
         */
        dao.recordVisit("matt", "provo");
        dao.recordVisit("matt", "guatemala");

        int countGuatemala = dao.getVisitCount("matt", "guatemala");
        System.out.println("(After recording) Matt has visited Guatemala " + countGuatemala + " time(s)");

        int countProvo = dao.getVisitCount("matt", "provo");
        System.out.println("(After recording) Matt has visited Provo " + countProvo + " time(s)");




        /**
         * Delete an item
         */
        dao.deleteVisit("matt", "provo");
        int countProvo2 = dao.getVisitCount("matt", "provo");
        System.out.println("(After deletion) Matt has visited Provo " + countProvo2 + " time(s)");





        /**
         * Get the first page (lastlocation = null) of items
         */
        List<Visit> visits = dao.getVisitedLocations("matt", 2, null);
        System.out.println("Matt has visited: " + visits);

        String lastLocation = visits.get(visits.size() - 1).getLocation();

        /**
         * Get the second page (lastlocation = the last thing returned from the first page) of items
         */
        List<Visit> visits2 = dao.getVisitedLocations("matt", 2, lastLocation);
        System.out.println("Matt has also visited: " + visits2);





        /**
         * Using an Index: Get the first page (lastVisitor = null) of items
         */
        List<Visit> visitsToItaly = dao.getVisitors("italy", 2, null);
        System.out.println("Italy was visited by: " + visitsToItaly);

        String lastVisitor = visitsToItaly.get(visitsToItaly.size() - 1).getVisitor();

        /**
         * Using an Index: Get the second page (lastVisitor = the last thing returned from the first page) of items
         */
        List<Visit> visitsToItaly2 = dao.getVisitors("italy", 2, lastVisitor);
        System.out.println("Italy was also visited by: " + visitsToItaly2);
    }
}
