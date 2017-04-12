package com.golstars.www.glostars.interfaces;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by edson on 06/03/17.
 */

public interface PopulatePage {

    class populatePageAsync extends AsyncTask<JSONObject, Integer, JSONObject>{
        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

        }
    };

    void callAsyncPopulate(Integer pg, String token) throws Exception;

    void bindDatatoUI(JSONObject object) throws Exception;

}
