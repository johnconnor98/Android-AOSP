package com.example.gamerefactor.data;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.ColorRes;

import com.example.gamerefactor.R;

public class ResourcesRepository {
    private final Context context;

    public ResourcesRepository(Context context) {
        this.context = context;
    }

    public int getModeColor(int mode, boolean state) {
        @ColorRes int colorRes;
        if (state) {
            switch (mode) {
                case 0: // BATTERY_SAVER_MODE
                    colorRes = R.color.light_green_mode;
                    break;
                case 1: // PRO_MODE
                    colorRes = R.color.light_blue_mode;
                    break;
                case 2: // PERFORMANCE_MODE
                    colorRes = R.color.light_orange_mode;
                    break;
                default:
                    colorRes = R.color.light_gray_mode;
            }
        } else {
            colorRes = R.color.light_gray_mode;
        }
        return context.getResources().getColor(colorRes);
    }

    @DrawableRes
    public int getButtonIcon(int position, String optionName, boolean state) {
        switch (optionName) {
            case "Answer Call":
                return state ? R.drawable.callanswerbtnbackgroundon : R.drawable.callanswerbtnbackgroundoff;
            case "Reject Call":
                return state ? R.drawable.callrejectbtnbackgroundon : R.drawable.callrejectbtnbackgroundoff;
            case "Mute Notify":
                return state ? R.drawable.notifybtnbackgroundon : R.drawable.notifybtnbackgroundoff;
            case "LockBright":
                return state ? R.drawable.brightnesslockon : R.drawable.brightnesslock;
            case "ScreenShot":
                return R.drawable.screenshotbtnbackgroundoff;
            case "Screen Record":
                return R.drawable.screenrecordingoff;
            default:
                return R.drawable.callanswerbtnbackgroundoff;
        }
    }

    @DrawableRes
    public int getModeIcon(int mode, boolean state) {
        if (state) {
            switch (mode) {
                case 0: // BATTERY_SAVER_MODE
                    return R.drawable.modebackgroud;
                case 1: // PRO_MODE
                    return R.drawable.modebackground2;
                case 2: // PERFORMANCE_MODE
                    return R.drawable.modebackground3;
            }
        }
        return R.drawable.modebtn;
    }
}