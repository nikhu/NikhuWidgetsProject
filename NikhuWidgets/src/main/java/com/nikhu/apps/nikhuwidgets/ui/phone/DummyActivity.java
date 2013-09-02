package com.nikhu.apps.nikhuwidgets.ui.phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.nikhu.apps.nikhuwidgets.R;

public class DummyActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sendBroadcast(new Intent(Intent.ACTION_MAIN)
				.addCategory(Intent.CATEGORY_HOME));
		Toast.makeText(this, R.string.dummy_help_text, Toast.LENGTH_LONG)
				.show();
		finish();
		super.onCreate(savedInstanceState);
	}

}
