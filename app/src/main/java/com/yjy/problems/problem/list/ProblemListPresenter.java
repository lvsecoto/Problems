package com.yjy.problems.problem.list;

import com.yjy.nlp.WordSegmentation;
import com.yjy.problems.BuildConfig;
import com.yjy.problems.data.Problem;
import com.yjy.problems.data.source.IProblemDataSource;
import com.yjy.problems.data.source.ProblemDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

class ProblemListPresenter implements ProblemListContract.Presenter {

    private static final String TAG = "ProblemListPresenter";

    private ProblemListContract.View mView;

    private IProblemDataSource mProblemDataSource = ProblemDataSource.getInstance();

    private List<Problem> mProblems;

    private IProblemDataSource.Filter mFilter = new ProblemDataSource.Filter();

    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    public void setView(ProblemListContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        reloadProblems();
        mView.showProblems(mProblems);
    }

    @Override
    public void stop() {
        mDisposables.clear();
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
    public void searchText(final String text) {
        if (text.equals("")) {
            mView.setHighlightText("");

            mFilter.setTextFilter(null);

            reloadProblems();
            mView.showProblems(mProblems);
            return;
        }

        mDisposables.add(new WordSegmentation(BuildConfig.FTP_CLOUD_API_KEY).segment(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> words) throws Exception {

                        setTextFilter(words);
                        setHighlightText(words);

                        reloadProblems();
                        mView.showProblems(mProblems);
                    }

                    private void setHighlightText(List<String> words) {
                        StringBuilder sb = new StringBuilder();
                        for (String word : words) {
                            sb.append(word).append("|");
                        }
                        sb.deleteCharAt(sb.length() - 1);

                        mView.setHighlightText(sb.toString());
                    }

                    private void setTextFilter(List<String> words) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("%");
                        for (String word : words) {
                            sb.append(word).append("%");
                        }

                        mFilter.setTextFilter(sb.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mFilter.setTextFilter(text);
                        mView.setHighlightText(text);
                        mView.showToast(throwable.getMessage());
                    }
                })
        );
    }

    @Override
    public void refreshProblems() {
        reloadProblems();
        mView.showProblems(mProblems);
    }

    @Override
    public void openFilterUI() {
        mView.showFilterUI();
    }

    @Override
    public void setDoneFilter(boolean done, boolean enable) {
        if (enable) {
            mFilter.setDoneFilter(done);
        } else {
            mFilter.clearDoneFilter();
        }

        reloadProblems();
        mView.showProblems(mProblems);
    }

    @Override
    public void setDateFilter(Date from, Date to, boolean enable) {
        if (enable) {
            mFilter.setDateFilter(from, to);
        } else {
            mFilter.clearDateFilter();
        }

        reloadProblems();
        mView.showProblems(mProblems);
    }

    private UUID getProblemId(int position) {
        return mProblems.get(position).getId();
    }

    private void reloadProblems() {
        mProblems = mProblemDataSource.getProblems(mFilter);
    }

}
