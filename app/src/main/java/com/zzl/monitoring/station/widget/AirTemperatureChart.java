package com.zzl.monitoring.station.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.zzl.monitoring.station.R;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

/**
 * Create by zilu 2020/09/03
 */
public class AirTemperatureChart extends LineChart {

    private int COLOR_AZURE;
    private int LINE;

    public AirTemperatureChart(Context context) {
        super(context);
    }

    public AirTemperatureChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AirTemperatureChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        COLOR_AZURE = getResources().getColor(R.color.azure);
        LINE = getResources().getColor(R.color.line);

        getDescription().setEnabled(false);
        setDrawGridBackground(false);
        setHighlightPerDragEnabled(true);
        setDragEnabled(true);
        setDrawBorders(false);
        animateX(1000);
        getLegend().setEnabled(false);

        getXAxis().setEnabled(false);

        YAxis yAxis = getAxisLeft();
        yAxis.setTextSize(7f);
        yAxis.setTextColor(ColorTemplate.getHoloBlue());
        yAxis.setGridColor(LINE);
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisMaximum(100);
        yAxis.setLabelCount(10);

        getAxisRight().setEnabled(false);

        setData();

    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();

//        for (int i = 1; i <= count; i++) {
//            float val = (float) (Math.random() * range);
//            values.add(new Entry(i, val));
//        }

        LineDataSet set1;

        if (getData() != null &&
                getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            getData().notifyDataChanged();
            notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // black lines and points
            set1.setColor(COLOR_AZURE);
            set1.setCircleColor(COLOR_AZURE);
            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(1f);

            // draw points as solid circles
//            set1.setDrawCircleHole(false);
            set1.setDrawCircles(false);

            // text size of values
            set1.setValueTextSize(5f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_azure);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(5f);
            data.setDrawValues(false);

            // set data
            setData(data);
        }
    }

    public void addEntry(float value) {

        LineData data = getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                return;
            }

            data.addEntry(new Entry(set.getEntryCount(), value), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            notifyDataSetChanged();

            // limit the number of visible entries
            setVisibleXRangeMaximum(24);
            // setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }
}
