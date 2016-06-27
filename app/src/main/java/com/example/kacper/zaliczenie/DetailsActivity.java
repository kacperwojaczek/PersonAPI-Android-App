package com.example.kacper.zaliczenie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kacper.zaliczenie.Helpers.FavouritesDatabase;
import com.example.kacper.zaliczenie.Models.Person;
import com.example.kacper.zaliczenie.Models.User;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private Person person;
    private User user;

    private Menu myMenu;

    private FavouritesDatabase database;


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addToFavourites:
                if (database.addPerson(person)) {
                    item.setVisible(false);
                    myMenu.findItem(R.id.removeFavourites).setVisible(true);
                    Toast toast = new Toast(this);
                    toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast toast = new Toast(this);
                    toast.makeText(this, "Failed to add to favourites", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.removeFavourites:
                if (database.removePerson(person.getUuid())) {
                    item.setVisible(false);
                    myMenu.findItem(R.id.addToFavourites).setVisible(true);
                    Toast toast = new Toast(this);
                    toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast toast = new Toast(this);
                    toast.makeText(this, "Failed to remove from favourites", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.detailsmenu, menu);
            myMenu = menu;

            if (database.chceckIfPersonExists(person)) {
                myMenu.findItem(R.id.addToFavourites).setVisible(false);
                myMenu.findItem(R.id.removeFavourites).setVisible(true);
            }
        }

        getSupportActionBar().setTitle(getString(R.string.appName));
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        database = new FavouritesDatabase(this);

        Intent intent = getIntent();

        try {
            String userId = intent.getStringExtra("userId");

            if (!userId.isEmpty()) {
                user = new User();

                user.setUuid(intent.getStringExtra("userId"));
                user.setUsername(intent.getStringExtra("username"));

                invalidateOptionsMenu();
            }
        } catch (Exception e) {
            user = null;
        }

        try {
            String response = intent.getStringExtra("response");

            person = new Person(response);

            if (user != null)
                person.setUserId(user.getUuid());

        } catch (Exception e) {

            person = (Person) intent.getParcelableExtra("person");
        }

        fillViews();
    }

    private void fillViews() {
        //image
        Picasso.with(this).load(person.getPhotoUrl()).into((ImageView) findViewById(R.id.personImage));

        //name
        TextView nameView = (TextView) findViewById(R.id.name);
        if (person.getName().equals("")) {
            nameView.setVisibility(View.GONE);
        } else {
            nameView.setText(person.getName());
        }

        //location
        TextView locationView = (TextView) findViewById(R.id.location);
        locationView.setText(person.getLocation());

        //social
        fillButton(R.id.facebook, R.id.facebookId, "facebook");
        fillButton(R.id.twitter, R.id.twitterId, "twitter");
        fillButton(R.id.google, R.id.googleId, "google");
        fillButton(R.id.github, R.id.githubId, "github");
        fillButton(R.id.website, R.id.websiteId, "website");

    }

    private void fillButton(int buttonId, int textId, final String name) {
        Button tempButton = (Button) findViewById(buttonId);
        TextView tempView = (TextView) findViewById(textId);

        if (person.get(name).equals("")) {
            tempButton.setVisibility(View.GONE);
            tempView.setVisibility(View.GONE);
        } else {
            //set listener
            tempButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(person.get(name))));
                }
            });
            //set profile name
            if (!name.equals("google"))
                tempView.setText(Uri.parse(person.get(name)).getPath());
            else {
                if (!person.getGoogle().equals(""))
                    tempView.setText((Uri.parse(person.getGoogle()).getPath()).substring(4));
            }
        }
    }
}
