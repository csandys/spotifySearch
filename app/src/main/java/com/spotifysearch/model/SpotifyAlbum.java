package com.spotifysearch.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by csandys on 10/13/16.
 */

@JsonObject
public class SpotifyAlbum {

    @JsonField
    List<AlbumImage> images;

    @JsonField
    String name;

    @JsonField
    String id;

    public String getName() {
        return name;
    }

    public List<AlbumImage> getImages() {
        return images;
    }
}
