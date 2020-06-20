package com.nabilkoneylaryea.yellowpages;

public class Constants {

    //Database

    //DB Name
    public static final String DATABASE_CONTACTS = "DATABASE_CONTACTS_2";

    //DB Version
    public static final int DATABASE_VERSION = 1;

    //Tables
    public static final String TABLE_CONTACTS = "TABLE_CONTACTS";

    //Columns:
    //0
    public static final String COLUMN_ID = "contact_id";
    //1
    public static final String COLUMN_FIRST_NAME = "contact_first_name";
    //2
    public static final String COLUMN_LAST_NAME = "contact_last_name";
    //3
    public static final String COLUMN_PHONE_NUMBER = "contact_phone_number";
    //4
    public static final String COLUMN_PFP = "contact_pfp";

    //Queries
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_CONTACTS + "(" + COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT, " + COLUMN_FIRST_NAME + " text, " + COLUMN_LAST_NAME + " text, " + COLUMN_PHONE_NUMBER + " text, " + COLUMN_PFP + " text)";
    public static final String GET_TABLE = "SELECT * FROM " + TABLE_CONTACTS;
    public static final String GET_TABLE_ALPHABETIZED = "SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + COLUMN_LAST_NAME;


}
