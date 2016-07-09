package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.SurveyMaster;

import java.util.ArrayList;

/**
 * Created by Jith on 10/18/2015.
 */
public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.ViewHolder> {
    private ArrayList<SurveyMaster> data;
    private Activity activity;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        CheckBox cb1;
        CheckBox cb2;
        CheckBox cb3;
        CheckBox cb4;


        public ViewHolder(View v) {

            super(v);
            tvQuestion = (TextView) v.findViewById(R.id.tv_question);
            cb1 = (CheckBox) v.findViewById(R.id.cb1);
            cb2 = (CheckBox) v.findViewById(R.id.cb2);
            cb3 = (CheckBox) v.findViewById(R.id.cb3);
            cb4 = (CheckBox) v.findViewById(R.id.cb4);
        }

    }

    public SurveyListAdapter(Activity activity, ArrayList<SurveyMaster> data) {
        this.data = new ArrayList<>(data);
        this.activity = activity;
    }

    public void setData(ArrayList<SurveyMaster> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public SurveyMaster getItem(int position) {
        return data.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_survey, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private void clearCheckbox(ViewHolder holder) {
        holder.cb1.setChecked(false);
        holder.cb2.setChecked(false);
        holder.cb3.setChecked(false);
        holder.cb4.setChecked(false);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SurveyMaster question = data.get(position);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                SurveyMaster dm = (SurveyMaster) cb.getTag();
                if (cb.isChecked()) {
                    clearCheckbox(holder);
                    cb.setChecked(true);
                    if (cb == holder.cb1) {
                        dm.checkedItemPosition = 1;
                    } else if (cb == holder.cb2) {
                        dm.checkedItemPosition = 2;
                    } else if (cb == holder.cb3) {
                        dm.checkedItemPosition = 3;
                    } else if (cb == holder.cb4) {
                        dm.checkedItemPosition = 4;
                    } else {

                    }
                } else {
                    dm.checkedItemPosition = 0;
                }
            }
        };
        holder.tvQuestion.setText((position + 1) + ". " + question.Question);
        clearCheckbox(holder);
        switch (question.checkedItemPosition) {
            case 1:
                holder.cb1.setChecked(true);
                break;
            case 2:
                holder.cb2.setChecked(true);
                break;
            case 3:
                holder.cb3.setChecked(true);
                break;
            case 4:
                holder.cb4.setChecked(true);
                break;
            default:
                break;

        }
        holder.cb1.setTag(question);
        holder.cb2.setTag(question);

        if (question.Type == Constants.TYPE_YES_OR_NO) {
            holder.cb1.setText("Yes");
            holder.cb2.setText("No");
            holder.cb3.setVisibility(View.GONE);
            holder.cb4.setVisibility(View.GONE);
        } else {
            holder.cb1.setText(question.Option1);
            holder.cb2.setText(question.Option2);
            holder.cb3.setText(question.Option3);
            holder.cb4.setText(question.Option4);
            holder.cb3.setVisibility(View.VISIBLE);
            holder.cb4.setVisibility(View.VISIBLE);
            holder.cb3.setTag(question);
            holder.cb4.setTag(question);
            holder.cb3.setOnClickListener(onClickListener);
            holder.cb4.setOnClickListener(onClickListener);
        }
        holder.cb1.setOnClickListener(onClickListener);
        holder.cb2.setOnClickListener(onClickListener);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    public ArrayList<SurveyMaster> getAnsweredQuestions() {
        ArrayList<SurveyMaster> surveyResult = new ArrayList<>();
        for (SurveyMaster sm : data) {
            if (sm.checkedItemPosition != 0) {
                surveyResult.add(sm);
            }
        }
        return surveyResult;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

