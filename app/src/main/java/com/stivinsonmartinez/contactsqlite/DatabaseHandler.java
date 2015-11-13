package com.stivinsonmartinez.contactsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="ContactManager",
                                TABLE_CONTACT="contacto",
                                KEY_ID="id",
                                KEY_NAME="name",
                                KEY_PHONE="phone",
                                KEY_EMAIL="email",
                                KEY_IMAGEURI="imageuri";


    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_CONTACT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_IMAGEURI + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);

    }

    public void createContact (Contact contact){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_PHONE, contact.get_phone());
        values.put(KEY_EMAIL,contact.get_email());
        values.put(KEY_IMAGEURI,contact.get_imageURI().toString());

        db.insert(TABLE_CONTACT, null, values);
        db.close();

    }

    public int getContactsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACT, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACT, null);

        if (cursor.moveToFirst()) {
            do {
                contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Uri.parse(cursor.getString(4))));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }
}
