package com.yjy.problems.problem.list;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yjy.problems.R;
import com.yjy.problems.data.Problem;
import com.yjy.problems.problem.detail.ProblemDetailActivity;
import com.yjy.problems.problem.detail.ProblemDetailFragment;
import com.yjy.problems.problem.list.adapter.OnItemClickListener;
import com.yjy.problems.problem.list.adapter.ProblemListAdapter;
import com.yjy.problems.problem.list.filter.FilterDialogFragment;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProblemListFragment extends Fragment implements ProblemListContract.View {

    @BindView(R.id.problemList)
    RecyclerView mProblemList;

    SearchView mSearch;

    private ProblemListAdapter mProblemListAdapter;

    private ProblemListContract.Presenter mPresenter;

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

    private FilterDialogFragment mFilterDialogFragment = new FilterDialogFragment();

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

        initProblemListAdapter();
        initProblemList();

        return rootView;
    }

    private void initProblemList() {
        mProblemList.setAdapter(mProblemListAdapter);
        mProblemList.setLayoutManager(new LinearLayoutManager(getContext()));
        mProblemList.setItemAnimator(new DefaultItemAnimator());
//        mProblemList.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mSearch.clearFocus();
//                hideIme();
//                return false;
//            }
//
//            private void hideIme() {
//                View view = getActivity().getWindow().peekDecorView();
//                if (view != null) {
//                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            }
//        });
    }

    private void initProblemListAdapter() {
        mProblemListAdapter = new ProblemListAdapter(getContext());
        mProblemListAdapter.setOnItemClickListener(mOnProblemItemClickListener);
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
                mPresenter.openFilterUI();
                return true;
        }

        return false;
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
    public void setHighlightText(String text) {
        mProblemListAdapter.setHighlightText(text);
    }

    @Override
    public void showFilterUI() {
        mFilterDialogFragment.show(getFragmentManager());
        mFilterDialogFragment.setOnConfigureListener(new FilterDialogFragment.OnConfigureListener() {
            @Override
            public void OnDoneFilterChange(boolean done, boolean enable) {
                mPresenter.setDoneFilter(done, enable);
            }

            @Override
            public void OnDateFilterChange(Date from, Date to, boolean enable) {
                mPresenter.setDateFilter(from, to, enable);
            }
        });
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

}
