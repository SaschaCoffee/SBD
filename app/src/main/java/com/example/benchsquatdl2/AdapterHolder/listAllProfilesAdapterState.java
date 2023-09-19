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
import com.example.benchsquatdl2.model.modelApi.State.germanstate;
import com.example.benchsquatdl2.model.modelApi.State.statename;
import com.example.benchsquatdl2.retrofit.RetrofitService;
import com.example.benchsquatdl2.retrofit.UserApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class listAllProfilesAdapterState extends RecyclerView.Adapter<listAllProfilesViewHolder>{

    //STATES


    Response<List<germanstate>> response;

    Context mcontext;





    public listAllProfilesAdapterState(Response<List<germanstate>> response, Context context) {
        this.response = response;
        this.mcontext = context;
    }


    @NonNull
    @Override
    public listAllProfilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlistlayout, parent, false);
        return new listAllProfilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listAllProfilesViewHolder holder, int position) {
        String none = "None";

                holder.tvName.setText(response.body().get(position).getName());
                holder.tv_city.setText("(" + response.body().get(position).getMeettown() + ")");

                //----------------------BENCH---------------------------//

                if(!response.body().get(position).getBest3benchkg().isEmpty() && Float.parseFloat(response.body().get(position).getBest3benchkg()) > 0){

                    holder.tv_bench.setText(response.body().get(position).getBest3benchkg());

                }
                else{
                    holder.tv_bench.setText("N.a");
                }

                //----------------------DEADLIFT---------------------------//

                if(!response.body().get(position).getBest3deadliftkg().isEmpty() && Float.parseFloat(response.body().get(position).getBest3deadliftkg()) > 0){

                    holder.tv_deadlift.setText(response.body().get(position).getBest3deadliftkg());

                }
                else{
                    holder.tv_deadlift.setText("N.a");
                }

                //----------------------SQUAT---------------------------//

                if(!response.body().get(position).getBest3squatkg().isEmpty() && Float.parseFloat(response.body().get(position).getBest3squatkg()) > 0){

                    holder.tv_squat.setText(response.body().get(position).getBest3squatkg());

                }
                else{
                    holder.tv_squat.setText("N.a");
                }


        if(response.body().get(position).getInstagramde().isEmpty() || response.body().get(position).getInstagramde().equals(none) ){
            holder.img.setImageResource(R.drawable.instagram_grey);

        }
        else{
            holder.img.setImageResource(R.drawable.instagram);
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!response.body().get(position).getInstagramde().isEmpty() || response.body().get(position).getInstagramde().equals(none)) {

                    String instagramUrl = "https://www.instagram.com/" + response.body().get(position).getInstagramde();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                    mcontext.startActivity(browserIntent);
                }
                else{
                    Toast.makeText(mcontext, "No Instagram", Toast.LENGTH_SHORT).show();
                }
            }
        });

















        }





      /*  if(body.get(position).getInstagram().isEmpty()){
            holder.img.setImageResource(R.drawable.instagram_grey);

        }
        else{
            holder.img.setImageResource(R.drawable.instagram);
        }*/

       /* holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!body.get(position).getInstagram().isEmpty()) {
                    Log.d("instagramPosition", " " + position + "" + body.get(position).getInstagram());
                    String instagramUrl = "https://www.instagram.com/" + body.get(position).getInstagram();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                    mcontext.startActivity(browserIntent);
                }
                else{
                    Toast.makeText(mcontext, "No Instagram", Toast.LENGTH_SHORT).show();
                }
            }
        });*/







        /*holder.tv_bench.setText(String.valueOf(benchList.get(position)));
        holder.tv_squat.setText(String.valueOf(squatList.get(position)));
        holder.tv_deadlift.setText(String.valueOf(deadliftList.get(position)));
        holder.tvName.setText(String.valueOf(nameList.get(position)));
        holder.countryOrState.setText("("+"Meet Country: "+ String.valueOf(cityList.get(position))+")");*/










    @Override
    public int getItemCount() {
        return response.body().size();
    }
}
