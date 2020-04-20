package com.example.lab1.Filter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.lab1.Scripts.Script;

import org.jetbrains.annotations.Nullable;

public class ScriptedFilter implements IFilter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int editColor(int col, @Nullable Object[] args) {
        if (args == null || args.length == 0)
            return col;
        int c1 = (Integer) args[0];
        int c2 = (Integer) args[1];
        Script script = (Script) args[2];
        return script.getColor(col, c1, c2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public Bitmap edit(@Nullable Bitmap image, @Nullable Object[] args) {
        if (image == null)
            return null;
        if (args == null || args.length == 0)
            return image;
        int c1 = (Integer) args[0];
        int c2 = (Integer) args[1];
        Script script = (Script) args[2];

        /*Bitmap newB = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < newB.getWidth(); i++)
            for (int j = 0; j < newB.getHeight(); j++) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    Color c = image.getColor(i, j);
                    newB.setPixel(i, j, editColor(Color.argb(c.alpha(), c.red(), c.green(), c.blue()), args));
                }
            }*/

        Bitmap rez = script.getImage(image, c1, c2);
        return rez;
    }
}
