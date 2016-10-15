package com.spotifysearch.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by csandys on 10/13/16.
 */

@JsonObject
public class SpotifyAlbums {

    @JsonField
    String href;

    @JsonField
    List<SpotifyAlbum> items;

    public String getHref() {
        return href;
    }

    public List<SpotifyAlbum> getAlbums() {
        return items;
    }
}
