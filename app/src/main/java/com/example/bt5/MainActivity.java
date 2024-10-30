package com.example.bt5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int READ_CALL_LOG_REQUEST_CODE = 100;

    private static final int CALL_LOG_LOADER = 1;
    private List<CallLogClass> callLogs = new ArrayList<>();

    private CallLogAdapter callLogAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG_REQUEST_CODE);


        ListView callLogListview = (ListView) findViewById(R.id.callLogList);
        callLogAdapter = new CallLogAdapter(MainActivity.this, R.layout.call_log_item, callLogs);
        callLogListview.setAdapter(callLogAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CALL_LOG_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoaderManager.getInstance(this).initLoader(CALL_LOG_LOADER, null, this);
            } else {
                Toast.makeText(this, "Permissions denied to read call log, you might want to turn it on manually", Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CALL_LOG_LOADER) {  // Define CALL_LOG_LOADER as the ID for call log data
            String[] SELECTED_FIELDS = new String[]{
                    CallLog.Calls._ID,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE
            };

            // Filter call logs to only get those older than the last loaded date
            String sortOrder = CallLog.Calls.DATE + " DESC"; // Order by date, most recent first

            return new CursorLoader(
                    this,
                    CallLog.Calls.CONTENT_URI,       // URI for the call log
                    SELECTED_FIELDS,                 // Columns to retrieve
                    null,                            // No selection clause (all calls)
                    null,                            // No selection arguments
                    sortOrder    // Sort by date (most recent first)
            );
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Only restart loader if permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            restartLoader();
        }
    }

    private void restartLoader() {
        LoaderManager.getInstance(this).restartLoader(CALL_LOG_LOADER, null, this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            // Reset the last loaded date to the maximum

            while (data.moveToNext()) {
                String id = data.getString(0);
                String phone = data.getString(1);
                int type = data.getInt(2);
                long timestamp = data.getLong(3);
                String formattedDate = dateFormat.format(new Date(timestamp));
                callLogs.add(new CallLogClass(id, phone, formattedDate, type));

            }


            callLogAdapter.notifyDataSetChanged();


            // Determine if we need to stop loading more data
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        callLogs.clear();
        callLogAdapter.notifyDataSetChanged();
    }
}