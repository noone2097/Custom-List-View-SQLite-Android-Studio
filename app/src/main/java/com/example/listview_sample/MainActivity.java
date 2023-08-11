package com.example.listview_sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DBHelper db;
    ListView listView;
    PersonAdapter adapter;
    ArrayList<PersonInfo> arrayList;
    public int item_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        listView = findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(modeListener);
        showData();

    }

    private void showData() {
        arrayList = db.getAllData();
        adapter = new PersonAdapter(this, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.insert_menu, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.insert_layout, null);
        EditText name = v.findViewById(R.id.TextText);
        EditText age = v.findViewById(R.id.TextNumber);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v)
                .setTitle("Add Data")
                .setMessage("Fill all fields.")
                .setPositiveButton("ADD", (dialogInterface, i) -> {
                    boolean isInserted = db.insertData(name.getText().toString().trim(), Integer.parseInt(age.getText().toString()));

                    if (isInserted) {
                        showData();
                        Toast.makeText(this, "Data Added.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Data adding failed.", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Cancel,", (dialogInterface, i) -> {
                });
        builder.create().show();

        return super.onOptionsItemSelected(item);
    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long l, boolean b) {

            item_selected = position;
            PersonInfo pi = arrayList.get(position);
            actionMode.setTitle(pi.getName());

        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.del_upd_menu, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

            if (Objects.equals(menuItem.getTitle(), "Delete")) {
                PersonInfo pi = arrayList.get(item_selected);
                int selectedPosition = pi.getId();

                Integer deleteRow = db.deleteRow(selectedPosition);

                if (deleteRow > 0) {
                    showData();
                    Toast.makeText(MainActivity.this, pi.getName() + " is deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete.", Toast.LENGTH_SHORT).show();
                }
            } else if (Objects.equals(menuItem.getTitle(), "Update")) {

                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.insert_layout, null);

                EditText name = v.findViewById(R.id.TextText);
                EditText age = v.findViewById(R.id.TextNumber);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                PersonInfo pi = arrayList.get(item_selected);
                builder.setView(v)
                        .setTitle("Update Data")
                        .setMessage("Fill all fields.")
                        .setPositiveButton("UPDATE", (dialogInterface, i) -> {
                            boolean isUpdated = db.updateData(pi.getId(), name.getText().toString().trim(), Integer.parseInt(age.getText().toString()));
                            if (isUpdated) {
                                showData();
                                Toast.makeText(MainActivity.this, "Data Updated.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Data updating failed.", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setNegativeButton("Cancel,", (dialogInterface, i) -> {
                        });
                builder.create().show();


                Toast.makeText(MainActivity.this, "Update", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    };

}