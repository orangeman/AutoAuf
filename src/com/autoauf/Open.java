// Copyright 2011 autoauf.com contributors. All rights reserved.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.

// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.autoauf;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.TextView;

public class Open extends Activity {
	
	static final double PRICE_PER_MINUTE = 0.17; //€

	private long startTime;
	private Handler handler;
	private Runnable updateTime;
	private TextView welcome;
	private TextView price;
	private TextView time;

	private WakeLock wl;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handler = new Handler();
		setContentView(R.layout.open);
		startTime = System.currentTimeMillis();

		welcome = (TextView) findViewById(R.id.welcome);
		welcome.append(" "+getIntent().getDataString()); // SMS orig phonenumber
		price = (TextView) findViewById(R.id.price);
		time = (TextView) findViewById(R.id.time);
		
		updateTime = new Runnable() {
			
			@Override
			public void run() {
				long seconds = (System.currentTimeMillis()-startTime) / 1000;
				price.setText(new DecimalFormat("€ #.##")
						.format((seconds/60f)*PRICE_PER_MINUTE));
				time.setText(String.format("%dh %dm %ds", 
						seconds / 3600,
						(seconds / 60) % 60,
						seconds % 60));
				handler.postDelayed(updateTime, 1000);
			}
		};

		handler.postDelayed(updateTime, 1000);

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
	}

	@Override
	protected void onPause() {
		super.onPause();
		wl.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		wl.acquire();
	}

}
