package com.example.kacper.zaliczenie.Helpers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kacper.zaliczenie.DetailsActivity;
import com.example.kacper.zaliczenie.Models.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kacper on 11-Jun-16.
 */
public class DownloadDataTask extends AsyncTask<Void, Void, String> {

    private String emailText;
    private Context context;
    private ProgressBar progressBar;
    private User user;

    public DownloadDataTask(Context context, ProgressBar progressBar, EditText emailText, User user) {
        this.emailText = emailText.getText().toString();
        this.context = context;
        this.progressBar = progressBar;
        this.user = user;
    }

    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... urls) {
        String email = emailText;

        try {
            URL url = new URL("https://api.fullcontact.com/v2/person.json?" + "email=" + email + "&apiKey=" + "368423982aa12505");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } catch (Exception e) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected void onPostExecute(String response) {

        progressBar.setVisibility(View.GONE);


        if (response != null) {
            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                int status = object.getInt("status");
                if (status == 422) {
                    Toast toast = new Toast(context);
                    toast.makeText(context, "The email address has incorrect format!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (status == 403) {
                    Toast toast = new Toast(context);
                    toast.makeText(context, "Rate limit reached, wait a while and try again", Toast.LENGTH_SHORT).show();
                    return;
                } else if (status >= 500) {
                    Toast toast = new Toast(context);
                    toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    return;
                } else if (status == 202) {
                    Toast toast = new Toast(context);
                    toast.makeText(context, "The server is searching, try again in a few minutes", Toast.LENGTH_SHORT).show();
                    return;
                } else if (status >= 400) {
                    Toast toast = new Toast(context);
                    toast.makeText(context, "No information found", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //if getting data was successful
                    Intent detailsIntent = new Intent(context, DetailsActivity.class);
                    detailsIntent.putExtra("response", response);
                    if (user != null) {
                        detailsIntent.putExtra("userId", user.getUuid());
                        detailsIntent.putExtra("username", user.getUsername());
                    }
                    context.startActivity(detailsIntent);
                }
            } catch (JSONException e) {
                Toast toast = new Toast(context);
                toast.makeText(context, "Unable to parse server response", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast toast = new Toast(context);
            toast.makeText(context, "There was no response from server", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}
