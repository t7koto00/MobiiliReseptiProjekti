package com.example.courseclash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class commentArrayAdapter extends ArrayAdapter<String> {

    public commentArrayAdapter(Context context, ArrayList<String> parts) {
        super(context,0, parts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            int layoutId = 0;
            layoutId = R.layout.comment_list_item;

            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }

        TextView partText = convertView.findViewById(R.id.commentView);
        partText.setText("testii");

        return convertView;
    }
}
