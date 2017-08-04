package com.golstars.www.glostars.DatabaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Arif on 8/4/2017.
 */

public class FollowerNotificationRealm extends RealmObject {
    @PrimaryKey
    public int id;
    public String FollowerNotificationRealmData;

    public FollowerNotificationRealm() {
    }

    public FollowerNotificationRealm(int id, String followerNotificationRealmData) {
        this.id = id;
        FollowerNotificationRealmData = followerNotificationRealmData;
    }
}
