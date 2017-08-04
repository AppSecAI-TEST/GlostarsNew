package com.golstars.www.glostars.DatabaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Arif on 8/4/2017.
 */

public class SingalPictureInfo extends RealmObject {
    @PrimaryKey
    public int id;
    public String data;

    public SingalPictureInfo() {
    }

    public SingalPictureInfo(int id, String data) {
        this.id = id;
        this.data = data;
    }
}
