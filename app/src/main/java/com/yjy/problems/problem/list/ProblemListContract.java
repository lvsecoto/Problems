package com.yjy.problems.problem.list;

import android.support.annotation.NonNull;
import com.yjy.problems.data.Problem;

import java.util.Date;
import java.util.List;
import java.util.UUID;

interface ProblemListContract {

    interface View {

        void setPresenter(Presenter presenter);

        void showToast(String text);

        void showProblems(List<Problem> problems);

        void showProblemDetailUI(@NonNull UUID problemId);

        void showProblemsDelete(List<Problem> problems, int atPosition);

        void setHighlightText(String text);

        void showFilterUI();
    }

    interface Presenter {

        void setView(View view);

        void start();

        void stop();

        void showProblemDetail(int position);

        void addProblem();

        void deleteProblem(int position);

        /**
         * Switch problem between done and undone
         */
        void doneProblem(int position);

        void searchText(String text);

        void refreshProblems();

        void openFilterUI();

        void setDoneFilter(boolean done, boolean enable);

        void setDateFilter(Date from, Date to, boolean enable);
    }

}
