package com.example.travelapp.activities;

import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class HideKeyboard {
    private static final HideKeyboard myHideKeyboard = new HideKeyboard();
    public static HideKeyboard getInstance() {return myHideKeyboard;}
    // if the user clicked somewhere that is not an EditText, hide the keyboard
    public boolean isTouchOutsideView(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // click on the editText
                return false;
            } else {
                return true;
            }
        }
        // ignore it if the focus was not on editText
        return false;
    }

    // hide the keyboard using inputMethodManager
    public void hideSoftInput(IBinder token, Context context) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
