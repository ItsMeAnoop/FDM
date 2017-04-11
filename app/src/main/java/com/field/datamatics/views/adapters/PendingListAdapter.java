package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.database.PendingRemarks;
import com.field.datamatics.database.PendingRemarks$Table;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.views.helper.ListDialogHelper;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;

/**
 * Created by Jith on 10/18/2015.
 * Pending list adapter
 */
public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.ViewHolder> {
    private ArrayList<JoinClientRoutePlan> data;
    private Activity activity;
    private GetListItemClickPosition callback;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvClienName;
        TextView tvSpeciality;
        TextView tvEmailPhone;
        TextView tv_view_remark;
        CardView cv;
        TextView tv_location_head;

        public ViewHolder(View v) {
            super(v);
            tvClienName = (TextView) v.findViewById(R.id.tv_client_name);
            tvSpeciality = (TextView) v.findViewById(R.id.tv_speciality);
            tvEmailPhone = (TextView) v.findViewById(R.id.tv_email_phno);
            tv_view_remark= (TextView) v.findViewById(R.id.tv_view_remark);
            cv= (CardView) v.findViewById(R.id.cv);
            tv_location_head= (TextView) v.findViewById(R.id.tv_location_head);
        }

    }

    public PendingListAdapter(Activity activity, ArrayList<JoinClientRoutePlan> data,GetListItemClickPosition callback) {
        this.data = data;
        this.activity = activity;
        this.callback=callback;
    }

    public void setData(ArrayList<JoinClientRoutePlan> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public JoinClientRoutePlan getItem(int position) {
        return data.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_clients_pending, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final JoinClientRoutePlan client = data.get(position);
        holder.tvClienName.setText(client.Client_First_Name + " " + client.Client_Last_Name);
        holder.tvSpeciality.setText(client.Speciality);
        holder.tvEmailPhone.setText(client.Client_Email);
        holder.tv_view_remark.setVisibility(View.VISIBLE);
        holder.tv_view_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemarks(client.Route_Plan_Number,client.Client_Number+"");
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.getPosition(position);
            }
        });
        holder.tv_location_head.setVisibility(View.GONE);
        try {
            if(position==0){
                holder.tv_location_head.setVisibility(View.VISIBLE);
                holder.tv_location_head.setText(client.Location);
            }
            else{
                if(!client.Location.equals(data.get(position-1).Location)){
                    holder.tv_location_head.setVisibility(View.VISIBLE);
                    holder.tv_location_head.setText(client.Location);
                }
            }
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

    private void showRemarks(int routePlanNo,String clientId){
        ArrayList<PendingRemarks>remarkses= (ArrayList<PendingRemarks>)
                new Select().from(PendingRemarks.class)
                        .where(Condition.column(PendingRemarks$Table.ROUTEPLANNO).eq(routePlanNo))
                        .and(Condition.column(PendingRemarks$Table.CLIENT_ID).eq(clientId))
                        .queryList();
        if(remarkses!=null&&remarkses.size()>0){
            ArrayList<String>remark=new ArrayList<>();
            for(int i=0;i<remarkses.size();i++){
                remark.add(remarkses.get(i).remarks+" - "+remarkses.get(i).datetime);
            }
            ListDialogHelper.getInstance().getAlertDialog(activity,remark);
        }
        else{
            Toast.makeText(activity, "No Remarks", Toast.LENGTH_SHORT).show();
        }

    }
    public interface GetListItemClickPosition{
        public void getPosition(int position);
    }
}
