package com.example.gamerefactor;

import android.app.StatusBarManager;
import android.content.Context;
import com.android.settingslib.fuelgauge.BatterySaverUtils;

public class SystemUIRepository {
    private final Context context;

    public SystemUIRepository(Context context) {
        this.context = context;
    }

    public void disableStatusBarFunctionality(int flag) {
        StatusBarManager statusBar = (StatusBarManager) context
                .getSystemService(Context.STATUS_BAR_SERVICE);

        if (statusBar != null) {
            statusBar.disable(flag);
        }
    }

    public void setPowerSaverMode(boolean isSet) {
        BatterySaverUtils.setPowerSaveMode(context, isSet, false);
    }
}
