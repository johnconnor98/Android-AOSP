package com.example.gamerefactor.media;

import android.content.Context;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.ScreenRecordDialog;

public class ScreenRecordingRepository {
    private final RecordingController recordingController;

    public ScreenRecordingRepository(RecordingController controller) {
        this.recordingController = controller;
    }

    public void startScreenRecording(Context context, Runnable onStartRecordingClicked) {
        if (!RecordingController.isInPrompt()) {
            RecordingController.mIsInPrompt = true;
            ScreenRecordDialog dialog = recordingController
                    .createScreenRecordDialog(context, onStartRecordingClicked);
            dialog.show();
            dialog.dismiss();
        }
    }
}