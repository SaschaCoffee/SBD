package com.example.benchsquatdl2.Fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.benchsquatdl2.AdapterHolder.listAllProfilesAdapterCountry;
import com.example.benchsquatdl2.AdapterHolder.listAllProfilesAdapterState;
import com.example.benchsquatdl2.R;
import com.example.benchsquatdl2.model.modelApi.Country.countryname;
import com.example.benchsquatdl2.model.modelApi.Country.germancountry;
import com.example.benchsquatdl2.model.modelApi.State.germanstate;
import com.example.benchsquatdl2.model.modelApi.State.statename;
import com.example.benchsquatdl2.model.modelApi.menuitemid;
import com.example.benchsquatdl2.retrofit.RetrofitService;
import com.example.benchsquatdl2.retrofit.UserApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FirstFragment extends Fragment  {
    public static final String COLUMN_ID = "ID";
    public static final String GERRRAWROH = "GERRRAWROH";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";

    Menu optionsMenu;

    private String selectedCountry,  selectedState;                 //vars to hold the values of selected State and District
    private TextView tvCountrySpinner, tvStateSpinner;             //declaring TextView to show the errors
    private Spinner countrySpinner, stateSpinner;                  //Spinners
    private ArrayAdapter<CharSequence> countryAdapter, stateAdapter;  //declare adapters for the spinners

    SQLiteDatabase db;
    RecyclerView contactView;
    DatabaseReference statistic, statisticUSA;
    Button sbd, add, btn_logregister, btn_sort;
    TextView tv_banner;
    RetrofitService retrofitService = new RetrofitService();
    UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
    public FirstFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String land;


        View vx = inflater.inflate(R.layout.list, container, false);
        statistic = FirebaseDatabase.getInstance().getReference();
        statisticUSA = FirebaseDatabase.getInstance().getReference();
        contactView = vx.findViewById(R.id.myContactList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        contactView.setLayoutManager(linearLayoutManager);
        contactView.setHasFixedSize(true);
        btn_sort = vx.findViewById(R.id.btn_sort);

        ImageButton ib = vx.findViewById(R.id.omg);
        PopupMenu dropDownMenu = new PopupMenu(getContext(), ib);




        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_list, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Integer menuitem = menuItem.getItemId();





                        if(!selectedState.isEmpty()) {
                            buildRecyclerviewSortingState(menuitem, selectedCountry, umwandelnString(selectedState));

                        }
                        else {
                            buildRecyclerviewSortingCountry(menuitem, selectedCountry);
                        }



                        return false;
                    }
                });
            }

        });








        Query statisticNi = statistic.child("statisticDataGermany2");
        Query statisticUSAA = statisticUSA.child("statisticUSA");



        //Country Spinner Initialisation
        countrySpinner = vx.findViewById(R.id.spinner_country);//Finds a view that was identified by the android:id attribute
        stateSpinner = vx.findViewById(R.id.spinner_states);

        //Country Spinner Initialisation
        countryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_country, R.layout.spinner_layout);

        // Specify the layout to use when the list of choices appear
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        countrySpinner.setAdapter(countryAdapter);            //Set the adapter to the spinner to populate the Country Spinner



        ArrayList<String> bundeslaender = new ArrayList<String>();
        bundeslaender.add("BB");
        bundeslaender.add("BE");
        bundeslaender.add("BW");
        bundeslaender.add("BY");
        bundeslaender.add("HE");
        bundeslaender.add("Niedersachsen");
        bundeslaender.add("MV");
        bundeslaender.add("NI");
        bundeslaender.add("NRW");
        bundeslaender.add("RP");
        bundeslaender.add("SH");
        bundeslaender.add("SN");
        bundeslaender.add("ST");
        bundeslaender.add("TH");

        String niedersachsen = "NI";



        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //State Spinner Initialisation
                stateSpinner = vx.findViewById(R.id.spinner_states);    //Finds a view that was identified by the android:id attribute in xml
                selectedCountry = countrySpinner.getSelectedItem().toString();




                int parentID = parent.getId();
                if (parentID == R.id.spinner_country){
                    switch (selectedCountry){
                        case "Select Your Country": stateAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_default_state, R.layout.spinner_layout);
                            break;
                        case "Germany": stateAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_german_states, R.layout.spinner_layout);

                        userApi.getgermancountry().enqueue(new Callback<List<germancountry>>() {
                            @Override
                            public void onResponse(Call<List<germancountry>> call, Response<List<germancountry>> response) {
                                buildRecyclerviewCountry(response.body());
                            }

                            @Override
                            public void onFailure(Call<List<germancountry>> call, Throwable t) {

                            }
                        });

                            break;

                        case "USA": stateAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_american_states, R.layout.spinner_layout);
                                statistic.child("statisticUsaNoStates").limitToFirst(20).addValueEventListener(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        ArrayList<String> benchList = new ArrayList<>();
                                        ArrayList<String> squatList = new ArrayList<>();
                                        ArrayList<String> deadliftList = new ArrayList<>();
                                        ArrayList<String> nameList = new ArrayList<>();
                                        HashMap<String, Double> totalListe = new HashMap<>();
                                        ArrayList<String> cityList = new ArrayList<>();




                                            //Second Loop, die User
                                            for (DataSnapshot yx : snapshot.getChildren()) {

                                                int size = Integer.parseInt(String.valueOf(yx.getChildrenCount()));
                                                String size2 = String.valueOf(size -1);
                                                long cc = yx.getChildrenCount();



                                            /* FIRE BASE ORDNER STRUKTUR

                                            0 Name, 1 Sex, 2 Event, 3 Equipment, 4 Age, 5 AgeClass, 6 BirthYearClass, 7 Division, 8 BodyweightKg, 9 WeightClassKg,
                                            10 Squat1Kg, 11 Squat2Kg, 12 Squat3Kg, 13 Squat4Kg, 14 Best3SquatKg, 15 Bench1Kg, 16 Bench2Kg,
                                            17 Bench3Kg, 18 Bench4Kg, 19 Best3BenchKg, 20 Deadlift1Kg, 21 Deadlift2Kg, 22 Deadlift3Kg,
                                            23 Deadlift4Kg, 24 Best3DeadliftKg, 25 TotalKg, 26 Place, 27 Dots, 28 Wilks, 29 Glossbrenner,
                                            30 Goodlift, 31 Tested, 32 Country, 33 State, 34 Federation, 35 ParentFederation, 36 Date,
                                            37 MeetCountry, 38 MeetState, 39 MeetTown, 40 MeetName.*/

                                                //Objects with various kind of numbers 0,1,2,3

                                                String xy = "";


                                                String bench = String.valueOf(yx.child(size2).child("19").getValue(Double.class));
                                                String deadlift = String.valueOf(yx.child(size2).child("24").getValue(Double.class));
                                                String squat = String.valueOf(yx.child(size2).child("14").getValue(Double.class));
                                                String name = String.valueOf(yx.child(size2).child("0").getValue(String.class));
                                                String city = String.valueOf(yx.child(size2).child("38").getValue(String.class));


                                                benchList.add(bench);
                                                deadliftList.add(deadlift);
                                                squatList.add(squat);
                                                nameList.add(name);
                                                cityList.add(city);





                                            }




                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                break;




                        case "Worldwide": stateAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_default_state, R.layout.spinner_layout);

                            statistic.child("statisticData").limitToFirst(300).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList<String> benchList = new ArrayList<>();
                                    ArrayList<String> squatList = new ArrayList<>();
                                    ArrayList<String> deadliftList = new ArrayList<>();
                                    ArrayList<String> nameList = new ArrayList<>();
                                    ArrayList<String> cityList = new ArrayList<>();

                                    for(DataSnapshot y : snapshot.getChildren()) {
                                        String xy = "";

                                        if(y.child("MeetCountry").exists()){
                                            for (DataSnapshot x : y.child("MeetCountry").getChildren()) {
                                                xy = x.getKey();
                                                //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {
                                            }
                                            String bro = y.child("MeetCountry").child(xy).getValue(String.class);
                                            cityList.add(String.valueOf(bro));
                                        }else {
                                            cityList.add(null);
                                        }


                                        if (y.child("Best3BenchKg").exists()) {
                                            for (DataSnapshot x : y.child("Best3BenchKg").getChildren()) {
                                                xy = x.getKey();
                                                //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {
                                            }
                                            Double bro = y.child("Best3BenchKg").child(xy).getValue(Double.class);
                                            benchList.add(String.valueOf(bro));
                                        } else {
                                            benchList.add(null);
                                        }

                                        if (y.child("Best3DeadliftKg").exists()) {
                                            for (DataSnapshot x : y.child("Best3DeadliftKg").getChildren()) {
                                                xy = x.getKey();
                                                //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {

                                            }
                                            Double bro = y.child("Best3DeadliftKg").child(xy).getValue(Double.class);
                                            deadliftList.add(String.valueOf(bro));
                                        } else {
                                            deadliftList.add(null);
                                        }

                                        if (y.child("Best3SquatKg").exists()) {
                                            for (DataSnapshot x : y.child("Best3SquatKg").getChildren()) {
                                                xy = x.getKey();
                                                //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {

                                            }
                                            Double bro = y.child("Best3SquatKg").child(xy).getValue(Double.class);
                                            squatList.add(String.valueOf(bro));
                                        } else {
                                            squatList.add(null);
                                        }

                                        if (y.child("Name").exists()) {
                                            for (DataSnapshot x : y.child("Name").getChildren()) {
                                                xy = x.getKey();
                                                //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {
                                            }
                                            String bro = y.child("Name").child(xy).getValue(String.class);
                                            nameList.add(bro);
                                        } else {
                                            nameList.add("null");
                                        }

                                    }

                                    //    modelDoubleList hMap = x.child("Best3BenchKg").getValue(t);
                                    //
                                }
                                //GenericTypeIndicator<HashMap<String,Double>> t = new GenericTypeIndicator<HashMap<String,Double>>() {
                                //   };
                                //   HashMap<String,Double> hMap = snapshot.child("Best3SquatKg").getValue(t);


                                //



                                //  }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            break;



                           default:  break;
                    }

                    // Specify the layout to use when the list of choices appear
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    stateSpinner.setAdapter(stateAdapter);            //Set the adapter to the spinner to populate the State Spinner

                    //When any item of the stateSpinner is selected
                    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            //Define City Spinner but we will populate the options through the selected state

                            selectedState = stateSpinner.getSelectedItem().toString();      //Obtain the selected State


                            int parentID = parent.getId();

                            if (parentID == R.id.spinner_states) {
                                switch (selectedState) {
                                    case "Select Your Country":
                                        stateAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                                R.array.array_default_state, R.layout.spinner_layout);
                                        break;
                                    case "Niedersachsen (NI)":
                                        stateAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                                R.array.array_german_states, R.layout.spinner_layout);

                                        buildRecyclerviewState("NI");

                                        break;

                                    case "USA":
                                        stateAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                                R.array.array_american_states, R.layout.spinner_layout);
                                        break;


                                    default:
                                        break;
                                }
                            }





                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



        tvCountrySpinner = vx.findViewById(R.id.textView_country);
        tvStateSpinner = vx.findViewById(R.id.textView_indian_states);


        ArrayList<Integer> x = new ArrayList<>();

        /*statistic.child("statisticData").addValueEventListener(new ValueEventListener() {
            int xx = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot x : snapshot.getChildren()){
                  String dd =  x.child("MeetState").child("0").getValue(String.class);


                   if(dd.equals("NI")){
                       xx++;
                   }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
      statistic.child("statisticData").limitToFirst(300).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> benchList = new ArrayList<>();
                ArrayList<String> squatList = new ArrayList<>();
                ArrayList<String> deadliftList = new ArrayList<>();
                ArrayList<String> nameList = new ArrayList<>();
                ArrayList<String> cityList = new ArrayList<>();
                ArrayList<String> instagramName = new ArrayList<>();

                for(DataSnapshot y : snapshot.getChildren()) {
                    String xy = "";

                    if(y.child("MeetCountry").exists()){
                        for (DataSnapshot x : y.child("MeetCountry").getChildren()) {
                            xy = x.getKey();
                            //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {
                        }
                        String bro = y.child("MeetCountry").child(xy).getValue(String.class);
                        cityList.add(String.valueOf(bro));
                    }else {
                        cityList.add(null);
                    }


                    if (y.child("Best3BenchKg").exists()) {
                        for (DataSnapshot x : y.child("Best3BenchKg").getChildren()) {
                            xy = x.getKey();
                            //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {
                        }
                        Double bro = y.child("Best3BenchKg").child(xy).getValue(Double.class);
                        benchList.add(String.valueOf(bro));
                    } else {
                        benchList.add(null);
                    }

                    if (y.child("Best3DeadliftKg").exists()) {
                        for (DataSnapshot x : y.child("Best3DeadliftKg").getChildren()) {
                            xy = x.getKey();
                            //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {

                        }
                        Double bro = y.child("Best3DeadliftKg").child(xy).getValue(Double.class);
                        deadliftList.add(String.valueOf(bro));
                    } else {
                        deadliftList.add(null);
                    }

                    if (y.child("Best3SquatKg").exists()) {
                        for (DataSnapshot x : y.child("Best3SquatKg").getChildren()) {
                            xy = x.getKey();
                            //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {

                        }
                        Double bro = y.child("Best3SquatKg").child(xy).getValue(Double.class);
                        squatList.add(String.valueOf(bro));
                    } else {
                        squatList.add(null);
                    }

                    if (y.child("Name").exists()) {
                        for (DataSnapshot x : y.child("Name").getChildren()) {
                            xy = x.getKey();
                            //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {
                        }
                        String bro = y.child("Name").child(xy).getValue(String.class);
                        nameList.add(bro);
                    } else {
                        nameList.add("null");
                    }

                    if (y.child("Name").exists()) {
                        for (DataSnapshot x : y.child("Name").getChildren()) {
                            xy = x.getKey();
                            //   GenericTypeIndicator<modelDoubleList> t = new GenericTypeIndicator<modelDoubleList>() {
                        }
                        String bro = y.child("Name").child(xy).getValue(String.class);
                        nameList.add(bro);
                    } else {
                        nameList.add("null");
                    }

                }


                //    modelDoubleList hMap = x.child("Best3BenchKg").getValue(t);
                //
                }
                //GenericTypeIndicator<HashMap<String,Double>> t = new GenericTypeIndicator<HashMap<String,Double>>() {
             //   };
             //   HashMap<String,Double> hMap = snapshot.child("Best3SquatKg").getValue(t);


             //



          //  }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return vx;


    }

    private void buildRecyclerviewSortingCountry(Integer menuitem, String selectedCountry) {



    }

    private void buildRecyclerviewSortingState(Integer menuitem, String selectedCountry, String selectedState) {
        statename sortmodel = new statename(selectedState);

        Log.d("menuID2",menuitem + selectedCountry);


        //SelecttedState als var einfügen, aber aufpassen. Es ist die lange schreibweise
        //z.b. Niedersachsen anstatt NI
        //UPDATE: umwandeln methode hinzugefügt. Ändert string "Niedersachsen (NI)" zu NI um
        statename statename = new statename(selectedState);

        switch(menuitem){
           case R.id.dsc:
               switch(selectedCountry){
                   case "Germany":
                       userApi.getsortgermanstate(statename).enqueue(new Callback<List<germanstate>>() {
                           @Override
                           public void onResponse(Call<List<germanstate>> call, Response<List<germanstate>> response) {
                               listAllProfilesAdapterState profilesAdapter = new listAllProfilesAdapterState(response, getContext());
                               contactView.setAdapter(profilesAdapter);
                           }

                           @Override
                           public void onFailure(Call<List<germanstate>> call, Throwable t) {

                           }
                       });
               }

        }


    }


    private void buildRecyclerviewCountry(List<germancountry> body) {

        listAllProfilesAdapterCountry profilesAdapterCountry = new listAllProfilesAdapterCountry(body, getContext());
        contactView.setAdapter(profilesAdapterCountry);

    }

    private void buildRecyclerviewSortingGermanStates(String selectedCountry) {

    }

    private void buildRecyclerviewState(String state) {

        statename xx = new statename(state);

        userApi.getgermanstate(xx).enqueue(new Callback<List<germanstate>>() {
            @Override
            public void onResponse(Call<List<germanstate>> call, Response<List<germanstate>> response) {

                listAllProfilesAdapterState profilesAdapter = new listAllProfilesAdapterState(response, getContext());
                contactView.setAdapter(profilesAdapter);

            }

            @Override
            public void onFailure(Call<List<germanstate>> call, Throwable t) {

            }
        });




    }

    private void buildRecyclerviewSortingGermanState() {



    }

    public static String umwandelnString(String input) {
        int start = input.indexOf("(");
        int end = input.indexOf(")");

        if (start != -1 && end != -1 && start < end) {
            return input.substring(start + 1, end);
        } else {
            return input; // Wenn keine Klammern gefunden wurden, gebe den Originalstring zurück
        }
    }









}
