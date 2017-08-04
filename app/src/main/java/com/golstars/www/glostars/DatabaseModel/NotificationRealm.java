package com.golstars.www.glostars.DatabaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Arif on 8/3/2017.
 */

public class NotificationRealm extends RealmObject {
    @PrimaryKey
    private int notificationId;
    private String notificationData;

    public NotificationRealm() {
    }

    public NotificationRealm(int notificationId, String notificationData) {
        this.notificationId = notificationId;
        this.notificationData = notificationData;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(String notificationData) {
        this.notificationData = notificationData;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", notificationData='" + notificationData + '\'' +
                '}';
    }
}
