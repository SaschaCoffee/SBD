package com.example.benchsquatdl2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.benchsquatdl2.model.modelApi.trainingdto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;
    List<trainingdto> body;
    private Date xy;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate,
                         List<Events> events, List<trainingdto> body) {
        super(context, R.layout.single_cell_layout);
        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        this.body = body;
        inflater = LayoutInflater.from(context);

    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //dates ist array, beinhaltet die 42 tage, also 2 monate

        Date monthDate = dates.get(position);
        Log.d("monthDate","" + monthDate);

        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;
        view = inflater.inflate(R.layout.single_cell_layout, parent,false);

        List<String> xtest = new ArrayList<>();
        Log.d("anzahlte2","" + body.size());

        for(int i = 0; i < body.size(); i++){
            xtest.add(body.get(i).getDate().toString());
            Log.d("getanzahl3","" + body.get(i).getDate().toString());
        }


        Date xy = dateCalendar.getTime();

        /*String test = "11-07-2023";
        xtest.add("12-08-2023");
        xtest.add("22-08-2023");*/


        String format = new SimpleDateFormat("dd-MM-yyy").format(xy);
        //es ist eine schleife von mehreren Tagen
        Log.d("dayofmonth22","eee" +format);




        Log.d("Test123","currentMonth" + currentMonth);
        Log.d("Test123","displayMonth" + displayMonth);

        //current month fix, Displaymonth erhöht sich um eins, wenn es neuen monat erreicht
        //displayMonth == currentMonth & displayYear == currentYear

        int startsize = xtest.size();



        for(int i = 0; i < startsize; i++) {

            TextView peace =view.findViewById(R.id.calendar_day);

            if (xtest.get(i).equals(format)) {
                peace.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.backgreen));
                switch(body.get(i).getUb_bezeichnung()){
                    case "bench":
                        peace.setText("B");
                        break;
                    case "deadlift":
                        peace.setText("D");
                        break;
                    case "squat":
                        peace.setText("S");
                        break;
                }

            }

        }
        // Hier werden die Kalendar Tage eingesetzt
       // TextView Day_Number = view.findViewById(R.id.calendar_day);
       // Day_Number.setText(String.valueOf(DayNo));

        return view ;
    }

    @Override
    public int getCount() {
        return dates.size();
    }


}
