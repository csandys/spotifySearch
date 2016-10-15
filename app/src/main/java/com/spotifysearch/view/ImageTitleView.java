package com.spotifysearch.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spotifysearch.BuildConfig;
import com.spotifysearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csandys on 10/13/16.
 */

public class ImageTitleView extends LinearLayout {

    private static final boolean LOGD = false && BuildConfig.DEBUG;
    private static final String TAG = "SS";

    @BindView(R.id.name) TextView mTextView;
    @BindView(R.id.artwork) ImageView mImageView;

    public ImageTitleView(Context context) {
        super(context);

        inflate(context, R.layout.image_title_view, this);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(Color.WHITE);

        ButterKnife.bind(this);
    }

    public void bind(String imageUrl, String title) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(getContext()).load(imageUrl).into(mImageView);
        }
        mTextView.setText(title);
    }

    public int getImageSize() {
        return mImageView.getLayoutParams().height;
    }
}
