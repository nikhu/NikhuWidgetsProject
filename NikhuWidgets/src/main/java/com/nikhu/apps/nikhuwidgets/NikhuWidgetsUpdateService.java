package com.nikhu.apps.nikhuwidgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nikhu.apps.nikhuwidgets.appwidget.ForexAppWidgetProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by bujji on 7/9/13.
 */
public class NikhuWidgetsUpdateService extends Service {
    private static final String TAG = NikhuWidgetsUpdateService.class.getName();
    private static final String FOREX_URL = "http://192.168.72.135:8080/forex/api/USD/INR/1";
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
        // Creating volley request queue.
        requestQueue = Volley.newRequestQueue(this);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());
        ComponentName forexAppWidget = new ComponentName(getApplicationContext(),
                ForexAppWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(forexAppWidget);

        // Register an onClickListener
        Intent clickIntent = new Intent(this.getApplicationContext(),
                ForexAppWidgetProvider.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                allWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 20 * 1000, pendingIntent);
        RemoteViews remoteViews = new RemoteViews(this
                .getApplicationContext().getPackageName(),
                R.layout.widget);
        remoteViews.setOnClickPendingIntent(R.id.textView2, pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        ComponentName thisWidget = new ComponentName(getApplicationContext(),
                ForexAppWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        Log.w(TAG, "Widgets (ForexAppWidgetProvider) Count:" + String.valueOf(allWidgetIds.length));

        for (final int widgetId : allWidgetIds) {
            Log.w(TAG, "Widget Id:" + String.valueOf(widgetId));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FOREX_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG, response.toString());
                    Log.i(TAG, "" + getPackageName());
                    String value = getCurrencyValue(response);
                    RemoteViews remoteViews = new RemoteViews(getPackageName(),
                            R.layout.widget);
                    // Set the text
                    remoteViews.setTextViewText(R.id.textView2,
                            value);
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((error != null) && (error.getMessage() != null)) {
                        Log.i(TAG, error.getMessage());
                    }
                    Log.i(TAG, "Error occurred while invoking service.");
                }
            }
            );
            requestQueue.add(jsonObjectRequest);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getCurrencyValue(JSONObject response) {
        try {
            JSONObject exchangeRate = response.getJSONObject("exchangeRate");
            String value = exchangeRate.getString("value");
            return value;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}

