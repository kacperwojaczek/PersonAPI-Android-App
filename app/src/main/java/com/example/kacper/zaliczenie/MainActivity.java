package com.example.kacper.zaliczenie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kacper.zaliczenie.Helpers.DownloadDataTask;
import com.example.kacper.zaliczenie.Helpers.UsersDatabase;
import com.example.kacper.zaliczenie.Models.User;

public class MainActivity extends AppCompatActivity {

    EditText emailText;
    ProgressBar progressBar;
    UsersDatabase database;
    LinearLayout loginBlock;
    Button loginButton;
    TextView username;
    TextView password;
    TextView helloText;
    User user = null;
    Context myContext = this;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.favourites:
                if (user != null) {
                    Intent favouritesIntent = new Intent(this, FavouritesActivity.class);
                    favouritesIntent.putExtra("userId", user.getUuid());
                    favouritesIntent.putExtra("username", user.getUsername());
                    myContext.startActivity(favouritesIntent);
                } else {
                    Toast toast = new Toast(this);
                    toast.makeText(this, "No user", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.logout:
                user = null;
                loginBlock.setVisibility(View.VISIBLE);
                helloText.setVisibility(View.GONE);
                invalidateOptionsMenu();
                Toast toast = new Toast(this);
                toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
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
            inflater.inflate(R.menu.mainmenu, menu);
        }
        getSupportActionBar().setTitle(getString(R.string.appName));
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.emailText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button searchButton = (Button) findViewById(R.id.queryButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailText.getText().toString().isEmpty()) {
                    new DownloadDataTask(myContext, progressBar, emailText, user).execute();
                } else {
                    Toast toast = new Toast(myContext);
                    toast.makeText(myContext, "Please input email adress!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        database = new UsersDatabase(this);

        loginBlock = (LinearLayout) findViewById(R.id.loginBlock);
        loginButton = (Button) findViewById(R.id.logIn);
        username = (TextView) findViewById(R.id.name);
        password = (TextView) findViewById(R.id.password);
        helloText = (TextView) findViewById(R.id.helloText);

        //LOGIN

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = database.getUser(username.getText().toString());
                if (user != null) {
                    if (password.getText().toString().equals(user.getPassword())) {

                        password.setText("");
                        loginBlock.setVisibility(View.GONE);
                        helloText.setText(new String("Hello " + user.getUsername() + "!"));
                        helloText.setVisibility(View.VISIBLE);

                        invalidateOptionsMenu();

                        Toast toast = new Toast(myContext);
                        toast.makeText(myContext, "Login successful", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast toast = new Toast(myContext);
                        toast.makeText(myContext, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast toast = new Toast(myContext);
                    toast.makeText(myContext, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = database.getUser(username.getText().toString());
                if (user == null) {
                    user = database.addUser(new User(username.getText().toString(), password.getText().toString()));
                    if (user != null) {
                        Toast toast = new Toast(myContext);
                        toast.makeText(myContext, "Successfully registered", Toast.LENGTH_SHORT).show();

                        password.setText("");
                        loginBlock.setVisibility(View.GONE);

                        helloText.setText(new String("Hello " + user.getUsername() + "!"));
                        helloText.setVisibility(View.VISIBLE);

                        invalidateOptionsMenu();
                    } else {
                        Toast toast = new Toast(myContext);
                        toast.makeText(myContext, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast toast = new Toast(myContext);
                    toast.makeText(myContext, "User already registered", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
