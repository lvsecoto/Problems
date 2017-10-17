package com.yjy.problems.problem.detail;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yjy.problems.R;
import com.yjy.problems.data.source.DbOpenHelper;
import com.yjy.problems.data.source.ProblemDataSource;
import com.yjy.problems.view.checkBox.CheckBox;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ProblemDetailFragment extends Fragment implements ProblemDetailContract.View {

    public static final String KEY_PROBLEM_ID = "KEY_PROBLEM_ID";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.description)
    EditText mDescription;

    @BindView(R.id.solution)
    EditText mSolution;

    @BindView(R.id.date)
    EditText mDate;

    @BindView(R.id.done)
    CheckBox mDone;

    @BindView(R.id.product)
    EditText mProductName;

    @BindView(R.id.orderId)
    EditText mOrderId;

    private ProblemDetailContract.Presenter mPresenter;

    private DatePickerDialog.OnDateSetListener mListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            mPresenter.onDateSet(calendar.getTime());
        }
    };

    private DatePickerDialog mDatePickerDialog;

    private DialogInterface.OnClickListener mOnListItemChoose = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPresenter.onListItemChoose(which);
        }
    };

    /**
     * Show the problem in a activity
     *
     * @param context   The activity context
     * @param activity  The activity will be added
     * @param problemId The problem id. If the id doesn't exist, a new problem with this id will be
     *                  created.
     */
    public static void showInActivity(Context context, Class activity, @NonNull UUID problemId) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(KEY_PROBLEM_ID, problemId);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_problem_detail, container, false);
        ButterKnife.bind(this, rootView);

        mDatePickerDialog = new DatePickerDialog(getContext(), mListener, 0, 0, 0);

        mPresenter = new ProblemDetailPresenter(
                this, new ProblemDataSource(new DbOpenHelper(getContext())), (UUID) getArguments().getSerializable(KEY_PROBLEM_ID)
        );

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void setPresenter(ProblemDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showDescription(String description) {
        mDescription.setText(description);
    }

    @Override
    public String getDescription() {
        return mDescription.getText().toString();
    }

    @Override
    public void showSolution(String solution) {
        mSolution.setText(solution);
    }

    @Override
    public String getSolution() {
        return mSolution.getText().toString();
    }

    @Override
    public void showDate(String date) {
        mDate.setText(date);
    }

    // TODO: 2017/10/11 use date string
    @Override
    public String getDate() {
        return mDate.getText().toString();
    }

    @Override
    public void showDone(boolean done) {
        mDone.setChecked(done);
    }

    @Override
    public boolean isDone() {
        return mDone.isChecked();
    }

    @Override
    public void showProductName(String productName) {
        mProductName.setText(productName);
    }

    @Override
    public String getProductName() {
        return mProductName.getText().toString();
    }

    @Override
    public void showOrderId(String orderId) {
        mOrderId.setText(orderId);
    }

    @Override
    public String getOrderId() {
        return mOrderId.getText().toString();
    }

    @Override
    public void showDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mDatePickerDialog.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        mDatePickerDialog.show();
    }

    @Override
    public void showChooseList(List<String> items) {
        String[] itemArray = new String[items.size()];
        new AlertDialog.Builder(getContext())
                .setItems(items.toArray(itemArray), mOnListItemChoose).show();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.chooseDate, R.id.chooseProduct})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.chooseDate:
                mPresenter.chooseDate();
                break;
            case R.id.chooseProduct:
                mPresenter.chooseProduct();
                break;
        }
    }
}
