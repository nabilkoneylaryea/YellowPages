package com.nabilkoneylaryea.yellowpages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.SearchView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.itemClickListener {
    RecyclerView rv_list;
    Adapter adapter;
    SearchView sv_search;
    DatabaseHelper db;
    public static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DatabaseHelper.getInstance(MainActivity.this);

        sv_search = findViewById(R.id.sv_search);
        rv_list = findViewById(R.id.rv_list);

        showList(db);

        sv_search.setImeOptions(EditorInfo.IME_ACTION_DONE); //Changes what is shown in the return key (CHECKMARK INSTEAD OF ENTER)

        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_goToAdd:
                Intent intent = new Intent(MainActivity.this, ActivityAddContact.class);
                startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {

            Log.i(TAG, "Running onActivityResult()");
            showList(db);

        }
    }

    private void showList(DatabaseHelper db) {

        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(MainActivity.this, db.getAll(), this);
        rv_list.setAdapter(adapter);
        Log.i(TAG,"Showing List");

    }

    @Override
    public void onClick(int position) {

        Intent intent = new Intent(this, ViewContactActivity.class);

        List<Contact> contacts;
        if (adapter.getData().size() == adapter.getDataFull().size()) {
            contacts = adapter.getDataFull();
        }
        else {
            contacts = adapter.getData();
        }

        //TODO SHOULD I GO BACK AND MAKE CONTACT CLASS PARCEABLE SO THAT IT CAN BE PASSED AS A PARCEABLE OBJECT!?

        Contact contact = contacts.get(position);
        Log.i(TAG, "onClick: " + position);
        String number = contact.getPhoneNumber();
        Log.i(TAG, "onClick: " + number);

        int ID = db.getItemID(number);
        intent.putExtra("ID", ID);
        Log.i(TAG, "onClick: " + ID);

        startActivityForResult(intent , 1);

    }

}
