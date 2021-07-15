package com.swbg.mlivestreaming.view.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.swbg.mlivestreaming.BuildConfig;
import com.swbg.mlivestreaming.R;
import com.swbg.mlivestreaming.bean.ImageBean;
import com.swbg.mlivestreaming.utils.DisplayUtils;
import com.swbg.mlivestreaming.utils.Utils;

import java.util.List;

/**
 * @author KCrason
 * @date 2018/4/27
 */
public class NineImageAdapter implements NineGridView.NineGridAdapter<Object> {

    private List<ImageBean> mImageBeans;

    private Context mContext;

    private RequestOptions mRequestOptions;

    private DrawableTransitionOptions mDrawableTransitionOptions;


    public NineImageAdapter(Context context, RequestOptions requestOptions, DrawableTransitionOptions drawableTransitionOptions, List<ImageBean> imageBeans) {
        this.mContext = context;
        this.mDrawableTransitionOptions = drawableTransitionOptions;
        this.mImageBeans = imageBeans;
        int itemSize = (new Utils().getDisplayWidth(context) - 2 * DisplayUtils.dp2px(4) - DisplayUtils.dp2px(54)) / 3;
        this.mRequestOptions = requestOptions.override(itemSize, itemSize);
    }

    @Override
    public int getCount() {
        return mImageBeans == null ? 0 : mImageBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageBeans == null ? null :
                position < mImageBeans.size() ? mImageBeans.get(position) : null;
    }

    @Override
    public View getView(int position, View itemView) {
        ImageView imageView;
        if (itemView == null) {
            imageView = new ImageView(mContext);
            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_F2F2F2));
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            imageView = (ImageView) itemView;
        }
        String url = mImageBeans.get(position).getUrl();
        if (!url.contains("http")) {
            url = BuildConfig.IMAGE_BASE_URL + url;
        }
        Glide.with(mContext).load( url).placeholder(R.mipmap.login_code_failed)
//                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(DisplayUtils.dp2px(4))))
                .centerCrop()
                .into(imageView);

//        Glide.with(mContext).load(url).apply(mRequestOptions).transition(mDrawableTransitionOptions).into(imageView);
        return imageView;
    }
}
