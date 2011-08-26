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

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class Close extends Activity {

	private WakeLock wl;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.close);

		((TextView) findViewById(R.id.invite)).append(
				" " + Open.PRICE_PER_MINUTE + "€ pro min");

		TelephonyManager phone = (TelephonyManager) 
				getSystemService(TELEPHONY_SERVICE);
		String phonenumber = phone.getLine1Number();
		if (phonenumber == null || phonenumber.equals(""))
			phonenumber = "xyz";
		// TODO Ask User for PhoneNumber
		((TextView) findViewById(R.id.phonenumber)).setText(phonenumber);
		
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
