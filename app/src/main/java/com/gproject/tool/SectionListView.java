package com.gproject.tool;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.gproject.androidcoffee12.R;


public class SectionListView extends LinearLayout {

    public SectionListView(Context context) {
        this(context, null);
    }

    public SectionListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOrientation(VERTICAL);
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1.0f, r.getDisplayMetrics());
        setPadding(0, (int)px, 0, (int)px);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        hideLastItemLine(getChildCount());
    }

    public void refresh() {
        int childCount = getChildCount();
        if (childCount > 0) {
            int visibleChildCount = 0;
            for (int i = 0; i < childCount; i++) {
                RelativeLayout itemView = (RelativeLayout) getChildAt(i);
                View divider = itemView.findViewById(R.id.divider);
                if (divider != null) {
                    divider.setVisibility(VISIBLE);
                }
                if (itemView.getVisibility() != GONE) {
                    visibleChildCount++;
                }
            }
            hideLastItemLine(visibleChildCount);
        }
    }

    private void hideLastItemLine(int count) {
        if (count > 0) {
            View lastChildView = getChildAt(count - 1);
            View divider = lastChildView.findViewById(R.id.divider);
            if (divider != null) {
                divider.setVisibility(GONE);
            }
        }
    }
}