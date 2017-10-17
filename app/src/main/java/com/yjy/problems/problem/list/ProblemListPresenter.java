package com.yjy.problems.problem.list;

import com.yjy.problems.data.Problem;
import com.yjy.problems.data.source.IProblemDataSource;
import com.yjy.problems.data.source.ProblemDataSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/10/7.
 */

class ProblemListPresenter implements ProblemListContract.Presenter {

    private static final int REQUEST_CHOOSE_FROM_DATE = 1;

    private static final int REQUEST_CHOOSE_TO_DATE = 2;

    private ProblemListContract.View mView;

    private IProblemDataSource mProblemDataSource = ProblemDataSource.getInstance();

    private List<Problem> mProblems;

    private IProblemDataSource.Filter mFilter = new ProblemDataSource.Filter();

    private Calendar mCalendar = Calendar.getInstance();

    private DateFormat mDateFormat = new SimpleDateFormat("yyyy - MM - dd", Locale.getDefault());

    @Override
    public void setView(ProblemListContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        reloadProblems();

        mView.showProblems(mProblems);

        mView.showFromDate(getNowDateString());
        mView.showToDate(getNowDateString());
    }

    @Override
    public void showProblemDetail(int position) {
        mView.showProblemDetailUI(getProblemId(position));
    }

    @Override
    public void addProblem() {
        mView.showProblemDetailUI(UUID.randomUUID());
    }

    @Override
    public void deleteProblem(int position) {
        mProblemDataSource.deleteProblem(
                getProblemId(position));

        reloadProblems();
        mView.showProblemsDelete(mProblems, position);
    }

    @Override
    public void doneProblem(int position) {
        Problem problem = mProblems.get(position);
        problem.setDone(
                !problem.isDone()
        );
        mProblemDataSource.updateProblem(problem);

        mView.showProblems(mProblems);
    }

    @Override
    public void searchText(String text) {
        mFilter.setTextFilter(text);

        reloadProblems();
        mView.showProblems(mProblems);
    }

    @Override
    public void chooseFromDate() {
        long maxDate = parseDateStringToLong(mView.getFilterToDate(), Long.MAX_VALUE);

        mView.setDatePickerMinDate(
                0
        );
        mView.setDatePickerMaxDate(
                maxDate
        );

        mView.setDatePickerDate(maxDate);
        mView.showDatePicker(REQUEST_CHOOSE_FROM_DATE);
    }

    @Override
    public void chooseToDate() {
        long minDate = parseDateStringToLong(mView.getFilterFromDate(), 0);

        mView.setDatePickerMinDate(
                minDate
        );
        mView.setDatePickerMaxDate(
                Long.MAX_VALUE
        );

        mView.setDatePickerDate(minDate);
        mView.showDatePicker(REQUEST_CHOOSE_TO_DATE);
    }

    @Override
    public void onChosenDate(int requestCode, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
        Date date = mCalendar.getTime();

        switch (requestCode) {
            case REQUEST_CHOOSE_FROM_DATE:
                mView.showFromDate(mDateFormat.format(date));

                break;

            case REQUEST_CHOOSE_TO_DATE:
                mView.showToDate(mDateFormat.format(date));
                break;
        }
    }

    @Override
    public void setupFilter() {
        setupDoneFilter();
        setupDateFilter();

        reloadProblems();
        mView.showProblems(mProblems);
    }

    private void setupDoneFilter() {
        if (!mView.isEnableDoneFilter()) {
            mFilter.clearDoneFilter();
            return;
        }

        mFilter.setDoneFilter(
                mView.isFilterDone()
        );
    }

    private void setupDateFilter() {
        if (!mView.isEnableDateFilter()) {
            mFilter.clearDateFilter();
            return;
        }

        try {
            mFilter.setDateFilter(
                    mDateFormat.parse(mView.getFilterFromDate()),
                    mDateFormat.parse(mView.getFilterToDate())
            );
        } catch (ParseException e) {
            // TODO: 2017/10/10 use resource string
            mView.showToast("problem date: from " + mView.getFilterFromDate() +
                    " to " + mView.getFilterToDate());
        }
    }

    @Override
    public void refreshProblems() {
        reloadProblems();
        mView.showProblems(mProblems);
    }

    private long parseDateStringToLong(String date, long defaultDate) {
        long maxDate;
        try {
            maxDate = mDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            maxDate = defaultDate;
        }

        return maxDate;
    }

    private UUID getProblemId(int position) {
        return mProblems.get(position).getId();
    }

    private void reloadProblems() {
        mProblems = mProblemDataSource.getProblems(mFilter);
    }

    private String getNowDateString() {
        return mDateFormat.format(new Date());
    }
}
