package com.example.ddm_firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TrainAdapter extends ArrayAdapter<Train> {
    private Context context;
    private ArrayList<Train> trains;

    public TrainAdapter(Context context, ArrayList<Train> trains) {
        super(context, R.layout.list_item_train, trains);
        this.context = context;
        this.trains = trains;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View itemView = li.inflate(R.layout.list_item_train, parent, false);

        TextView lblTrain = itemView.findViewById(R.id.lblTrain);
        TextView lblWeight = itemView.findViewById(R.id.lblWeight);
        TextView lblAge = itemView.findViewById(R.id.lblAge);
        TextView lblSex = itemView.findViewById(R.id.lblSex);
        TextView lblHeight = itemView.findViewById(R.id.lblHeight);
        TextView lblDay = itemView.findViewById(R.id.lblDay);

        lblTrain.setText(trains.get(position).getName());
        lblWeight.setText(String.valueOf(trains.get(position).getWeight()));
        lblAge.setText(String.valueOf(trains.get(position).getAge()));
        lblHeight.setText(String.valueOf(trains.get(position).getHeight()));
        lblSex.setText(trains.get(position).getSex());
        lblDay.setText(String.valueOf(trains.get(position).getDay()));

        return itemView;
    }
}
