package com.example.travelapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Map;
import com.anychart.core.map.series.Choropleth;
import com.anychart.enums.HAlign;
import com.anychart.enums.SelectionMode;
import com.anychart.scales.LinearColor;
import com.example.travelapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);


        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Map map = AnyChart.map();

        map.title()
                .enabled(true)
                .useHtml(true)
                .hAlign(String.valueOf(HAlign.CENTER))
                .fontFamily("Verdana, Helvetica, Arial, sans-serif")
                .padding(20, 0, 10, 0)
                .text("<span style=\"color:#7c868e; font-size: 25 px\"> Travel Map" +
//                        "Love Wine The Most.</span> <br>" +
                        "<br><span style=\"color:#545f69; font-size: 14px\"> United States of America" +
                        "</span>");
//
//        map.credits()
//                .enabled(true)
//                .url("https://en.wikipedia.org/wiki/List_of_sovereign_states_and_dependent_territories_by_population_density")
//                .text("Data source: https://en.wikipedia.org/wiki/List_of_sovereign_states_and_dependent_territories_by_population_density");
////                .logoSrc("https://static.anychart.com/images/maps_samples/USA_Map_with_Linear_Scale/favicon.ico");

        map.geoData("anychart.maps.united_states_of_america");

//        ColorRange colorRange = map.colorRange();
//        colorRange.enabled(false)
//                .colorLineSize(10)
//                .stroke("#B9B9B9")
//                .labels("{ 'padding': 3 }")
//                .labels("{ 'size': 7 }");
//        colorRange.ticks()
//                .enabled(true)
//                .stroke("#B9B9B9")
//                .position(SidePosition.OUTSIDE)
//                .length(10);
//        colorRange.minorTicks()
//                .enabled(true)
//                .stroke("#B9B9B9")
//                .position("outside")
//                .length(5);

        map.interactivity().selectionMode(SelectionMode.NONE);
        map.padding(0, 0, 0, 0);

        Choropleth series = map.choropleth(getData());
        LinearColor linearColor = LinearColor.instantiate();
        linearColor.colors(new String[]{ "#81d4fa", "#81d4fa", "#81d4fa", "#81d4fa"});
        series.colorScale(linearColor);
        series.hovered()
                .fill("#2E6171")
                .stroke("#2E6171");
        series.selected()
                .fill("#2E6171")
                .stroke("#2E6171");
        series.labels(true).enabled(true);
        series.labels().fontSize(10);
        series.labels().fontColor("ffffff");
        series.labels().format("{%Value}");

        series.tooltip()
                .useHtml(true)
                .format("function() {\n" +
                        "            return '<span style=\"font-size: 13px\"> </span>';\n" +
                        "          }");


        anyChartView.addScript("file:///android_asset/united_states_of_america.js");
        anyChartView.addScript("file:///android_asset/proj4.js");
        anyChartView.setChart(map);



    }

    private List<DataEntry> getData() {
        List<DataEntry> data = new ArrayList<>();

//        data.add(new CustomDataEntry("US.MN", "Minnesota", 8.4));
//        data.add(new CustomDataEntry("US.MT", "Montana", 8.5));
//        data.add(new CustomDataEntry("US.ND", "North Dakota", 5.1));
//        data.add(new CustomDataEntry("US.ID", "Idaho", 8));
//        data.add(new CustomDataEntry("US.WA", "Washington", 13.1));
//        data.add(new CustomDataEntry("US.AZ", "Arizona", 9.7));
//        data.add(new CustomDataEntry("US.CA", "California", 14));
//        data.add(new CustomDataEntry("US.CO", "Colorado", 8.7));
//        data.add(new CustomDataEntry("US.NV", "Nevada", 14.7));
//        data.add(new CustomDataEntry("US.NM", "New Mexico", 6.9));
//        data.add(new CustomDataEntry("US.OR", "Oregon", 12.2));
//        data.add(new CustomDataEntry("US.UT", "Utah", 3.2));
//        data.add(new CustomDataEntry("US.WY", "Wyoming", 5.2));
//        data.add(new CustomDataEntry("US.AR", "Arkansas", 4.2));
//        data.add(new CustomDataEntry("US.IA", "Iowa", 4.7));
//        data.add(new CustomDataEntry("US.KS", "Kansas", 3.2));
//        data.add(new CustomDataEntry("US.MO", "Missouri", 7.2));
//        data.add(new CustomDataEntry("US.NE", "Nebraska", 5));
//        data.add(new CustomDataEntry("US.OK", "Oklahoma", 4.5));
//        data.add(new CustomDataEntry("US.SD", "South Dakota", 5));
//        data.add(new CustomDataEntry("US.LA", "Louisiana", 5.7));
//        data.add(new CustomDataEntry("US.TX", "Texas", 5));
//        data.add(new CustomDataEntry("US.CT", "Connecticut", 14.4, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.MA", "Massachusetts", 16.9, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.NH", "New Hampshire", 19.6));
//        data.add(new CustomDataEntry("US.RI", "Rhode Island", 14, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.VT", "Vermont", 17.5));
//        data.add(new CustomDataEntry("US.AL", "Alabama", 6));
//        data.add(new CustomDataEntry("US.FL", "Florida", 12.4));
//        data.add(new CustomDataEntry("US.GA", "Georgia", 5.9));
//        data.add(new CustomDataEntry("US.MS", "Mississippi", 2.8));
//        data.add(new CustomDataEntry("US.SC", "South Carolina", 6.1));
//        data.add(new CustomDataEntry("US.IL", "Illinois", 10.2));
//        data.add(new CustomDataEntry("US.IN", "Indiana", 6.1));
//        data.add(new CustomDataEntry("US.KY", "Kentucky", 3.9));
//        data.add(new CustomDataEntry("US.NC", "North Carolina", 6.6));
//        data.add(new CustomDataEntry("US.OH", "Ohio", 7.2));
//        data.add(new CustomDataEntry("US.TN", "Tennessee", 5.4));
//        data.add(new CustomDataEntry("US.VA", "Virginia", 10.7));
//        data.add(new CustomDataEntry("US.WI", "Wisconsin", 9.1));
//        data.add(new CustomDataEntry("US.WY", "Wyoming", 5.2, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.WV", "West Virginia", 2.4));
//        data.add(new CustomDataEntry("US.DE", "Delaware", 13.5, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.DC", "District of Columbia", 25.7, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.MD", "Maryland", 8.9, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.NJ", "New Jersey", 14.9, new LabelDataEntry(false)));
//        data.add(new CustomDataEntry("US.NY", "New York", 11.9));
//        data.add(new CustomDataEntry("US.PA", "Pennsylvania", 5.6));
//        data.add(new CustomDataEntry("US.ME", "Maine", 10.4));
//        data.add(new CustomDataEntry("US.HI", "Hawaii", 13.1));
//        data.add(new CustomDataEntry("US.AK", "Alaska", 10.9));
//        data.add(new CustomDataEntry("US.MI", "Michigan", 7.6));

        data.add(new CustomDataEntry("US.MN", "Minnesota", 8.4,new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.MT", "Montana", 8.5, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.ND", "North Dakota", 5.1, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.ID", "Idaho", 8, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.WA", "Washington", 13.1, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.AZ", "Arizona", 9.7, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.CA", "California", 14, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.CO", "Colorado", 8.7, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.NV", "Nevada", 14.7, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.NM", "New Mexico", 6.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.OR", "Oregon", 12.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.UT", "Utah", 3.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.WY", "Wyoming", 5.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.AR", "Arkansas", 4.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.IA", "Iowa", 4.7, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.KS", "Kansas", 3.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.MO", "Missouri", 7.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.NE", "Nebraska", 5, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.OK", "Oklahoma", 4.5, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.SD", "South Dakota", 5, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.LA", "Louisiana", 5.7, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.TX", "Texas", 5, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.CT", "Connecticut", 14.4, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.MA", "Massachusetts", 16.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.NH", "New Hampshire", 19.6, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.RI", "Rhode Island", 14, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.VT", "Vermont", 17.5, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.AL", "Alabama", 6, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.FL", "Florida", 12.4, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.GA", "Georgia", 5.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.MS", "Mississippi", 2.8, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.SC", "South Carolina", 6.1, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.IL", "Illinois", 10.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.IN", "Indiana", 6.1, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.KY", "Kentucky", 3.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.NC", "North Carolina", 6.6, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.OH", "Ohio", 7.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.TN", "Tennessee", 5.4, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.VA", "Virginia", 10.7, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.WI", "Wisconsin", 9.1, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.WY", "Wyoming", 5.2, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.WV", "West Virginia", 2.4, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.DE", "Delaware", 13.5, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.DC", "District of Columbia", 25.7, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.MD", "Maryland", 8.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.NJ", "New Jersey", 14.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.NY", "New York", 11.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.PA", "Pennsylvania", 5.6, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.ME", "Maine", 10.4, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.HI", "Hawaii", 13.1, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.AK", "Alaska", 10.9, new LabelDataEntry(false)));
        data.add(new CustomDataEntry("US.MI", "Michigan", 7.6, new LabelDataEntry(false)));

        return data;
    }

    class CustomDataEntry extends DataEntry {
        public CustomDataEntry(String id, String name, Number value) {
            setValue("id", id);
            setValue("name", name);
            setValue("value", value);
        }
        public CustomDataEntry(String id, String name, Number value, LabelDataEntry label) {
            setValue("id", id);
            setValue("name", name);
            setValue("value", value);
            setValue("label", label);
        }
    }

    class LabelDataEntry extends DataEntry {
        LabelDataEntry(Boolean enabled) {
            setValue("enabled", enabled);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_search:
                    Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_inventory:
                    Intent intent3 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_wishlist:
                    Intent intent4 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_profile:
                    Intent intent5 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent5);
                    break;

            }
            return false;
        }

    };

}

