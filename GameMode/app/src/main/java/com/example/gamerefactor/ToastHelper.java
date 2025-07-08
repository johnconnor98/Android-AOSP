package com.example.gamerefactor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastHelper {
    public static void showCustomToast(Context context, String text, Drawable image) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.standard_toast_layout, null);

            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);

            ImageView imageView = layout.findViewById(R.id.toastimage);
            TextView textView = layout.findViewById(R.id.message);

            imageView.setBackground(image);
            textView.setText(text);
            toast.setView(layout);
            toast.show();
        }, 50);
    }
}