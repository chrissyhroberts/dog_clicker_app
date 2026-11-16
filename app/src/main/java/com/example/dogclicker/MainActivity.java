package com.example.dogclicker;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private static final int NOTIFICATION_PERMISSION_REQUEST = 42;
    private static final int BG = Color.rgb(19, 18, 24);
    private static final int PANEL = Color.rgb(31, 29, 38);
    private static final int CREAM = Color.rgb(255, 247, 232);
    private static final int MUTED = Color.rgb(174, 164, 151);
    private static final int ORANGE = Color.rgb(225, 140, 55);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        ClickNotification.ensureChannel(this);
        if (Build.VERSION.SDK_INT >= 33
                && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST);
        } else {
            ClickNotification.show(this);
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(24), dp(26), dp(24), dp(24));
        root.setBackgroundColor(BG);
        root.setOnTouchListener((view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                ClickSound.play(this);
                return true;
            }
            return true;
        });

        TextView eyebrow = text("DOG TRAINING", 12, MUTED, Typeface.BOLD);
        eyebrow.setLetterSpacing(0.22f);
        root.addView(eyebrow, centeredWrap());

        TextView title = text("Dog Clicker", 34, CREAM, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title, centeredWrap());

        TextView subtitle = text("A crisp cue, right when you need it.", 15, MUTED, Typeface.NORMAL);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle, centeredWrap());

        Space topSpace = new Space(this);
        root.addView(topSpace, new LinearLayout.LayoutParams(1, 0, 1f));

        TextView clickButton = text("CLICK", 30, CREAM, Typeface.BOLD);
        clickButton.setGravity(Gravity.CENTER);
        clickButton.setLetterSpacing(0.12f);
        clickButton.setBackground(round(ORANGE, 140));
        clickButton.setElevation(dp(10));
        clickButton.setOnTouchListener((view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                ClickSound.play(this);
                view.setScaleX(0.96f);
                view.setScaleY(0.96f);
                return true;
            }
            if (event.getActionMasked() == MotionEvent.ACTION_UP
                    || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                view.setScaleX(1f);
                view.setScaleY(1f);
                return true;
            }
            return true;
        });
        LinearLayout.LayoutParams clickParams = new LinearLayout.LayoutParams(dp(236), dp(236));
        root.addView(clickButton, clickParams);

        Space bottomSpace = new Space(this);
        root.addView(bottomSpace, new LinearLayout.LayoutParams(1, 0, 1f));

        TextView choose = text("QUICK CUES", 11, MUTED, Typeface.BOLD);
        choose.setLetterSpacing(0.18f);
        root.addView(choose, centeredWrap());

        LinearLayout choices = new LinearLayout(this);
        choices.setGravity(Gravity.CENTER);
        choices.setPadding(0, dp(10), 0, 0);
        for (int count = 1; count <= 3; count++) {
            TextView choice = text(String.valueOf(count), 18, CREAM, Typeface.BOLD);
            choice.setGravity(Gravity.CENTER);
            choice.setBackground(round(PANEL, 18));
            choice.setOnClickListener(view -> ClickSound.playCount(this,
                    Integer.parseInt(((TextView) view).getText().toString())));
            LinearLayout.LayoutParams choiceParams = new LinearLayout.LayoutParams(dp(58), dp(48));
            choiceParams.setMargins(dp(5), 0, dp(5), 0);
            choices.addView(choice, choiceParams);
        }
        root.addView(choices, new LinearLayout.LayoutParams(-2, dp(58)));

        TextView hint = text("Tap anywhere for one click  •  Use the lock screen for 1 / 2 / 3", 12, MUTED, Typeface.NORMAL);
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(0, dp(12), 0, 0);
        root.addView(hint, centeredWrap());
        setContentView(root);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST
                && results.length > 0
                && results[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ClickNotification.show(this);
        }
    }

    private TextView text(String value, int size, int color, int style) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setTypeface(Typeface.DEFAULT, style);
        return view;
    }

    private GradientDrawable round(int color, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        return drawable;
    }

    private LinearLayout.LayoutParams centeredWrap() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        return params;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
