package com.jetopto.bsm.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static Toast mToast;

    private static boolean hasNavBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (id > 0)
            return resources.getBoolean(id);
        else
            return false;
    }

    public static int getNavigationBarHeight(Resources resources) {
        if (!hasNavBar(resources)) {
            return 0;
        }
        int id = resources
                .getIdentifier("navigation_bar_height_landscape",
                        "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }

        return 0;
    }

    public static int getNavigationBarWidth(Resources resources) {
        if (!hasNavBar(resources)) {
            return 0;
        }
        int orientation = resources.getConfiguration().orientation;
        boolean isSmartphone = resources.getConfiguration().smallestScreenWidthDp < 600;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && isSmartphone) {
            int id = resources.getIdentifier("navigation_bar_width", "dimen",
                    "android");
            if (id > 0) {
                return resources.getDimensionPixelSize(id);
            }
        }
        return 0;
    }

    public static boolean shouldHideNavBar(Resources resources) {
        //Reference mNavigationBarOnBottom in PhoneWindowManager.java
        Log.i(TAG, "NavBarWidth: " + getNavigationBarWidth(resources));
        Log.i(TAG, "NavBarHeight: " + getNavigationBarHeight(resources));
        return getNavigationBarWidth(resources) < getNavigationBarHeight(resources);
    }

    public static void showToast(Context context, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }

}
