package com.example.roman.imagegrid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.roman.imagegrid.PictureData;
import com.example.roman.imagegrid.R;

import java.util.ArrayList;

/**
 * Created by Roman on 13.03.2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridHolder> {

    BitmapUtils mBitmapUtils = new BitmapUtils();

    private ArrayList<PictureData> pictures;
    private Context context;
    private Resources resources;
    private ColorMatrixColorFilter grayscaleFilter;
    private LayoutInflater layoutInflater;

    OnItemClickListener mItemClickListener;


    public GridAdapter(Context context) {
        this.context = context;
        resources = context.getResources();
        pictures = mBitmapUtils.loadPhotos(resources);
        ColorMatrix grayMatrix = new ColorMatrix();
        grayMatrix.setSaturation(0);
        grayscaleFilter = new ColorMatrixColorFilter(grayMatrix);
        //    layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View image = layoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new GridHolder(image);
    }

    @Override
    public void onBindViewHolder(GridHolder holder, int position) {
        PictureData pictureData = pictures.get(position);

        BitmapDrawable thumbnailDrawable =
                new BitmapDrawable(resources, pictureData.thumbnail);
        thumbnailDrawable.setColorFilter(grayscaleFilter);

        holder.image.setImageDrawable(thumbnailDrawable);
        //            holder.image.setOnClickListener(thumbnailClickListener);
        holder.image.setTag(pictureData);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public static class GridHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public GridHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}