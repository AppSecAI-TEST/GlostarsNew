package com.golstars.www.glostars;

import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.adapters.PostData;

import java.util.ArrayList;

/**
 * Created by Arif on 4/27/2017.
 */

public interface AdapterInfomation {
    public ArrayList<Hashtag> getAllData();
    public PostData getAdapter();

}
