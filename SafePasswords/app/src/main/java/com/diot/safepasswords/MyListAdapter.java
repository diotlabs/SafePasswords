package com.diot.safepasswords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<ListDataModal> {

    List<ListDataModal> datalist;
    Context context;
    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public MyListAdapter(Context context, int resource, List<ListDataModal> datalist) {
        super(context, resource, datalist);
        this.context = context;
        this.resource = resource;
        this.datalist = datalist;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        ImageView imageView = view.findViewById(R.id.imageView_list);
        TextView textViewName = view.findViewById(R.id.textView_title);
        TextView textViewWeb = view.findViewById(R.id.textView_website);

        //getting the hero of the specified position
        ListDataModal hero = datalist.get(position);

        //adding values to the list item
       // imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));
        textViewName.setText(hero.getTitle());
        textViewWeb.setText(hero.getWebsite());


        //
        //get first letter of each String item
        String firstLetter = String.valueOf(hero.getTitle().charAt(0));


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position));
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter.toUpperCase(), color); // radius in px

        imageView.setImageDrawable(drawable);

//


        //finally returning the view
        return view;
    }

    //this method will remove the item from the list
    private void removeItem(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item
                datalist.remove(position);

                //reloading the list
                notifyDataSetChanged();
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
