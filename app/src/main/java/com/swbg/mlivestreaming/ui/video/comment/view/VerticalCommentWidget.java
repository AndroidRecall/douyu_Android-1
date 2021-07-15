//package com.swbg.mlivestreaming.ui.video.comment.view;
//
//import android.content.Context;
//import android.text.SpannableStringBuilder;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//
//import com.swbg.mlivestreaming.R;
//import com.swbg.mlivestreaming.bean.CommentBean;
//import com.swbg.mlivestreaming.utils.ScreenUtils;
//import com.swbg.mlivestreaming.view.gridview.SimpleWeakObjectPool;
//
//import java.util.List;
//
///**
// * @author KCrason
// * @date 2018/4/27
// */
//public class VerticalCommentWidget extends LinearLayout implements ViewGroup.OnHierarchyChangeListener, OnItemClickPopupMenuListener {
//
//    private List<CommentBean> mCommentBeans;
//
//    private LayoutParams mLayoutParams;
//    private SimpleWeakObjectPool<View> COMMENT_TEXT_POOL;
//    private int mCommentVerticalSpace;
//
//    public VerticalCommentWidget(Context context) {
//        super(context);
//        init();
//    }
//
//    public VerticalCommentWidget(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public VerticalCommentWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    private void init() {
//        mCommentVerticalSpace = ScreenUtils.dp2px(3f);
//        COMMENT_TEXT_POOL = new SimpleWeakObjectPool<>();
//        setOnHierarchyChangeListener(this);
//    }
//
//
//    public void addComments(List<CommentBean> commentBeans,String commentId, boolean isStartAnimation) {
//        this.mCommentBeans = commentBeans;
//        if (commentBeans != null) {
//            int oldCount = getChildCount();
//            int newCount = commentBeans.size();
//            if (oldCount > newCount) {
//                removeViewsInLayout(newCount, oldCount - newCount);
//            }
//            for (int i = 0; i < newCount; i++) {
//                boolean hasChild = i < oldCount;
//                View childView = hasChild ? getChildAt(i) : null;
//                CommentBean commentBean = commentBeans.get(i);
//                SpannableStringBuilder commentSpan = commentBean.getCommentContentSpan(commentId);
//                if (childView == null) {
//                    childView = COMMENT_TEXT_POOL.get();
//                    if (childView == null) {
//                        addViewInLayout(makeCommentItemView(commentSpan, i,isStartAnimation), i, generateMarginLayoutParams(i), true);
//                    } else {
//                        addCommentItemView(childView, commentSpan, i,isStartAnimation);
//                    }
//                } else {
//                    updateCommentData(childView, commentSpan, i,isStartAnimation);
//                }
//            }
//            requestLayout();
//        }
//    }
//
//
//    /**
//     * 更新指定的position的comment
//     */
//    public void updateTargetComment(int position, List<CommentBean> commentBeans) {
//        int oldCount = getChildCount();
//        for (int i = 0; i < oldCount; i++) {
//            if (i == position) {
//                View childView = getChildAt(i);
//                if (childView != null) {
//                    CommentBean commentBean = commentBeans.get(i);
//                    SpannableStringBuilder commentSpan = commentBean.getCommentContentSpan();
////                    TranslationState translationState = commentBean.getTranslationState();
//                    updateCommentData(childView, commentSpan, i, true);
//                }
//                break;
//            }
//        }
//        requestLayout();
//    }
//
//
//    /**
//     * 創建Comment item view
//     */
//    private View makeCommentItemView(SpannableStringBuilder content, int index,  boolean isStartAnimation) {
//
//            return makeContentTextView(content, index);
//
//    }
//
//
//    /**
//     * 添加需要的Comment View
//     */
//    private void addCommentItemView(View view, SpannableStringBuilder builder, int index, boolean isStartAnimation) {
//      if (view instanceof TextView) {
////            if (translationState == TranslationState.START) {
//                ((TextView) view).setText(builder);
////                addOnItemClickPopupMenuListener(view, index, TranslationState.START);
//                addViewInLayout(view, index, generateMarginLayoutParams(index), true);
////            } else {
////                addViewInLayout(makeCommentItemView(builder, index, translationState, isStartAnimation), index, generateMarginLayoutParams(index), true);
////            }
//        }
//    }
//
//
////    private void addOnItemClickPopupMenuListener(View view, int index, TranslationState translationState) {
////        view.setOnLongClickListener(v -> {
////            Utils.showPopupMenu(getContext(), VerticalCommentWidget.this, index, v, translationState);
////            return false;
////        });
////    }
//
//    /**
//     * 更新comment list content
//     */
//    private void updateCommentData(View view, SpannableStringBuilder builder, int index, boolean isStartAnimation) {
//      if (view instanceof TextView) {
////            if (translationState == TranslationState.START) {
//                ((TextView) view).setText(builder);
////            } else {
////                addViewInLayout(makeCommentItemView(builder, index, translationState, isStartAnimation), index, generateMarginLayoutParams(index), true);
////                removeViewInLayout(view);
////            }
//        }
//    }
//
//    private TextView makeContentTextView(SpannableStringBuilder content, int index) {
//        TextView textView = new TextView(getContext());
//        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.main_gray));
////        textView.setBackgroundResource(R.drawable.selector_view_name_state);
//        textView.setTextSize(13f);
////        textView.setLineSpacing(mCommentVerticalSpace, 1f);
//        textView.setText(content);
//        textView.setMovementMethod(new TextMovementMethod());
////        addOnItemClickPopupMenuListener(textView, index, TranslationState.START);
//        return textView;
//    }
//
//
//    private LayoutParams generateMarginLayoutParams(int index) {
//        if (mLayoutParams == null) {
//            mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//        if (mCommentBeans != null && index > 0) {
////            mLayoutParams.bottomMargin = index == mCommentBeans.size() - 1 ? 0 : mCommentVerticalSpace;
////            mLayoutParams.bottomMargin = index == mCommentBeans.size() - 1 ? 0 : ScreenUtils.dp2px(9f);
//        }
//        mLayoutParams.topMargin = ScreenUtils.dp2px(9f);
//        mLayoutParams.leftMargin = ScreenUtils.dp2px(7f);
//        mLayoutParams.rightMargin = ScreenUtils.dp2px(7f);
//        return mLayoutParams;
//    }
//
//    @Override
//    public void onChildViewAdded(View parent, View child) {
//
//    }
//
//    @Override
//    public void onChildViewRemoved(View parent, View child) {
//        COMMENT_TEXT_POOL.put(child);
//    }
//
//    @Override
//    public void onItemClickCopy(int position) {
//        Toast.makeText(getContext(), "已复制", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onItemClickTranslation(int position) {
////        if (mCommentBeans != null && position < mCommentBeans.size()) {
////            mCommentBeans.get(position).setTranslationState(TranslationState.CENTER);
////            updateTargetComment(position, mCommentBeans);
////            TimerUtils.timerTranslation(() -> {
////                if (mCommentBeans != null && position < mCommentBeans.size()) {
////                    mCommentBeans.get(position).setTranslationState(TranslationState.END);
////                    updateTargetComment(position, mCommentBeans);
////                }
////            });
////        }
//    }
//
//
//    @Override
//    public void onItemClickHideTranslation(int position) {
////        if (mCommentBeans != null && position < mCommentBeans.size()) {
////            mCommentBeans.get(position).setTranslationState(TranslationState.START);
////            updateTargetComment(position, mCommentBeans);
////        }
//    }
//
//    @Override
//    public void onItemClickCollection(int position) {
//        Toast.makeText(getContext(), "已收藏", Toast.LENGTH_SHORT).show();
//    }
//
//
//}
