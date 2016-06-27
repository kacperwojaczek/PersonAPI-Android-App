package com.example.kacper.zaliczenie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kacper.zaliczenie.Helpers.FavouritesAdapter;
import com.example.kacper.zaliczenie.Models.User;

import org.json.JSONObject;

public class FavouritesActivity extends AppCompatActivity {

    User user = null;
    FavouritesAdapter adapter;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favouritesmenu, menu);
        getSupportActionBar().setTitle(getString(R.string.appName));
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        initializeList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        Intent intent = getIntent();
        try {
            String userId = intent.getStringExtra("userId");

            if (!userId.isEmpty()) {
                user = new User();

                user.setUuid(intent.getStringExtra("userId"));
                user.setUsername(intent.getStringExtra("username"));

            }
        } catch (Exception e) {
            user = null;
        }

        initializeList();

    }

    private void initializeList() {

        ListView list = (ListView) findViewById(R.id.favouritesList);

        adapter = new FavouritesAdapter(this, user);

        list.setAdapter(adapter);

        final Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra("userId", user.getUuid());
        detailsIntent.putExtra("username", user.getUsername());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                detailsIntent.putExtra("person", adapter.getItem(position));
                startActivity(detailsIntent);
            }
        });
    }

}
