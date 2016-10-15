package com.spotifysearch.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by csandys on 10/13/16.
 */

@JsonObject
public class AlbumImage {

    @JsonField
    int height;

    @JsonField
    int width;

    @JsonField
    String url;

    public String getUrl() {
        return url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
