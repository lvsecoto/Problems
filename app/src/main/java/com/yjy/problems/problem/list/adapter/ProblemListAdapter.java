package com.yjy.problems.problem.list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.yjy.problems.data.Problem;

import java.util.List;

public class ProblemListAdapter extends RecyclerView.Adapter {

    private final LayoutInflater mLayoutInflater;

    private List<Problem> mData;

    private OnItemClickListener mOnItemClickListener;

    private String mHighLightText;

    public ProblemListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ProblemItemViewHolder.create(mLayoutInflater, parent, viewType, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ProblemItemViewHolder)holder).setHighLightText(mHighLightText);
        ((ProblemItemViewHolder) holder).showProblem(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setHighlightText(String text) {
        mHighLightText = text;
    }

    public void setData(List<Problem> data) {
        mData = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
