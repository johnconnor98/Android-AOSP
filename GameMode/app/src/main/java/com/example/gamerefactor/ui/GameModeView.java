package com.example.gamerefactor.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.gamerefactor.R;
import com.example.gamerefactor.data.GameModeRepository;
import com.example.gamerefactor.data.ResourcesRepository;
import com.example.gamerefactor.viewmodel.GameModeViewModel;

public class GameModeView {
    private Context context;
    private GameModeViewModel viewModel;
    private ResourcesRepository resourcesRepo;

    private Button batterySaverModeBtn;
    private Button proModeBtn;
    private Button performanceModeBtn;
    private SeekBar brightnessControl;
    private TextView cpuPercentText;
    private TextView batteryPercentText;
    private TextView thermalTempText;
    private ViewGroup floatView;

    public GameModeView(@NonNull Context context,
                        @NonNull ViewGroup floatView,
                        @NonNull GameModeViewModel viewModel,
                        @NonNull ResourcesRepository resourcesRepo) {
        this.context = context;
        this.viewModel = viewModel;
        this.resourcesRepo = resourcesRepo;

        // Initialize views
        initializeViews();
        setupObservers();
        setupClickListeners();
    }

    public GameModeView(Context context, ViewGroup floatView) {
        this.context = context;
        this.floatView = floatView;
        initializeDependencies();
        initializeViews();
        setupObservers();
        setupClickListeners();
    }

    private void initializeViews() {
        batterySaverModeBtn = floatView.findViewById(R.id.BatterySaverMode);
        proModeBtn = floatView.findViewById(R.id.ProMode);
        performanceModeBtn = floatView.findViewById(R.id.PerformanceMode);
        brightnessControl = floatView.findViewById(R.id.seekBar);
        cpuPercentText = floatView.findViewById(R.id.cpupercent);
        batteryPercentText = floatView.findViewById(R.id.batterypercent);
        thermalTempText = floatView.findViewById(R.id.thermaltemp);
    }

    private void initializeDependencies() {
        // Initialize ViewModel using Application context
        viewModel = new ViewModelProvider(
                (ViewModelStoreOwner) context.getApplicationContext(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(
                        (Application) context.getApplicationContext()
                )
        ).get(GameModeViewModel.class);

        // Initialize Repository
        resourcesRepo = new ResourcesRepository(context);
    }

    @SuppressLint("StringFormatInvalid")
    private void setupObservers() {
        viewModel.getCurrentGameMode().observe((LifecycleOwner) context, this::updateGameModeUI);

        viewModel.getCpuUsage().observe((LifecycleOwner) context, usage -> {
            cpuPercentText.setText(context.getString(R.string.cpu_percentage, usage));
        });

        viewModel.getBatteryLevel().observe((LifecycleOwner) context, level -> {
            batteryPercentText.setText(context.getString(R.string.percentage, level));
        });

        viewModel.getThermalTemp().observe((LifecycleOwner) context, temp -> {
            thermalTempText.setText(context.getString(R.string.percentage, temp));
        });

        viewModel.getBrightnessLevel().observe((LifecycleOwner) context, level -> {
            if (brightnessControl.getProgress() != level) {
                brightnessControl.setProgress(level);
            }
        });
    }

    private void setupClickListeners() {
        batterySaverModeBtn.setOnClickListener(v ->
                viewModel.setGameMode(GameModeViewModel.BATTERY_SAVER_MODE));

        proModeBtn.setOnClickListener(v ->
                viewModel.setGameMode(GameModeViewModel.PRO_MODE));

        performanceModeBtn.setOnClickListener(v ->
                viewModel.setGameMode(GameModeViewModel.PERFORMANCE_MODE));

        brightnessControl.setOnSeekBarChangeListener(new SimpleSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    viewModel.setBrightness(progress);
                }
            }
        });
    }

    private void updateGameModeUI(int mode) {
        // Update button backgrounds
        batterySaverModeBtn.setBackgroundResource(
                resourcesRepo.getModeIcon(mode, mode == GameModeViewModel.BATTERY_SAVER_MODE));
        proModeBtn.setBackgroundResource(
                resourcesRepo.getModeIcon(mode, mode == GameModeViewModel.PRO_MODE));
        performanceModeBtn.setBackgroundResource(
                resourcesRepo.getModeIcon(mode, mode == GameModeViewModel.PERFORMANCE_MODE));

        // Update button text colors
        batterySaverModeBtn.setTextColor(
                resourcesRepo.getModeColor(mode, mode == GameModeViewModel.BATTERY_SAVER_MODE));
        proModeBtn.setTextColor(
                resourcesRepo.getModeColor(mode, mode == GameModeViewModel.PRO_MODE));
        performanceModeBtn.setTextColor(
                resourcesRepo.getModeColor(mode, mode == GameModeViewModel.PERFORMANCE_MODE));
    }

    public void cleanup() {
        // 1. Remove any observers
        if (context instanceof LifecycleOwner) {
            viewModel.getCurrentGameMode().removeObservers((LifecycleOwner) context);
            viewModel.getCpuUsage().removeObservers((LifecycleOwner) context);
            viewModel.getBatteryLevel().removeObservers((LifecycleOwner) context);
            viewModel.getThermalTemp().removeObservers((LifecycleOwner) context);
            viewModel.getBrightnessLevel().removeObservers((LifecycleOwner) context);
        }

        // 2. Clear references
        batterySaverModeBtn = null;
        proModeBtn = null;
        performanceModeBtn = null;
        brightnessControl = null;
        cpuPercentText = null;
        batteryPercentText = null;
        thermalTempText = null;
    }

    // Simple SeekBar listener to reduce boilerplate
    private abstract static class SimpleSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}