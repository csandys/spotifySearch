package com.spotifysearch.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by csandys on 10/13/16.
 */

@JsonObject
public class SearchResponse<T> {

    @JsonField
    public T albums;
}
