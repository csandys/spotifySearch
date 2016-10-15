package com.spotifysearch.api.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.spotifysearch.api.SearchSpotifyApi;
import com.spotifysearch.model.SearchResponse;
import com.spotifysearch.model.SpotifyAlbums;
import com.spotifysearch.retrofit.RetrofitManager;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by csandys on 10/13/16.
 */
public class SearchLoader extends BaseLoader<SpotifyAlbums> {

    private final String mSearchPhrase;

    public SearchLoader(Context context, String searchPhrase) {
        super(context);
        if (TextUtils.isEmpty(searchPhrase)) {
            throw new IllegalArgumentException("SearchLoader searchPhrase is empty");
        }
        mSearchPhrase = searchPhrase;
    }

    @Override
    public SpotifyAlbums loadInBackground() {
        Log.d("SS", "loadInBackground");
        Response<SearchResponse<SpotifyAlbums>> response;

        try {
            response = RetrofitManager.getInstance().getRetrofit()
                    .create(SearchSpotifyApi.class)
                    .getAlbumsSync(mSearchPhrase).execute();

            if (response.isSuccessful()) {
                return response.body().albums;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onReleaseResources(SpotifyAlbums value) {

    }
}
