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
public class AddRecords extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        insertEvent1();
        insertEvent2();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void insertEvent1() {
        ContentValues eventContentValues = getEventContentValues1();
        Uri eventInsertUri = mContext.getContentResolver().insert(EventsContract.EventsEntry.CONTENT_URI, eventContentValues);
        long eventRowId = ContentUris.parseId(eventInsertUri);

        // Verify we inserted a row
        assertTrue(eventRowId > 0);
    }
    public void insertEvent2() {
        ContentValues eventContentValues = getEventContentValues2();
        Uri eventInsertUri = mContext.getContentResolver().insert(EventsContract.EventsEntry.CONTENT_URI, eventContentValues);
        long eventRowId = ContentUris.parseId(eventInsertUri);

        // Verify we inserted a row
        assertTrue(eventRowId > 0);
    }

//        // Query for all rows and validate cursor
//        Cursor eventCursor = mContext.getContentResolver().query(
//                EventsContract.EventsEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null
//        );
//        validateCursor(eventCursor, eventContentValues);
//        eventCursor.close();
//
//        // Query for specific row and validate cursor
//        eventCursor = mContext.getContentResolver().query(
//                EventsContract.EventsEntry.buildEventsUri(eventRowId),
//                null,
//                null,
//                null,
//                null
//        );
//        validateCursor(eventCursor, eventContentValues);
//        eventCursor.close();



    private ContentValues getEventContentValues1(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventsContract.EventsEntry.COLUMN_ENAME, "event 1");
        contentValues.put(EventsContract.EventsEntry.COLUMN_EDATE, "2016-01-02");
        return contentValues;
    }

    private ContentValues getEventContentValues2(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventsContract.EventsEntry.COLUMN_ENAME, "event 2");
        contentValues.put(EventsContract.EventsEntry.COLUMN_EDATE, "2016-01-02");

        return contentValues;
    }


//    private void validateCursor(Cursor valueCursor, ContentValues expectedValues){
//        assertTrue(valueCursor.moveToFirst());
//
//        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
//
//        for(Map.Entry<String, Object> entry : valueSet){
//            String columnName = entry.getKey();
//            int idx = valueCursor.getColumnIndex(columnName);
//            assertFalse(idx == -1);
//            switch(valueCursor.getType(idx)){
//                case Cursor.FIELD_TYPE_FLOAT:
//                    assertEquals(entry.getValue(), valueCursor.getDouble(idx));
//                    break;
//                case Cursor.FIELD_TYPE_INTEGER:
//                    assertEquals(Integer.parseInt(entry.getValue().toString()), valueCursor.getInt(idx));
//                    break;
//                case Cursor.FIELD_TYPE_STRING:
//                    assertEquals(entry.getValue(), valueCursor.getString(idx));
//                    break;
//                default:
//                    assertEquals(entry.getValue().toString(), valueCursor.getString(idx));
//                    break;
//            }
//        }
//        valueCursor.close();
//    }
}
