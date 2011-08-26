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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent it) {

		if (it.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

			Toast.makeText(context, "SMS Received", Toast.LENGTH_LONG);

			if (it.getExtras() != null) {
				Object[] pdus = (Object[]) it.getExtras().get("pdus");
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);

				if (sms.getMessageBody().toUpperCase().contains("AUF"))
					context.startActivity(new Intent(context, Open.class)
							.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(
									Uri.parse("tel:"
											+ sms.getOriginatingAddress())));
				if (sms.getMessageBody().toUpperCase().contains("ZU"))
					context.startActivity(new Intent(context, Close.class)
							.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
		}
	}
}
