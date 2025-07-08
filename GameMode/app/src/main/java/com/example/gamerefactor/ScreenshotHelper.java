package com.example.gamerefactor;

import android.content.Context;
import android.content.Intent;

public class ScreenshotHelper {
    public static void takeScreenshot(Context context) {
        Intent intent = new Intent("com.android.screenshot");
        intent.setAction("com.android.screenshot");
        context.sendBroadcast(intent);
    }
}