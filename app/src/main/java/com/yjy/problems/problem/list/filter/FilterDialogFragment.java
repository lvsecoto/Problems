package com.yjy.problems.problem.list.filter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yjy.problems.R;
import com.yjy.problems.view.checkBox.CheckBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 */

public class FilterDialogFragment extends DialogFragment {

    @BindView(R.id.enableDoneFilter)
    Switch mEnDoneFilterSW;

    @BindView(R.id.done)
    CheckBox mDoneCB;

    @BindView(R.id.enableDateFilter)
    Switch mEnDateFilterSW;

    @BindView(R.id.fromDate)
    TextView mFromDateTV;

    @BindView(R.id.chooseFromDate)
    TextView mChooseFromDate;

    @BindView(R.id.toDate)
    TextView mToDateTV;

    @BindView(R.id.chooseToDate)
    TextView mChooseToDate;

    private DateFormat mDateFormat = new SimpleDateFormat("yyyy - MM - dd", Locale.getDefault());

    private Date mFromDate = new Date();

    private Date mToDate = new Date();

    private OnConfigureListener mOnConfigureListener = null;

    public FilterDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.window_filter, container, false);
        ButterKnife.bind(this, view);

        mDoneCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConfigureListener.OnDoneFilterChange(
                        mDoneCB.isChecked(),
                        mEnDoneFilterSW.isChecked()
                );
            }
        });

        mEnDoneFilterSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConfigureListener.OnDoneFilterChange(
                        mDoneCB.isChecked(),
                        mEnDoneFilterSW.isChecked()
                );
            }
        });

        mFromDateTV.setText(dateToString(mFromDate));
        mFromDateTV.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                calendar.setTime(mFromDate);

                new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                mFromDate = calendar.getTime();
                                mFromDateTV.setText(dateToString(mFromDate));

                                mOnConfigureListener.OnDateFilterChange(
                                        mFromDate,
                                        mToDate,
                                        mEnDateFilterSW.isChecked()
                                );
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();

            }
        });

        mToDateTV.setText(dateToString(mToDate));
        mToDateTV.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                calendar.setTime(mToDate);

                new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                mToDate = calendar.getTime();
                                mToDateTV.setText(dateToString(mToDate));

                                mOnConfigureListener.OnDateFilterChange(
                                        mFromDate,
                                        mToDate,
                                        mEnDateFilterSW.isChecked()
                                );
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();

            }
        });

        mEnDateFilterSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConfigureListener.OnDateFilterChange(
                        mFromDate,
                        mToDate,
                        mEnDateFilterSW.isChecked());
            }
        });

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "dialog");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String dateToString(Date date) {
        return mDateFormat.format(date);
    }

    public void setOnConfigureListener(OnConfigureListener onConfigureListener) {
        mOnConfigureListener = onConfigureListener;
    }

    public interface OnConfigureListener {

        void OnDoneFilterChange(boolean done, boolean enable);

        void OnDateFilterChange(Date from, Date to, boolean enable);
    }
}
