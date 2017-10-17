package com.yjy.problems.problem.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yjy.problems.R;
import com.yjy.problems.data.Problem;
import com.yjy.problems.view.checkBox.CheckBox;

import java.text.SimpleDateFormat;
import java.util.Locale;

class ProblemItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static SimpleDateFormat mSimpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @BindView(R.id.description)
    TextView mDescriptionTextView;

    @BindView(R.id.solution)
    TextView mResolveTextView;

    @BindView(R.id.detail)
    TextView mDetailTextView;

    @BindView(R.id.date)
    TextView mDateTextView;

    @BindView(R.id.done)
    CheckBox mDoneCheckBox;

    @BindView(R.id.delete)
    ImageView mDeleteButton;

    private OnItemClickListener mOnItemClickListener;

    private StringBuilder mDetailBuilder = new StringBuilder();

    private ProblemItemViewHolder(View view) {
        super(view);
    }

    static ProblemItemViewHolder create(
            LayoutInflater inflater,
            ViewGroup parent,
            int viewType,
            OnItemClickListener onClickListener) {

        View rootView = inflater.inflate(R.layout.item_problem, parent, false);
        ProblemItemViewHolder viewHolder = new ProblemItemViewHolder(rootView);
        ButterKnife.bind(viewHolder, rootView);

        rootView.setOnClickListener(viewHolder);
        viewHolder.mDoneCheckBox.setOnClickListener(viewHolder);
        viewHolder.mDeleteButton.setOnClickListener(viewHolder);

        viewHolder.setOnItemClickListener(onClickListener);
        return viewHolder;
    }

    private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    void showProblem(Problem mProblem) {
        mDescriptionTextView.setText(mProblem.getDescription());
        mResolveTextView.setText(mProblem.getSolution());
        mDateTextView.setText(
                mSimpleDateFormat.format(
                        mProblem.getDate()
                )
        );

        showDetail(mProblem.getProductName(), mProblem.getOrderId());

        mDoneCheckBox.setChecked(
                mProblem.isDone()
        );
    }

    private void showDetail(String productName, String orderId) {
        boolean hasNoProductName = productName.equals("");
        boolean hasNoOrderId = orderId.equals("");

        if (hasNoProductName && hasNoOrderId) {
            mDetailTextView.setVisibility(View.GONE);
        } else {
            mDetailTextView.setVisibility(View.VISIBLE);
            mDetailBuilder.setLength(0);
            mDetailBuilder.append(productName);

            if (!hasNoOrderId) {
                mDetailBuilder.append(" , ").append(orderId);
            }
        }

        mDetailTextView.setText(mDetailBuilder.toString());
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
