package com.yjy.problems.problem.list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.yjy.problems.data.Problem;

import java.util.List;

public class ProblemListAdapter extends RecyclerView.Adapter<ProblemItemViewHolder> {

    private final LayoutInflater mLayoutInflater;

    private List<Problem> mProblemList;

    private OnItemClickListener mOnItemClickListener;

    private String mHighLightText;

    public ProblemListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ProblemItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ProblemItemViewHolder.create(mLayoutInflater, parent, viewType, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ProblemItemViewHolder holder, int position) {
        holder.setHighLightText(mHighLightText);
        holder.showProblem(mProblemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mProblemList.size();
    }

    public void setHighlightText(String text) {
        mHighLightText = text;
    }

    public void setProblemList(List<Problem> problemList) {
        mProblemList = problemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
