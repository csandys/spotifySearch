package com.spotifysearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.spotifysearch.view.ImageTitleView;
import com.spotifysearch.BuildConfig;
import com.spotifysearch.model.AlbumImage;
import com.spotifysearch.model.SpotifyAlbum;

import java.util.List;

/**
 * Created by csandys on 10/13/16.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    private static final boolean LOGD = false && BuildConfig.DEBUG;
    private static final String TAG = "SS";

    List<SpotifyAlbum> mAlbumList;

    public AlbumsAdapter(List<SpotifyAlbum> albumList) {
        mAlbumList = albumList;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumViewHolder(new ImageTitleView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.bind(mAlbumList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    public void update(List<SpotifyAlbum> albums) {
        mAlbumList = albums;
        notifyDataSetChanged();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {

        public AlbumViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(SpotifyAlbum album) {
            List<AlbumImage> images = album.getImages();
            int imageSize = ((ImageTitleView)itemView).getImageSize();
            String imageUrl = null;
            if (images != null && images.size()>0) {
                AlbumImage bestImage = null;
                for (AlbumImage albumImage : images) {
                    if (bestImage == null ||
                        ( albumImage.getWidth() > imageSize) &&
                          albumImage.getHeight() > imageSize &&
                          bestImage.getWidth() > albumImage.getWidth() &&
                          bestImage.getHeight() > albumImage.getHeight()
                        )  {
                        bestImage = albumImage;
                    }
                }
                if (bestImage != null) {
                    if (LOGD) {
                        for (AlbumImage albumImage : images) {
                            Log.d(TAG, (albumImage == bestImage ? "->" : "  ") + albumImage.getWidth() + "x" + albumImage.getWidth());
                        }
                    }
                    imageUrl = bestImage.getUrl();
                }
            }
            if (LOGD) Log.d(TAG, "Bind Album " + album.getName());
            ((ImageTitleView)itemView).bind(imageUrl, album.getName());
        }
    }
}

