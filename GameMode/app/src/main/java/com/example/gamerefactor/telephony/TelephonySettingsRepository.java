package com.example.gamerefactor.telephony;

import android.content.Context;
import android.provider.Settings;

public class TelephonySettingsRepository {
    private static final String CALL_BLOCKING_ENABLED = "game_mode_call_blocking";
    private static final String AUTO_ANSWER_ENABLED = "game_mode_auto_answer";

    private Context context;

    public TelephonySettingsRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean isCallBlockingEnabled() {
        return Settings.Secure.getInt(context.getContentResolver(),
                CALL_BLOCKING_ENABLED, 0) == 1;
    }

    public void setCallBlockingEnabled(boolean enabled) {
        Settings.Secure.putInt(context.getContentResolver(),
                CALL_BLOCKING_ENABLED, enabled ? 1 : 0);
    }

    public boolean isAutoAnswerEnabled() {
        return Settings.Secure.getInt(context.getContentResolver(),
                AUTO_ANSWER_ENABLED, 0) == 1;
    }

    public void setAutoAnswerEnabled(boolean enabled) {
        Settings.Secure.putInt(context.getContentResolver(),
                AUTO_ANSWER_ENABLED, enabled ? 1 : 0);
    }
}