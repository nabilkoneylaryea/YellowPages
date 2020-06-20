package com.nabilkoneylaryea.yellowpages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityAddContact extends AppCompatActivity {
    EditText firstNameView, lastNameView, phoneNumberView;
    TextView tv_addImg, tv_add_error;
    CircleImageView civ_pfp;
    Button btn_add;
    Button btn_cancel;
    DatabaseHelper db;
    Uri img;
    public static final String TAG = "ACTIVITY_ADD_CONTACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        setTitle("Add Contact");
        firstNameView = findViewById(R.id.et_firstName);
        lastNameView = findViewById(R.id.et_lastName);
        phoneNumberView = findViewById(R.id.et_number);
        btn_add  = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel_add);
        civ_pfp = findViewById(R.id.civ_pfp);
        tv_addImg = findViewById(R.id.tv_addImg);
        tv_add_error = findViewById(R.id.tv_add_error);
        db = DatabaseHelper.getInstance(this);


        civ_pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should put this code block in a void pickFromGallery method to improve readability and concision!!
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent (Intent.ACTION_OPEN_DOCUMENT);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                String [] mimeTypes = {"image/png", "image/jpeg"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, 2);
            }
        });
        tv_addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent (Intent.ACTION_OPEN_DOCUMENT);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                String [] mimeTypes = {"image/png", "image/jpeg"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, 2);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Contact contact = newContact();

                //Add safegaurds and backups in the case of empty names and phone nummbers
                // First Name: Unknown
                // Phone Number: if else if else block of various conditions for improperly entered numbers e.g. too long/ too short/ enter a phone number
                boolean isValid = contact.getPhoneNumber().length() > 0;
                if (isValid) {
                    tv_add_error.setText("");
                    db.add(contact);
                    Log.i(TAG, "Added contact to database: " + contact.toString());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    tv_add_error.setText("!");
                    Toast.makeText(ActivityAddContact.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });


    }

    public Contact newContact() {

        Contact contact = new Contact();
        contact.setFirstName(firstNameView.getText().toString());
        contact.setLastName(lastNameView.getText().toString());
        contact.setPhoneNumber(phoneNumberView.getText().toString());
        contact.setImg(img);
        return contact;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
            img = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int flags = data.getFlags();
                flags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(img, flags);
            }
            civ_pfp.setImageURI(img);

        }
    }
}
