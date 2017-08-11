package com.golstars.www.glostars.DatabaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Arif on 8/11/2017.
 */

public class UserProfileRealm extends RealmObject {
    @PrimaryKey
    public String id;
    public String data;

    public UserProfileRealm() {
    }


    public UserProfileRealm(String id, String data) {
        this.id = id;
        this.data = data;
    }
}
