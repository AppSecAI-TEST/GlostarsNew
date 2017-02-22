package com.golstars.www.glostars;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

/**
 * Created by admin on 2/21/2017.
 */

public class dateSettings implements DatePickerDialog.OnDateSetListener {

    Context context;
     public dateSettings(Context context){

         this.context = context;

     }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
