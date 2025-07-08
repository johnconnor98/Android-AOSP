package com.example.gamerefactor;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.Nullable;

import com.example.gamerefactor.ui.GameModeView;

public class GameModeService extends Service {
    private GameModeView gameModeView;
    private WindowManager windowManager;
    private ViewGroup floatView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Initialize floating view
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        floatView = (ViewGroup) inflater.inflate(R.layout.activity_notes_dialogue, null);

        // Setup window params and add view
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getLayoutType(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = -490; // gameModeuiWidth
        params.y = 50;

        windowManager.addView(floatView, params);

        // Initialize MVVM components
        gameModeView = new GameModeView(this, floatView);

        // Setup touch listeners and other service-specific logic
        setupTouchListeners();
    }

    private void setupTouchListeners() {
        // Add your touch handling logic here
    }

    private int getLayoutType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            return WindowManager.LayoutParams.TYPE_TOAST;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (gameModeView != null) {
            gameModeView.cleanup();  // Changed from onDestroy() to cleanup()
        }

        try {
            if (floatView != null && windowManager != null) {
                windowManager.removeView(floatView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}