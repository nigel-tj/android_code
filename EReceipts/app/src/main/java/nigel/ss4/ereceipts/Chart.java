package nigel.ss4.ereceipts;

import java.util.*;
import java.io.*;
import java.text.DateFormat;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint.Align;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import org.json.*;

public class Chart extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    void log(String msg) {
        Log.d("graph",msg);
    }

    public void onClickMe(View v) {
        Intent chart = ChartFactory.getLineChartIntent(getApplicationContext(), getDataset(), getRenderer());
        startActivity(chart);
    }

    private XYMultipleSeriesRenderer getRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setXLabelsColor(Color.YELLOW);
        renderer.setShowGrid(true);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setChartTitle("Closure Profile");
        renderer.setXTitle("Date");
        renderer.setYTitle("Closure");
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.WHITE);
        renderer.addSeriesRenderer(r);
        renderer.setZoomButtonsVisible(true);
        return renderer;
    }

    private XYMultipleSeriesDataset getDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        TimeSeries series = new TimeSeries("Demo");
        AssetManager am = getAssets();
        try {
            InputStream is = am.open("closure.json");
            String json = readStream(is);
            is.close();
            JSONObject obj = (JSONObject)new JSONTokener(json).nextValue();
            JSONArray arr = obj.getJSONArray("data");
            double closure = 0.0;
            for (int i = 0; i < arr.length(); i++) {
                JSONArray point = arr.getJSONArray(i);
                Date d = new Date(point.getLong(0));
                double val = point.getDouble(1);
                closure += val;
                series.add(d,closure);
            }
        } catch (Exception e) {
            Log.d("graph",e.getMessage());
        }
        dataset.addSeries(series);
        return dataset;
    }

    private String readStream(InputStream is) {
        ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        int n = 0;
        try {
            while (-1 != (n = is.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.d("graph",e.getMessage());
            return null;
        }
        return output.toString();
    }

}