package com.example.gamerefactor.data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.example.gamerefactor.ScreenshotHelper;
import com.example.gamerefactor.media.ScreenRecordingRepository;

import java.util.ArrayList;
import java.util.List;

public class GameModeRepository {
    private final Context context;
    private final PackageManager packageManager;
    private final ScreenshotHelper screenshotHelper;
    private final ScreenRecordingRepository screenRecordingRepository;

    public GameModeRepository(Context context,
                              ScreenshotHelper screenshotHelper, ScreenRecordingRepository screenRecordingRepository) {
        this.context = context.getApplicationContext();
        this.packageManager = context.getPackageManager();
        this.screenshotHelper = screenshotHelper;
        this.screenRecordingRepository = screenRecordingRepository;
    }

    /**
     * Gets list of launchable apps
     */
    public List<ResolveInfo> getInstalledApps() {
        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            return packageManager.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL);
        } catch (Exception e) {
            return new ArrayList<>(); // Return empty list on error
        }
    }

    /**
     * Launches an application by package name
     * @param packageName The package to launch
     */
    public void launchApp(String packageName) {
        try {
            Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            }
        } catch (Exception e) {
            // Handle launch error
        }
    }

    /**
     * Takes screenshot using system capability
     */
    public void takeScreenshot() {
        screenshotHelper.takeScreenshot(context.getApplicationContext());
    }

    /**
     * Starts screen recording flow
     */
    public void startScreenRecording() {
        screenRecordingRepository.startScreenRecording(context.getApplicationContext(), ()->{
            // on start recording click
            // implement timer
        });
    }
}