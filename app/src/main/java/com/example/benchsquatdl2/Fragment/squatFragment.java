package com.example.benchsquatdl2.Fragment;

//Genaue erklärung: siehe benchfragment. Top header

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.benchsquatdl2.AdapterHolder.RepKgAdapter;
import com.example.benchsquatdl2.AdapterHolder.cardViewAdapter;
import com.example.benchsquatdl2.Events;
import com.example.benchsquatdl2.MyGridAdapter;
import com.example.benchsquatdl2.R;
import com.example.benchsquatdl2.dialogExtend;
import com.example.benchsquatdl2.model.RepKgModel;
import com.example.benchsquatdl2.model.commentModel;
import com.example.benchsquatdl2.model.greenCardModel;
import com.example.benchsquatdl2.model.modelApi.trainingdto;
import com.example.benchsquatdl2.model.modelApi.trainingsdaten;
import com.example.benchsquatdl2.model.modelSquat;
import com.example.benchsquatdl2.retrofit.RetrofitService;
import com.example.benchsquatdl2.retrofit.UserApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class squatFragment extends Fragment implements
        cardViewAdapter.PlayPauseClick  {

    ImageButton NextButton,PreviousButton;
    TextView CurrentDate;

    ImageView iv;
    MyGridAdapter myGridAdapter;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy", Locale.ENGLISH);


    ArrayList<greenCardModel> lstBook = new ArrayList<>();
    private FirebaseUser user;
    private DatabaseReference reference, referenceTraininglogPrivate, referenceTraininglogPublic;

    private RecyclerView mRecyclerView;
    private ProgressBar pgBar;
    private TextView tv;
    private EditText et_first_set, et_second_set, et_third_set, et_kg, et_kg2, et_kg3;
    int progr = 0;

    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    private Button btn_add_data,btn_delete_yes,btn_delete_item, btn_upload_data, buttonOpenDialog, btn_delete_data;
    private AlertDialog dialog;
    private int counter = 0;
    private double tempCounter;

    private android.app.AlertDialog dialogCalendar;
    int count = 0;
    RepKgModel RepKgModel;
    int arraySizeContactAdapter;

    ArrayList<RepKgAdapter> save = new ArrayList<>();
    AlertDialog.Builder builder;

    String adapterCallBackString = "l1_reps";
    int counterNew = 0;
    int progrrr = 0;

    private LinearLayout linearLayout;

    RetrofitService retrofitService = new RetrofitService();
    UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View vx = inflater.inflate(R.layout.activitycard_squat, container, false);
        NextButton = vx.findViewById(R.id.nextBtn);
        PreviousButton = vx.findViewById(R.id.previousBtn2);
        CurrentDate = vx.findViewById(R.id.current_date);
        gridView = vx.findViewById(R.id.gridview);


        iv = vx.findViewById(R.id.iv_squat);
        linearLayout = vx.findViewById(R.id.calendarLinearLayout);


        SetUpCalendar();

        buttonOpenDialog = vx.findViewById(R.id.btn_add_data_card_squat);

        btn_delete_item = vx.findViewById(R.id.btn_delete_item_squat);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                builder = new AlertDialog.Builder(getActivity());
                View vieww = getLayoutInflater().inflate(R.layout.open_training_data, null);
                builder.setView(vieww);
                dialog = builder.create();
                dialog.show();
                dialog.create();

                Date monthDate = dates.get(position);
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(monthDate);
                Date xy = dateCalendar.getTime();
                String format = new SimpleDateFormat("dd-MM-yyy").format(xy);
                String neuFormat = format;



                String bench = "bench";


                trainingsdaten trainingsdaten = new trainingsdaten("bench",
                        neuFormat,"2",null,null,null,null,
                        neuFormat,null,null,null,null);

                TextView rep1 = vieww.findViewById(R.id.tv_reps_opendialog);
                TextView rep2 = vieww.findViewById(R.id.tv_reps_opendialog2);
                TextView rep3 = vieww.findViewById(R.id.tv_reps_opendialog3);
                TextView rep4 = vieww.findViewById(R.id.tv_reps_opendialog4);
                TextView rep5 = vieww.findViewById(R.id.tv_reps_opendialog5);

// KG STARTS HERE, BUT I NAMED IT TO REPS FOR THE LOOP IN THE NEXT PARAGRAPH
                TextView kg1 = vieww.findViewById(R.id.tv_kg_opendialog);
                TextView kg2 = vieww.findViewById(R.id.tv_kg_opendialog2);
                TextView kg3 = vieww.findViewById(R.id.tv_kg_opendialog3);
                TextView kg4 = vieww.findViewById(R.id.tv_kg_opendialog4);
                TextView kg5 = vieww.findViewById(R.id.tv_kg_opendialog5);

                TextView date = vieww.findViewById(R.id.tv_date);


                userApi.testemich(trainingsdaten).enqueue(new Callback<com.example.benchsquatdl2.model.modelApi.trainingsdaten>() {
                    @Override
                    public void onResponse(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Response<com.example.benchsquatdl2.model.modelApi.trainingsdaten> response) {

                        try {

                            date.setText(response.body().getDate());
                            rep1.setText(response.body().getRep1());
                            rep2.setText(response.body().getRep2());
                            rep3.setText(response.body().getRep3());
                            rep4.setText(response.body().getRep4());
                            rep5.setText(response.body().getRep5());

                            kg1.setText(response.body().getKg1());
                            kg2.setText(response.body().getKg2());
                            kg3.setText(response.body().getKg3());
                            kg4.setText(response.body().getKg4());
                            kg5.setText(response.body().getKg5());

                            Log.d("gettheid","" + response.body().getId());




                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void onFailure(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Throwable t) {

                    }
                });




            }
        });


        btn_delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getActivity());
                View vieww = getLayoutInflater().inflate(R.layout.delete_areyousure, null);
                btn_delete_yes = vieww.findViewById(R.id.btn_delete_yes);
                builder.setView(vieww);

                dialog = builder.create();
                dialog.show();
                dialog.create();

                btn_delete_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        deleteLastItem();
                        dialog.cancel();

                    }
                });



            }
        });


        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                for (int i = count;i>0;i--){
                    count--;

                }

                counter = 1;
                counterNew = 1;

                //OPEN DIALOG
                builder = new AlertDialog.Builder(getActivity());
                dialogExtend xx = new dialogExtend();
                builder.setTitle("Enter Data");

                //FIRST RUN, COUNT == 1, ARRAY SIZE IN CONTACTADAPTER NULL
                View vieww = getLayoutInflater().inflate(R.layout.custom_dialog_list_recyclerview, null);
                RecyclerView contactView = vieww.findViewById(R.id.rv_customdialog);

                Context emptyContext;

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

                contactView.setLayoutManager(linearLayoutManager);
                contactView.setHasFixedSize(true);

                DialogInterface.OnKeyListener xxy = new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        String x = "1";

                        EditText l1_reps, l1_kg, l2_reps_one, l2_reps_two,
                                l2_kg_one, l2_kg_two, l3_heavy_button_one,
                                l3_heavy_button_two, l3_heavy_button_three,
                                l3_pencil, l3_pencil_two, l3_pencil_three, l3_reps,
                                l3_reps_two, l3_reps_three, l3_kg_one, l3_kg_two,
                                l3_kg_three, l4_reps, l4_reps_two, l4_reps_three,
                                l4_reps_four, l4_kg_one, l4_kg_two, l4_kg_three,
                                l4_kg_four,l5_reps, l5_reps_two, l5_reps_three,
                                l5_reps_four, l5_reps_five, l5_kg_one, l5_kg_two, l5_kg_three,
                                l5_kg_four, l5_kg_five;


                        l1_reps = vieww.findViewById(R.id.layer_one_reps);
                        l1_kg = vieww.findViewById(R.id.layer_one_kg);

                        //Layer Two
                        l2_reps_one = vieww.findViewById(R.id.layer_two_et_symbol_reps_single_item);
                        l2_reps_two = vieww.findViewById(R.id.layer_two_et_symbol_reps_single_item_two);
                        l2_kg_one = vieww.findViewById(R.id.layer_two_et_kg_card_single_item);
                        l2_kg_two = vieww.findViewById(R.id.layer_two_et_kg_card_single_item_two);

                        //Layer Three
                        l3_reps = vieww.findViewById(R.id.layer_three_et_symbol_reps_single_item);
                        l3_reps_two = vieww.findViewById(R.id.layer_three_et_symbol_reps_single_item_two);
                        l3_reps_three = vieww.findViewById(R.id.layer_three_et_symbol_reps_single_item_two_three);

                        l3_kg_one = vieww.findViewById(R.id.layer_three_et_kg_card_single_item);
                        l3_kg_two = vieww.findViewById(R.id.layer_three_et_kg_card_single_item_two);
                        l3_kg_three = vieww.findViewById(R.id.layer_three_et_kg_card_single_item_three);

                        //Layer Four
                        l4_reps = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item);
                        l4_reps_two = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item_two);
                        l4_reps_three = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item_three);
                        l4_reps_four = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item_four);

                        l4_kg_one = vieww.findViewById(R.id.layer_four_et_kg_card_single_item);
                        l4_kg_two = vieww.findViewById(R.id.layer_four_et_kg_card_single_item_two);
                        l4_kg_three = vieww.findViewById(R.id.layer_four_et_kg_card_single_item_three);
                        l4_kg_four =  vieww.findViewById(R.id.layer_four_et_kg_card_single_item_four);


                        //Layer five
                        l5_reps = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item);
                        l5_reps_two = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_two);
                        l5_reps_three = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_three);
                        l5_reps_four = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_four);
                        l5_reps_five = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_five);

                        l5_kg_one = vieww.findViewById(R.id.layer_five_et_kg_card_single_item);
                        l5_kg_two = vieww.findViewById(R.id.layer_five_et_kg_card_single_item_two);
                        l5_kg_three = vieww.findViewById(R.id.layer_five_et_kg_card_single_item_three);
                        l5_kg_four =  vieww.findViewById(R.id.layer_five_et_kg_card_single_item_four);
                        l5_kg_five = vieww.findViewById(R.id.layer_five_et_kg_card_single_item_five);



                        int code = keyEvent.getKeyCode();



                        if(keyEvent.getAction() == KeyEvent.ACTION_UP && !adapterCallBackString.isEmpty() ){
                            switch (code) {
                                case KeyEvent.KEYCODE_VOLUME_UP:

                                    tempCounter = counterNew;

                                    switch(adapterCallBackString){
                                        case "l1_reps":
                                            int text_rep;
                                            if(l1_reps.getText().toString().isEmpty() || l1_reps.getText().toString().trim().equals("0")){
                                                text_rep = 0;
                                            }
                                            else{
                                                text_rep  = Integer.parseInt(l1_reps.getText().toString().trim());
                                            }
                                            l1_reps.setText(String.valueOf(3+(text_rep)));
                                            break;
                                        case "l1_kg":

                                            int text_kg;
                                            if(l1_kg.getText().toString().isEmpty() || l1_kg.getText().toString().trim().equals("0")){
                                                text_kg = 0;
                                            }
                                            else{
                                                text_kg  = Integer.parseInt(l1_kg.getText().toString().trim());
                                            }

                                            l1_kg.setText(String.valueOf(20+(text_kg)));

                                            break;
                                        case "l2_reps_one":
                                            int txt_rep_l2 = Integer.parseInt(l2_reps_one.getText().toString().trim());

                                            l2_reps_one.setText(String.valueOf(3 + txt_rep_l2));
                                            break;
                                        case "l2_reps_two":
                                            int txt_rep2_l2 = Integer.parseInt(l2_reps_two.getText().toString().trim());
                                            l2_reps_two.setText(String.valueOf(3 + txt_rep2_l2));
                                            break;
                                        case "l2_kg_one":
                                            int txt_kg_l2 = Integer.parseInt(l2_kg_one.getText().toString().trim());
                                            l2_kg_one.setText(String.valueOf(5 + txt_kg_l2));
                                            break;
                                        case "l2_kg_two":
                                            int txt_kg2_l2 = Integer.parseInt(l2_kg_two.getText().toString().trim());
                                            l2_kg_two.setText(String.valueOf(5 + txt_kg2_l2));
                                            break;


                                        case "l3_kg_one":
                                            int txt_kg_l3 = Integer.parseInt(l3_kg_one.getText().toString().trim());
                                            l3_kg_one.setText(String.valueOf(5 + txt_kg_l3));
                                            break;
                                        case "l3_kg_two":
                                            int txt_kg2_l3 = Integer.parseInt(l3_kg_two.getText().toString().trim());
                                            l3_kg_two.setText(String.valueOf(5 + txt_kg2_l3));
                                            break;
                                        case "l3_kg_three":
                                            int txt_kg3_l3 = Integer.parseInt(l3_kg_three.getText().toString().trim());
                                            l3_kg_three.setText(String.valueOf(5 + txt_kg3_l3));
                                            break;

                                        case "l3_reps":
                                            int txt_rep_l3 = Integer.parseInt(l3_reps.getText().toString().trim());
                                            l3_reps.setText(String.valueOf(3 + txt_rep_l3));
                                            break;
                                        case "l3_reps_two":
                                            int txt_rep2_l3 = Integer.parseInt(l3_reps_two.getText().toString().trim());
                                            l3_reps_two.setText(String.valueOf(txt_rep2_l3 + 3));
                                            break;
                                        case "l3_reps_three":
                                            int txt_rep3_l3 = Integer.parseInt(l3_reps_three.getText().toString().trim());
                                            l3_reps_three.setText(String.valueOf(3 + txt_rep3_l3));
                                            break;

                                        case "l4_kg_one":
                                            int txt_kg_l4 = Integer.parseInt(l4_kg_one.getText().toString().trim());
                                            l4_kg_one.setText(String.valueOf(5 + txt_kg_l4));
                                            break;
                                        case "l4_kg_two":
                                            int txt_kg2_l4 = Integer.parseInt(l4_kg_two.getText().toString().trim());
                                            l4_kg_two.setText(String.valueOf(5 + txt_kg2_l4));
                                            break;
                                        case "l4_kg_three":
                                            int txt_kg3_l4 = Integer.parseInt(l4_kg_three.getText().toString().trim());
                                            l4_kg_three.setText(String.valueOf(5 + txt_kg3_l4));
                                            break;
                                        case "l4_kg_four":
                                            int txt_kg4_l4 = Integer.parseInt(l4_kg_four.getText().toString().trim());
                                            l4_kg_four.setText(String.valueOf(5 + txt_kg4_l4));
                                            break;

                                        case "l4_reps":
                                            int txt_rep_l4 = Integer.parseInt(l4_reps.getText().toString().trim());
                                            l4_reps.setText(String.valueOf(3 + txt_rep_l4));
                                            break;
                                        case "l4_reps_two":
                                            int txt_rep2_l4 = Integer.parseInt(l4_reps_two.getText().toString().trim());
                                            l4_reps_two.setText(String.valueOf(txt_rep2_l4 + 3));
                                            break;
                                        case "l4_reps_three":
                                            int txt_rep3_l4 = Integer.parseInt(l4_reps_three.getText().toString().trim());
                                            l4_reps_three.setText(String.valueOf(txt_rep3_l4 + 3));
                                            break;
                                        case "l4_reps_four":
                                            int txt_rep4_l4 = Integer.parseInt(l4_reps_four.getText().toString().trim());
                                            l4_reps_four.setText(String.valueOf(txt_rep4_l4 + 3));
                                            break;

                                        case "l5_reps":
                                            int txt_rep_l5 = Integer.parseInt(l5_reps.getText().toString().trim());
                                            l5_reps.setText(String.valueOf(3 + txt_rep_l5));
                                            break;
                                        case "l5_reps_two":
                                            int txt_rep2_l5 = Integer.parseInt(l5_reps_two.getText().toString().trim());
                                            l5_reps_two.setText(String.valueOf(3 + txt_rep2_l5));
                                            break;
                                        case "l5_reps_three":
                                            int txt_rep3_l5 = Integer.parseInt(l5_reps_three.getText().toString().trim());
                                            l5_reps_three.setText(String.valueOf(3 + txt_rep3_l5));
                                            break;
                                        case "l5_reps_four":
                                            int txt_rep4_l5 = Integer.parseInt(l5_reps_four.getText().toString().trim());
                                            l5_reps_four.setText(String.valueOf(3 + txt_rep4_l5));
                                            break;
                                        case "l5_reps_five":
                                            int txt_rep5_l5 = Integer.parseInt(l5_reps_five.getText().toString().trim());
                                            l5_reps_five.setText(String.valueOf(3 + txt_rep5_l5));
                                            break;

                                        case "l5_kg_one":
                                            int txt_kg_l5 = Integer.parseInt(l5_kg_one.getText().toString().trim());
                                            l5_kg_one.setText(String.valueOf(5 + txt_kg_l5));
                                            break;
                                        case "l5_kg_two":
                                            int txt_kg2_l5 = Integer.parseInt(l5_kg_two.getText().toString().trim());
                                            l5_kg_two.setText(String.valueOf(5 + txt_kg2_l5));
                                            break;
                                        case "l5_kg_three":
                                            int txt_kg3_l5 = Integer.parseInt(l5_kg_three.getText().toString().trim());
                                            l5_kg_three.setText(String.valueOf(5 + txt_kg3_l5));
                                            break;
                                        case "l5_kg_four":
                                            int txt_kg4_l5 = Integer.parseInt(l5_kg_four.getText().toString().trim());
                                            l5_kg_four.setText(String.valueOf(5 +txt_kg4_l5));
                                            break;
                                        case "l5_kg_five":
                                            int txt_kg5_l5 = Integer.parseInt(l5_kg_five.getText().toString().trim());
                                            l5_kg_five.setText(String.valueOf(5 + txt_kg5_l5));
                                            break;
                                    }
                                    return true;
                            }

                        }


                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && !adapterCallBackString.isEmpty()) {
                            switch (code) {

                                case KeyEvent.KEYCODE_VOLUME_DOWN:
                                    double counterNewDown = tempCounter - 1.25;
                                    tempCounter = counterNewDown;

                                    switch (adapterCallBackString) {
                                        case "l1_reps":
                                            int l1_reps_down;

                                            if(l1_reps.getText().toString().isEmpty() || l1_reps.getText().toString().trim().equals("0")){
                                                l1_reps_down = 1;
                                            }
                                            else{
                                                l1_reps_down = Integer.parseInt(l1_reps.getText().toString());
                                            }

                                            l1_reps.setText(String.valueOf(l1_reps_down - 1));
                                            break;
                                        case "l1_kg":
                                            int l1_kg_down;

                                            if(l1_kg.getText().toString().isEmpty() || l1_kg.getText().toString().trim().equals("0")){
                                                l1_kg_down = 2;
                                            }else{
                                                l1_kg_down = Integer.parseInt(l1_kg.getText().toString());
                                            }

                                            l1_kg.setText(String.valueOf(l1_kg_down - 2));
                                            break;

                                        case "l2_reps_two":
                                            int txt_rep2_l2;

                                            if(l2_reps_two.getText().toString().trim().equals("0") || l2_reps_two.getText().toString().isEmpty()){
                                                txt_rep2_l2 = 1;
                                            }
                                            else {
                                                boolean test = l2_reps_two.getText().equals("0");

                                                txt_rep2_l2 = Integer.parseInt(l2_reps_two.getText().toString().trim());
                                            }
                                            l2_reps_two.setText(String.valueOf(txt_rep2_l2 -1));
                                            break;

                                        case "l2_kg_two":
                                            int txt_kg_l2;

                                            if(l2_kg_two.getText().toString().trim().equals("0") || l2_kg_two.getText().toString().isEmpty()){
                                                txt_kg_l2 = 2;
                                            }
                                            else {
                                                txt_kg_l2 = Integer.parseInt(l2_kg_two.getText().toString().trim());
                                            }

                                            l2_kg_two.setText(String.valueOf(txt_kg_l2 - 2));
                                            break;


                                        case "l3_kg_one":

                                            int txt_kg_l3;

                                            if(l2_kg_two.getText().toString().trim().equals("0") || l2_kg_two.getText().toString().isEmpty()){
                                                txt_kg_l3 = 2;
                                            }
                                            else {
                                                txt_kg_l3 = Integer.parseInt(l3_kg_one.getText().toString().trim());
                                            }

                                            l3_kg_one.setText(String.valueOf(txt_kg_l3 - 2));

                                            break;

                                        case "l3_kg_two":

                                            int txt_kg2_l3;

                                            if(l3_kg_two.getText().toString().trim().equals("0") || l3_kg_two.getText().toString().isEmpty()){
                                                txt_kg2_l3 = 2;
                                            }
                                            else {
                                                txt_kg2_l3 = Integer.parseInt(l3_kg_two.getText().toString().trim());
                                            }

                                            l3_kg_two.setText(String.valueOf(txt_kg2_l3 - 2));

                                            break;
                                        case "l3_kg_three":

                                            int txt_kg3_l3;

                                            if(l3_kg_three.getText().toString().trim().equals("0") || l3_kg_three.getText().toString().isEmpty()){
                                                txt_kg3_l3 = 2;
                                            }
                                            else {
                                                txt_kg3_l3 = Integer.parseInt(l3_kg_three.getText().toString().trim());
                                            }

                                            l3_kg_three.setText(String.valueOf(txt_kg3_l3 - 2));

                                            break;

                                        case "l3_reps":
                                            int txt_rep_l3;

                                            if(l3_reps.getText().toString().trim().equals("0") || l3_reps.getText().toString().isEmpty()){
                                                txt_rep_l3 = 1;
                                            }
                                            else {
                                                txt_rep_l3 = Integer.parseInt(l3_reps.getText().toString().trim());
                                            }

                                            l3_reps.setText(String.valueOf(txt_rep_l3 - 1));
                                            break;
                                        case "l3_reps_two":

                                            int txt_rep2_l3;

                                            if(l3_reps_two.getText().toString().trim().equals("0") || l3_reps_two.getText().toString().isEmpty()){
                                                txt_rep2_l3 = 1;
                                            }
                                            else {
                                                txt_rep2_l3 = Integer.parseInt(l3_reps_two.getText().toString().trim());
                                            }

                                            l3_reps_two.setText(String.valueOf(txt_rep2_l3 - 1));

                                            break;
                                        case "l3_reps_three":

                                            int txt_rep3_l3;

                                            if(l3_reps_three.getText().toString().trim().equals("0") || l3_reps_three.getText().toString().isEmpty()){
                                                txt_rep3_l3 = 1;
                                            }
                                            else {
                                                txt_rep3_l3 = Integer.parseInt(l3_reps_three.getText().toString().trim());
                                            }

                                            l3_reps_three.setText(String.valueOf(txt_rep3_l3 - 1));
                                            break;

                                        case "l4_kg_one":
                                            int txt_kg_l4;

                                            if(l4_kg_one.getText().toString().trim().equals("0") || l4_kg_one.getText().toString().isEmpty()){
                                                txt_kg_l4 = 2;
                                            }
                                            else {
                                                txt_kg_l4 = Integer.parseInt(l4_kg_one.getText().toString().trim());
                                            }

                                            l4_kg_one.setText(String.valueOf(txt_kg_l4 - 2));
                                            break;
                                        case "l4_kg_two":
                                            int txt_kg2_l4;

                                            if(l4_kg_two.getText().toString().trim().equals("0") || l4_kg_two.getText().toString().isEmpty()){
                                                txt_kg2_l4 = 2;
                                            }
                                            else {
                                                txt_kg2_l4 = Integer.parseInt(l4_kg_two.getText().toString().trim());
                                            }

                                            l4_kg_two.setText(String.valueOf(txt_kg2_l4 - 2));

                                            break;
                                        case "l4_kg_three":

                                            int txt_kg3_l4;

                                            if(l4_kg_three.getText().toString().trim().equals("0") || l4_kg_three.getText().toString().isEmpty()){
                                                txt_kg3_l4 = 2;
                                            }
                                            else {
                                                txt_kg3_l4 = Integer.parseInt(l4_kg_three.getText().toString().trim());
                                            }

                                            l4_kg_three.setText(String.valueOf(txt_kg3_l4 - 2));

                                            break;
                                        case "l4_kg_four":

                                            int txt_kg4_l4;

                                            if(l4_kg_four.getText().toString().trim().equals("0") || l4_kg_four.getText().toString().isEmpty()){
                                                txt_kg4_l4 = 2;
                                            }
                                            else {
                                                txt_kg4_l4 = Integer.parseInt(l4_kg_four.getText().toString().trim());
                                            }

                                            l4_kg_four.setText(String.valueOf(txt_kg4_l4 - 2));
                                            break;

                                        case "l4_reps":
                                            int txt_reps_l4;

                                            if(l4_reps.getText().toString().trim().equals("0") || l4_reps.getText().toString().isEmpty()){
                                                txt_reps_l4 = 1;
                                            }
                                            else {
                                                txt_reps_l4 = Integer.parseInt(l4_reps.getText().toString().trim());
                                            }

                                            l4_reps.setText(String.valueOf(txt_reps_l4 - 1));

                                            break;
                                        case "l4_reps_two":

                                            int txt_reps2_l4;

                                            if(l4_reps_two.getText().toString().trim().equals("0") || l4_reps_two.getText().toString().isEmpty()){
                                                txt_reps2_l4 = 1;
                                            }
                                            else {
                                                txt_reps2_l4 = Integer.parseInt(l4_reps_two.getText().toString().trim());
                                            }

                                            l4_reps.setText(String.valueOf(txt_reps2_l4 - 1));

                                            l4_reps_two.setText(String.valueOf(txt_reps2_l4 - 1));
                                            break;
                                        case "l4_reps_three":
                                            int txt_reps3_l4;

                                            if(l4_reps_three.getText().toString().trim().equals("0") || l4_reps_three.getText().toString().isEmpty()){
                                                txt_reps3_l4 = 1;
                                            }
                                            else {
                                                txt_reps3_l4 = Integer.parseInt(l4_reps_three.getText().toString().trim());
                                            }
                                            l4_reps_three.setText(String.valueOf(txt_reps3_l4 - 1));
                                            break;
                                        case "l4_reps_four":
                                            int txt_reps4_l4;

                                            if(l4_reps_four.getText().toString().trim().equals("0") || l4_reps_four.getText().toString().isEmpty()){
                                                txt_reps4_l4 = 1;
                                            }
                                            else {
                                                txt_reps4_l4 = Integer.parseInt(l4_reps_four.getText().toString().trim());
                                            }

                                            l4_reps_four.setText(String.valueOf(txt_reps4_l4 - 1));

                                            break;

                                        case "l5_reps":
                                            int txt_reps_l5;

                                            if(l5_reps.getText().toString().trim().equals("0") || l5_reps.getText().toString().isEmpty()){
                                                txt_reps_l5 = 1;
                                            }
                                            else {
                                                txt_reps_l5 = Integer.parseInt(l5_reps.getText().toString().trim());
                                            }

                                            l5_reps.setText(String.valueOf(txt_reps_l5 - 1));

                                            break;
                                        case "l5_reps_two":
                                            int txt_rep2_l5;

                                            if(l5_reps_two.getText().toString().trim().equals("0") || l5_reps_two.getText().toString().isEmpty()){
                                                txt_rep2_l5 = 1;
                                            }
                                            else {
                                                txt_rep2_l5 = Integer.parseInt(l5_reps_two.getText().toString().trim());
                                            }

                                            l5_reps_two.setText(String.valueOf(txt_rep2_l5 - 1));

                                            break;
                                        case "l5_reps_three":
                                            int txt_rep3_l5;

                                            if(l5_reps_three.getText().toString().trim().equals("0") || l5_reps_three.getText().toString().isEmpty()){
                                                txt_rep3_l5 = 1;
                                            }
                                            else {
                                                txt_rep3_l5 = Integer.parseInt(l5_reps_three.getText().toString().trim());
                                            }

                                            l5_reps_three.setText(String.valueOf(txt_rep3_l5 - 1));
                                            break;

                                        case "l5_reps_four":
                                            int txt_rep4_l5;

                                            if(l5_reps_four.getText().toString().trim().equals("0") || l5_reps_four.getText().toString().isEmpty()){
                                                txt_rep4_l5 = 1;
                                            }
                                            else {
                                                txt_rep4_l5 = Integer.parseInt(l5_reps_four.getText().toString().trim());
                                            }
                                            l5_reps_four.setText(String.valueOf(txt_rep4_l5 - 1));
                                            break;
                                        case "l5_reps_five":
                                            int txt_rep5_l5;

                                            if(l5_reps_five.getText().toString().trim().equals("0") || l5_reps_five.getText().toString().isEmpty()){
                                                txt_rep5_l5 = 1;
                                            }
                                            else {
                                                txt_rep5_l5 = Integer.parseInt(l5_reps_five.getText().toString().trim());
                                            }

                                            l5_reps_five.setText(String.valueOf(txt_rep5_l5 - 1));
                                            break;

                                        case "l5_kg_one":
                                            int txt_kg_l5;

                                            if(l5_kg_one.getText().toString().trim().equals("0") || l5_kg_one.getText().toString().isEmpty()){
                                                txt_kg_l5 = 2;
                                            }
                                            else {
                                                txt_kg_l5 = Integer.parseInt(l5_kg_one.getText().toString().trim());
                                            }

                                            l5_kg_one.setText(String.valueOf(txt_kg_l5 - 2));

                                            break;
                                        case "l5_kg_two":
                                            int txt_kg2_l5;

                                            if(l5_kg_two.getText().toString().trim().equals("0") || l5_kg_two.getText().toString().isEmpty()){
                                                txt_kg2_l5 = 2;
                                            }
                                            else {
                                                txt_kg2_l5 = Integer.parseInt(l5_kg_two.getText().toString().trim());
                                            }

                                            l5_kg_two.setText(String.valueOf(txt_kg2_l5 - 2));
                                            break;

                                        case "l5_kg_three":
                                            int txt_kg3_l5;

                                            if(l5_kg_three.getText().toString().trim().equals("0") || l5_kg_three.getText().toString().isEmpty()){
                                                txt_kg3_l5 = 2;
                                            }
                                            else {
                                                txt_kg3_l5 = Integer.parseInt(l5_kg_three.getText().toString().trim());
                                            }

                                            l5_kg_three.setText(String.valueOf(txt_kg3_l5 - 2));

                                            break;
                                        case "l5_kg_four":
                                            int txt_kg4_l5;

                                            if(l5_kg_four.getText().toString().trim().equals("0") || l5_kg_four.getText().toString().isEmpty()){
                                                txt_kg4_l5 = 2;
                                            }
                                            else {
                                                txt_kg4_l5 = Integer.parseInt(l5_kg_four.getText().toString().trim());
                                            }

                                            l5_kg_four.setText(String.valueOf(txt_kg4_l5 - 2));

                                            break;
                                        case "l5_kg_five":

                                            int txt_kg5_l5;

                                            if(l5_kg_five.getText().toString().trim().equals("0") || l5_kg_five.getText().toString().isEmpty()){
                                                txt_kg5_l5 = 2;
                                            }
                                            else {
                                                txt_kg5_l5 = Integer.parseInt(l5_kg_five.getText().toString().trim());
                                            }

                                            l5_kg_five.setText(String.valueOf(txt_kg5_l5 - 2));

                                            break;

                                    }
                                    return true;
                            }
                        }


                        {


                        }

                        return false;
                    }
                };

                builder.setOnKeyListener(xxy);


                builder.setView(vieww);

                btn_add_data = vieww.findViewById(R.id.btn_add_rv);
                btn_upload_data = vieww.findViewById(R.id.upload_button);
                btn_delete_data = vieww.findViewById(R.id.delete_btn_rv);
                et_first_set = vieww.findViewById(R.id.layer_one_reps);
                et_kg = vieww.findViewById(R.id.layer_one_kg);
                dialog = builder.create();
                dialog.show();
                dialog.create();


                contactView.setVisibility(View.VISIBLE);
                RepKgAdapter mAdapter = new RepKgAdapter(getActivity(), 1, dialog, RepKgModel);

                //IMPORT FUNCTION. WITHOUT IT -> Save will jump to 2,3,4 (SIZE)
                boolean x = dialog.isShowing();
                if(x){
                    if(save.size() > 0) {
                        save.remove(0);
                    }
                }
                save.add(0,mAdapter);
                contactView.setAdapter(save.get(0));
                mAdapter.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);

                RetrofitService retrofitService = new RetrofitService();
                UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

                Calendar dateCalendar = Calendar.getInstance();
                Date xy = dateCalendar.getTime();
                String format = new SimpleDateFormat("dd-MM-yyy").format(xy);


                btn_upload_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DateFormat df = new SimpleDateFormat("d MMM yyyy");
                        String date = df.format(Calendar.getInstance().getTime());
                        String dummyTxt = "Add a comment";

                        switch(save.size()){

                            case 1:
                                EditText first_kg = vieww.findViewById(R.id.layer_one_kg);
                                EditText first_rep = vieww.findViewById(R.id.layer_one_reps);

                                String first_kg_string = first_kg.getText().toString().trim();
                                String first_rep_string = first_rep.getText().toString().trim();

                                if(!first_kg_string.isEmpty() && !first_rep_string.isEmpty()) {


                                    trainingsdaten trainingsdaten = new trainingsdaten("squat",
                                            format,first_rep_string,null,null,null,null,
                                            first_kg_string,null,null,null,null);

                                    userApi.savedata(trainingsdaten).enqueue(new Callback<com.example.benchsquatdl2.model.modelApi.trainingsdaten>() {
                                        @Override
                                        public void onResponse(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Response<com.example.benchsquatdl2.model.modelApi.trainingsdaten> response) {

                                            refreshCalendar();

                                        }

                                        @Override
                                        public void onFailure(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Throwable t) {

                                        }
                                    });




                                    progr++;





                                    dialog.cancel();
                                    //RESET
                                    count = 0;

                                    counter = 0;
                                    counterNew = 0;

                                    save.remove(0);
                                    adapterCallBackString = "l1_reps";

                                }
                                else{
                                    Toast.makeText(dialog.getContext(), "Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show();

                                }
                                break;

                            case 2:
                                EditText first_kg_layer2 = vieww.findViewById(R.id.layer_two_et_kg_card_single_item);
                                EditText first_rep_layer2 = vieww.findViewById(R.id.layer_two_et_symbol_reps_single_item);
                                EditText second_kg_layer2 = vieww.findViewById(R.id.layer_two_et_kg_card_single_item_two);
                                EditText second_rep_layer2 = vieww.findViewById(R.id.layer_two_et_symbol_reps_single_item_two);

                                String first_kg_l2 = first_kg_layer2.getText().toString().trim();
                                String second_kg_l2 = second_kg_layer2.getText().toString().trim();
                                String first_rep_l2 = first_rep_layer2.getText().toString().trim();
                                String second_rep_l2 = second_rep_layer2.getText().toString().trim();

                                if(!first_kg_l2.isEmpty() && !second_kg_l2.isEmpty() && !first_rep_l2.isEmpty() &&
                                        !second_rep_l2.isEmpty()) {

                                    trainingsdaten trainingsdaten = new trainingsdaten("squat",
                                            format,first_rep_l2,second_rep_l2,null,null,null,
                                            first_kg_l2,second_kg_l2,null,null,null);

                                    userApi.savedata(trainingsdaten).enqueue(new Callback<com.example.benchsquatdl2.model.modelApi.trainingsdaten>() {
                                        @Override
                                        public void onResponse(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Response<com.example.benchsquatdl2.model.modelApi.trainingsdaten> response) {
                                            refreshCalendar();
                                        }

                                        @Override
                                        public void onFailure(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Throwable t) {

                                        }
                                    });


                                    progr++;

                                    updateProgressBar(progr);



                                    dialog.cancel();
                                    //RESET
                                    count = 0;
                                    save.clear();


                                    adapterCallBackString = "l1_reps";
                                    counterNew = 0;
                                    counter = 0;



                                }
                                else{
                                    Toast.makeText(getActivity(), "Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 3:
                                EditText first_kg_layer3 = vieww.findViewById(R.id.layer_three_et_kg_card_single_item);
                                EditText second_kg_layer3 = vieww.findViewById(R.id.layer_three_et_kg_card_single_item_two);
                                EditText third_kg_layer3 = vieww.findViewById(R.id.layer_three_et_kg_card_single_item_three);

                                EditText first_rep_layer3 = vieww.findViewById(R.id.layer_three_et_symbol_reps_single_item);
                                EditText second_rep_layer3 = vieww.findViewById(R.id.layer_three_et_symbol_reps_single_item_two);
                                EditText third_rep_layer3 = vieww.findViewById(R.id.layer_three_et_symbol_reps_single_item_two_three);

                                String first_kg_l3 = first_kg_layer3.getText().toString().trim();
                                String second_kg_l3 = second_kg_layer3.getText().toString().trim();
                                String third_kg_l3 = third_kg_layer3.getText().toString().trim();

                                String first_rep_l3 = first_rep_layer3.getText().toString().trim();
                                String second_rep_l3 = second_rep_layer3.getText().toString().trim();
                                String third_rep_l3 = third_rep_layer3.getText().toString().trim();

                                if(!first_rep_l3.isEmpty() && !second_rep_l3.isEmpty() &&
                                        !third_rep_l3.isEmpty() && !first_kg_l3.isEmpty() && !second_kg_l3.isEmpty() && !third_kg_l3.isEmpty()){

                                    trainingsdaten trainingsdaten = new trainingsdaten("squat",
                                            format,first_rep_l3,second_rep_l3,third_rep_l3,null,null,
                                            first_kg_l3,second_kg_l3,third_kg_l3,null,null);

                                    userApi.savedata(trainingsdaten).enqueue(new Callback<com.example.benchsquatdl2.model.modelApi.trainingsdaten>() {
                                        @Override
                                        public void onResponse(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Response<com.example.benchsquatdl2.model.modelApi.trainingsdaten> response) {
                                            refreshCalendar();
                                        }

                                        @Override
                                        public void onFailure(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Throwable t) {

                                        }
                                    });



                                    progr++;

                                    updateProgressBar(progr);


                                    counterNew = 0;

                                    dialog.cancel();

                                    counter = 0;

                                    //RESET
                                    count = 0;
                                    save.clear();

                                    adapterCallBackString = "l1_reps";

                                }
                                else{
                                    Toast.makeText(getActivity(), "Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case 4:
                                EditText first_kg_layer4 = vieww.findViewById(R.id.layer_four_et_kg_card_single_item);
                                EditText second_kg_layer4 = vieww.findViewById(R.id.layer_four_et_kg_card_single_item_two);
                                EditText third_kg_layer4 = vieww.findViewById(R.id.layer_four_et_kg_card_single_item_three);
                                EditText fourth_kg_layer4 = vieww.findViewById(R.id.layer_four_et_kg_card_single_item_four);

                                EditText first_rep_l4 = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item);
                                EditText second_rep_l4 = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item_two);
                                EditText third_rep_l4 = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item_three);
                                EditText fourth_rep_l4 = vieww.findViewById(R.id.layer_four_et_symbol_reps_single_item_four);

                                String rp1_l4 = first_rep_l4.getText().toString().trim();
                                String rp2_l4 = second_rep_l4.getText().toString().trim();
                                String rp3_l4 = third_rep_l4.getText().toString().trim();
                                String rp4_l4 = fourth_rep_l4.getText().toString().trim();

                                String kg1_l4 = first_kg_layer4.getText().toString().trim();
                                String kg2_l4 = second_kg_layer4.getText().toString().trim();
                                String kg3_l4 = third_kg_layer4.getText().toString().trim();
                                String kg4_l4 = fourth_kg_layer4.getText().toString().trim();

                                if(!rp1_l4.isEmpty() && !rp2_l4.isEmpty() && !rp3_l4.isEmpty() && !rp4_l4.isEmpty()
                                        && !kg1_l4.isEmpty() && !kg2_l4.isEmpty() && !kg3_l4.isEmpty() && !kg4_l4.isEmpty()) {

                                    trainingsdaten trainingsdaten = new trainingsdaten("squat",
                                            format,rp1_l4,rp2_l4,rp3_l4,rp4_l4,null,
                                            kg1_l4,kg2_l4,kg3_l4,kg4_l4,null);

                                    userApi.savedata(trainingsdaten).enqueue(new Callback<com.example.benchsquatdl2.model.modelApi.trainingsdaten>() {
                                        @Override
                                        public void onResponse(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Response<com.example.benchsquatdl2.model.modelApi.trainingsdaten> response) {
                                            refreshCalendar();
                                        }

                                        @Override
                                        public void onFailure(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Throwable t) {

                                        }
                                    });

                                    



                                    progr++;
                                    updateProgressBar(progr);



                                    counter = 0;

                                    dialog.cancel();

                                    //RESET
                                    count = 0;
                                    save.clear();
                                    adapterCallBackString = "l1_reps";
                                    counterNew = 0;

                                }

                                else{
                                    Toast.makeText(getActivity(), "Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show();
                                }


                                break;

                            case 5:
                                EditText first_kg_layer5 = vieww.findViewById(R.id.layer_five_et_kg_card_single_item);
                                EditText second_kg_layer5 = vieww.findViewById(R.id.layer_five_et_kg_card_single_item_two);
                                EditText third_kg_layer5 = vieww.findViewById(R.id.layer_five_et_kg_card_single_item_three);
                                EditText fourth_kg_layer5 = vieww.findViewById(R.id.layer_five_et_kg_card_single_item_four);
                                EditText fifth_kg_layer5 = vieww.findViewById(R.id.layer_five_et_kg_card_single_item_five);

                                EditText first_rep_l5 = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item);
                                EditText second_rep_l5 = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_two);
                                EditText third_rep_l5 = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_three);
                                EditText fourth_rep_l5 = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_four);
                                EditText fifth_rep_l5 = vieww.findViewById(R.id.layer_five_et_symbol_reps_single_item_five);


                                String rp1_l5 = first_rep_l5.getText().toString().trim();
                                String rp2_l5 = second_rep_l5.getText().toString().trim();
                                String rp3_l5 = third_rep_l5.getText().toString().trim();
                                String rp4_l5 = fourth_rep_l5.getText().toString().trim();
                                String rp5_l5 = fifth_rep_l5.getText().toString().trim();

                                String kg1_l5 = first_kg_layer5.getText().toString().trim();
                                String kg2_l5 = second_kg_layer5.getText().toString().trim();
                                String kg3_l5 = third_kg_layer5.getText().toString().trim();
                                String kg4_l5 = fourth_kg_layer5.getText().toString().trim();
                                String kg5_l5 = fifth_kg_layer5.getText().toString().trim();

                                if(!rp1_l5.isEmpty() && !rp2_l5.isEmpty() && !rp3_l5.isEmpty() && !rp4_l5.isEmpty() && !rp5_l5.isEmpty() &&
                                        !kg1_l5.isEmpty() && !kg2_l5.isEmpty() && !kg3_l5.isEmpty() &&
                                        !kg4_l5.isEmpty() && !kg5_l5.isEmpty()){

                                    trainingsdaten trainingsdaten = new trainingsdaten("squat",
                                            format,rp1_l5,rp2_l5,rp3_l5,rp4_l5,rp5_l5,
                                            kg1_l5,kg2_l5,kg3_l5,kg4_l5,kg5_l5);

                                    userApi.savedata(trainingsdaten).enqueue(new Callback<com.example.benchsquatdl2.model.modelApi.trainingsdaten>() {
                                        @Override
                                        public void onResponse(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Response<com.example.benchsquatdl2.model.modelApi.trainingsdaten> response) {
                                            refreshCalendar();
                                        }

                                        @Override
                                        public void onFailure(Call<com.example.benchsquatdl2.model.modelApi.trainingsdaten> call, Throwable t) {

                                        }
                                    });


                                    progr++;

                                    updateProgressBar(progr);



                                    counter = 0;

                                    dialog.cancel();
                                    //RESET
                                    count = 0;
                                    adapterCallBackString = "l1_reps";
                                    counterNew = 0;
                                    save.clear();

                                }
                                break;
                        }
                    }
                });

                btn_delete_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(count > 0){
                            if(count == 1) {

                                contactView.setVisibility(View.VISIBLE);
                                arraySizeContactAdapter = mAdapter.returnMyObject().size();


                                RepKgAdapter mAdapter_two = new RepKgAdapter(getActivity(), 1, dialog, RepKgModel);
                                save.remove(1);
                                save.set(0, mAdapter_two);

                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_two);

                                mAdapter_two.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);
                                count = 0;
                                counter = 0;
                                adapterCallBackString = "l1_reps_one";

                                counterNew = 0;


                            }

                            if(count == 2){

                                RepKgAdapter mAdapter_three = new RepKgAdapter(getActivity(), 2, dialog, RepKgModel);
                                save.remove(2);
                                save.set(1, mAdapter_three);

                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_three);

                                mAdapter_three.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);

                                count = 1;
                                counter = 0;
                                adapterCallBackString = "l2_reps_one";
                                counterNew = 0;

                            }

                            if(count == 3){

                                RepKgAdapter mAdapter_three = new RepKgAdapter(getActivity(), 3, dialog, RepKgModel);
                                save.remove(3);
                                save.set(2, mAdapter_three);

                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_three);

                                mAdapter_three.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);

                                count = 2;
                                counter = 0;
                                adapterCallBackString = "l3_reps_one";
                                counterNew = 0;

                            }

                            if(count == 4){

                                RepKgAdapter mAdapter_three = new RepKgAdapter(getActivity(), 4, dialog, RepKgModel);
                                save.remove(4);
                                save.set(3, mAdapter_three);


                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_three);

                                mAdapter_three.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);

                                count = 3;
                                counter = 0;


                                adapterCallBackString = "l4_reps_five";
                                counterNew = 0;

                            }






                        }
                    }
                });


                btn_add_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(count < 4 && !save.get(0).returnMyObject().isEmpty()){


                            count++;
                            int size = 0;



                            if(count == 1) {
                                contactView.setVisibility(View.VISIBLE);
                                arraySizeContactAdapter = mAdapter.returnMyObject().size();

                                RepKgModel = new RepKgModel(save.get(0).returnMyObject().get(0), save.get(0).returnMyObject().get(1), null, null,null,null,null,null,null,null);
                                RepKgAdapter mAdapter_two = new RepKgAdapter(getActivity(), 2, dialog, RepKgModel);
                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_two);
                                save.add(1,mAdapter_two);
                                size = mAdapter_two.returnMyObject().size();
                                mAdapter_two.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);
                                adapterCallBackString = "l2_reps_two";
                            }


                            if( count == 2 ){
                                Log.d("countsize9","2:" + save.get(1).returnMyObject().size());
                                RepKgModel = new RepKgModel(save.get(1).returnMyObject().get(0), save.get(1).returnMyObject().get(1), save.get(1).returnMyObject().get(2), save.get(1).returnMyObject().get(3),null,null,null,null,null,null);
                                RepKgAdapter mAdapter_three = new RepKgAdapter(getActivity(), 3, dialog, RepKgModel);
                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_three);
                                save.add(2, mAdapter_three);
                                mAdapter_three.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);
                                adapterCallBackString = "l3_reps_three";
                            }

                            if (count == 3){
                                Log.d("countsize9","3:" + save.get(2).returnMyObject().size());
                                RepKgModel = new RepKgModel(save.get(2).returnMyObject().get(0), save.get(2).returnMyObject().get(1), save.get(2).returnMyObject().get(2), save.get(2).returnMyObject().get(3), save.get(2).returnMyObject().get(4), save.get(2).returnMyObject().get(5),null,null,null,null);
                                RepKgAdapter mAdapter_twoo = new RepKgAdapter(getActivity(), 4, dialog, RepKgModel);
                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_twoo);
                                save.add(3,mAdapter_twoo);

                                mAdapter_twoo.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);
                                adapterCallBackString = "l4_reps_four";
                            }

                            if (count == 4){
                                Log.d("countsize9","4:" + save.get(3).returnMyObject().size());
                                RepKgModel = new RepKgModel(save.get(3).returnMyObject().get(0), save.get(3).returnMyObject().get(1), save.get(3).returnMyObject().get(2), save.get(3).returnMyObject().get(3), save.get(3).returnMyObject().get(4), save.get(3).returnMyObject().get(5),save.get(3).returnMyObject().get(6), save.get(3).returnMyObject().get(7),null,null);
                                RepKgAdapter mAdapter_two = new RepKgAdapter(getActivity(), 5, dialog, RepKgModel);
                                contactView.setVisibility(View.VISIBLE);
                                contactView.setAdapter(mAdapter_two);
                                save.add(4,mAdapter_two);
                                mAdapter_two.setPlayPauseClickListener(squatFragment.this::imageButtonOnClick2);
                                adapterCallBackString = "l5_reps_one";
                            }


                        }
                        else{
                            Toast.makeText(getContext(), "Fill out everything. Only 5 sets are allowed", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }



        });
        return vx;
    }

    private String saveRep(String x) {
        return x;
    }


    private void deleteString() {
        this.adapterCallBackString = "l1_reps";
    }






    private void refreshCalendar(){
        userApi.getDatabyID().enqueue(new Callback<List<trainingdto>>() {
            @Override
            public void onResponse(Call<List<trainingdto>> call, Response<List<trainingdto>> response) {
                myGridAdapter = new MyGridAdapter(getActivity(),dates,calendar,eventsList,response.body());
                gridView.setAdapter(myGridAdapter);
                gridView.invalidateViews();

            }
            @Override
            public void onFailure(Call<List<trainingdto>> call, Throwable t) {

            }
        });
    }

    public void imageButtonOnClick(View v, int position) {
        // TODO: Implement this
        Log.d("cardview","hello Sir");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialogExtend xx = new dialogExtend();
        View view = getLayoutInflater().inflate(R.layout.open_training_data, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        TextView date = view.findViewById(R.id.tv_date);
        MultiAutoCompleteTextView cc = view.findViewById(R.id.multiAutoCompleteTextView);

        TextView rep1 = view.findViewById(R.id.tv_reps_opendialog);
        TextView rep2 = view.findViewById(R.id.tv_reps_opendialog2);
        TextView rep3 = view.findViewById(R.id.tv_reps_opendialog3);
        TextView rep4 = view.findViewById(R.id.tv_reps_opendialog4);
        TextView rep5 = view.findViewById(R.id.tv_reps_opendialog5);

// KG STARTS HERE, BUT I NAMED IT TO REPS FOR THE LOOP IN THE NEXT PARAGRAPH
        TextView rep6 = view.findViewById(R.id.tv_kg_opendialog);
        TextView rep7 = view.findViewById(R.id.tv_kg_opendialog2);
        TextView rep8 = view.findViewById(R.id.tv_kg_opendialog3);
        TextView rep9 = view.findViewById(R.id.tv_kg_opendialog4);
        TextView rep10 = view.findViewById(R.id.tv_kg_opendialog5);

        String xy = (String.valueOf(position+1));

        Button save = view.findViewById(R.id.button3);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getComment = cc.getText().toString().trim();
                commentModel xx = new commentModel(getComment);


                dialog.dismiss();

            }
        });
    }

    public void deleteLastItem(){


        if (progr > 0){
            progr--;
        }
    }

    public void imageButtonOnClick2(View v, int position, String x) {
        // TODO: Implement this

        View g = v;



        EditText hey = v.findViewById(R.id.layer_two_et_symbol_reps_single_item);


        this.adapterCallBackString = x;

        builder.setView(g);



    }

    private void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        //Calendar Day of Month ist richtig (aktuelles Datum), Danach wird es auf 1 gesetzt
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 2;

        //mit -2 kann ich die Tageszahlen verschieben.OUTPUT =5
        //Day of Week = An welcher stelle steht der tag,
        //DAY_OF_WEEK is the day of the week (7 days), DAY_OF_MONTH is the day of the month (<=31 days)

        //wichtig für das pinke feld
        //start 01.07, -FirstDayofMonth, wir gehen paar tage zurück
        //In der while schlafen fange wir paar tage früher an zu zählen, als der Monat anfängt
        //Bsp. 26,27,28,29,30, dann 01 bis max 42

        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        userApi.getDatabyID().enqueue(new Callback<List<trainingdto>>() {
            @Override
            public void onResponse(Call<List<trainingdto>> call, Response<List<trainingdto>> response) {
                myGridAdapter = new MyGridAdapter(getActivity(),dates,calendar,eventsList,response.body());
                gridView.setAdapter(myGridAdapter);
            }
            @Override
            public void onFailure(Call<List<trainingdto>> call, Throwable t) {

            }
        });



    }




    private void updateProgressBar(int z) {
        int x = z;
        String zy = String.valueOf(z) + "%";

       // tv.setText(zy);
    }


}
