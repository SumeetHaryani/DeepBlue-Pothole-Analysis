package com.example.sugamxp.myapplication;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

public class PicAdapter extends  RecyclerView.Adapter<PicAdapter.PicHolder> {

    private static final String TAG = "AssetAdapter";
    private Context mContext;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ArrayList<PicInfo> mlist;
    private int flag;
    private PicInfo picInfo;
    private FirebaseUser firebaseUser;
    private String pk;
    String[] descriptionData = {"Unattended", "In Process", "Completed"};
    public PicAdapter(Context context, ArrayList<PicInfo> list) {
        mContext = context;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("result");
        mlist = list;
//                Log.d(TAG, "AssetAdapter: " + String.valueOf(mlist.size()));
    }

    @Override
    public PicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        Log.d(TAG, "onCreateViewHolder: GOT REQUEST CALLED===================");
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.status_cardview, parent, false);
        return new PicHolder(view);

    }

    @Override
    public void onBindViewHolder(final PicHolder holder, final int position) {
        if (mlist.size() != 0) {
//            Log.d(TAG, "onBindViewHolder: called" + String.valueOf(position))
            picInfo = mlist.get(holder.getAdapterPosition());
            if (mlist.get(holder.getAdapterPosition()).getNumber_of_potholes()==1) {
                holder.assetName.setText("Area - " + String.valueOf(mlist.get(holder.getAdapterPosition()).getPothole_area()));
            }else{
                holder.assetName.setText("");
            }
            holder.d.setText("Submitted on " + mlist.get(holder.getAdapterPosition()).getDate());
            holder.date.setText("Number of holes - " + String.valueOf(mlist.get(holder.getAdapterPosition()).getNumber_of_potholes()));
            Glide.with(mContext).load(mlist.get(holder.getAdapterPosition()).getPic_url()).into(holder.image);
            String p = mlist.get(holder.getAdapterPosition()).getStatus();
            if(p.equals("Unattended")){
                holder.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
            }
            else if(p.equals("In Process") || p.equals("In Progress")){
                holder.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            }else{
                holder.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);

            }


        }
    }

    class PicHolder extends RecyclerView.ViewHolder {

        TextView assetName;
        TextView date;
        ImageView image;
        TextView d;
        StateProgressBar stateProgressBar;
        public PicHolder(View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.tv_title);
            date = itemView.findViewById(R.id.tv_date);
            image = itemView.findViewById(R.id.image_display);
            d = itemView.findViewById(R.id.date);
            stateProgressBar = itemView.findViewById(R.id.progress_bar);
            stateProgressBar.setStateDescriptionData(descriptionData);
            stateProgressBar.enableAnimationToCurrentState(true);
        }

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


}
