package com.example.travelapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.Fragment.DatePickerFragment;
import com.example.travelapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TripPlanningActivity extends AppCompatActivity {

    private ArrayList<OptionsItem> mOptionsList1, mOptionsList2, mOptionsList3,
            mOptionsList4, itineraryNameList;

    Calendar c;
    DatePickerDialog dpd1, dpd2, dpd3, dpd4, dpd5,dpd6;

    private SpinnerOptionsAdapter mAdapter1, mAdapter2, mAdapter3, mAdapter4;
    private SpinnerOptionsAdapter mItiAdapter1, mItiAdapter2, mItiAdapter3,
            mItiAdapter4, mItiAdapter5, mItiAdapter6;

    Button submitButton;

    Spinner item1spinner, item2spinner, item3spinner, item4spinner;
    Spinner itinerary1spinner, itinerary2spinner, itinerary3spinner,
            itinerary4spinner, itinerary5spinner, itinerary6spinner;

    EditText tripTitle, tripFrom, tripTO, fromTime, toTime;
    EditText option1name, option2name, option3name, option4name;
    EditText qty1, qty2, qty3, qty4;
    EditText price1, price2, price3, price4;
    EditText noOfDays;

    TextView itiDatePicker1, itiDatePicker2,itiDatePicker3,itiDatePicker4,itiDatePicker5,itiDatePicker6;

    EditText itiName1, itiName2, itiName3, itiName4, itiName5, itiName6;

    Bitmap bmpflight, bmpcar, bmptrain, bmpbus, bmphotel, bmpmotel, bmpresort;
    Bitmap bmprestaurant, bmpbar, bmpbowling, bmpconcert, bmpcasino, bmpgame;

    Bitmap park, zoo, swimming, presentation, meeting, skatepark, trekking;
    Bitmap museum, mountain, cycling, contest, beach, aquarium;

    Bitmap scaledflight, scaledcar, scaledtrain, scaledbus,
            scaledhotel, scaledmotel, scaledresort;
    Bitmap scaledrestaurant, scaledbar, scaledbowling, scaledconcert,
            scaledcasino, scaledgame;

    Bitmap spark, szoo, sswimming, spresentation, smeeting, sskatepark, strekking;
    Bitmap smuseum, smountain, scycling, scontest, sbeach, saquarium;


    Bitmap bmpbi, scaledbmp, bmpline, scaledline;
    int pageWidth = 1200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planning);

        submitButton = findViewById(R.id.submitbutton);

        tripTitle = findViewById(R.id.trip_title);
        tripFrom = findViewById(R.id.trip_from);
        tripTO = findViewById(R.id.trip_to);

        noOfDays = findViewById(R.id.trip_number_of_days);


        bmpbi = BitmapFactory.decodeResource(getResources(), R.drawable.pdfbi);
        scaledbmp = Bitmap.createScaledBitmap(bmpbi, 1200, 618, false);

        bmpline = BitmapFactory.decodeResource(getResources(), R.drawable.verticalline);
        scaledline = Bitmap.createScaledBitmap(bmpline, 120, 80, false);

        park = BitmapFactory.decodeResource(getResources(), R.drawable.park);
        spark = Bitmap.createScaledBitmap(park, 150, 150, false);
        contest = BitmapFactory.decodeResource(getResources(), R.drawable.contest);
        scontest = Bitmap.createScaledBitmap(contest, 150, 150, false);
        swimming = BitmapFactory.decodeResource(getResources(), R.drawable.swimming);
        sswimming = Bitmap.createScaledBitmap(swimming, 150, 150, false);
        meeting = BitmapFactory.decodeResource(getResources(), R.drawable.meeting);
        smeeting = Bitmap.createScaledBitmap(meeting, 150, 150, false);
        trekking = BitmapFactory.decodeResource(getResources(), R.drawable.trekking);
        strekking = Bitmap.createScaledBitmap(trekking, 150, 150, false);
        skatepark = BitmapFactory.decodeResource(getResources(), R.drawable.skatepark);
        sskatepark = Bitmap.createScaledBitmap(skatepark, 150, 150, false);
        mountain = BitmapFactory.decodeResource(getResources(), R.drawable.mountain);
        smountain = Bitmap.createScaledBitmap(mountain, 150, 150, false);
        museum = BitmapFactory.decodeResource(getResources(), R.drawable.museum);
        smuseum = Bitmap.createScaledBitmap(museum, 150, 150, false);
        zoo = BitmapFactory.decodeResource(getResources(), R.drawable.zoo);
        szoo = Bitmap.createScaledBitmap(zoo, 150, 150, false);

        presentation = BitmapFactory.decodeResource(getResources(), R.drawable.presentation);
        spresentation = Bitmap.createScaledBitmap(presentation, 150, 150, false);
        cycling = BitmapFactory.decodeResource(getResources(), R.drawable.cycling);
        scycling = Bitmap.createScaledBitmap(cycling, 150, 150, false);
        aquarium = BitmapFactory.decodeResource(getResources(), R.drawable.aquarium);
        saquarium = Bitmap.createScaledBitmap(aquarium, 150, 150, false);
        beach = BitmapFactory.decodeResource(getResources(), R.drawable.beach);
        sbeach = Bitmap.createScaledBitmap(beach, 150, 150, false);


        bmpflight = BitmapFactory.decodeResource(getResources(), R.drawable.flight);
        scaledflight = Bitmap.createScaledBitmap(bmpflight, 100, 100, false);
        bmpcar = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        scaledcar = Bitmap.createScaledBitmap(bmpcar, 100, 100, false);
        bmptrain = BitmapFactory.decodeResource(getResources(), R.drawable.train);
        scaledtrain = Bitmap.createScaledBitmap(bmptrain, 100, 100, false);
        bmpbus = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
        scaledbus = Bitmap.createScaledBitmap(bmpbus, 100, 100, false);


        bmphotel = BitmapFactory.decodeResource(getResources(), R.drawable.hotel);
        scaledhotel = Bitmap.createScaledBitmap(bmphotel, 100, 100, false);
        bmpmotel = BitmapFactory.decodeResource(getResources(), R.drawable.motel);
        scaledmotel = Bitmap.createScaledBitmap(bmpmotel, 100, 100, false);
        bmpresort = BitmapFactory.decodeResource(getResources(), R.drawable.resort);
        scaledresort = Bitmap.createScaledBitmap(bmpresort, 100, 100, false);


        bmprestaurant = BitmapFactory.decodeResource(getResources(), R.drawable.restaurant);
        scaledrestaurant = Bitmap.createScaledBitmap(bmprestaurant, 100, 100, false);
        bmpbar = BitmapFactory.decodeResource(getResources(), R.drawable.bar);
        scaledbar = Bitmap.createScaledBitmap(bmpbar, 100, 100, false);


        bmpbowling = BitmapFactory.decodeResource(getResources(), R.drawable.bowling);
        scaledbowling = Bitmap.createScaledBitmap(bmpbowling, 100, 100, false);
        bmpconcert = BitmapFactory.decodeResource(getResources(), R.drawable.concert);
        scaledconcert = Bitmap.createScaledBitmap(bmpconcert, 100, 100, false);
        bmpcasino = BitmapFactory.decodeResource(getResources(), R.drawable.casino);
        scaledcasino = Bitmap.createScaledBitmap(bmpcasino, 100, 100, false);
        bmpgame = BitmapFactory.decodeResource(getResources(), R.drawable.sport);
        scaledgame = Bitmap.createScaledBitmap(bmpgame, 100, 100, false);


        item1spinner = findViewById(R.id.options_spinner1);
        item2spinner = findViewById(R.id.options_spinner2);
        item3spinner = findViewById(R.id.options_spinner3);
        item4spinner = findViewById(R.id.options_spinner4);

        itinerary1spinner = findViewById(R.id.itinerary_spinner1);
        itinerary2spinner = findViewById(R.id.itinerary_spinner2);
        itinerary3spinner = findViewById(R.id.itinerary_spinner3);
        itinerary4spinner = findViewById(R.id.itinerary_spinner4);
        itinerary5spinner = findViewById(R.id.itinerary_spinner5);
        itinerary6spinner = findViewById(R.id.itinerary_spinner6);

        itiName1 = findViewById(R.id.itinerary_et1);
        itiName2 = findViewById(R.id.itinerary_et2);
        itiName3 = findViewById(R.id.itinerary_et3);
        itiName4 = findViewById(R.id.itinerary_et4);
        itiName5 = findViewById(R.id.itinerary_et5);
        itiName6 = findViewById(R.id.itinerary_et6);

        itiDatePicker1 = findViewById(R.id.itinerary_date1);
        itiDatePicker2 = findViewById(R.id.itinerary_date2);
        itiDatePicker3 = findViewById(R.id.itinerary_date3);
        itiDatePicker4 = findViewById(R.id.itinerary_date4);
        itiDatePicker5 = findViewById(R.id.itinerary_date5);
        itiDatePicker6 = findViewById(R.id.itinerary_date6);


        option1name = findViewById(R.id.spinner_name_1);
        option2name = findViewById(R.id.spinner_name_2);
        option3name = findViewById(R.id.spinner_name_3);
        option4name = findViewById(R.id.spinner_name_4);

        qty1 = findViewById(R.id.spinner_qty_1);
        qty2 = findViewById(R.id.spinner_qty_2);
        qty3 = findViewById(R.id.spinner_qty_3);
        qty4 = findViewById(R.id.spinner_qty_4);

        price1 = findViewById(R.id.spinner_price_1);
        price2 = findViewById(R.id.spinner_price_2);
        price3 = findViewById(R.id.spinner_price_3);
        price4 = findViewById(R.id.spinner_price_4);

        spinnerList1();
        spinnerList2();
        spinnerList3();
        spinnerList4();

        itineraryList();

        mItiAdapter1 = new SpinnerOptionsAdapter(this, itineraryNameList);
        mItiAdapter2 = new SpinnerOptionsAdapter(this, itineraryNameList);
        mItiAdapter3 = new SpinnerOptionsAdapter(this, itineraryNameList);
        mItiAdapter4 = new SpinnerOptionsAdapter(this, itineraryNameList);
        mItiAdapter5 = new SpinnerOptionsAdapter(this, itineraryNameList);
        mItiAdapter6 = new SpinnerOptionsAdapter(this, itineraryNameList);

        mAdapter1 = new SpinnerOptionsAdapter(this, mOptionsList1);
        mAdapter2 = new SpinnerOptionsAdapter(this, mOptionsList2);
        mAdapter3 = new SpinnerOptionsAdapter(this, mOptionsList3);
        mAdapter4 = new SpinnerOptionsAdapter(this, mOptionsList4);

        itinerary1spinner.setAdapter(mItiAdapter1);
        itinerary2spinner.setAdapter(mItiAdapter2);
        itinerary3spinner.setAdapter(mItiAdapter3);
        itinerary4spinner.setAdapter(mItiAdapter4);
        itinerary5spinner.setAdapter(mItiAdapter5);
        itinerary6spinner.setAdapter(mItiAdapter6);

        item1spinner.setAdapter(mAdapter1);
        item2spinner.setAdapter(mAdapter2);
        item3spinner.setAdapter(mAdapter3);
        item4spinner.setAdapter(mAdapter4);

        itinerary1spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        itinerary2spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        itinerary3spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        itinerary4spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        itinerary5spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        itinerary6spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        item1spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        item2spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        item3spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        item4spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        itiDatePicker1.setOnClickListener(v -> {
            c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            dpd1 = new DatePickerDialog(TripPlanningActivity.this, (datePicker, mYear, mMonth, mDay)
                    -> itiDatePicker1.setText(mDay+"-"+(mMonth+1)+"-"+mYear), day, month, year);
            dpd1.show();
        });

        itiDatePicker2.setOnClickListener(v -> {
            c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            dpd2 = new DatePickerDialog(TripPlanningActivity.this, (datePicker, mYear, mMonth, mDay)
                    -> itiDatePicker2.setText(mDay+"-"+(mMonth+1)+"-"+mYear), day, month, year);
            dpd2.show();
        });
        itiDatePicker3.setOnClickListener(v -> {
            c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            dpd3 = new DatePickerDialog(TripPlanningActivity.this, (datePicker, mYear, mMonth, mDay)
                    -> itiDatePicker3.setText(mDay+"-"+(mMonth+1)+"-"+mYear), day, month, year);
            dpd3.show();
        });itiDatePicker4.setOnClickListener(v -> {
            c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            dpd4 = new DatePickerDialog(TripPlanningActivity.this, (datePicker, mYear, mMonth, mDay)
                    -> itiDatePicker4.setText(mDay+"-"+(mMonth+1)+"-"+mYear), day, month, year);
            dpd4.show();
        });itiDatePicker5.setOnClickListener(v -> {
            c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            dpd5 = new DatePickerDialog(TripPlanningActivity.this, (datePicker, mYear, mMonth, mDay)
                    -> itiDatePicker5.setText(mDay+"-"+(mMonth+1)+"-"+mYear), day, month, year);
            dpd5.show();
        });itiDatePicker6.setOnClickListener(v -> {
            c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            dpd6 = new DatePickerDialog(TripPlanningActivity.this, (datePicker, mYear, mMonth, mDay)
                    -> itiDatePicker6.setText(mDay+"-"+(mMonth+1)+"-"+mYear), day, month, year);
            dpd6.show();
        });

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();
    }

    private void spinnerList1() {
        mOptionsList1 = new ArrayList<>();
        mOptionsList1.add(new OptionsItem("Select", R.drawable.add_item));
        mOptionsList1.add(new OptionsItem("Flight", R.drawable.flight));
        mOptionsList1.add(new OptionsItem("Car", R.drawable.car));
        mOptionsList1.add(new OptionsItem("Train", R.drawable.train));
        mOptionsList1.add(new OptionsItem("Bus", R.drawable.bus));
    }

    private void spinnerList2() {
        mOptionsList2 = new ArrayList<>();
        mOptionsList2.add(new OptionsItem("Select", R.drawable.add_item));
        mOptionsList2.add(new OptionsItem("Hotel", R.drawable.hotel));
        mOptionsList2.add(new OptionsItem("Motel", R.drawable.motel));
        mOptionsList2.add(new OptionsItem("Resort", R.drawable.resort));
//        mOptionsList1.add(new OptionsItem("Hi", R.drawable.ic_add_circle_black_24dp));
    }

    private void spinnerList3() {
        mOptionsList3 = new ArrayList<>();
        mOptionsList3.add(new OptionsItem("Select", R.drawable.add_item));
        mOptionsList3.add(new OptionsItem("Restaurant", R.drawable.restaurant));
        mOptionsList3.add(new OptionsItem("Bar", R.drawable.bar));
//        mOptionsList1.add(new OptionsItem("Hi", R.drawable.ic_add_circle_black_24dp));
    }

    private void spinnerList4() {
        mOptionsList4 = new ArrayList<>();
        mOptionsList4.add(new OptionsItem("Select", R.drawable.add_item));
        mOptionsList4.add(new OptionsItem("Bowling", R.drawable.bowling));
        mOptionsList4.add(new OptionsItem("Concert", R.drawable.concert));
        mOptionsList4.add(new OptionsItem("Casino", R.drawable.casino));
        mOptionsList4.add(new OptionsItem("Game", R.drawable.sport));
    }

    private void itineraryList() {
        itineraryNameList = new ArrayList<>();
        itineraryNameList.add(new OptionsItem("Select", R.drawable.add_item));
        itineraryNameList.add(new OptionsItem("Park", R.drawable.park));
        itineraryNameList.add(new OptionsItem("Zoo", R.drawable.zoo));
        itineraryNameList.add(new OptionsItem("Museum", R.drawable.museum));
        itineraryNameList.add(new OptionsItem("Aquarium", R.drawable.aquarium));
        itineraryNameList.add(new OptionsItem("Mountain", R.drawable.mountain));
        itineraryNameList.add(new OptionsItem("Meeting", R.drawable.meeting));
        itineraryNameList.add(new OptionsItem("Presentation", R.drawable.presentation));
        itineraryNameList.add(new OptionsItem("Contest", R.drawable.contest));
        itineraryNameList.add(new OptionsItem("Cycling", R.drawable.cycling));
        itineraryNameList.add(new OptionsItem("Swimming", R.drawable.swimming));
        itineraryNameList.add(new OptionsItem("Trekking", R.drawable.trekking));
        itineraryNameList.add(new OptionsItem("Beach", R.drawable.beach));
        itineraryNameList.add(new OptionsItem("Skatepark", R.drawable.skatepark));
    }

    private void createPDF() {
        submitButton.setOnClickListener(v -> {

            if (tripTitle.getText().toString().length() == 0||
                    tripFrom.getText().toString().length() == 0 ||
                    tripTO.getText().toString().length() == 0 ||
                    qty1.getText().toString().length() == 0 ||
                    qty2.getText().toString().length() == 0 ||
                    price1.getText().toString().length() == 0 ||
                    price2.getText().toString().length() == 0)
            {
                Toast.makeText(TripPlanningActivity.this, "Some Fields are empty!", Toast.LENGTH_LONG).show();
            }
            else {

                Toast toast = Toast.makeText(TripPlanningActivity.this, "Please Open the file name Traveler's_Diaries_Trip_Plan.pdf!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


                PdfDocument myPdfDocument = new PdfDocument();
                Paint myPaint = new Paint();
                Paint myPaint2 = new Paint();
                Paint titlePaint = new Paint();
                Paint titlePaint2 = new Paint();

                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                Canvas canvas = myPage1.getCanvas();

                canvas.drawBitmap(scaledbmp, 0, 0, myPaint);

//                titlePaint.setTextAlign(Paint.Align.CENTER);
//                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                titlePaint.setTextSize(70);
//                canvas.drawText("Travel Diaries", pageWidth / 2, 270, titlePaint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(40);
                canvas.drawText("Trip Plan", pageWidth / 2, 350, titlePaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText("Trip   " + tripTitle.getText(), 20, 440, myPaint);

//                canvas.drawLine(680, 600, 700, 610, myPaint);

                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText("From   " + tripFrom.getText(), 20, 490, myPaint);
                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                canvas.drawText("Time   " + fromTime.getText(), 20, 490, myPaint);


                myPaint.setTextAlign(Paint.Align.RIGHT);
                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText("To   " + tripTO.getText(), pageWidth - 20, 440, myPaint);
                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                canvas.drawText("Time   " + toTime.getText(), pageWidth - 20, 440, myPaint);
//                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText("Days  " + noOfDays.getText(), pageWidth - 20, 490, myPaint);

                if (item1spinner.getSelectedItemPosition() == 1) {
                    canvas.drawBitmap(scaledflight, 30, 600, myPaint);
                }
                if (item1spinner.getSelectedItemPosition() == 2) {
                    canvas.drawBitmap(scaledcar, 30, 600, myPaint);
                }
                if (item1spinner.getSelectedItemPosition() == 3) {
                    canvas.drawBitmap(scaledtrain, 30, 600, myPaint);
                }
                if (item1spinner.getSelectedItemPosition() == 4) {
                    canvas.drawBitmap(scaledbus, 30, 600, myPaint); }


                if (item2spinner.getSelectedItemPosition() == 1) {
                    canvas.drawBitmap(scaledhotel, 30, 800, myPaint);
                }
                if (item2spinner.getSelectedItemPosition() == 2) {
                    canvas.drawBitmap(scaledmotel, 30, 800, myPaint);
                }
                if (item2spinner.getSelectedItemPosition() == 3) {
                    canvas.drawBitmap(scaledresort, 30, 800, myPaint); }


                if (item3spinner.getSelectedItemPosition() == 1) {
                    canvas.drawBitmap(scaledrestaurant, 30, 1000, myPaint);
                }
                if (item3spinner.getSelectedItemPosition() == 2) {
                    canvas.drawBitmap(scaledbar, 30, 1000, myPaint); }


                if (item4spinner.getSelectedItemPosition() == 1) {
                    canvas.drawBitmap(scaledbowling, 30, 1200, myPaint);
                }
                if (item4spinner.getSelectedItemPosition() == 2) {
                    canvas.drawBitmap(scaledconcert, 30, 1200, myPaint);
                }
                if (item4spinner.getSelectedItemPosition() == 3) {
                    canvas.drawBitmap(scaledcasino, 30, 1200, myPaint);
                }
                if (item4spinner.getSelectedItemPosition() == 4) {
                    canvas.drawBitmap(scaledgame, 30, 1200, myPaint); }



                int total1 = 0, total2 = 0, total3 = 0, total4 = 0;
                if (item1spinner.getSelectedItemPosition() != 0) {
                    canvas.drawText(option1name.getText().toString(), 400, 650, myPaint);
                    canvas.drawText("$" + price1.getText().toString(), 850, 650, myPaint);
                    canvas.drawText(qty1.getText().toString(), 970, 650, myPaint);
                    total1 = (int) (Integer.parseInt(qty1.getText().toString()) * Float.parseFloat(price1.getText().toString()));
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("$" + total1, pageWidth - 40, 650, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT); }

                if (item2spinner.getSelectedItemPosition() != 0) {
                    canvas.drawText(option2name.getText().toString(), 350, 850, myPaint);
                    canvas.drawText("$" + price2.getText().toString(), 800, 850, myPaint);
                    canvas.drawText(qty2.getText().toString(), 950, 850, myPaint);
                    total2 = (int) (Integer.parseInt(qty2.getText().toString()) * Float.parseFloat(price2.getText().toString()));
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("$" + total2, pageWidth - 40, 850, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT); }

                if (item3spinner.getSelectedItemPosition() != 0) {
                    canvas.drawText(option3name.getText().toString(), 350, 1050, myPaint);
                    canvas.drawText("$" + price3.getText().toString(), 800, 1050, myPaint);
                    canvas.drawText(qty3.getText().toString(), 950, 1050, myPaint);
                    total3 = (int) (Integer.parseInt(qty3.getText().toString()) * Float.parseFloat(price3.getText().toString()));
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("$" + total3, pageWidth - 40, 1050, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT); }

                if (item4spinner.getSelectedItemPosition() != 0) {
                    canvas.drawText(option4name.getText().toString(), 350, 1250, myPaint);
                    canvas.drawText("$" + price4.getText().toString(), 800, 1250, myPaint);
                    canvas.drawText(qty1.getText().toString(), 950, 1250, myPaint);
                    total4 = (int) (Integer.parseInt(qty4.getText().toString()) * Float.parseFloat(price4.getText().toString()));
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("$" + total4, pageWidth - 40, 1250, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT); }

                float subtotal = total1 + total2 + total3 + total4;
//                canvas.drawLine(680, 1200, pageWidth - 20, 1200, myPaint);
                canvas.drawText("Total", 450, 1500, myPaint);
                canvas.drawText("", 650, 1500, myPaint);
                myPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.valueOf(subtotal), 850, 1500, myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Tax(12%)", 450, 1550, myPaint);
                canvas.drawText("", 650, 1550, myPaint);
                myPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.valueOf(subtotal * 12 / 100), 850, 1550, myPaint);
                myPaint.setTextAlign(Paint.Align.LEFT);

                myPaint.setColor(Color.rgb(221, 235, 238));
                canvas.drawRect(20, 1750, pageWidth - 20, 1650, myPaint);

                myPaint.setColor(Color.BLACK);
                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(45);
                canvas.drawText("Total", 450, 1715, myPaint);
                myPaint.setTextSize(45);
                myPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.valueOf(subtotal + (subtotal * 12 / 100)), 850, 1715, myPaint);

                myPdfDocument.finishPage(myPage1);

                PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(1200, 2010, 2).create();
                PdfDocument.Page myPage2 = myPdfDocument.startPage(myPageInfo2);
                Canvas canvas2 = myPage2.getCanvas();

                canvas2.drawBitmap(scaledbmp, 0, 0, myPaint2);

                titlePaint2.setTextAlign(Paint.Align.CENTER);
                titlePaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint2.setTextSize(45);
                canvas2.drawText("Travel Itinerary", pageWidth / 2, 350, titlePaint2);


                if (itinerary1spinner.getSelectedItemPosition() != 0) {
                    myPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    myPaint2.setTextAlign(Paint.Align.LEFT);
                    myPaint2.setTextSize(35);
                    canvas2.drawText(itiName1.getText().toString(),500, 500, myPaint2 );
                    canvas2.drawText(String.valueOf(itiDatePicker1.getText()),500,450,myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() != 0) {
                    myPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    myPaint2.setTextAlign(Paint.Align.LEFT);
                    myPaint2.setTextSize(35);
                    canvas2.drawText(itiName2.getText().toString(),500, 750, myPaint2 );
                    canvas2.drawText(String.valueOf(itiDatePicker2.getText()),500,700,myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() != 0) {
                    myPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    myPaint2.setTextAlign(Paint.Align.LEFT);
                    myPaint2.setTextSize(35);
                    canvas2.drawText(itiName3.getText().toString(),500, 1000, myPaint2 );
                    canvas2.drawText(String.valueOf(itiDatePicker3.getText()),500,950,myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() != 0) {
                    myPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    myPaint2.setTextAlign(Paint.Align.LEFT);
                    myPaint2.setTextSize(35);
                    canvas2.drawText(itiName4.getText().toString(),500, 1250, myPaint2 );
                    canvas2.drawText(String.valueOf(itiDatePicker4.getText()),500,1200,myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() != 0) {
                    myPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    myPaint2.setTextAlign(Paint.Align.LEFT);
                    myPaint2.setTextSize(35);
                    canvas2.drawText(itiName5.getText().toString(),500, 1500, myPaint2 );
                    canvas2.drawText(String.valueOf(itiDatePicker5.getText()),500,1450,myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() != 0) {
                    myPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    myPaint2.setTextAlign(Paint.Align.LEFT);
                    myPaint2.setTextSize(35);
                    canvas2.drawText(itiName6.getText().toString(),500, 1750, myPaint2 );
                    canvas2.drawText(String.valueOf(itiDatePicker6.getText()),500,1700,myPaint2);
                }


                if (itinerary1spinner.getSelectedItemPosition() == 1) {
                    canvas2.drawBitmap(spark, 150, 400, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 2) {
                    canvas2.drawBitmap(szoo, 150, 400, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 3) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(smuseum, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 4) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(saquarium, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 5) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(smountain, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 6) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(smeeting, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 7) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(spresentation, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 8) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(scontest, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 9) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(scycling, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 10) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(sswimming, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 11) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(strekking, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 12) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(sbeach, 150, 400, myPaint2); }
                if (itinerary1spinner.getSelectedItemPosition() == 13) {
                    canvas2.drawBitmap(scaledline, 180, 550, myPaint2);
                    canvas2.drawBitmap(sskatepark, 150, 400, myPaint2);
                }


                if (itinerary2spinner.getSelectedItemPosition() == 1) {
                    canvas2.drawBitmap(spark, 150, 650, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 2) {
                    canvas2.drawBitmap(szoo, 150, 650, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 3) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(smuseum, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 4) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(saquarium, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 5) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(smountain, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 6) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(smeeting, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 7) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(spresentation, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 8) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(scontest, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 9) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(scycling, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 10) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(sswimming, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 11) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(strekking, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 12) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(sbeach, 150, 650, myPaint2); }
                if (itinerary2spinner.getSelectedItemPosition() == 13) {
                    canvas2.drawBitmap(scaledline, 180, 800, myPaint2);
                    canvas2.drawBitmap(sskatepark, 150, 650, myPaint2);
                }


                if (itinerary3spinner.getSelectedItemPosition() == 1) {
                    canvas2.drawBitmap(spark, 150, 900, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 2) {
                    canvas2.drawBitmap(szoo, 150, 900, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 3) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(smuseum, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 4) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(saquarium, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 5) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(smountain, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 6) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(smeeting, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 7) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(spresentation, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 8) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(scontest, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 9) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(scycling, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 10) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(sswimming, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 11) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(strekking, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 12) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(sbeach, 150, 900, myPaint2); }
                if (itinerary3spinner.getSelectedItemPosition() == 13) {
                    canvas2.drawBitmap(scaledline, 180, 1050, myPaint2);
                    canvas2.drawBitmap(sskatepark, 150, 900, myPaint2);
                }

                if (itinerary4spinner.getSelectedItemPosition() == 1) {
                    canvas2.drawBitmap(spark, 150, 1150, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 2) {
                    canvas2.drawBitmap(szoo, 150, 1150, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 3) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(smuseum, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 4) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(saquarium, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 5) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(smountain, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 6) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(smeeting, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 7) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(spresentation, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 8) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(scontest, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 9) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(scycling, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 10) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(sswimming, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 11) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(strekking, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 12) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(sbeach, 150, 1150, myPaint2); }
                if (itinerary4spinner.getSelectedItemPosition() == 13) {
                    canvas2.drawBitmap(scaledline, 180, 1300, myPaint2);
                    canvas2.drawBitmap(sskatepark, 150, 1150, myPaint2);
                }

                if (itinerary5spinner.getSelectedItemPosition() == 1) {
                    canvas2.drawBitmap(spark, 150, 1400, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 2) {
                    canvas2.drawBitmap(szoo, 150, 1400, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 3) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(smuseum, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 4) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(saquarium, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 5) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(smountain, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 6) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(smeeting, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 7) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(spresentation, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 8) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(scontest, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 9) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(scycling, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 10) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(sswimming, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 11) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(strekking, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 12) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(sbeach, 150, 1400, myPaint2); }
                if (itinerary5spinner.getSelectedItemPosition() == 13) {
                    canvas2.drawBitmap(scaledline, 180, 1550, myPaint2);
                    canvas2.drawBitmap(sskatepark, 150, 1400, myPaint2);
                }

                if (itinerary6spinner.getSelectedItemPosition() == 1) {
                    canvas2.drawBitmap(spark, 150, 1650, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 2) {
                    canvas2.drawBitmap(szoo, 150, 1650, myPaint2);
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 3) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(smuseum, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 4) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(saquarium, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 5) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(smountain, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 6) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(smeeting, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 7) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(spresentation, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 8) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(scontest, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 9) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(scycling, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 10) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(sswimming, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 11) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(strekking, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 12) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(sbeach, 150, 1650, myPaint2); }
                if (itinerary6spinner.getSelectedItemPosition() == 13) {
                    canvas2.drawBitmap(scaledline, 180, 1800, myPaint2);
                    canvas2.drawBitmap(sskatepark, 150, 1650, myPaint2);
                }

                myPdfDocument.finishPage(myPage2);

                File file = new File(Environment.getExternalStorageDirectory(), "/Traveler's_Diaries_Trip_Plan.pdf");

                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myPdfDocument.close();
            }

            Intent intent = new Intent(TripPlanningActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}