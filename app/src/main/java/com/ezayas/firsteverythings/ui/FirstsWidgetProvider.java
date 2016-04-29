package com.ezayas.firsteverythings.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ezayas.firsteverythings.R;
import com.ezayas.firsteverythings.data.EventsContract;

/**
 * Created by x on 4/27/2016.
 *
 * Provider for a widget showing a random event (a memory!)
 */
public class FirstsWidgetProvider extends AppWidgetProvider {

    private static final String[] WIDGET_COLUMNS = {
            EventsContract.EventsEntry._ID,
            EventsContract.EventsEntry.COLUMN_ENAME,
            EventsContract.EventsEntry.COLUMN_ENOTES
    };

    // These indices are tied to WIDGET_COLUMNS
    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_NAME = 1;
    static final int COL_EVENT_NOTES = 2;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Retrieve all of the widget ids we need to update
        ComponentName thisWidget = new ComponentName(context,FirstsWidgetProvider.class);
        int[] allWidgetIds  = appWidgetManager.getAppWidgetIds(thisWidget);

        // Get data from the Event database
        Uri eventsUri = EventsContract.EventsEntry.CONTENT_URI;
        Cursor data = context.getContentResolver()
                .query(eventsUri,                               //table
                        WIDGET_COLUMNS,                       // projection
                        null,                                  // selection
                        null,                                  //selection args
                        null);                                 // sortOrder

        // get a randon event
        if (data != null) {
            if (data.getCount() > 1){
                int random = (int)(Math.random() * data.getCount());
                data.moveToPosition(random);
//                Log.d("database count: ", String.valueOf(data.getCount()));
//                Log.d("random: ", String.valueOf(random));
            }
            else {
                data.moveToFirst();
            }
        }
        else {
            Toast.makeText(context, "Add a memory!", Toast.LENGTH_SHORT).show();
            return;
        }

        // extract the event name and notes from the cursor
        String eName = data.getString(COL_EVENT_NAME);
        String eNotes = data.getString(COL_EVENT_NOTES);
        data.close();

        // Perform on each widget
        for (int appWidgetId : allWidgetIds) {
            int layoutId = R.layout.widget_firsts;
            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

            // Add data to the remote views
            views.setTextViewText(R.id.widget_name, eName);
            views.setTextViewText(R.id.widget_notes, eNotes);

            // Create an intent to launch MainActivity if widget is clicked
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,2,intent,0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }
}
