package com.example.travelapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.travelapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SpinnerOptionsAdapter extends ArrayAdapter<OptionsItem> {

    public SpinnerOptionsAdapter(Context context, ArrayList<OptionsItem> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.options_spinner_row, parent, false);
        }

        ImageView imageViewOptions = convertView.findViewById(R.id.options_image);
        TextView textViewOptions = convertView.findViewById(R.id.options_names);

        OptionsItem currentItem = getItem(position);

        if (currentItem != null) {
            imageViewOptions.setImageResource(currentItem.getOptionImage());
            textViewOptions.setText(currentItem.getOptionsName());

        }
        return convertView;
    }
}
