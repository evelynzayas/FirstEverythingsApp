package com.ezayas.firsteverythings;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.ezayas.firsteverythings.data.EventsContract;

import java.util.Map;
import java.util.Set;

/**
 * Created by x on 3/20/2016
 */
public class ProviderTest extends AndroidTestCase {
    private static final String TEST_EVENT_NAME = "Orig Event Name";
    private static final String TEST_UPDATE_EVENT_NAME = "Upd Event Name";


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testDeleteAllRecords();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testDeleteAllRecords();
    }

    public void testDeleteAllRecords(){
        // Delete events
        mContext.getContentResolver().delete(
                EventsContract.EventsEntry.CONTENT_URI,
                null,
                null
        );

        // Ensure events were deleted
        Cursor cursor = mContext.getContentResolver().query(
                EventsContract.EventsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    public void testGetType(){
        // content_authority = "content://com.ezayas.firsteverythings/:

        //-- EVENT --//
        // content_authority + event
        String type = mContext.getContentResolver().getType(EventsContract.EventsEntry.CONTENT_URI);
        assertEquals(EventsContract.EventsEntry.CONTENT_TYPE, type);

        //-- EVENT_ID --//
        // content_authority + event/id
        type = mContext.getContentResolver().getType(EventsContract.EventsEntry.buildEventsUri(0));
        assertEquals(EventsContract.EventsEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testInsertReadEvent(){
        ContentValues eventContentValues = getEventContentValues();
        Uri eventInsertUri = mContext.getContentResolver().insert(EventsContract.EventsEntry.CONTENT_URI, eventContentValues);
        long eventRowId = ContentUris.parseId(eventInsertUri);

        // Verify we inserted a row
        assertTrue(eventRowId > 0);

        // Query for all rows and validate cursor
        Cursor eventCursor = mContext.getContentResolver().query(
                EventsContract.EventsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        validateCursor(eventCursor, eventContentValues);
        eventCursor.close();

        // Query for specific row and validate cursor
        eventCursor = mContext.getContentResolver().query(
                EventsContract.EventsEntry.buildEventsUri(eventRowId),
                null,
                null,
                null,
                null
        );
        validateCursor(eventCursor, eventContentValues);
        eventCursor.close();
    }


    public void testUpdateEvent(){
        // Insert the event first.
        // No need to verify, we have a test for that.
        ContentValues eventContentValues = getEventContentValues();
        Uri eventInsertUri = mContext.getContentResolver().insert(EventsContract.EventsEntry.CONTENT_URI, eventContentValues);
        long eventRowId = ContentUris.parseId(eventInsertUri);

        // UpdateValues
        ContentValues updatedEventContentValues = new ContentValues(eventContentValues);
        updatedEventContentValues.put(EventsContract.EventsEntry._ID, eventRowId);
        updatedEventContentValues.put(EventsContract.EventsEntry.COLUMN_ENAME, TEST_UPDATE_EVENT_NAME);
        mContext.getContentResolver().update(
                EventsContract.EventsEntry.CONTENT_URI,
                updatedEventContentValues,
                EventsContract.EventsEntry._ID + " = ?",
                new String[]{String.valueOf(eventRowId)}
        );

        // Query for that specific row and verify it
        Cursor eventCursor = mContext.getContentResolver().query(
                EventsContract.EventsEntry.buildEventsUri(eventRowId),
                null,
                null,
                null,
                null
        );
        validateCursor(eventCursor, updatedEventContentValues);
        eventCursor.close();
    }

    private ContentValues getEventContentValues(){
        ContentValues contentValues = new ContentValues();
        //contentValues.put(EventsContract.EventsEntry.COLUMN_LOC_KEY, "12345");
        contentValues.put(EventsContract.EventsEntry.COLUMN_ENAME, TEST_EVENT_NAME);
        contentValues.put(EventsContract.EventsEntry.COLUMN_EDATE, "2016-01-01");
        contentValues.put(EventsContract.EventsEntry.COLUMN_ELOC, "157.23.7.6");
        contentValues.put(EventsContract.EventsEntry.COLUMN_ENOTES, "notes here");
        return contentValues;
    }


    private void validateCursor(Cursor valueCursor, ContentValues expectedValues){
        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for(Map.Entry<String, Object> entry : valueSet){
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            switch(valueCursor.getType(idx)){
                case Cursor.FIELD_TYPE_FLOAT:
                    assertEquals(entry.getValue(), valueCursor.getDouble(idx));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    assertEquals(Integer.parseInt(entry.getValue().toString()), valueCursor.getInt(idx));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    assertEquals(entry.getValue(), valueCursor.getString(idx));
                    break;
                default:
                    assertEquals(entry.getValue().toString(), valueCursor.getString(idx));
                    break;
            }
        }
        valueCursor.close();
    }
}
