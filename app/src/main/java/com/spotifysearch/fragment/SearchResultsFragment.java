package com.spotifysearch.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spotifysearch.R;
import com.spotifysearch.adapter.AlbumsAdapter;
import com.spotifysearch.api.loader.SearchLoader;
import com.spotifysearch.model.SpotifyAlbum;
import com.spotifysearch.model.SpotifyAlbums;
import com.spotifysearch.view.BottomInset;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by csandys on 10/13/16.
 */

public class SearchResultsFragment extends Fragment implements LoaderManager.LoaderCallbacks<SpotifyAlbums>{

    private static final boolean LOGD = false && BuildConfig.DEBUG;
    private static final String TAG = "SS";

    @BindView(R.id.recyclerlistView) RecyclerView mRecyclerView;
    @BindView(R.id.searchValue) TextView mSearchTitle;
    @BindView(R.id.remove) ImageButton mRemove;
    @BindView(R.id.busy) ProgressBar mBusy;
    @BindView(R.id.noResults) TextView mNoResults;


    private static final String SEARCH = "searchValue";
    private static final int LOADERID = 1;
    private AlbumsAdapter mAdapter;
    private Unbinder mUnbinder;

    @Nullable
    public static SearchResultsFragment newInstance(String searchValue) {

        if (!TextUtils.isEmpty(searchValue)) {
            SearchResultsFragment searchResultsFragment = new SearchResultsFragment();

            Bundle bundle = new Bundle();
            bundle.putString(SEARCH, searchValue);
            searchResultsFragment.setArguments(bundle);
            return searchResultsFragment;
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_results_fragment, container, false);

        mUnbinder = ButterKnife.bind(this, root);
        mSearchTitle.setText(getArguments().getString(SEARCH));

        return root;
    }

    @OnClick(R.id.remove)
    void remove() {
        getActivity()
                .getFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new BottomInset(getActivity()));
        getLoaderManager().initLoader(LOADERID, getArguments(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public Loader<SpotifyAlbums> onCreateLoader(int id, Bundle args) {
        String search = args.getString(SEARCH);
        if (LOGD) Log.d(TAG, "onCreateLoader " + search);
        return new SearchLoader(getActivity(), search);
    }

    @Override
    public void onLoadFinished(Loader<SpotifyAlbums> loader, SpotifyAlbums albums) {
        if (LOGD) Log.d(TAG, "onLoadFinished");
        if (albums != null) {
            List<SpotifyAlbum> albumList = albums.getAlbums();
            if (albumList != null) {
                if (mAdapter == null) {
                    mAdapter = new AlbumsAdapter(albums.getAlbums());
                } else {
                    mAdapter.update(albums.getAlbums());
                }
                mRecyclerView.setAdapter(mAdapter);
                mBusy.setVisibility(View.GONE);
                mNoResults.setVisibility(albumList.size() == 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<SpotifyAlbums> loader) {
        // No Resources need to be released
    }
}
