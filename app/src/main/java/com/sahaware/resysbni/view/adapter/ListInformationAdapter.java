package com.sahaware.resysbni.view.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataGeneralInformation;

import java.util.List;

public class ListInformationAdapter extends RecyclerView.Adapter<ListInformationAdapter.MyViewHolder> {

    private List<DataGeneralInformation> informationList;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, description;
        public RelativeLayout buttonLayout, img;
        /**
         * You must use the ExpandableLinearLayout in the recycler view.
         * The ExpandableRelativeLayout doesn't work.
         */
        public ExpandableLinearLayout expandableLayout;

        public MyViewHolder(View v) {
            super(v);
            description = (TextView)v.findViewById(R.id.txt_description);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);
            img = (RelativeLayout) v.findViewById(R.id.img);
        }
    }


    public ListInformationAdapter(List<DataGeneralInformation> informationList) {
        this.informationList = informationList;
        for (int i = 0; i < informationList.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.list_row_information, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DataGeneralInformation item = informationList.get(position);
        holder.textView.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_dark_background));
        holder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.DECELERATE_INTERPOLATOR));
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.img, 0f, 180f).start();
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.img, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.img.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}