package com.example.gamerefactor.telephony;

import android.content.Context;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

public class CallStateHandler extends PhoneStateListener {
    private Context context;
    private final TelecomManager telecomManager;
    private final CallStateCallback callback;
    private final TelephonySettingsRepository telephonySettingsRepository;

    public interface CallStateCallback {
        void onCallBlocked(String phoneNumber);
        void onCallAnswered(String phoneNumber);
        void onCallStateIdle();
    }

    public CallStateHandler(Context context, CallStateCallback callback) {
        this.context = context.getApplicationContext();
        this.telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        this.callback = callback;
        this.telephonySettingsRepository = new TelephonySettingsRepository(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                callback.onCallStateIdle();
                break;

            case TelephonyManager.CALL_STATE_RINGING:
                handleIncomingCall(phoneNumber);
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                // Handle active call if needed
                break;
        }
    }

    private void handleIncomingCall(String phoneNumber) {
        if (telephonySettingsRepository.isCallBlockingEnabled()) {
            endCallIfBlocked(phoneNumber);
        } else if (telephonySettingsRepository.isAutoAnswerEnabled()) {
            answerCallAutomatically(phoneNumber);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void endCallIfBlocked(String phoneNumber) {
        try {
            telecomManager.endCall();
            showToast("Call from " + phoneNumber + " rejected");
            callback.onCallBlocked(phoneNumber);
        } catch (Exception e) {
            Log.e("CallStateHandler", "Error ending call", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void answerCallAutomatically(String phoneNumber) {
        try {
            telecomManager.acceptRingingCall();
            showToast("Call from " + phoneNumber + " accepted");
            callback.onCallAnswered(phoneNumber);
        } catch (Exception e) {
            Log.e("CallStateHandler", "Error answering call", e);
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
