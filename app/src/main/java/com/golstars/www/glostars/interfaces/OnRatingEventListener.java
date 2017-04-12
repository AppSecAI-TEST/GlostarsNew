package com.golstars.www.glostars.interfaces;

import com.golstars.www.glostars.models.Post;

/**
 * Created by edson on 02/03/17.
 */

public interface OnRatingEventListener {
    void onRatingBarChange(Post item, float value, int postPosition);
}
