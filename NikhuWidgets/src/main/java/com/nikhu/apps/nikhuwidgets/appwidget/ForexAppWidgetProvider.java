package com.nikhu.apps.nikhuwidgets.appwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.nikhu.apps.nikhuwidgets.NikhuWidgetsUpdateService;
import com.nikhu.apps.nikhuwidgets.R;

public class ForexAppWidgetProvider extends AppWidgetProvider {
	static final String TAG = ForexAppWidgetProvider.class.getName();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.v(TAG, "onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                ForexAppWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(),
                NikhuWidgetsUpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
        context.startService(intent);
	}

    @Override
    public void onDisabled(Context context) {
        Log.v(TAG, "onDisabled");
        super.onDisabled(context);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context,
                ForexAppWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        // Stops the service.
        Intent serviceIntent = new Intent(context, NikhuWidgetsUpdateService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        context.stopService(serviceIntent);
        Log.v(TAG, "Stop the service.");
        // Cancel the alarm manager.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ForexAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                allWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Log.v(TAG, "Cancelled alarm.");
    }
}
