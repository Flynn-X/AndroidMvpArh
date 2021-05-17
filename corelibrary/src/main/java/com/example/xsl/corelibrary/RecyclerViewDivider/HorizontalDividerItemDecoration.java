package com.example.xsl.corelibrary.RecyclerViewDivider;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by yqritc on 2015/01/15.
 *  使用 方法：recyclerView.addItemDecoration(
 new HorizontalDividerItemDecoration.Builder(this)
 .color(Color.RED)
 .sizeResId(R.dimen.divider)
 .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
 .build());
 * @version 2.0
 * @author xsl
 * @date 2017/09/16
 * @dec
 * 优化解决如下问题
 * 1、getDimensionPixelSize（dp、sp）替换成 getDimensionPixelOffset（px）使最小单位可以到达 1px
 * 2、增加分割线支持四面随意添加
 * 3、修复gridmaanger 下显示问题（还有小问题，持续优化）
 * 4、增加调用方法。
 *  注意：改动较大，核心计算方法请勿参考开源源码原理。
 *
 */
public class HorizontalDividerItemDecoration extends FlexibleDividerDecoration {

    private MarginProvider mMarginProvider;

    protected HorizontalDividerItemDecoration(Builder builder) {
        super(builder);
        mMarginProvider = builder.mMarginProvider;
    }

    @Override
    protected Rect getDividerBound(int position, RecyclerView parent, View child) {
        Rect bounds = new Rect(0, 0, 0, 0);
        int transitionX = (int) ViewCompat.getTranslationX(child);
        int transitionY = (int) ViewCompat.getTranslationY(child);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        bounds.left = child.getLeft() + transitionX;
        bounds.right = child.getRight() + transitionX;

        int dividerSize = getDividerSize(position, parent);
        if (mDividerType == DividerType.DRAWABLE || mDividerType == DividerType.SPACE) {
            if (alignLeftEdge(parent, position)) {
                bounds.left += mMarginProvider.dividerLeftMargin(position, parent);
            }

            if (alignRightEdge(parent, position)) {
                bounds.right -= mMarginProvider.dividerRightMargin(position, parent);
            } else {
                // 交叉位置特殊处理
                bounds.right += getDividerSize(position, parent);
            }
            bounds.top = child.getBottom() + params.bottomMargin + transitionY;
            bounds.bottom = bounds.top + dividerSize;
        } else {
            int halfSize = dividerSize / 2;
            bounds.top = child.getBottom() + params.bottomMargin + halfSize + transitionY;
            bounds.bottom = bounds.top;
        }

        if (mPositionInsideItem) {
            bounds.top -= dividerSize;
            bounds.bottom -= dividerSize;
        }

        return bounds;
    }

    private boolean alignLeftEdge(RecyclerView parent, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();
            int spanCount = manager.getSpanCount();
            if (manager.getOrientation() == GridLayoutManager.VERTICAL) // 垂直布局
            {
                if (lookup.getSpanIndex(position, spanCount) == 0) // 第一列
                {
                    return true;
                }
            } else // 水平布局
            {
                if (manager.getReverseLayout()) {
                    return lookup.getSpanGroupIndex(position, spanCount) == lookup.getSpanGroupIndex(parent.getAdapter().getItemCount() - 1, spanCount);
                } else {
                    if (lookup.getSpanGroupIndex(position, spanCount) == 0) {
                        return true;
                    }
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) manager.findViewByPosition(position).getLayoutParams();
            int spanCount = manager.getSpanCount();
            int spanIndex = params.getSpanIndex();

            if (manager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) // 垂直布局
            {
                return spanIndex == 0;
            } else // 水平布局
            {
                if (manager.getReverseLayout()) {
                    int[] lastPosition = manager.findLastVisibleItemPositions(null);
                    boolean hasDirectionAlign = false;
                    for (int p : lastPosition) {
                        if (p != position && p != -1) {
                            StaggeredGridLayoutManager.LayoutParams params1 = (StaggeredGridLayoutManager.LayoutParams) manager.findViewByPosition(p).getLayoutParams();
                            if (params1.getSpanIndex() == spanIndex) {
                                hasDirectionAlign = true;
                                break;
                            }
                        }
                    }
                    return !hasDirectionAlign;
                } else {
                    return position < spanCount;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            return true;
        }
        return false;
    }

    private boolean alignRightEdge(RecyclerView parent, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();
            int spanCount = manager.getSpanCount();
            int itemCount = parent.getAdapter().getItemCount();
            if (manager.getOrientation() == GridLayoutManager.VERTICAL) // 垂直布局
            {
                if (positionTotalSpanSize(manager, position) == spanCount) {
                    return true;
                }
            } else // 水平布局
            {
                if (manager.getReverseLayout()) {
                    return lookup.getSpanGroupIndex(position, spanCount) == 0;
                } else {
                    int lastRowFirstPosition = 0;
                    for (int i = itemCount - 1; i >= 0; i--) {
                        if (lookup.getSpanIndex(i, spanCount) == 0) {
                            lastRowFirstPosition = i;
                            break;
                        }
                    }
                    if (position >= lastRowFirstPosition) {
                        return true;
                    }
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) manager.findViewByPosition(position).getLayoutParams();
            int spanCount = manager.getSpanCount();
            int spanIndex = params.getSpanIndex();

            if (manager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) // 垂直布局
            {
                return spanIndex == spanCount - 1;
            } else // 水平布局
            {
                if (manager.getReverseLayout()) {
                    return position < spanCount;
                } else {
                    int[] lastPosition = manager.findLastVisibleItemPositions(null);

                    boolean hasRight = false;
                    for (int p : lastPosition) {
                        if (p != position && p != -1) {
                            StaggeredGridLayoutManager.LayoutParams params1 = (StaggeredGridLayoutManager.LayoutParams) manager.findViewByPosition(p).getLayoutParams();
                            if (params1.getSpanIndex() == spanIndex) {
                                hasRight = true;
                                break;
                            }
                        }
                    }
                    return !hasRight;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            return true;
        }
        return false;
    }

    @Override
    protected void setItemOffsets(Rect outRect, int position, RecyclerView parent) {
        if (mPositionInsideItem) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        /**
         * GGridLayoutManager extends LinearLayoutManager 特别注意这点
         */
        if (mShowFirstLastDivider && position == 0 && !(parent.getLayoutManager() instanceof GridLayoutManager)){
            outRect.set(0, getDividerSize(position, parent), 0, getDividerSize(position, parent));
        }else if (mShowFirstLastDivider && position < spaceCount && parent.getLayoutManager() instanceof GridLayoutManager){
            outRect.set(0, getDividerSize(position, parent), 0, getDividerSize(position, parent));
        }else {
            outRect.set(0, 0, 0, getDividerSize(position, parent));
        }

    }

    private int getDividerSize(int position, RecyclerView parent) {
        if (mPaintProvider != null) {
            return (int) mPaintProvider.dividerPaint(position, parent).getStrokeWidth();
        } else if (mSizeProvider != null) {
            return mSizeProvider.dividerSize(position, parent);
        } else if (mDrawableProvider != null) {
            Drawable drawable = mDrawableProvider.drawableProvider(position, parent);
            return drawable.getIntrinsicHeight();
        } else if (mSpaceProvider != null) {
            return mSpaceProvider.dividerSize(position, parent);
        }
        throw new RuntimeException("failed to get size");
    }

    /**
     * Interface for controlling divider margin
     */
    public interface MarginProvider {

        /**
         * Returns left margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return left margin
         */
        int dividerLeftMargin(int position, RecyclerView parent);

        /**
         * Returns right margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return right margin
         */
        int dividerRightMargin(int position, RecyclerView parent);
    }

    public static class Builder extends FlexibleDividerDecoration.Builder<Builder> {

        private MarginProvider mMarginProvider = new MarginProvider() {
            @Override
            public int dividerLeftMargin(int position, RecyclerView parent) {
                return 0;
            }

            @Override
            public int dividerRightMargin(int position, RecyclerView parent) {
                return 0;
            }
        };

        public Builder(Context context) {
            super(context);
        }

        public Builder margin(final int leftMargin, final int rightMargin) {
            return marginProvider(new MarginProvider() {
                @Override
                public int dividerLeftMargin(int position, RecyclerView parent) {
                    return leftMargin;
                }

                @Override
                public int dividerRightMargin(int position, RecyclerView parent) {
                    return rightMargin;
                }
            });
        }

        public Builder margin(int horizontalMargin) {
            return margin(horizontalMargin, horizontalMargin);
        }

        public Builder marginResId(@DimenRes int leftMarginId, @DimenRes int rightMarginId) {
            return margin(mResources.getDimensionPixelOffset(leftMarginId),
                    mResources.getDimensionPixelOffset(rightMarginId));
        }

        public Builder marginResId(@DimenRes int horizontalMarginId) {
            return marginResId(horizontalMarginId, horizontalMarginId);
        }

        public Builder marginProvider(MarginProvider provider) {
            mMarginProvider = provider;
            return this;
        }

        public HorizontalDividerItemDecoration build() {
            checkBuilderParams();
            return new HorizontalDividerItemDecoration(this);
        }
    }
}