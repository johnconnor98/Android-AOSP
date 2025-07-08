package com.example.gamerefactor;

import android.content.Context;
import android.provider.Settings;

public class SystemSettingsRepository {
    private static final String SYSTEM_KEY = "screen_brightness_mode";
    private static final String GAME_MODE_STATE = "GameModeState";
    private static final String NOTIFY_STATE = "GameMode_NotifyState";
    private static final String CALL_BLOCK_STATE = "GameMode_CallBlockState";

    private final Context context;

    public SystemSettingsRepository(Context context) {
        this.context = context;
    }

    // Brightness related methods
    public void setBrightnessLevel(int value) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, value);
    }

    public int getBrightnessLevel() {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, -1);
    }

    public int getAdaptiveBrightness() {
        return Settings.System.getInt(context.getContentResolver(),
                SYSTEM_KEY, 0);
    }

    public void toggleAdaptiveBrightness() {
        Settings.System.putInt(context.getContentResolver(),
                SYSTEM_KEY, 1 - getAdaptiveBrightness());
    }

    // Game mode related methods
    public int getCurrentMode() {
        int mode = Settings.Secure.getInt(context.getContentResolver(),
                GAME_MODE_STATE, -1);
        if (mode == -1) {
            setCurrentMode(2); // Default to performance mode
            return 2;
        }
        return mode;
    }

    public void setCurrentMode(int mode) {
        Settings.Secure.putInt(context.getContentResolver(),
                GAME_MODE_STATE, mode);
    }

    // Notification state
    public int getNotificationState() {
        int state = Settings.Secure.getInt(context.getContentResolver(),
                NOTIFY_STATE, -1);
        if (state == -1) {
            setNotificationState(0);
            return 0;
        }
        return state;
    }

    public void setNotificationState(int state) {
        Settings.Secure.putInt(context.getContentResolver(),
                NOTIFY_STATE, state);
    }

    // Call blocking state
    public int getCallBlockState() {
        int state = Settings.Secure.getInt(context.getContentResolver(),
                CALL_BLOCK_STATE, -1);
        if (state == -1) {
            setCallBlockState(0);
            return 0;
        }
        return state;
    }

    public void setCallBlockState(int state) {
        Settings.Secure.putInt(context.getContentResolver(),
                CALL_BLOCK_STATE, state);
    }
}