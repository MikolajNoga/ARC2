package pl.edu.pjwstk.ARC2.bigquery;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryError;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.TableId;
import java.util.List;
import java.util.Map;

public class TableInsertRows {


    public static void tableInsertRows(
            String datasetName, String tableName, Map<String, Object> rowContent) {
        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

            // Get table
            TableId tableId = TableId.of(datasetName, tableName);

            // Inserts rowContent into datasetName:tableId.
            InsertAllResponse response =
                    bigquery.insertAll(
                            InsertAllRequest.newBuilder(tableId)
                                    // More rows can be added in the same RPC by invoking .addRow() on the builder.
                                    // You can also supply optional unique row keys to support de-duplication
                                    // scenarios.
                                    .addRow(rowContent)
                                    .build());

            if (response.hasErrors()) {
                // If any of the insertions failed, this lets you inspect the errors
                for (Map.Entry<Long, List<BigQueryError>> entry : response.getInsertErrors().entrySet()) {
                    System.out.println("Response error: \n" + entry.getValue());
                }
            }
            System.out.println("Rows successfully inserted into table");
        } catch (BigQueryException e) {
            System.out.println("Insert operation not performed \n" + e.toString());
        }
    }
}
