package com.yjy.problems.problem.list;

import android.support.annotation.NonNull;
import com.yjy.problems.data.Problem;

import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/10/7.
 */

interface ProblemListContract {

    interface View {

        void setPresenter(Presenter presenter);

        void showToast(String text);

        void showProblems(List<Problem> problems);

        void showProblemDetailUI(@NonNull UUID problemId);

        void showProblemsDelete(List<Problem> problems, int atPosition);

        boolean isEnableDoneFilter();

        Boolean isFilterDone();

        void setDatePickerDate(long date);

        void setDatePickerMinDate(long minDate);

        void setDatePickerMaxDate(long maxDate);

        void showDatePicker(int requestCode);

        void showFromDate(String date);

        void showToDate(String date);

        boolean isEnableDateFilter();

        String getFilterFromDate();

        String getFilterToDate();

        void setHighlightText(String text);
    }

    interface Presenter {

        void setView(View view);

        void start();

        void showProblemDetail(int position);

        void addProblem();

        void deleteProblem(int position);

        /**
         * Switch problem between done and undone
         */
        void doneProblem(int position);

        void searchText(String text);

        void chooseFromDate();

        void chooseToDate();

        void onChosenDate(int requestCode, int year, int month, int dayOfMonth);

        void setupFilter();

        void refreshProblems();
    }

}
