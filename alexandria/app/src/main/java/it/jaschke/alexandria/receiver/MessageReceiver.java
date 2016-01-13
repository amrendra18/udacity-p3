package it.jaschke.alexandria.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import it.jaschke.alexandria.activity.MainActivity;

/**
 * Created by Amrendra Kumar on 14/01/16.
 */
public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(MainActivity.MESSAGE_KEY) != null) {
            Toast.makeText(context, intent.getStringExtra(MainActivity.MESSAGE_KEY), Toast.LENGTH_LONG).show();
        }
    }
}