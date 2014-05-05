/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arasthel.googlenavdrawermenu.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.os.Build;

import com.actionbarsherlock.app.SherlockActivity;

import android.support.v7.app.ActionBarActivity;

import org.arasthel.googlenavdrawermenu.R;
import org.arasthel.googlenavdrawermenu.adapters.GoogleNavigationDrawerAdapter;
import org.arasthel.googlenavdrawermenu.utils.Utils;

public class GoogleNavigationDrawer extends DrawerLayout {

    private ListView mListView;

    private int mListPaddingTop = 0;
    private int mListPaddingBottom = 0;
    private int mListPaddingLeft = 0;
    private int mListPaddingRight = 0;

    private int mDrawerGravity = GravityCompat.START;

    private OnNavigationSectionSelected mSelectionListener;

    private String[] mMainSections;
    private String[] mSecondarySections;

    private int[] mMainSectionsDrawableIds;
    private int[] mSecondarySectionsDrawableIds;

    private View mHeaderView;
    private View mFooterView;

    private boolean mHeaderClickable = true;
    private boolean mFooterClickable = true;

    private int checkPosition;
	
	private Activity mActivity;
	
	private boolean mShouldChangeTitle = false;
	
    public GoogleNavigationDrawer(Context context) {
        super(context);
    }

    public GoogleNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GoogleNavigationDrawer, 0, 0);

        configureWithTypedArray(a);
    }

    public GoogleNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GoogleNavigationDrawer, defStyle, 0);

        configureWithTypedArray(a);
    }

    /**
     * We need to override onFinishInflate so that it adds the ListView when it has finished inflating itself
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setListViewSections(mMainSections, mSecondarySections, mMainSectionsDrawableIds, mSecondarySectionsDrawableIds);
    }

	public void setShouldChangeTitle(Activity activity, boolean shouldChangeTitle) {
		if (shouldChangeTitle)
			mActivity = activity;
		else
			mActivity = null;
		mShouldChangeTitle = shouldChangeTitle;
	}
	
    /**
     * Configure View with custom attrs
     * @param typedArray - A TypedArray
     */
    public void configureWithTypedArray(TypedArray typedArray) {
        mListPaddingTop = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingTop, mListPaddingTop);
        mListPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingBottom, mListPaddingBottom);
        mListPaddingRight = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingRight, mListPaddingRight);
        mListPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingLeft, mListPaddingLeft);

        mDrawerGravity = typedArray.getInt(R.styleable.GoogleNavigationDrawer_drawer_gravity, mDrawerGravity);

        mMainSections = Utils.convertToStringArray(typedArray.getTextArray(R.styleable.GoogleNavigationDrawer_list_mainSectionsEntries));
        mSecondarySections = Utils.convertToStringArray(typedArray.getTextArray(R.styleable.GoogleNavigationDrawer_list_secondarySectionsEntries));

        int mainSectDrawableId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_mainSectionsDrawables, -1);

        if(mMainSections != null) {
            mMainSectionsDrawableIds = new int[mMainSections.length];

            if (mainSectDrawableId != -1) {
                TypedArray mainSectTypedArray = getResources().obtainTypedArray(mainSectDrawableId);
                for (int i = 0; i < mMainSections.length; i++) {
                    mMainSectionsDrawableIds[i] = mainSectTypedArray.getResourceId(i, 0);
                }
                mainSectTypedArray.recycle();
            }
        }

        int secondarySectDrawableId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_secondarySectionsDrawables, -1);

        if(mSecondarySections != null) {
            mSecondarySectionsDrawableIds = new int[mSecondarySections.length];

            if (secondarySectDrawableId != -1) {
                TypedArray secondarySectTypedArray = getResources().obtainTypedArray(secondarySectDrawableId);
                for (int i = 0; i < mSecondarySections.length; i++) {
                    mSecondarySectionsDrawableIds[i] = secondarySectTypedArray.getResourceId(i, 0);
                }
                secondarySectTypedArray.recycle();
            }
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int headerViewId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_headerView, -1);
        if(headerViewId != -1) {
            mHeaderView = inflater.inflate(headerViewId, null);
            mHeaderClickable = typedArray.getBoolean(R.styleable.GoogleNavigationDrawer_list_headerClickable, true);
        }

        int footerViewId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_footerView, -1);
        if(footerViewId != -1) {
            mFooterView = inflater.inflate(footerViewId, null);
            mFooterClickable = typedArray.getBoolean(R.styleable.GoogleNavigationDrawer_list_footerClickable, true);
        }

        typedArray.recycle();

    }

    /**
     * The ListView menu is inflated and added to de DrawerLayout
     */
    private void configureList() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) inflater.inflate(R.layout.navigation_list, this, false);
        mListView.setPadding(mListPaddingLeft, mListPaddingTop, mListPaddingRight, mListPaddingBottom);
        ((DrawerLayout.LayoutParams) mListView.getLayoutParams()).gravity = mDrawerGravity;
        if(mHeaderView != null) {
            setMenuHeader(mHeaderView,mHeaderClickable);
        }
        if(mFooterView != null) {
            setMenuFooter(mFooterView,mFooterClickable);
        }
        addView(mListView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(mHeaderView != null && i == 0 && !mHeaderClickable) {
                    return;
                }

                if(mFooterView != null && i == mListView.getCount()-1 && !mFooterClickable) {
                    return;
                }

                check(i);

                if(mSelectionListener != null) {
                    mSelectionListener.onSectionSelected(view, i, l);
                }
				
				if (mShouldChangeTitle && i != 0 && i != mListView.getCount() - 1) {
					CharSequence title = (CharSequence) getItem(i);
					if (mActivity instanceof SherlockActivity)
						((SherlockActivity) mActivity).getSupportActionBar().setTitle(title);
					else if (mActivity instanceof ActionBarActivity)
						((ActionBarActivity) mActivity).getSupportActionBar().setTitle(title);
					else if (Build.VERSION.SDK_INT >= 11)
					    mActivity.getActionBar().setTitle(title);
				}
				
                closeDrawerMenu();
            }
        });
    }

    /**
     * The way to set the ListView adapter items
     * @param mainSections A String array with the main section titles. Cannot be null.
     * @param secondarySections A String array with the secondary section titles. May be null.
     * @param mainDrawableIds An integer array with the ids of the main sections icons. Must have the same length as mainSections. May be null.
     * @param secondaryDrawableIds An integer array with the ids of the secondary sections icons. Must have the same length as secondarySections. May be null.
     */
    public void setListViewSections(String[] mainSections, String[] secondarySections, int[] mainDrawableIds, int[] secondaryDrawableIds) {
        if(mListView == null) {
            configureList();
        }
        GoogleNavigationDrawerAdapter adapter = new GoogleNavigationDrawerAdapter(getContext(), mainSections, secondarySections, mainDrawableIds, secondaryDrawableIds);
        mListView.setAdapter(adapter);
        if(mHeaderView != null && !isHeaderClickable()) {
            check(1);
        } else {
            check(0);
        }
    }

    /**
     * Check an item on the ListView
     * @param position The position to check
     */
    public void check(int position) {
        mListView.setItemChecked(checkPosition, false);
        mListView.setItemChecked(position, true);
        checkPosition = position;
    }


    /**
     * Helper to access the isDrawerOpen method of the ListView menu.
     * @return whether it's opened or not
     */
    public boolean isDrawerMenuOpen() {
        return super.isDrawerOpen(mListView);
    }

    /**
     * Helper to open the ListView menu.
     */
    public void openDrawerMenu() {
        super.openDrawer(mListView);
    }

    /**
     * Helper to close the ListView menu.
     */
    public void closeDrawerMenu() {
        super.closeDrawer(mListView);
    }

    /**
     * Set custom listener so when a ListView item is clicked, it's also checked
     * @param listener The OnNavigationSectionSelected listener. Use null to disable.
     */
    public void setOnNavigationSectionSelected(OnNavigationSectionSelected listener) {
        mSelectionListener = listener;
    }

    /**
     * Set a custom header view
     * @param v The header view
     */
    public void setMenuHeader(View v) {
        if(mListView != null) {
            mHeaderView = v;
            mListView.addHeaderView(v);
        }
    }

    /**
     * Set a custom header view
     * @param v The header view
     */
    public void setMenuHeader(View v, boolean clickable) {
        if(mListView != null) {
            setHeaderClickable(clickable);
            if(mListView.getAdapter() != null) {
                ListAdapter adapter = (ListAdapter) mListView.getAdapter();
                removeView(mListView);
                configureList();
                mListView.addHeaderView(v, null, isHeaderClickable());
                mListView.setAdapter(adapter);
            } else {
                mListView.addHeaderView(v, null, isHeaderClickable());
            }
            mHeaderView = v;
        }
    }

    /**
     * Returns the header of the ListView
     * @return The header of the ListView
     */
    public View getMenuHeader() {
        return mHeaderView;
    }

    /**
     * Set a custom footer view
     * @param v The footer view
     */
    public void setMenuFooter(View v, boolean clickable) {
        if(mListView != null) {
            setFooterClickable(clickable);
            if(mListView.getAdapter() != null) {
                ListAdapter adapter = mListView.getAdapter();
                removeView(mListView);
                configureList();
                mListView.addFooterView(v, null, isFooterClickable());
                mListView.setAdapter(adapter);
            } else {
                mListView.addFooterView(v, null, isFooterClickable());
            }
            mFooterView = v;
        }
    }

    /**
     * Adding a header and a footer to an already populated ListView is quite inefficient.
     * To make it less messy, please use this method if you are going to add both of them.
     * @param header The header View
     * @param footer The footer View
     */
    public void setMenuHeaderAndFooter(View header, View footer, boolean headerClickable, boolean footerClickable) {
        if(mListView != null) {
            setHeaderClickable(headerClickable);
            setFooterClickable(footerClickable);
            if(mListView.getAdapter() != null) {
                ListAdapter adapter = mListView.getAdapter();
                removeView(mListView);
                configureList();
                mListView.addHeaderView(header, null, isHeaderClickable());
                mListView.addFooterView(footer, null, isFooterClickable());
                mListView.setAdapter(adapter);
            } else {
                mListView.addFooterView(header, null, isHeaderClickable());
                mListView.addFooterView(footer, null, isFooterClickable());
            }
            mHeaderView = header;
            mFooterView = footer;
        }
    }

    public boolean isHeaderClickable() {
        return mHeaderClickable;
    }

    public void setHeaderClickable(boolean mHeaderClickable) {
        this.mHeaderClickable = mHeaderClickable;
    }

    public boolean isFooterClickable() {
        return mFooterClickable;
    }

    public void setFooterClickable(boolean mFooterClickable) {
        this.mFooterClickable = mFooterClickable;
    }

    /**
     * Returns the footer of the ListView
     * @return The footer of the ListView
     */
    public View getMenuFooter() {
        return mFooterView;
    }

    public interface OnNavigationSectionSelected {

        public void onSectionSelected(View v, int i, long l);

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("view", superState);
        bundle.putInt("position", checkPosition);
        bundle.putBoolean("isdraweropen", isDrawerMenuOpen());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        super.onRestoreInstanceState(bundle.getParcelable("view"));
        check(bundle.getInt("position"));
        if (bundle.getBoolean("isdraweropen", false))
            openDrawerMenu();
    }
}
