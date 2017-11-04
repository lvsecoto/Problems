package com.yjy.problems.problem.detail;

import android.support.annotation.NonNull;
import com.yjy.problems.data.Problem;
import com.yjy.problems.data.source.IProblemDataSource;
import com.yjy.problems.data.source.ProblemDataSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

class ProblemDetailPresenter implements ProblemDetailContract.Presenter {

    private final Problem mProblem;

    private ProblemDetailContract.View mView;

    private IProblemDataSource mProblemDataSource;

    private boolean mIsProblemNew = false;

    private Date mNowDate = new Date();

    private DateFormat mDateFormat = new SimpleDateFormat("yyyy - MM - dd", Locale.getDefault());

    private List<String> mProductNames;

    ProblemDetailPresenter(ProblemDetailContract.View view, ProblemDataSource
            problemDataSource, UUID problemId) {
        mView = view;
        mProblemDataSource = problemDataSource;

        mProblem = getProblem(problemId);
    }

    @NonNull
    private Problem getProblem(UUID problemId) {
        Problem problem = mProblemDataSource.getProblem(problemId);

        if (problem == null) {
            problem = createProblem(problemId);

            mIsProblemNew = true;
        } else {
            mIsProblemNew = false;
        }

        return problem;
    }

    @NonNull
    private Problem createProblem(UUID problemId) {
        Problem problem;
        problem = new Problem();
        problem.setId(problemId);

        Date dateNow = new Date();
        problem.setCreateDate(dateNow);
        problem.setDate(dateNow);
        return problem;
    }

    @Override
    public void start() {
        showProblem(mProblem);
    }

    private void showProblem(Problem problem) {
        mView.showDescription(problem.getDescription());
        mView.showSolution(problem.getSolution());
        mView.showDate(dateToString(problem.getDate()));
        mView.showProductName(problem.getProductName());
        mView.showOrderId(problem.getOrderId());
        mView.showDone(problem.isDone());
    }

    private String dateToString(Date date) {
        return mDateFormat.format(date);
    }

    @Override
    public void stop() {
        if (Pattern.compile("\\s+|").matcher(mView.getDescription()).matches()) {
            mView.alertEmptyProblemDescriptionNotAllows();
            return;
        }

        mProblem.setDescription(mView.getDescription());
        mProblem.setSolution(mView.getSolution());
        mProblem.setDate(stringToDate(mView.getDate()));
        mProblem.setDone(mView.isDone());
        mProblem.setProductName(mView.getProductName());
        mProblem.setOrderId(mView.getOrderId());

        if (mIsProblemNew) {
            mProblemDataSource.addProblem(mProblem);
        } else {
            mProblemDataSource.updateProblem(mProblem);
        }

    }

    @NonNull
    private Date stringToDate(String dateString) {
        try {
            return mDateFormat.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
    }

    @Override
    public void chooseDate() {
        mView.showDatePicker(mNowDate);
    }

    @Override
    public void onDateSet(Date date) {
        mProblem.setDate(date);
        showProblem(mProblem);
    }

    @Override
    public void chooseProduct() {
        mProductNames = mProblemDataSource.getProductNames();

        mView.showChooseList(mProductNames);
    }

    @Override
    public void onListItemChoose(int which) {
        mView.showProductName(mProductNames.get(which));
    }

}
