package com.nikhu.apps.nikhuwidgets.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

public class ForexAppWidgetProvider extends AppWidgetProvider {
	static final String TAG = ForexAppWidgetProvider.class.getName();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.v(TAG, "onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
