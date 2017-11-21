package com.yjy.problems.problem.list;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.*;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yjy.problems.R;
import com.yjy.problems.data.Problem;
import com.yjy.problems.problem.detail.ProblemDetailActivity;
import com.yjy.problems.problem.detail.ProblemDetailFragment;
import com.yjy.problems.problem.list.adapter.OnItemClickListener;
import com.yjy.problems.problem.list.adapter.ProblemListAdapter;
import com.yjy.problems.view.checkBox.CheckBox;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ProblemListFragment extends Fragment implements ProblemListContract.View {

    ToolbarView mToolbarView = new ToolbarView();

    @BindView(R.id.problemList)
    RecyclerView mProblemList;

    SearchView mSearch;

    private FilterView mFilterView = new FilterView();

    private ProblemListAdapter mProblemListAdapter;

    private PopupWindow mFilterWindow;

    private ProblemListContract.Presenter mPresenter;

    private DatePickerDialog.OnDateSetListener mDataSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mPresenter.onChosenDate((int) view.getTag(), year, month, dayOfMonth);
        }
    };

    private PopupWindow.OnDismissListener
            mOnFilterWindowDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            mPresenter.setupFilter();
        }
    };

    private OnItemClickListener mOnProblemItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, final int position) {
            switch (view.getId()) {
                case R.id.container:
                    mPresenter.showProblemDetail(position);
                    break;

                case R.id.done:
                    mPresenter.doneProblem(position);
                    break;

                case R.id.delete:
                    showDeleteProblemDialog(position);
                    break;
            }
        }
    };

    private DatePickerDialog mDatePickerDialog;

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            return;
        }

        mPresenter.refreshProblems();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_problem_list, container, false);
        ButterKnife.bind(this, rootView);

        initToolbar();
        initProblemList();
        initFilterWindow();

        mDatePickerDialog = new DatePickerDialog(getContext(),
                mDataSetListener, 0, 0, 0);

        return rootView;
    }

    private void initToolbar() {
        ButterKnife.bind(mToolbarView, getActivity());
    }

    private void initProblemList() {
        initProblemListAdapter();

        mProblemList.setAdapter(mProblemListAdapter);
        mProblemList.setLayoutManager(new LinearLayoutManager(getContext()));
        mProblemList.setItemAnimator(new DefaultItemAnimator());
        mProblemList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSearch.clearFocus();
                hideIme();
                return false;
            }

            private void hideIme() {
                View view = getActivity().getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private void initFilterWindow() {
        View filterView = inflateFilterView();

        ButterKnife.bind(mFilterView, filterView);

        mFilterWindow = new PopupWindow(filterView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams
                .WRAP_CONTENT, true);

        mFilterWindow.setOnDismissListener(mOnFilterWindowDismissListener);
    }

    private void initProblemListAdapter() {
        mProblemListAdapter = new ProblemListAdapter(getContext());
        mProblemListAdapter.setOnItemClickListener(mOnProblemItemClickListener);
    }

    @NonNull
    private ViewGroup inflateFilterView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        ViewGroup rootView = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        rootView.setLayoutParams(params);
        layoutInflater.inflate(R.layout.window_filter, rootView, true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new ProblemListPresenter();
        mPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_problem_list, menu);

        MenuItem item = menu.findItem(R.id.search);
        mSearch = (SearchView) MenuItemCompat.getActionView(item);
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.searchText(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addProblem:
                mPresenter.addProblem();
                return true;
            case R.id.filter:
                openFilterWindow(mToolbarView.mToolbar);
                return true;
        }

        return false;
    }

    private void openFilterWindow(View view) {
        mFilterWindow.showAsDropDown(view, 0, 0, Gravity.BOTTOM | Gravity.END);
    }

    @Override
    public void setPresenter(ProblemListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProblems(List<Problem> problems) {
        mProblemListAdapter.setProblemList(problems);
        mProblemListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProblemDetailUI(@NonNull UUID problemId) {
        ProblemDetailFragment.showInActivity(getContext(), ProblemDetailActivity.class, problemId);
    }

    @Override
    public void showProblemsDelete(List<Problem> problems, int atPosition) {
        mProblemListAdapter.setProblemList(problems);
        mProblemListAdapter.notifyItemRemoved(atPosition);
    }

    @Override
    public boolean isEnableDoneFilter() {
        return mFilterView.mEnableDoneFilter.isChecked();
    }

    @Override
    public Boolean isFilterDone() {
        return mFilterView.mFilterDone.isChecked();
    }

    @Override
    public void setDatePickerDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        mDatePickerDialog.getDatePicker().updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    @Override
    public void setDatePickerMinDate(long minDate) {
        mDatePickerDialog.getDatePicker().setMinDate(minDate);
    }

    @Override
    public void setDatePickerMaxDate(long maxDate) {
        mDatePickerDialog.getDatePicker().setMaxDate(maxDate);
    }

    @Override
    public void showDatePicker(int requestCode) {
        mDatePickerDialog.getDatePicker().setTag(requestCode);
        mDatePickerDialog.show();
    }

    @Override
    public void showFromDate(String date) {
        mFilterView.mFromDate.setText(date);
    }

    @Override
    public void showToDate(String date) {
        mFilterView.mToDate.setText(date);
    }

    @Override
    public boolean isEnableDateFilter() {
        return mFilterView.mEnableDateFilter.isChecked();
    }

    @Override
    public String getFilterFromDate() {
        return mFilterView.mFromDate.getText().toString();
    }

    @Override
    public String getFilterToDate() {
        return mFilterView.mToDate.getText().toString();
    }

    @Override
    public void setHighlightText(String text) {
        mProblemListAdapter.setHighlightText(text);
    }

    private void showDeleteProblemDialog(final int position) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.msgIsRemoveProblem)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteProblem(position);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    class ToolbarView {

        @BindView(R.id.toolbar)
        Toolbar mToolbar;

    }

    class FilterView {

        @BindView(R.id.enableDoneFilter)
        Switch mEnableDoneFilter;

        @BindView(R.id.done)
        CheckBox mFilterDone;

        @BindView(R.id.chooseFromDate)
        TextView mChooseFromDate;

        @BindView(R.id.fromDate)
        TextView mFromDate;

        @BindView(R.id.toDate)
        TextView mToDate;

        @BindView(R.id.chooseToDate)
        TextView mChooseToDate;

        @BindView(R.id.enableDateFilter)
        Switch mEnableDateFilter;

        @OnClick({R.id.chooseFromDate, R.id.chooseToDate})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chooseFromDate:
                    mPresenter.chooseFromDate();
                    break;
                case R.id.chooseToDate:
                    mPresenter.chooseToDate();
                    break;
            }
        }
    }

}
