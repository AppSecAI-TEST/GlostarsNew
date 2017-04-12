package com.golstars.www.glostars.interfaces;

import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.models.Post;

/**
 * Created by edson on 03/03/17.
 */

public interface OnItemClickListener {
    void onItemClickPost(Post item);

    void onItemClickNotif(NotificationObj notif);
}