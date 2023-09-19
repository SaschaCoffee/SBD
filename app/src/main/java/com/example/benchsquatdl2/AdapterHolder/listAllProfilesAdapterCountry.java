package com.example.benchsquatdl2.AdapterHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.benchsquatdl2.R;
import com.example.benchsquatdl2.model.modelApi.Country.germancountry;
import com.example.benchsquatdl2.model.modelApi.Country.countryname;
import com.example.benchsquatdl2.retrofit.RetrofitService;
import com.example.benchsquatdl2.retrofit.UserApi;

import java.util.List;

import retrofit2.Response;

public class listAllProfilesAdapterCountry extends RecyclerView.Adapter<listAllProfilesViewHolder> {

    String country;
    Context mcontext;
    List<germancountry> response;


    public listAllProfilesAdapterCountry(List<germancountry> response, Context context) {
        this.mcontext = context;
        this.response = response;

    }


    @NonNull
    @Override
    public listAllProfilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlistlayout, parent, false);
        return new listAllProfilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listAllProfilesViewHolder holder, int position) {




                holder.tvName.setText(response.get(position).getName());
                holder.tv_city.setText("(" + response.get(position).getState() + ")");

                //----------------------BENCH---------------------------//

                if(!response.get(position).getBench().isEmpty() && Float.parseFloat(response.get(position).getBench()) > 0){

                    holder.tv_bench.setText(response.get(position).getBench());

                }
                else{
                    holder.tv_bench.setText("N.a");
                }

                //----------------------DEADLIFT---------------------------//

                if(!response.get(position).getDeadlift().isEmpty() && Float.parseFloat(response.get(position).getDeadlift()) > 0){

                    holder.tv_deadlift.setText(response.get(position).getDeadlift());

                }
                else{
                    holder.tv_deadlift.setText("N.a");
                }

                //----------------------SQUAT---------------------------//

                if(!response.get(position).getSquat().isEmpty() && Float.parseFloat(response.get(position).getSquat()) > 0){

                    holder.tv_squat.setText(response.get(position).getSquat());

                }
                else{
                    holder.tv_squat.setText("N.a");
                }

                //---------------INSTAGRAM-------------//


        String none = "None";

        if(response.get(position).getInstagram().equals(none) || response.get(position).getInstagram().isEmpty()){
            holder.img.setImageResource(R.drawable.instagram_grey);

        }
        else{
            holder.img.setImageResource(R.drawable.instagram);
        }

       holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!response.get(position).getInstagram().equals(none) || response.get(position).getInstagram().isEmpty()) {
                    Log.d("instagramPosition", " " + position + "" + response.get(position).getInstagram());
                    String instagramUrl = "https://www.instagram.com/" + response.get(position).getInstagram();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                    mcontext.startActivity(browserIntent);
                }
                else{
                    Toast.makeText(mcontext, "No Instagram", Toast.LENGTH_SHORT).show();
                }
            }
        });













    }

    @Override
    public int getItemCount() {
        return response.size();
    }



}
