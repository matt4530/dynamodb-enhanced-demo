import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.util.*;
import java.util.stream.Collectors;


public class VisitsDAO {

    private static final String TableName = "visits_demo";
    public static final String IndexName = "location-visitor-index2";

    private static final String VisitorAttr = "visitor";
    private static final String LocationAttr = "location";

    // DynamoDB client
    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }



    /**
     * Retrieve the number of times visitor has visited location
     *
     * @param visitor
     * @param location
     * @return
     */
    public int getVisitCount(String visitor, String location) {
        DynamoDbTable<Visit> table = enhancedClient.table(TableName, TableSchema.fromBean(Visit.class));
        Key key = Key.builder()
                .partitionValue(visitor).sortValue(location)
                .build();

        Visit visit = table.getItem(key);
        return visit == null ? 0 : visit.getVisit_count();
    }

    /**
     * Increment the number of times visitor has visited location
     *
     * @param visitor
     * @param location
     */
    public void recordVisit(String visitor, String location) {
        DynamoDbTable<Visit> table = enhancedClient.table(TableName, TableSchema.fromBean(Visit.class));
        Key key = Key.builder()
                .partitionValue(visitor).sortValue(location)
                .build();

        // load it if it exists
        Visit visit = table.getItem(key);
        if(visit != null) {
            visit.setVisit_count(visit.getVisit_count() + 1);
            table.updateItem(visit);
        } else {
            Visit newVisit = new Visit();
            newVisit.setVisitor(visitor);
            newVisit.setLocation(location);
            newVisit.setVisit_count(1);
            table.putItem(newVisit);
        }
    }

    /**
     * Delete all visits of visitor to location
     *
     * @param visitor
     * @param location
     */
    public void deleteVisit(String visitor, String location) {
        DynamoDbTable<Visit> table = enhancedClient.table(TableName, TableSchema.fromBean(Visit.class));
        Key key = Key.builder()
                .partitionValue(visitor).sortValue(location)
                .build();
        table.deleteItem(key);
    }

    /**
     * Fetch the next page of locations visited by visitor
     *
     * @param visitor The visitor of interest
     * @param pageSize The maximum number of locations to include in the result
     * @param lastLocation The last location returned in the previous page of results
     * @return The next page of locations visited by visitor
     */
    public List<Visit> getVisitedLocations(String visitor, int pageSize, String lastLocation) {
        DynamoDbTable<Visit> table = enhancedClient.table(TableName, TableSchema.fromBean(Visit.class));
        Key key = Key.builder()
                .partitionValue(visitor)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));
                // If you use iterators, it auto-fetches next page always, so instead limit the stream below
                //.limit(5);

        if(isNonEmptyString(lastLocation)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(VisitorAttr, AttributeValue.builder().s(visitor).build());
            startKey.put(LocationAttr, AttributeValue.builder().s(lastLocation).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        return table.query(request)
                .items()
                .stream()
                .limit(pageSize)
                .collect(Collectors.toList());

        /*
         * Alternative Implementation 1 of the line above, using forEach
         */
        //List<Visit> visits = new ArrayList<>();
        //table.query(request).items().stream().limit(pageSize).forEach(v -> visits.add(v));
        //return visits;

        /*
         * Alternative Implementation 2 of the line above, using a for loop
         */
        //List<Visit> visits = new ArrayList<>();
        //for(Visit visit : table.query(request).items()) {
        //    // stop iteration if we've reached the number of items asked for
        //    if(visits.size() >= pageSize)
        //        break;
        //    visits.add(visit);
        //}
        //return visits;

        /*
         * Alternative Implementation 3 of the line above, using a while loop
         */
        //List<Visit> visits = new ArrayList<>();
        //Iterator<Visit> it = table.query(request).items().iterator();
        //// while there are elements to iterate over, and we haven't reached the number of items asked for
        //while(it.hasNext() && visits.size() < pageSize){
        //    Visit visit = it.next();
        //    visits.add(visit);
        //}
        //return visits;
    }

    /**
     * Fetch the next page of visitors who have visited location
     *
     * @param location The location of interest
     * @param pageSize The maximum number of visitors to include in the result
     * @param lastVisitor The last visitor returned in the previous page of results
     * @return The next page of visitors who have visited location
     */
    public List<Visit>  getVisitors(String location, int pageSize, String lastVisitor) {
        DynamoDbIndex<Visit> index = enhancedClient.table(TableName, TableSchema.fromBean(Visit.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(location)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                // Unlike Tables, querying from an Index returns a PageIterable, so we want to just ask for
                // 1 page with pageSize items
                .limit(pageSize);

        if(isNonEmptyString(lastVisitor)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(LocationAttr, AttributeValue.builder().s(location).build());
            startKey.put(VisitorAttr, AttributeValue.builder().s(lastVisitor).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<Visit> visits = new ArrayList<>();

        SdkIterable<Page<Visit>> results2 = index.query(request);
        PageIterable<Visit> pages = PageIterable.create(results2);
        // limit 1 page, with pageSize items
        pages.stream()
            .limit(1)
            .forEach(visitsPage -> visitsPage.items().forEach(v -> visits.add(v)));

        return visits;
    }

}