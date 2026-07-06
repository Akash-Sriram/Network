package de.mangelow.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class RadioNetwork extends Activity {

    private static final String TAG = "RadioNetwork";
    private static final int PERMISSION_REQUEST_CODE = 101;
    private Context context;	
    private TelephonyManager tm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");

        context = getApplicationContext();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Requesting READ_PHONE_STATE permission");
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
                return;
            }
        }

        showNetworkTypeAndLaunchSettings();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
                showNetworkTypeAndLaunchSettings();
            } else {
                Log.w(TAG, "Permission denied by user");
                Toast.makeText(context, "Permission Denied! Cannot read network type.", Toast.LENGTH_LONG).show();
                launchSettingsAndFinish();
            }
        }
    }

    private void showNetworkTypeAndLaunchSettings() {
        String type = "UNKNOWN";
        try {
            int networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                networkType = tm.getDataNetworkType();
            } else {
                networkType = tm.getNetworkType();
            }

            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS: type = "GPRS"; break;
                case TelephonyManager.NETWORK_TYPE_EDGE: type = "EDGE"; break;
                case TelephonyManager.NETWORK_TYPE_UMTS: type = "UMTS"; break;
                case TelephonyManager.NETWORK_TYPE_HSDPA: type = "HSDPA"; break;
                case TelephonyManager.NETWORK_TYPE_HSUPA: type = "HSUPA"; break;
                case TelephonyManager.NETWORK_TYPE_HSPA: type = "HSPA"; break;
                case TelephonyManager.NETWORK_TYPE_HSPAP: type = "HSPA+"; break;
                case TelephonyManager.NETWORK_TYPE_LTE: type = "LTE"; break;
                case TelephonyManager.NETWORK_TYPE_IDEN: type = "iDEN"; break;
                case TelephonyManager.NETWORK_TYPE_CDMA: type = "cmdaOne"; break;
                case TelephonyManager.NETWORK_TYPE_1xRTT: type = "CDMA2000 1xRTT"; break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0: type = "CDMA2000 1xEV-DO Rev. 0"; break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A: type = "CDMA2000 1xEV-DO Rev. A"; break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B: type = "CDMA2000 1xEV-DO Rev. B"; break;
                case TelephonyManager.NETWORK_TYPE_EHRPD: type = "CDMA2000 eHRPD"; break;
                case TelephonyManager.NETWORK_TYPE_GSM: type = "GSM"; break;
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA: type = "UMTS (TD-SCDMA)"; break;
                case TelephonyManager.NETWORK_TYPE_IWLAN: type = "IWLAN"; break;
                case 19: type = "LTE_CA"; break; // TelephonyManager.NETWORK_TYPE_LTE_CA
                case 20: type = "NR (5G)"; break; // TelephonyManager.NETWORK_TYPE_NR
                case 30: type = "DC-HSPA+"; break;
            }
        } catch (SecurityException e) {
            type = "Permission Required";
            Log.e(TAG, "SecurityException reading network type", e);
        }

        Log.d(TAG, "Network type: " + type);
        Toast.makeText(context, type, Toast.LENGTH_LONG).show();
        launchSettingsAndFinish();
    }

    private void launchSettingsAndFinish() {
        boolean success = false;
        
        // Try Intent 1: settings package with ACTION_MAIN
        try {
            Log.d(TAG, "Attempting Intent 1: com.android.settings/.RadioInfo with ACTION_MAIN");
            Intent i = new Intent("android.intent.action.MAIN");
            i.setClassName("com.android.settings", "com.android.settings.RadioInfo");
            startActivity(i);
            success = true;
        } catch (Exception e) {
            Log.e(TAG, "Intent 1 failed", e);
        }

        // Try Intent 2: phone package with ACTION_MAIN
        if (!success) {
            try {
                Log.d(TAG, "Attempting Intent 2: com.android.phone/.settings.RadioInfo with ACTION_MAIN");
                Intent i = new Intent("android.intent.action.MAIN");
                i.setClassName("com.android.phone", "com.android.phone.settings.RadioInfo");
                startActivity(i);
                success = true;
            } catch (Exception e) {
                Log.e(TAG, "Intent 2 failed", e);
            }
        }

        // Try Intent 3: settings package without action
        if (!success) {
            try {
                Log.d(TAG, "Attempting Intent 3: com.android.settings/.RadioInfo explicit");
                Intent i = new Intent();
                i.setClassName("com.android.settings", "com.android.settings.RadioInfo");
                startActivity(i);
                success = true;
            } catch (Exception e) {
                Log.e(TAG, "Intent 3 failed", e);
            }
        }

        // Try Intent 4: phone package without action
        if (!success) {
            try {
                Log.d(TAG, "Attempting Intent 4: com.android.phone/.settings.RadioInfo explicit");
                Intent i = new Intent();
                i.setClassName("com.android.phone", "com.android.phone.settings.RadioInfo");
                startActivity(i);
                success = true;
            } catch (Exception e) {
                Log.e(TAG, "Intent 4 failed", e);
            }
        }

        if (!success) {
            Log.e(TAG, "All intents failed to launch RadioInfo settings screen");
            Toast.makeText(context, "Could not open Radio Info settings.", Toast.LENGTH_LONG).show();
        }
        
        Log.d(TAG, "Finishing activity");
        finish();
    }
}
