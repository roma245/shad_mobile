package com.example.roman.imagegrid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by Roman on 19.03.2016.
 */
public class NumberPickerPreference extends DialogPreference {

    private NumberPicker numberPicker;
    private SharedPreferences mPrefs;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences( getContext() );

        return generateNumberPicker();
    }

    public NumberPicker generateNumberPicker() {
        numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(4);
        numberPicker.setMaxValue(6);

        String col_num = mPrefs.getString("pref_col_num", "4");

        numberPicker.setValue(Integer.parseInt(col_num));

        return numberPicker;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            int col_num = numberPicker.getValue();

            SharedPreferences.Editor editor = mPrefs.edit();

            //store data to pref
            editor.putString("pref_col_num", String.valueOf(col_num));
            editor.commit();

        }
    }
}