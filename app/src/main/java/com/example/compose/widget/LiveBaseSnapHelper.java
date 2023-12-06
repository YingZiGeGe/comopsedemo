package com.example.compose.widget;

import android.util.DisplayMetrics;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * 仿androidx 库中{@link SnapHelper}，在这基础上更改了滑动停止后查找 targetview 逻辑及统一了fling滑动与{@method snapToTargetExistingView}
 * 中SmoothScroller
 *
 * @author xinjing
 * @date 2021-05-07
 */
public abstract class LiveBaseSnapHelper extends RecyclerView.OnFlingListener {
    static final float MILLISECONDS_PER_INCH = 100f;

    RecyclerView mRecyclerView;
    protected int mSumDy = 0;
    protected int mSumDx = 0;
    protected boolean mScrolled = false;

    // Handles the snap on scroll case.
    private final RecyclerView.OnScrollListener mScrollListener =
            new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && mScrolled) {
                        snapToTargetExistingView(mSumDx, mSumDy);
                        mScrolled = false;
                        mSumDy = 0;
                        mSumDx = 0;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    if (dx != 0 || dy != 0) {
                        mScrolled = true;
                        mSumDy += dy;
                        mSumDx += dx;
                    }
                }
            };

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            return false;
        }
        int minFlingVelocity = mRecyclerView.getMinFlingVelocity();
        return (Math.abs(velocityY) > minFlingVelocity || Math.abs(velocityX) > minFlingVelocity)
                && snapFromFling(layoutManager, velocityX, velocityY);
    }

    /**
     * Attaches the {@link SnapHelper} to the provided RecyclerView, by calling
     * {@link RecyclerView#setOnFlingListener(RecyclerView.OnFlingListener)}.
     * You can call this method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove SnapHelper from the current
     *                     RecyclerView.
     * @throws IllegalArgumentException if there is already a {@link RecyclerView.OnFlingListener}
     *                                  attached to the provided {@link RecyclerView}.
     */
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
            throws IllegalStateException {
        if (mRecyclerView == recyclerView) {
            return; // nothing to do
        }
        if (mRecyclerView != null) {
            destroyCallbacks();
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            setupCallbacks();
            snapToTargetExistingView(0, 0);
        }
    }

    /**
     * Called when an instance of a {@link RecyclerView} is attached.
     */
    private void setupCallbacks() throws IllegalStateException {
        if (mRecyclerView.getOnFlingListener() != null) {
            throw new IllegalStateException("An instance of OnFlingListener already set.");
        }
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRecyclerView.setOnFlingListener(this);
    }

    /**
     * Called when the instance of a {@link RecyclerView} is detached.
     */
    private void destroyCallbacks() {
        mRecyclerView.removeOnScrollListener(mScrollListener);
        mRecyclerView.setOnFlingListener(null);
    }

    /**
     * Helper method to facilitate for snapping triggered by a fling.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @param velocityX     Fling velocity on the horizontal axis.
     * @param velocityY     Fling velocity on the vertical axis.
     * @return true if it is handled, false otherwise.
     */
    private boolean snapFromFling(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX,
                                  int velocityY) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return false;
        }

        RecyclerView.SmoothScroller smoothScroller = createScroller(0.3F, layoutManager);
        if (smoothScroller == null) {
            return false;
        }

        int targetPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY);
        if (targetPosition == RecyclerView.NO_POSITION) {
            return false;
        }

        smoothScroller.setTargetPosition(targetPosition);
        layoutManager.startSmoothScroll(smoothScroller);
        return true;
    }

    /**
     * Snaps to a target view which currently exists in the attached {@link RecyclerView}. This
     * method is used to snap the view when the {@link RecyclerView} is first attached; when
     * snapping was triggered by a scroll and when the fling is at its final stages.
     */
    void snapToTargetExistingView(int scrollX, int scrollY) {
        if (mRecyclerView == null) {
            return;
        }
        if (scrollY == 0) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        View snapView = findSnapView(layoutManager, scrollX, scrollY);
        if (snapView == null) {
            return;
        }
        RecyclerView.SmoothScroller smoothScroller = createScroller(0.4F, layoutManager);
        if (smoothScroller == null) {
            return;
        }
        int targetPosition = layoutManager.getPosition(snapView);
        if (targetPosition == RecyclerView.NO_POSITION) {
            return;
        }
        smoothScroller.setTargetPosition(targetPosition);
        layoutManager.startSmoothScroll(smoothScroller);

    }

    /**
     * Creates a scroller to be used in the snapping implementation.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @return a {@link RecyclerView.SmoothScroller} which will handle the scrolling.
     */
    @Nullable
    protected RecyclerView.SmoothScroller createScroller(float animSwitchRatio, RecyclerView.LayoutManager layoutManager) {
        return createSnapScroller(animSwitchRatio, layoutManager);
    }

    /**
     * Creates a scroller to be used in the snapping implementation.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @return a {@link LinearSmoothScroller} which will handle the scrolling.
     * @deprecated use {@link #createScroller(float, RecyclerView.LayoutManager)} instead.
     */
    @Nullable
    @Deprecated
    protected LinearSmoothScroller createSnapScroller(float animSwitchRatio, RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(mRecyclerView.getContext()) {
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                if (mRecyclerView == null) {
                    // The associated RecyclerView has been removed so there is no action to take.
                    return;
                }
                int[] snapDistances = calculateDistanceToFinalSnap(mRecyclerView.getLayoutManager(),
                        targetView);
                final int dx = snapDistances[0];
                final int dy = snapDistances[1];
                final int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
    }

    /**
     * Override this method to snap to a particular point within the target view or the container
     * view on any axis.
     * <p>
     * This method is called when the {@link SnapHelper} has intercepted a fling and it needs
     * to know the exact distance required to scroll by in order to snap to the target view.
     *
     * @param layoutManager the {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}
     * @param targetView    the target view that is chosen as the view to snap
     * @return the output coordinates the put the result into. out[0] is the distance
     * on horizontal axis and out[1] is the distance on vertical axis.
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public abstract int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                                       @NonNull View targetView);

    /**
     * Override this method to provide a particular target view for snapping.
     * <p>
     * This method is called when the {@link SnapHelper} is ready to start snapping and requires
     * a target view to snap to. It will be explicitly called when the scroll state becomes idle
     * after a scroll. It will also be called when the {@link SnapHelper} is preparing to snap
     * after a fling and requires a reference view from the current set of child views.
     * <p>
     * If this method returns {@code null}, SnapHelper will not snap to any view.
     *
     * @param layoutManager the {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}
     * @return the target view to which to snap on fling or end of scroll
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public abstract View findSnapView(RecyclerView.LayoutManager layoutManager, int xOrientation, int yOrientation);

    /**
     * Override to provide a particular adapter target position for snapping.
     *
     * @param layoutManager the {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}
     * @param velocityX     fling velocity on the horizontal axis
     * @param velocityY     fling velocity on the vertical axis
     * @return the target adapter position to you want to snap or {@link RecyclerView#NO_POSITION}
     * if no snapping should happen
     */
    public abstract int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
                                               int velocityY);
}
