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

package org.arasthel.googlenavdrawermenu.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.arasthel.googlenavdrawermenu.R;
import org.arasthel.googlenavdrawermenu.views.CheckableRelativeLayout;
import org.arasthel.googlenavdrawermenu.views.CheckedTextView;

/**
 * Created by Arasthel on 14/04/14.
 */
public class GoogleNavigationDrawerAdapter extends BaseAdapter {

    private static final int TYPE_MAIN = 0;
    private static final int TYPE_SECONDARY = 1;

    private String[] mMainSections;
    private String[] mSecondarySections;
    private int[] mMainSectionsDrawableIds;
    private int[] mSecondarySectionsDrawableIds;

    private int mMainBackResId = R.drawable.main_section_background;
    private int mSecondaryBackResId = R.drawable.secondary_section_background;

    private Context mContext;

    public GoogleNavigationDrawerAdapter(Context context) {
        mContext = context;
    }

    public GoogleNavigationDrawerAdapter(Context context, String[] mainSections, String[] secondarySections, int[] mainSectionsDrawableIds, int[] secondarySectionsDrawableIds) {
        this(context);
        mMainSections = mainSections;
        mSecondarySections = secondarySections;
        mMainSectionsDrawableIds = mainSectionsDrawableIds;
        mSecondarySectionsDrawableIds = secondarySectionsDrawableIds;
    }

    public GoogleNavigationDrawerAdapter(Context context, String[] mainSections, String[] secondarySections, int[] mainSectionsDrawableIds, int[] secondarySectionsDrawableIds, int mainBackResId, int secondaryBackResId) {
        this(context, mainSections, secondarySections, mainSectionsDrawableIds, secondarySectionsDrawableIds);
        mMainBackResId = mainBackResId;
        mSecondaryBackResId = secondaryBackResId;
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= mMainSections.length) ? TYPE_SECONDARY : TYPE_MAIN;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(mMainSections != null) {
            count += mMainSections.length;
        }
        if(mSecondarySections != null) {
            count += mSecondarySections.length;
        }
        return count;
    }

    @Override
    public Object getItem(int i) {
        if(i >= mMainSections.length && mSecondarySections != null) {
            return mSecondarySections[i - mMainSections.length];
        } else {
            return mMainSections[i];
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int icon = -1;
        switch (getItemViewType(i)) {
            case TYPE_MAIN:
                PrimaryHolder primaryHolder;
                if(view == null) {
                    view = View.inflate(mContext, R.layout.main_navigation_item, null);
                    view.setBackgroundResource(mMainBackResId);
                    primaryHolder = new PrimaryHolder();
                    primaryHolder.primaryTextView = (CheckedTextView) view.findViewById(android.R.id.text1);
                    primaryHolder.primaryImageView = (ImageView) view.findViewById(android.R.id.icon);
                    view.setTag(primaryHolder);
                } else {
                    primaryHolder = (PrimaryHolder) view.getTag();
                }
                primaryHolder.primaryTextView.setText((CharSequence) getItem(i));
                if(((CheckableRelativeLayout) view).isChecked()) {
                    primaryHolder.primaryTextView.setTypeface(null, Typeface.BOLD);
                } else {
                    primaryHolder.primaryTextView.setTypeface(null, Typeface.NORMAL);
                }
                icon = getPrimaryDrawableId(i);
                if(icon != -1) {
                    primaryHolder.primaryImageView.setImageResource(icon);
                    primaryHolder.primaryImageView.setVisibility(View.VISIBLE);
                } else {
                    primaryHolder.primaryImageView.setVisibility(View.GONE);
                }
                break;
            case TYPE_SECONDARY:
                SecondaryHolder holder;
                if(view == null) {
                    view = View.inflate(mContext, R.layout.secondary_navigation_item, null);
                    holder = new SecondaryHolder();
                    view.setBackgroundResource(mSecondaryBackResId);
                    holder.secondaryTextView = (CheckedTextView) view.findViewById(android.R.id.text1);
                    holder.secondaryImageView = (ImageView) view.findViewById(android.R.id.icon);
                    holder.topDivider = view.findViewById(R.id.google_nav_drawer_divider_top);
                    view.setTag(holder);
                } else {
                    holder = (SecondaryHolder) view.getTag();
                }
                if(i != mMainSections.length) {
                    holder.topDivider.setVisibility(View.INVISIBLE);
                } else {
                    holder.topDivider.setVisibility(View.VISIBLE);
                }
                holder.secondaryTextView.setText((CharSequence) getItem(i));

                if(((CheckableRelativeLayout) view).isChecked()) {
                    holder.secondaryTextView.setTypeface(null, Typeface.BOLD);
                } else {
                    holder.secondaryTextView.setTypeface(null, Typeface.NORMAL);
                }

                icon = getSecondaryDrawableId(i);
                if(icon != -1) {
                    holder.secondaryImageView.setImageResource(icon);
                    holder.secondaryImageView.setVisibility(View.VISIBLE);
                } else {
                    holder.secondaryImageView.setVisibility(View.GONE);
                }

                break;
        }
        return view;
    }

    private int getPrimaryDrawableId(int position) {
        if(mMainSectionsDrawableIds == null) {
            return -1;
        }

        if(mMainSectionsDrawableIds.length <= position) {
            return -1;
        }

        return mMainSectionsDrawableIds[position];
    }

    private int getSecondaryDrawableId(int position) {
        position = position - mMainSections.length;
        if(mSecondarySectionsDrawableIds == null) {
            return -1;
        }

        if(mSecondarySectionsDrawableIds.length <= position) {
            return -1;
        }

        return mSecondarySectionsDrawableIds[position];
    }

    private class PrimaryHolder {
        public CheckedTextView primaryTextView;
        public ImageView primaryImageView;
    }

    private class SecondaryHolder {
        public CheckedTextView secondaryTextView;
        public ImageView secondaryImageView;
        public View topDivider;
    }

    public String[] getMainSections() {
        return mMainSections;
    }

    public void setMainSections(String[] mMainSections) {
        this.mMainSections = mMainSections;
    }

    public String[] getSecondarySections() {
        return mSecondarySections;
    }

    public void setSecondarySections(String[] mSecondarySections) {
        this.mSecondarySections = mSecondarySections;
    }

    public int[] getMainSectionsDrawableIds() {
        return mMainSectionsDrawableIds;
    }

    public void setMainSectionsDrawableIds(int[] mMainSectionsDrawableIds) {
        this.mMainSectionsDrawableIds = mMainSectionsDrawableIds;
    }

    public int[] getSecondarySectionsDrawableIds() {
        return mSecondarySectionsDrawableIds;
    }

    public void setSecondarySectionsDrawableIds(int[] mSecondarySectionsDrawableIds) {
        this.mSecondarySectionsDrawableIds = mSecondarySectionsDrawableIds;
    }

    public int getMainBackResId() {
        return mMainBackResId;
    }

    public void setMainBackResId(int mMainBackResId) {
        this.mMainBackResId = mMainBackResId;
    }

    public int getSecondaryBackResId() {
        return mSecondaryBackResId;
    }

    public void setSecondaryBackResId(int mSecondaryBackResId) {
        this.mSecondaryBackResId = mSecondaryBackResId;
    }
}
