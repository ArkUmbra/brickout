package com.arkumbra.brickout.view;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by lukegardener on 2017/08/07.
 */

public class FontUtils {
    private static final String TYPEFACE_ASSET_PATH = "space_age/space age.ttf";

    private static Typeface typeface;


    public static void initialise(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), TYPEFACE_ASSET_PATH);
    }

    public static Typeface getAppTypeface() {
        return FontUtils.typeface;
    }
}
