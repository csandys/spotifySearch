package com.spotifysearch.api;

import com.spotifysearch.model.SearchResponse;
import com.spotifysearch.model.SpotifyAlbums;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by csandys on 10/13/16.
 */

//https://api.spotify.com/v1/search?type=album&limit=2&q=bowie

public interface SearchSpotifyApi {

    int LIMIT = 10;
    String METHOD = "v1/search/";

    @GET(METHOD + "?type=album&limit=" + LIMIT)
    Observable<SearchResponse<SpotifyAlbums>> getAlbums(@Query("q") String searchPhrase);

    @GET(METHOD + "?type=album&limit=" + LIMIT)
    Call<SearchResponse<SpotifyAlbums>> getAlbumsSync(@Query("q") String searchPhrase);
}
