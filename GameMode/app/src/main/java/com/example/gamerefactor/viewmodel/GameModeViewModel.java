package com.example.gamerefactor.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.BatteryManager;
import android.os.CpuUsageInfo;
import android.os.Handler;
import android.os.HardwarePropertiesManager;
import android.os.Looper;

import androidx.annotation.DrawableRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gamerefactor.SystemSettingsRepository;
import com.example.gamerefactor.SystemUIRepository;
import com.example.gamerefactor.data.ResourcesRepository;

import java.util.Timer;
import java.util.TimerTask;

public class GameModeViewModel extends AndroidViewModel {
    private static final String TAG = "GameModeViewModel";

    // Game mode constants
    public static final int BATTERY_SAVER_MODE = 0;
    public static final int PRO_MODE = 1;
    public static final int PERFORMANCE_MODE = 2;

    // LiveData for UI observation
    private MutableLiveData<Integer> currentGameMode = new MutableLiveData<>(PRO_MODE);
    private MutableLiveData<Integer> cpuUsage = new MutableLiveData<>(0);
    private MutableLiveData<Integer> batteryLevel = new MutableLiveData<>(0);
    private MutableLiveData<Integer> thermalTemp = new MutableLiveData<>(0);
    private MutableLiveData<Integer> brightnessLevel = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> isCharging = new MutableLiveData<>(false);

    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer systemStatsTimer;

    private final SystemSettingsRepository settingsRepo;
    private final SystemUIRepository systemUIRepo;
    private final ResourcesRepository resourcesRepo;

    public GameModeViewModel(Application application) {
        super(application);

        Context context = application.getApplicationContext();
        settingsRepo = new SystemSettingsRepository(context);
        systemUIRepo = new SystemUIRepository(context);
        resourcesRepo = new ResourcesRepository(context);
        startSystemStatsMonitoring();
    }

    public int getModeColor(int mode, boolean state) {
        return resourcesRepo.getModeColor(mode, state);
    }

    @DrawableRes
    public int getButtonIcon(int position, String optionName, boolean state) {
        return resourcesRepo.getButtonIcon(position, optionName, state);
    }

    @DrawableRes
    public int getModeIcon(int mode, boolean state) {
        return resourcesRepo.getModeIcon(mode, state);
    }

    private void startSystemStatsMonitoring() {
        systemStatsTimer = new Timer();
        systemStatsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateSystemStats();
            }
        }, 0, 8000); // Update every 8 seconds
    }

    private void updateSystemStats() {
        Context context = getApplication();

        // Update CPU usage
        HardwarePropertiesManager hpm = (HardwarePropertiesManager) context.getSystemService(Context.HARDWARE_PROPERTIES_SERVICE);
        if (hpm != null) {
            CpuUsageInfo[] cpuinfo = hpm.getCpuUsages();
            int total_time = 0, active_time = 0;
            for (CpuUsageInfo temp : cpuinfo) {
                total_time += temp.getTotal();
                active_time += temp.getActive();
            }
            int percent = ((active_time * 100) / total_time);
            cpuUsage.postValue(percent);

            // Update thermal temperature
            float[] cpuinfothermal = hpm.getDeviceTemperatures(
                    HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU,
                    HardwarePropertiesManager.TEMPERATURE_CURRENT);
            if (cpuinfothermal != null && cpuinfothermal.length > 0) {
                thermalTemp.postValue((int) cpuinfothermal[0]);
            }
        }

        // Update battery level
        BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        if (bm != null) {
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            batteryLevel.postValue(batLevel);
        }

        // Update brightness level using repository
        brightnessLevel.postValue(settingsRepo.getBrightnessLevel());
    }

    public void setGameMode(int mode) {
        if (mode < BATTERY_SAVER_MODE || mode > PERFORMANCE_MODE) {
            return;
        }

        currentGameMode.setValue(mode);
        settingsRepo.setCurrentMode(mode);

        // Apply mode-specific settings
        switch (mode) {
            case BATTERY_SAVER_MODE:
                if (isCharging.getValue() != null && !isCharging.getValue()) {
                    systemUIRepo.setPowerSaverMode(true);
                }
                break;
            case PRO_MODE:
            case PERFORMANCE_MODE:
                systemUIRepo.setPowerSaverMode(false);
                break;
        }
    }

    public void setBrightness(int progress) {
        int val = (progress * 255) / 100;
        settingsRepo.setBrightnessLevel(val);
        brightnessLevel.setValue(progress);
    }

    public void setIsCharging(boolean charging) {
        isCharging.setValue(charging);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (systemStatsTimer != null) {
            systemStatsTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }

    // Getters for LiveData
    public MutableLiveData<Integer> getCurrentGameMode() {
        return currentGameMode;
    }

    public MutableLiveData<Integer> getCpuUsage() {
        return cpuUsage;
    }

    public MutableLiveData<Integer> getBatteryLevel() {
        return batteryLevel;
    }

    public MutableLiveData<Integer> getThermalTemp() {
        return thermalTemp;
    }

    public MutableLiveData<Integer> getBrightnessLevel() {
        return brightnessLevel;
    }

    public MutableLiveData<Boolean> getIsCharging() {
        return isCharging;
    }
}