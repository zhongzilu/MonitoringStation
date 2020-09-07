package com.zzl.monitoring.station;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;

import com.zzl.monitoring.station.serialport.ComData;
import com.zzl.monitoring.station.serialport.Packet;
import com.zzl.monitoring.station.serialport.RealtimeData;
import com.zzl.monitoring.station.serialport.SerialPortHandler;
import com.zzl.monitoring.station.widget.AirMoistureChart;
import com.zzl.monitoring.station.widget.AirTemperatureChart;
import com.zzl.monitoring.station.widget.SimpleTotalRadiationChart;
import com.zzl.monitoring.station.widget.WindDirectionView;
import com.zzl.serialport.MyFunc;
import com.zzl.serialport.SerialPortClient;
import com.zzl.serialport.SerialPortManager;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements Handler.Callback, SerialPortHandler.Callback {

    private TextView mainTitleText, pressureText, ultravioletRaysText, pmText, windSpeedText;
    private AirTemperatureChart airTempChart;
    private AirMoistureChart airMoistureChart;
    private SimpleTotalRadiationChart simpleRadiationChart;
    private WindDirectionView windDirView;

    private SerialPortClient client;

    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTitleText = findViewById(R.id.mainTitleText);
        pressureText = findViewById(R.id.pressureText);
        ultravioletRaysText = findViewById(R.id.ultravioletRaysText);
        pmText = findViewById(R.id.pmText);
        windSpeedText = findViewById(R.id.windSpeedText);
        windDirView = findViewById(R.id.windDirectionView);

        airTempChart = findViewById(R.id.airTempChart);
        airMoistureChart = findViewById(R.id.airMoistureChart);
        simpleRadiationChart = findViewById(R.id.simpleRadiationChart);

        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mainTitleText.setText(buildMainTitleSpannable());
        pressureText.setText(buildItemSpannable(R.string.format_pressure, 0));
        ultravioletRaysText.setText(buildItemSpannable(R.string.format_ultraviolet_rays, 0));
        pmText.setText(buildItemSpannable(R.string.format_pm, 0));

        initSerialPort();
    }

    private void initSerialPort() {
        client = SerialPortManager.get()
                .setPort("/dev/ttyS1")
                .setBaudRate(9600)
                .setDataHandler(new SerialPortHandler(this))
                .create();

        client.open();

        client.post(Packet.getRealtimeDateCMD());
    }

    private void addEntry() {
        airTempChart.addEntry((float) (Math.random() * 40));
        airMoistureChart.addEntry((float) (Math.random() * 40));
        simpleRadiationChart.addEntry((float) (Math.random() * 40));
    }

    @Override
    public boolean handleMessage(Message msg) {
        client.post(Packet.getRealtimeDateCMD());
//        addEntry();
        handler.sendEmptyMessageDelayed(0, 1000);
        return true;
    }

    private SpannableStringBuilder buildItemSpannable(int resId, float value) {
        String text = getString(resId, value);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        int start = text.indexOf(" ") + 1;

        RelativeSizeSpan size = new RelativeSizeSpan(.5f);
        builder.setSpan(size, start, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }

    private SpannableStringBuilder buildMainTitleSpannable() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E hh:mm", Locale.CHINA);

        String text = getString(R.string.format_main_title_text, format.format(new Date()));
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        int start = text.indexOf("\n") + 1;
        RelativeSizeSpan size = new RelativeSizeSpan(.6f);
        builder.setSpan(size, start, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }

    @Override
    public void onReceivedData(ComData bean) {
//        parseReceiveDate(bean);
        RealtimeData data = Packet.parseRealtimeResp(bean.bRec);

        Log.e("PortTest-->", Arrays.toString(data.getChannel()));

        windSpeedText.setText(getString(R.string.format_wind_speed_text, data.getChannel(Channel.WIND_SPEED) / 10f));
        windDirView.setDirection(data.getChannel(Channel.WIND_DIR));
        pressureText.setText(buildItemSpannable(R.string.format_pressure,data.getChannel(Channel.PRESSURE)));
        airTempChart.addEntry(data.getChannel(Channel.TEMPERATURE));
        airMoistureChart.addEntry(data.getChannel(Channel.MOISTURE));
        simpleRadiationChart.addEntry(data.getChannel(Channel.SIMPLE_TOTAL_RADIATION));
        ultravioletRaysText.setText(buildItemSpannable(R.string.format_ultraviolet_rays, data.getChannel(Channel.U_RAY) / 100f));
    }

    private String parseReceiveDate(ComData bean) {
        String hex = MyFunc.ByteArrToHex(bean.bRec);
        Log.e("PortTest-->", "onReceivedData: " + hex);
        return hex;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing() && client != null) {
            client.close();
        }
    }
}
