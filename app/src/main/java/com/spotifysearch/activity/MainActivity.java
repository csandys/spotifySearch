package com.spotifysearch.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.spotifysearch.R;
import com.spotifysearch.fragment.SearchResultsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private static final long ANMATION_TIME = 2000;
    private static final long VIEW_WIDTH_ANIMATION = 300;

    @BindView(R.id.searchValue) EditText mSearchEditText;
    @BindView(R.id.logo) ImageView mLogo;
    @BindView(R.id.titleLogo) ImageView mTitleLogo;
    @BindView(R.id.search) ImageButton mSearchButton;
    @BindView(R.id.titlebarContainer) RelativeLayout mTitlebarContainer;
    @BindView(R.id.searchResults) LinearLayout mSearchResultsContainer;
    @BindView(R.id.fragmentContainer) ScrollView mFragmentsContainer;

    private RelativeLayout.LayoutParams mLayoutParams;
    private int mViewId = 1;
    private String mLastSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLayoutParams = (RelativeLayout.LayoutParams) mSearchEditText.getLayoutParams();

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch();
                    return true;
                }
                return false;
            }
        });
        // Fade in the logo
        mTitlebarContainer.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mTitlebarContainer.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN ) {
                mTitlebarContainer.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            } else {
                mTitlebarContainer.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
            mLogo.setAlpha(0.0f);
            mLogo.animate()
                    .alpha(1.0f)
                    .setDuration(ANMATION_TIME);
            if (showingSearch()) {
                openSearch();
            }
        }
    };

    private boolean showingSearch() {
        return mSearchEditText.getVisibility() == View.VISIBLE;
    }

    @OnClick(R.id.search)
    public void onSearch() {
        if (showingSearch()){
            final String searchPhrase = mSearchEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(searchPhrase)) {

                if (mLastSearch != null && mLastSearch.equals(searchPhrase)) {
                    closeSearch(null);
                    hideKeyboard();
                    return;
                }
                mLastSearch = searchPhrase;

                hideKeyboard();
                closeSearch(new Runnable() {
                    @Override
                    public void run() {
                        SearchResultsFragment fragment = SearchResultsFragment.newInstance(searchPhrase);

                        if (fragment != null) {
                            FrameLayout frameLayout = new FrameLayout(MainActivity.this);
                            frameLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            frameLayout.setId(mViewId);
                            mSearchResultsContainer.addView(frameLayout, 0);
                            addFragment(mViewId, fragment);
                            mViewId++;
                            mFragmentsContainer.scrollTo(0, 0);
                        }
                    }
                });
            } else {
                closeSearch(null);
                hideKeyboard();
            }
        } else {
            openSearch();
        }
    }

    private void addFragment(int viewResId, Fragment fragment) {
            getFragmentManager()
                    .beginTransaction()
                    .add(viewResId, fragment)
                    .commit();
    }

    private void closeSearch(@Nullable final Runnable onDone) {
        mSearchButton.setEnabled(false);
        hideEdit(mSearchButton.getLeft() - mTitleLogo.getRight(), new Runnable() {
            @Override
            public void run() {
                mSearchButton.setEnabled(true);
                if (onDone != null) {
                    onDone.run();
                }
            }
        });
    }
    private void openSearch() {
        mSearchButton.setEnabled(false);
        showEdit(mSearchButton.getLeft() - mTitleLogo.getRight(), new Runnable() {
            @Override
            public void run() {
                mSearchButton.setEnabled(true);
                showKeyboard(mSearchEditText);
            }
        });
    }

    protected void hideKeyboard() {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showKeyboard(View v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    /**
     * Called by ObjectAnimator
     * @param width
     */
    protected void setAnimateValue(int width) {
        mLayoutParams.width = width;
        mLayoutParams.leftMargin = mSearchButton.getLeft() - width;
        mSearchEditText.setLayoutParams(mLayoutParams);
    }

    @Override
    public void onBackPressed() {
        if (showingSearch()) {
            closeSearch(null);
            return;
        }
        super.onBackPressed();
    }

    public void showEdit(int width, final Runnable onDone) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "animateValue", 0, width);
        objectAnimator.setDuration(VIEW_WIDTH_ANIMATION);
        objectAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                mSearchEditText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onDone.run();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        objectAnimator.start();
    }

    public void hideEdit(int startWdith, final Runnable onDone) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "animateValue", startWdith, 0);
        objectAnimator.setDuration(VIEW_WIDTH_ANIMATION);
        objectAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mSearchEditText.setVisibility(View.GONE);
                onDone.run();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        objectAnimator.start();
    }

}


