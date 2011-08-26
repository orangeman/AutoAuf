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

import android.os.Handler;
import android.util.Log;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.api.exception.IncompatibilityException;

public class Auto {

	static IOIO ioio;
	static PwmOutput servo;
	static Handler handler = new Handler();
	
	static { init(); }
	
	static void init() {
		if (ioio != null)
			ioio.disconnect();
		ioio = IOIOFactory.create();
		Log.d("IOIO", "factory created");
		try {
			ioio.waitForConnect();
			Log.d("IOIO", "connection established");
			servo = ioio.openPwmOutput(3, 50);
			Log.d("IOIO", "servo connected");
		} catch (ConnectionLostException e) {
			Log.e("IOIO", "connection lost");
			e.printStackTrace();
		} catch (IncompatibilityException e) {
			Log.e("IOIO", "incompatibility");
			e.printStackTrace();
		}
	}
	
	static void auf() {
		try {
			Log.d("Auto", "AUF");
			servo.setDutyCycle((float) (0.045));
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					try {
						servo.setDutyCycle((float) (0.075));
					} catch (ConnectionLostException e) {
						e.printStackTrace();
					}
				}
			}, 500);
		} catch (ConnectionLostException e) {
			init();
			auf();
		}
	}
	
	static void zu() {
		try {
			servo.setDutyCycle((float) (0.105));
			Log.d("Auto", "ZU");
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					try {
						servo.setDutyCycle((float) (0.075));
					} catch (ConnectionLostException e) {
						e.printStackTrace();
					}
				}
			}, 500);
		} catch (Exception e) {
			init();
			zu();
		}
	}
	
}
