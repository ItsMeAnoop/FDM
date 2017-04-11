package com.field.datamatics.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.GlobalMessages;
import com.field.datamatics.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by Jith on 28/10/2015.
 * Message list dapter
 */
public class GlobalMessageAdapter extends RecyclerView.Adapter<GlobalMessageAdapter.Holder> {
    private ArrayList<GlobalMessages> data;
    private Context activity;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvDate, tvMessage, tvSender;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        ImageView imgMessageStatus;

        Holder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvMessage = (TextView) view.findViewById(R.id.tv_message);
            tvSender = (TextView) view.findViewById(R.id.tv_sender);
            content = (LinearLayout) view.findViewById(R.id.content);
            contentWithBG = (LinearLayout) view.findViewById(R.id.contentWithBackground);
            imgMessageStatus = (ImageView) view.findViewById(R.id.img_message_status);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public GlobalMessageAdapter(Context activity, ArrayList<GlobalMessages> data) {
        this.data = data;
        this.activity = activity;
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_message, viewGroup, false);
        Holder viewHolder = new Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        GlobalMessages message = data.get(position);

        setAlignment(holder, message.isOutgoing);

        if (message.isOutgoing) {
            holder.tvSender.setVisibility(View.GONE);

            int icon_id = message.status == 1 ? R.drawable.ic_sent : R.drawable.ic_not_sent;
            holder.imgMessageStatus.setImageResource(icon_id);
            holder.imgMessageStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvSender.setText(message.senderName + " : ");
            holder.tvSender.setVisibility(View.VISIBLE);
            holder.imgMessageStatus.setVisibility(View.GONE);
        }
        holder.tvMessage.setText(message.message);
        try {
            holder.tvDate.setText(Utilities.dateToString(message.date, "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy hh:mm a"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<GlobalMessages> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    private void setAlignment(Holder holder, boolean isMe) {
        if (isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.tvMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.tvMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.tvDate.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.tvDate.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.tvMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.tvMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.tvDate.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.tvDate.setLayoutParams(layoutParams);
        }
    }
}

