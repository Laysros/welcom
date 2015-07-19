package com.sabayrean.welcom.adapter;

/**
 * Created by LAYLeangsros on 11/07/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sabayrean.welcom.R;
import com.sabayrean.welcom.app.AppController;
import com.sabayrean.welcom.image.FeedImageView;

import java.util.Collections;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
    private Context context;

    private LayoutInflater inflater;
    private List<FeedItem> data = Collections.emptyList();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedAdapter(Context context, List<FeedItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.feed_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FeedItem current = data.get(position);



        FeedItem item = data.get(position);
        holder.name.setText(item.getName());

/*        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);*/

        holder.timestamp.setText(item.getTimeStamp());

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            holder.statusMsg.setText(item.getStatus());
            holder.statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            holder.statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getUrl() != null) {
            holder.url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            holder.url.setMovementMethod(LinkMovementMethod.getInstance());
            holder.url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            holder.url.setVisibility(View.GONE);
        }

        // user profile pic
        holder.profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        // Feed image
        if (item.getImge() != null) {
            holder.feedImageView.setImageUrl(item.getImge(), imageLoader);
            holder.feedImageView.setVisibility(View.VISIBLE);
            holder.feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            holder.feedImageView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, timestamp, statusMsg, url;
        NetworkImageView profilePic;
        FeedImageView feedImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            statusMsg = (TextView) itemView.findViewById(R.id.txtStatusMsg);
            url = (TextView) itemView.findViewById(R.id.txtUrl);

            profilePic = (NetworkImageView) itemView.findViewById(R.id.profilePic);
            feedImageView = (FeedImageView) itemView.findViewById(R.id.feedImage1);



        }
    }
}