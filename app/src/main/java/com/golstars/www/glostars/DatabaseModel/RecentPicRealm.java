package com.golstars.www.glostars.DatabaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Arif on 8/8/2017.
 */

public class RecentPicRealm extends RealmObject {

    @PrimaryKey
    public int id;
    public String data;

    public RecentPicRealm() {
    }

    public RecentPicRealm(int id, String data) {
        this.id = id;
        this.data = data;
    }
}
