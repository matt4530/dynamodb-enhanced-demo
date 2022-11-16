import java.util.List;

public class Main {
    public static void main(String[] args) {
        VisitsDAO dao = new VisitsDAO();

        /**
         * Delete old items
         */
        dao.deleteVisit("matt", "provo");
        dao.deleteVisit("matt", "guatemala");
        dao.deleteVisit("matt", "orem");
        dao.deleteVisit("matt", "italy");
        dao.deleteVisit("elliot", "italy");
        dao.deleteVisit("nate", "italy");
        dao.deleteVisit("adam", "italy");




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
         * Add more items
         */
        dao.recordVisit("matt", "orem");
        dao.recordVisit("matt", "italy");

        dao.recordVisit("elliot", "italy");
        dao.recordVisit("nate", "italy");
        dao.recordVisit("adam", "italy");

        /**
         * Get the first page (lastlocation = null) of items
         */
        DataPage<Visit> page = dao.getVisitedLocations("matt", 2, null);
        List<Visit> visits = page.getValues();
        verify(page.isHasMorePages());
        System.out.println("Matt has visited: " + visits);

        String lastLocation = visits.get(visits.size() - 1).getLocation();

        /**
         * Get the second page (lastlocation = the last thing returned from the first page) of items
         */
        DataPage<Visit> page2 = dao.getVisitedLocations("matt", 2, lastLocation);
        List<Visit> visits2 = page2.getValues();
        verify(!page2.isHasMorePages());
        System.out.println("Matt has also visited: " + visits2);





        /**
         * Using an Index: Get the first page (lastVisitor = null) of items
         */
        DataPage<Visit> page3 = dao.getVisitors("italy", 2, null);
        List<Visit> visitsToItaly = page3.getValues();
        verify(page3.isHasMorePages());
        System.out.println("Italy was visited by: " + visitsToItaly);

        String lastVisitor = visitsToItaly.get(visitsToItaly.size() - 1).getVisitor();

        /**
         * Using an Index: Get the second page (lastVisitor = the last thing returned from the first page) of items
         */
        DataPage<Visit> page4 = dao.getVisitors("italy", 2, lastVisitor);
        List<Visit> visitsToItaly2 = page4.getValues();
        verify(!page4.isHasMorePages());
        System.out.println("Italy was also visited by: " + visitsToItaly2);
    }

    private static void verify(boolean b) {
        if (!b) {
            throw new IllegalStateException();
        }
    }
}
