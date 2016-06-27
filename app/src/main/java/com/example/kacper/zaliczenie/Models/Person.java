package com.example.kacper.zaliczenie.Models;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.kacper.zaliczenie.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.UUID;

/**
 * Created by Kacper on 18-Jun-16.
 */
public class Person implements Parcelable {

    private String uuid = "";
    private String userId = "";
    private String photoUrl = "";
    private String name = "";
    private String location = "";
    private String facebook = "";
    private String twitter = "";
    private String google = "";
    private String github = "";
    private String website = "";

    public Person(String response) {

        uuid = UUID.randomUUID().toString();

        try {
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

            //photo
            try {
                JSONArray photos = object.getJSONArray("photos");
                JSONObject photo = photos.getJSONObject(0);
                photoUrl = photo.getString("url");
            } catch (JSONException e) {
            }

            //name
            try {
                JSONObject contact = object.getJSONObject("contactInfo");
                name = contact.getString("fullName");
            } catch (JSONException e) {
            }

            //website
            try {
                JSONObject contact = object.getJSONObject("contactInfo");
                website = contact.getJSONArray("websites").getJSONObject(0).getString("url");
            } catch (JSONException e) {
            }

            //localization
            try {
                JSONObject local = object.getJSONObject("demographics");
                JSONObject deduced = local.getJSONObject("locationDeduced");
                location = deduced.getString("deducedLocation");
            } catch (JSONException e) {
            }

            //social networks
            try {
                JSONArray social = object.getJSONArray("socialProfiles");

                for (int i = 0; i < social.length(); i++) {
                    try {
                        if (social.getJSONObject(i).getString("typeId").equals("facebook")) {
                            facebook = social.getJSONObject(i).getString("url");
                        }

                        if (social.getJSONObject(i).getString("typeId").equals("twitter")) {
                            twitter = social.getJSONObject(i).getString("url");
                        }

                        if (social.getJSONObject(i).getString("typeId").equals("google")) {
                            google = social.getJSONObject(i).getString("url");
                        }
                        if (social.getJSONObject(i).getString("typeId").equals("github")) {
                            github = social.getJSONObject(i).getString("url");
                        }
                    } catch (JSONException e) {
                    }

                }

            } catch (JSONException e) {
            }


        } catch (JSONException e) {
        }
    }

    public Person() {
    }

    public String get(String name) {
        switch (name) {
            case "photo":
                return getPhotoUrl();
            case "name":
                return getName();
            case "location":
                return getLocation();
            case "facebook":
                return getFacebook();
            case "twitter":
                return getTwitter();
            case "google":
                return getGoogle();
            case "github":
                return getGithub();
            case "website":
                return getWebsite();
            default:
                return null;
        }
    }

    public String getPhotoUrl() {
        if (photoUrl.equals(""))
            photoUrl = Resources.getSystem().getString(R.string.placeholderImage);
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getGoogle() {
        return google;
    }

    public String getGithub() {
        return github;
    }

    public String getWebsite() {
        return website;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(userId);
        dest.writeString(photoUrl);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(facebook);
        dest.writeString(twitter);
        dest.writeString(google);
        dest.writeString(github);
        dest.writeString(website);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    private Person(Parcel in) {
        uuid = in.readString();
        userId = in.readString();
        photoUrl = in.readString();
        name = in.readString();
        location = in.readString();
        facebook = in.readString();
        twitter = in.readString();
        google = in.readString();
        github = in.readString();
        website = in.readString();
    }
}
