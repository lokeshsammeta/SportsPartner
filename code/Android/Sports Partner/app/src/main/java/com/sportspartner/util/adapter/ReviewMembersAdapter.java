package com.sportspartner.util.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sportspartner.R;
import com.sportspartner.models.UserOutline;
import com.sportspartner.models.UserReview;
import com.sportspartner.service.ActivityCallBack;
import com.sportspartner.service.ModelResult;
import com.sportspartner.service.ResourceService;

import java.util.ArrayList;

/**
 * @author Xiaochen Li
 */

public class ReviewMembersAdapter extends RecyclerView.Adapter<ReviewMembersAdapter.MyViewHolder> {
    private ArrayList<UserOutline> listMembers;
    private Context context;

    public ReviewMembersAdapter(ArrayList<UserOutline> listMembers, Context context) {
        this.listMembers = new ArrayList<>(listMembers);
        this.context = context;
    }

    @Override
    public ReviewMembersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review_member, parent, false);

        return new ReviewMembersAdapter.MyViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final ReviewMembersAdapter.MyViewHolder holder, int position) {
        UserOutline user = listMembers.get(position);

        String iconUUID = user.getIconUUID();
        ResourceService.getImage(context, iconUUID, ResourceService.IMAGE_SMALL, new ActivityCallBack<Bitmap>(){
            @Override
            public void getModelOnSuccess(ModelResult<Bitmap> result){
                if (result.isStatus()) {
                    holder.icon.setImageBitmap(result.getModel());
                } else{
                    //if failure, show a toast
                    Log.e("ReviewMemAdapter",
                            "Load icon error: "+result.getMessage());
                }
            }
        });

        holder.name.setText(user.getUserName());
    }

    @Override
    public int getItemCount() {
        return listMembers.size();
    }

    public String getUserIdByPosition(int pos) {
        try {
            return listMembers.get(pos).getUserId();
        } catch (Exception ex) {
            return null;
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView name;
        private Context context;
        public RatingBar punctuality;
        public RatingBar participation;
        public EditText comment;

        public MyViewHolder(View view, Context context) {
            super(view);
            this.icon = (ImageView) view.findViewById(R.id.profile_photo);
            this.name = (TextView) view.findViewById(R.id.profile_name);
            this.context = context;
            this.punctuality = view.findViewById(R.id.rating_punctuality);
            this.participation = view.findViewById(R.id.rating_participation);
            this.comment = view.findViewById(R.id.comment);
        }

        public double getPunctuality() {
            return punctuality.getRating();
        }

        public double getParticipation() {
            return participation.getRating();
        }

        public String getComment() {
            return comment.getText().toString();
        }
    }
}
