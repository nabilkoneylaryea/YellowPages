package com.nabilkoneylaryea.yellowpages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditContactActivity extends AppCompatActivity {

    private static final String TAG = "EDIT_CONTACT_ACTIVITY";
    EditText et_firstName, et_lastName, et_number;
    CircleImageView civ_pfp;
    Button btn_confirm, btn_cancel;
    DatabaseHelper db = DatabaseHelper.getInstance(this);

    String oldFirstName, oldLastName, oldPhoneNumber;
    String newFirstName, newLastName, newPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        et_firstName = findViewById(R.id.et_firstName_edit);
        et_lastName = findViewById(R.id.et_lastName_edit);
        et_number = findViewById(R.id.et_number_edit);
        civ_pfp = findViewById(R.id.civ_edit_pfp);
        btn_confirm = findViewById(R.id.btn_confirm_edit);
        btn_cancel = findViewById(R.id.btn_cancel_edit);

        Intent oldIntent = getIntent();

        oldFirstName = oldIntent.getStringExtra("FIRST_NAME");
        oldLastName = oldIntent.getStringExtra("LAST_NAME");
        oldPhoneNumber = oldIntent.getStringExtra("PHONE_NUMBER");
        boolean hasImage = oldIntent.getBooleanExtra("HAS_IMAGE", false);
        if(hasImage) {
            String imgUriString = oldIntent.getStringExtra("IMAGE");
            Uri img = Uri.parse(imgUriString);
            civ_pfp.setImageURI(img);
        }

        et_firstName.setHint(oldFirstName);
        et_lastName.setHint(oldLastName);
        et_number.setHint(oldPhoneNumber);


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newFirstName = et_firstName.getText().toString();
                newLastName = et_lastName.getText().toString();
                newPhoneNumber = et_number.getText().toString();
                updateContact();
                Intent intent = new Intent();
                intent.putExtra("NEW_FIRST_NAME", newFirstName);
                intent.putExtra("NEW_LAST_NAME", newLastName);
                intent.putExtra("NEW_PHONE_NUMBER", newPhoneNumber);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateContact () {

        Cursor data = db.getItemID(oldPhoneNumber);

        int id = -1;
        if (data.moveToFirst()) {

            do {

                id = data.getInt(0);

            } while (data.moveToNext());

        } else {
            Log.i(TAG, "updateContact: Contact ID not found in correlation to phone number");
        }

        boolean firstNameDifferent = newFirstName.length() > 0 && !(newFirstName.equals(oldFirstName));
        boolean lastNameDifferent = newLastName.length() > 0 && !(newLastName.equals(oldLastName));
        boolean numberDifferent = newPhoneNumber.length() > 0 && !(newPhoneNumber.equals(oldPhoneNumber));

        if (firstNameDifferent) {
            db.updateFirstName(id, oldFirstName, newFirstName);
            Log.i(TAG, "updateContact: First Name: " + newFirstName);
        }
        else {
            newFirstName = oldFirstName;
        }

        if (lastNameDifferent) {
            db.updateLastName(id, oldLastName, newLastName);
            Log.i(TAG, "updateContact: Last Name: " + newLastName);
        }
        else {
            newFirstName = oldFirstName;
        }

        if (numberDifferent) {
           db.updatePhoneNumber(id, oldPhoneNumber, newPhoneNumber);
            Log.i(TAG, "updateContact: Phone Number: " + newPhoneNumber);
        }
        else {
            newFirstName = oldFirstName;
        }

        et_firstName.setText(newFirstName);
        et_lastName.setText(newLastName);
        et_number.setText(newPhoneNumber);


    }
}
