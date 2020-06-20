package com.nabilkoneylaryea.yellowpages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String TAG = "DATABASE_HELPER";



    private static DatabaseHelper DB;

    private DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_CONTACTS, null, Constants.DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (DB == null) {
            DB = new DatabaseHelper(context.getApplicationContext());
        }
        return DB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = Constants.CREATE_TABLE;
        db.execSQL(query);
        Log.i(TAG, "Successfully created table " + Constants.TABLE_CONTACTS);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //DOES THIS EREASE THE PREVIOUSLY EXISTING DATA??
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACTS);
        onCreate(db);

    }

    public Cursor getItemID(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + Constants.COLUMN_ID + " FROM " + Constants.TABLE_CONTACTS +
                " WHERE " + Constants.COLUMN_PHONE_NUMBER + " = '" + number + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + Constants.TABLE_CONTACTS;
        db.execSQL(query);
    }

    public void deleteContact(int id, String firstName, String lastName, String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + Constants.TABLE_CONTACTS +
                " WHERE " + Constants.COLUMN_ID + " = '" + id + "' AND " + Constants.COLUMN_PHONE_NUMBER + " = '" + phoneNumber + "'";
        try {
            db.execSQL(query);
            Log.i(TAG, "deleteContact: Deleted: " + firstName + " " + lastName);
        } catch (SQLiteException e) {
            Log.i(TAG, "deleteContact: " + e.toString());
        }
    }

    public void updateFirstName(int id, String old, String updated) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query =
                "UPDATE " + Constants.TABLE_CONTACTS + " SET " + Constants.COLUMN_FIRST_NAME + " = '" + updated + "' " +
                        "WHERE " + Constants.COLUMN_ID + " = '" + id + "' AND " + Constants.COLUMN_FIRST_NAME + " = '" + old + "'";
        Log.i(TAG, "updateFirstName: " + query);

        try {
            db.execSQL(query);
        } catch (SQLiteException e) {
            Log.i(TAG, "updateFirstName: " + e.toString());
        }
    }

    public void updateLastName(int id, String old, String updated) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query =
                "UPDATE " + Constants.TABLE_CONTACTS + " SET " + Constants.COLUMN_LAST_NAME + " = '" + updated + "' " +
                        "WHERE " + Constants.COLUMN_ID + " = '" + id + "' AND " + Constants.COLUMN_LAST_NAME + " = '" + old + "'";
        Log.i(TAG, "updateFirstName: " + query);

        try {
            db.execSQL(query);
        } catch (SQLiteException e) {
            Log.i(TAG, "updateFirstName: " + e.toString());
        }
    }

    public void updatePhoneNumber(int id, String old, String updated) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query =
                "UPDATE " + Constants.TABLE_CONTACTS + " SET " + Constants.COLUMN_PHONE_NUMBER + " = '" + updated + "' " +
                        "WHERE " + Constants.COLUMN_ID + " = '" + id + "' AND " + Constants.COLUMN_PHONE_NUMBER + " = '" + old + "'";
        Log.i(TAG, "updateFirstName: " + query);

        try {
            db.execSQL(query);
        } catch (SQLiteException e) {
            Log.i(TAG, "updateFirstName: " + e.toString());
        }
    }


    public void add(Contact contact) {

        SQLiteDatabase db = getWritableDatabase();
        long ID = addOrUpdate(contact);
        db.beginTransaction();

        try {
            ContentValues cv = new ContentValues();

            cv.put(Constants.COLUMN_ID, ID);
            cv.put(Constants.COLUMN_FIRST_NAME, contact.getFirstName());
            cv.put(Constants.COLUMN_LAST_NAME,contact.getLastName());
            cv.put(Constants.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
            if (contact.hasImage()) {
                cv.put(Constants.COLUMN_PFP, contact.getImg().toString());
            }
            db.insertOrThrow(Constants.TABLE_CONTACTS, null, cv);
            db.setTransactionSuccessful();
            Log.i(TAG, "Successfully added contact to database");
            Log.i(TAG, contact.toString());

        } catch (Exception e) {
            Log.i(TAG, "Error adding to database: " + e.toString());
        } finally {
            db.endTransaction();
        }
    }

    protected long  addOrUpdate(Contact contact) {
        long ID = -1;
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues cv = new ContentValues();

            if(contact.hasImage()) {
                cv.put(Constants.COLUMN_PFP, contact.getImg().toString());
            }
            cv.put(Constants.COLUMN_FIRST_NAME, contact.getFirstName());
            cv.put(Constants.COLUMN_LAST_NAME, contact.getLastName());
            cv.put(Constants.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());

            int row = db.update(Constants.TABLE_CONTACTS, cv, Constants.COLUMN_PHONE_NUMBER + " = ?", new String[]{contact.getPhoneNumber()});

            if (row == 1) {

                String query = Constants.GET_TABLE;
                Cursor cursor = db.rawQuery(query, null);

                try {
                    if (cursor.moveToNext()) {

                        ID = cursor.getInt(0);
                        db.setTransactionSuccessful();
                        Log.i(TAG, "Successfully got ID for existing contact: " + ID);

                    }
                } catch (Exception e) {

                    Log.i(TAG, "Error adding or updating database: " + e.toString());

                } finally {

                    if(cursor != null && !cursor.isClosed()) {

                        cursor.close();

                    }

                }
            }
            else {
                ID = db.insertOrThrow(Constants.TABLE_CONTACTS, null, cv);
                db.setTransactionSuccessful();
                Log.i(TAG, "Successfully got ID for new contact: " + ID);
            }

        } catch (Exception e) {

            Log.i(TAG, "Error adding or updating database: " + e.toString());

        } finally {db.endTransaction();}

        return ID;
    }

    public List<Contact> getAll() {
        List<Contact> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();

        try {
            String query = Constants.GET_TABLE_ALPHABETIZED;
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();

                    contact.setId(cursor.getInt(0));
                    contact.setFirstName(cursor.getString(1));
                    contact.setLastName(cursor.getString(2));
                    contact.setPhoneNumber(cursor.getString(3));
                    if (cursor.getString(4) != null) {
                        contact.setImg(Uri.parse(cursor.getString(4)));
                    }
                    list.add(contact);

                }while (cursor.moveToNext());
                db.setTransactionSuccessful();
                Log.i(TAG, "Successfully added contacts to a List");
            }

        }
        catch (Exception e) {
            Log.i(TAG, "Error getting list from database: " + e.toString());
        } finally {
            db.endTransaction();
        }


        return list;
    }
}
