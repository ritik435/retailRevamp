package com.neotechInnovations.retailrevamp.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.R;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleSheetsManager {

    private static final String TAG = "GoogleSheetsManager";
    private static final String APPLICATION_NAME = "RetailRevamp";
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/spreadsheets", "https://www.googleapis.com/auth/drive");
    private static final List<String> SCOPES1 = Arrays.asList();

    public interface GoogleSheetsCallback {
        void onSheetCreated(String sheetLink);

        void onDataCalculated(Map<String, List<Integer>> hmStatsRetrieve);
        void onSheetCreationFailed(Exception e);

    }

    static Context context;
    static HttpTransport httpTransport;
    static JsonFactory jsonFactory;
    static GoogleCredential credential;

    public GoogleSheetsManager(Context context) {
        this.context = context;
        httpTransport = new NetHttpTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        jsonFactory = new GsonFactory();

//        Drive driveService = getDriveService(accessToken);
        Log.d(TAG, "StatsPopulate: 1");
//        InputStream inputStream = context.getResources().openRawResource(R.raw.retail_service);
        InputStream inputStream = null;
        try {
            credential = GoogleCredential.fromStream(inputStream).createScoped(SCOPES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addListsToGoogleSheets(final List<List<Object>> lists, boolean toCreate, final GoogleSheetsCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    if (toCreate) {
                        return createSheetAndPopulate(lists);
                    } else {
                        return StatsPopulate(lists);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error adding lists to Google Sheets", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String sheetLink) {
                if (sheetLink != null) {
                    callback.onSheetCreated(sheetLink);
                } else {
                    callback.onSheetCreationFailed(new Exception("Failed to create sheet"));
                }
            }
        }.execute();
    }

    public static void retrieveDataFromGoogleSheets(String spreadSheetId, final GoogleSheetsCallback callback) {
        new AsyncTask<Void, Void, Map<String, List<Integer>>>() {
            @Override
            protected Map<String, List<Integer>> doInBackground(Void... voids) {
                try {
                    return retrieveListsFromGoogleSheets(spreadSheetId );
                } catch (Exception e) {
                    Log.e(TAG, "Error adding lists to Google Sheets", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Map<String, List<Integer>> stringListMap) {
                super.onPostExecute(stringListMap);
                if (stringListMap != null) {
                    callback.onDataCalculated(stringListMap);
                } else {
                    callback.onSheetCreationFailed(new Exception("Failed to create sheet"));
                }
            }

            //            @Override
//            protected void onPostExecute(String sheetLink) {
//                if (sheetLink != null) {
//                    callback.onSheetCreated(sheetLink);
//                    callback.onDataCalculated(sheetLink);
//                } else {
//                    callback.onSheetCreationFailed(new Exception("Failed to create sheet"));
//                }
//            }
        }.execute();
    }


    private static String createSheetAndPopulate(List<List<Object>> lists) throws IOException, GeneralSecurityException {
        Log.d(TAG, "createSheetAndPopulate: 0");
//        HttpTransport httpTransport = new NetHttpTransport();
////        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        JsonFactory jsonFactory = new GsonFactory();
//
////        Drive driveService = getDriveService(accessToken);
//        Log.d(TAG, "createSheetAndPopulate: 1");
//        InputStream inputStream = context.getResources().openRawResource(R.raw.retail_service);
//        GoogleCredential credential = GoogleCredential.fromStream(inputStream);
////        GoogleCredential credential1 = GoogleCredential.fromStream(inputStream);
//        Log.d(TAG, "createSheetAndPopulate: credential " + credential.getAccessToken());
//        credential = credential.createScoped(SCOPES);
//        credential1 = credential1.createScoped(SCOPES1);
        Log.d(TAG, "createSheetAndPopulate: 2 a");
        Drive driveservice = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        Sheets sheetsService =
                new Sheets.Builder(httpTransport, jsonFactory, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        Log.d(TAG, "createSheetAndPopulate: 2 b");
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties().setTitle("My Statstics"));
        Log.d(TAG, "createSheetAndPopulate: 2 c");
//        spreadsheet = sheetsService.spreadsheets().create(spreadsheet).execute();
        spreadsheet = sheetsService.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();

        Log.d(TAG, "createSheetAndPopulate: 3");
        String spreadsheetId = spreadsheet.getSpreadsheetId();
        HomepageActivity.spreadsheetId = spreadsheetId;
//        String spreadsheetId = "1O8OTBCdAykL5RFce43OmPOxQHpXNPw_FUUPlESKmvMk";
        Log.d(TAG, "createSheetAndPopulate: 4 " + lists.get(0).get(0));
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("Sheet2"))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("Sheet3"))));
        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest).execute();

        List<ValueRange> data = new java.util.ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            List<Object> innerList = lists.get(i);
            List<List<Object>> values = new ArrayList<>();
            values.add(innerList);
            data.add(new ValueRange().setRange("Sheet1!A" + (i + 1)).setValues(values));
        }
        for (int i = 0; i < lists.size(); i++) {
            List<Object> innerList = lists.get(i);
            List<List<Object>> values = new ArrayList<>();
            values.add(innerList);
            data.add(new ValueRange().setRange("Sheet2!A" + (i + 1)).setValues(values));
        }
        for (int i = 0; i < lists.size(); i++) {
            List<Object> innerList = lists.get(i);
            List<List<Object>> values = new ArrayList<>();
            values.add(innerList);
            data.add(new ValueRange().setRange("Sheet3!A" + (i + 1)).setValues(values));
        }
        Log.d(TAG, "createSheetAndPopulate: 5");
        BatchUpdateValuesRequest batchUpdateRequest = new BatchUpdateValuesRequest().setValueInputOption("RAW").setData(data);
        sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
        Log.d(TAG, "createSheetAndPopulate: 6");
        String strLink = Tags.PREFIX_SPREADSHEET_LINK + spreadsheetId;
        Log.d(TAG, "createSheetAndPopulate: : " + strLink);

        // Create a new permission
        // Share the spreadsheet with everyone
        Permission newPermission = new Permission()
                .setType("user")
                .setRole("writer") // Set the desired role (reader, writer, owner)
                .setEmailAddress("developer.ritik435@gmail.com");

        // Add the permission to the file
        driveservice.permissions().create(spreadsheetId, newPermission).execute();


        return strLink;
    }

    private static String StatsPopulate(List<List<Object>> lists) throws IOException, GeneralSecurityException {
        Log.d(TAG, "StatsPopulate: 0");
//        GoogleCredential credential1 = GoogleCredential.fromStream(inputStream);
        Log.d(TAG, "StatsPopulate: credential " + credential.getAccessToken());
        credential = credential.createScoped(SCOPES);
//        credential1 = credential1.createScoped(SCOPES1);
        Log.d(TAG, "StatsPopulate: 2 a");
        Drive driveservice = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        Sheets sheetsService =
                new Sheets.Builder(httpTransport, jsonFactory, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        Log.d(TAG, "StatsPopulate: 2 b");
//        Spreadsheet spreadsheet = new Spreadsheet()
//                .setProperties(new SpreadsheetProperties().setTitle("My Spreadsheet"));
//        Log.d(TAG, "createSheetAndPopulate: 2 c");
////        spreadsheet = sheetsService.spreadsheets().create(spreadsheet).execute();
//        spreadsheet = sheetsService.spreadsheets().create(spreadsheet)
//                .setFields("spreadsheetId")
//                .execute();

        Log.d(TAG, "StatsPopulate: 3");
//        String spreadsheetId = spreadsheet.getSpreadsheetId();

        String spreadsheetId = HomepageActivity.spreadsheetId;
        Log.d(TAG, "StatsPopulate: 4 " + lists.get(0).get(0));
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("Sheet2"))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("Sheet3"))));
        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest).execute();
        List<ValueRange> data = new java.util.ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            List<Object> innerList = lists.get(i);
            List<List<Object>> values = new ArrayList<>();
            values.add(innerList);
            data.add(new ValueRange().setRange("Sheet2!A" + (i + 2)).setValues(values));
        }
        Log.d(TAG, "StatsPopulate: 5");
        BatchUpdateValuesRequest batchUpdateRequest = new BatchUpdateValuesRequest().setValueInputOption("RAW").setData(data);
        sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
        Log.d(TAG, "StatsPopulate: 6");
        String strLink = Tags.PREFIX_SPREADSHEET_LINK + spreadsheetId;
        Log.d(TAG, "StatsPopulate: : " + strLink);

        // Create a new permission
        // Share the spreadsheet with everyone
        Permission newPermission = new Permission()
                .setType("user")
                .setRole("writer") // Set the desired role (reader, writer, owner)
                .setEmailAddress("developer.ritik435@gmail.com");

        // Add the permission to the file
        driveservice.permissions().create(spreadsheetId, newPermission).execute();


        return strLink;
    }

    private static Map<String, List<Integer>> retrieveListsFromGoogleSheets(final String spreadsheetId) {
        Log.d(TAG, "retrieveListsFromGoogleSheets: enteredd...");
        Map<String, List<Integer>> dataMap = new HashMap<>();
        try {
            Sheets sheetsService = new Sheets.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            Log.d(TAG, "retrieveListsFromGoogleSheets: 1");
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, "Sheet1!A2:F")
                    .execute();
            Log.d(TAG, "retrieveListsFromGoogleSheets: 2");
            List<List<Object>> values = response.getValues();
            Log.d(TAG, "retrieveListsFromGoogleSheets: 3");
            if (values != null) {
                for (List<Object> row : values) {
                    if (row.size() >= 6) { // Assuming each row has 5 columns
                        String key = row.get(0).toString();
                        List<Integer> valuesList = new ArrayList<>();
                        for (int i = 1; i < row.size(); i++) {
                            valuesList.add(Integer.parseInt(row.get(i).toString()));
                        }
                        Log.d(TAG, "retrieveListsFromGoogleSheets: 4 : "+key);
                        dataMap.put(key, valuesList);
                    }
                }
            }
            Log.d(TAG, "retrieveListsFromGoogleSheets: 5 : "+dataMap.containsKey("02 May 24"));
        } catch (IOException e) {
            Log.e(TAG, "Error retrieving data from Google Sheets: " + e.getMessage());
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing integer: " + e.getMessage());
        }
        return dataMap;
    }
}

