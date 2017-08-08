package com.golstars.www.glostars.DatabaseModel;

import io.realm.RealmObject;

/**
 * Created by Arif on 8/9/2017.
 */

public class CompetionRealm extends RealmObject {
    public int id;
    public String data;

    public CompetionRealm() {
    }

    public CompetionRealm(int id, String data) {
        this.id = id;
        this.data = data;
    }
}
