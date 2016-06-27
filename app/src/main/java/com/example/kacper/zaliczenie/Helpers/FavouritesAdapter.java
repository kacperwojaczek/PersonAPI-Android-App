package com.example.kacper.zaliczenie.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kacper.zaliczenie.Models.Person;
import com.example.kacper.zaliczenie.Models.User;
import com.example.kacper.zaliczenie.R;
import com.squareup.picasso.Picasso;

import java.util.Vector;

/**
 * Created by Kacper on 19-Jun-16.
 */
public class FavouritesAdapter extends BaseAdapter {

    User user;
    private FavouritesDatabase database;
    private Context context;
    private Vector<Person> favourites;

    public FavouritesAdapter(Context context, User user) {
        this.user = user;
        this.context = context;
        this.database = new FavouritesDatabase(context);
        this.favourites = database.getPersons(user.getUuid());
    }

    @Override
    public int getItemViewType(int position) { return 1; }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return favourites.size();
    }

    public int getLayoutId(int position) {
        return R.layout.favourites_row;
    }

    @Override
    public Person getItem(int position) {
        return favourites.elementAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View favouriteView;

        if (convertView == null) {
            favouriteView = LayoutInflater.from(context).inflate(getLayoutId(position), parent, false);
        } else {
            favouriteView = convertView;
        }

        bindFavouriteToView(getItem(position), favouriteView);

        return favouriteView;
    }

    private void bindFavouriteToView(Person person, View favouriteView) {

        ImageView favouritePhoto = (ImageView) favouriteView.findViewById(R.id.favouritePhoto);
        Picasso.with(context).load(person.getPhotoUrl()).into(favouritePhoto);

        TextView favouriteName = (TextView) favouriteView.findViewById(R.id.favouriteName);
        if (favouriteName != null)
            favouriteName.setText(person.getName());
    }

}
