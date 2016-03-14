package com.example.roman.imagegrid;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Roman on 13.03.2016.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PACKAGE = "com.example.android.imagegrid";

    private int mColumnCount = 5;
    boolean newApi = true;

    RecyclerView mGrid;
    Context mContext;
    GridAdapter adapter;

    public PlaceholderFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Context) context;
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGrid = (RecyclerView) rootView.findViewById(R.id.grid);
        adapter = new GridAdapter(getActivity());

        return rootView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGrid.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        mGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });


        //adapter.SetOnItemClickListener(new GridAdapter.OnItemClickListener() {
        mGrid.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                                                                    new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // do something with position
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && newApi) {
                    PictureData info = (PictureData) v.getTag();
                    Intent subActivity = new Intent(getActivity(),
                            PictureDetailsActivity2.class);
                    subActivity.putExtra(PACKAGE + ".resourceId", info.resourceId);
                    getActivity().startActivity(subActivity,
                            ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                                    v.findViewById(R.id.imageView), "image").toBundle());


                } else {
                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)
                    int[] screenLocation = new int[2];
                    v.getLocationOnScreen(screenLocation);
                    PictureData info = (PictureData) v.getTag();
                    Intent subActivity = new Intent(getActivity(),
                            PictureDetailsActivity.class);
                    int orientation = getResources().getConfiguration().orientation;
                    subActivity.
                            putExtra(PACKAGE + ".orientation", orientation).
                            putExtra(PACKAGE + ".resourceId", info.resourceId).
                            putExtra(PACKAGE + ".left", screenLocation[0]).
                            putExtra(PACKAGE + ".top", screenLocation[1]).
                            putExtra(PACKAGE + ".width", v.getWidth()).
                            putExtra(PACKAGE + ".height", v.getHeight()).
                            putExtra(PACKAGE + ".description", info.description);
                    startActivity(subActivity);

                    // Override transitions: we don't want the normal window animation in addition
                    // to our custom one
                    getActivity().overridePendingTransition(0, 0);
                }
            };
        }));


        mGrid.setAdapter(adapter);
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


}