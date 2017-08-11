package com.golstars.www.glostars.DatabaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Arif on 8/9/2017.
 */

public class CompetionRealm extends RealmObject {
    @PrimaryKey
    public int id;
    public String data;

    public CompetionRealm() {
    }

    public CompetionRealm(int id, String data) {
        this.id = id;
        this.data = data;
    }
}
