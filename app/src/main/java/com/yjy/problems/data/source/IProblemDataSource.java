package com.yjy.problems.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.yjy.problems.data.Problem;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface IProblemDataSource {

    void deleteAll();

    void addProblem(@NonNull Problem problem);

    /**
     * @return return Null if no problem found
     */
    @Nullable
    Problem getProblem(@NonNull UUID uuid);

    void updateProblem(@NonNull Problem problem);

    List<Problem> getProblems();

    List<Problem> getProblems(Filter filter);

    void deleteProblem(UUID mProblemId);

    List<String> getProductNames();

    int getCount();

    interface Filter {

        void setTextFilter(String match);

        void setDoneFilter(boolean isDone);

        void clearDoneFilter();

        void setDateFilter(Date from, Date to);

        void clearDateFilter();

        @NonNull
        String parseFilter();
    }
}
