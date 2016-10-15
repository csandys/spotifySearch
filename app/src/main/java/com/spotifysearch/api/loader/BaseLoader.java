package com.spotifysearch.api.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by csandys on 10/13/16.
 */

public abstract class BaseLoader<T> extends AsyncTaskLoader<T> {


    private T mValue;

    protected abstract void onReleaseResources(T value);

    public BaseLoader(Context context) {
        super(context);
    }


    @Override
    public void deliverResult(T value) {
        if (isReset()) {
            if (value != null) {
                onReleaseResources(value);
            }
        }
        T oldValue = mValue;
        mValue = value;

        if (isStarted()) {
            super.deliverResult(value);
        }

        if (oldValue != null) {
            onReleaseResources(oldValue);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mValue != null) {
            deliverResult(mValue);
        }

        if (takeContentChanged() || mValue == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(T value) {
        super.onCanceled(value);
        onReleaseResources(value);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mValue != null) {
            onReleaseResources(mValue);
            mValue = null;
        }
    }
}
