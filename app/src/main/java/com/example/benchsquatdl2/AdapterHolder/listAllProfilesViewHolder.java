package com.example.benchsquatdl2.AdapterHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.benchsquatdl2.R;


public class listAllProfilesViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, countryOrState, tv_squat, tv_bench, tv_deadlift, tv_city;
    ImageView img2;

    ImageButton img;



    public listAllProfilesViewHolder(@NonNull View itemView) {
        super(itemView);

        //contactlistlayout.xml
        tvName = itemView.findViewById(R.id.contactName);
        countryOrState = itemView.findViewById(R.id.countryOrState);
        tv_squat = itemView.findViewById(R.id.tv_squat);
        tv_bench = itemView.findViewById(R.id.tv_bench);
        tv_deadlift = itemView.findViewById(R.id.tv_deadlift);
        img = itemView.findViewById(R.id.deleteContact);
        tv_city = itemView.findViewById(R.id.countryOrState);

    }


}
