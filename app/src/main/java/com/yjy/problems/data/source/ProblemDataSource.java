package com.yjy.problems.data.source;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.yjy.problems.ProblemsApplication;
import com.yjy.problems.data.Problem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProblemDataSource implements IProblemDataSource {

    private static IProblemDataSource mInstance;

    private SQLiteOpenHelper mDbOpenHelper;

    public ProblemDataSource(SQLiteOpenHelper dbOpenHelper) {
        this.mDbOpenHelper = dbOpenHelper;
    }

    public static IProblemDataSource getInstance() {
        if (mInstance == null) {
            mInstance = new ProblemDataSource(new DbOpenHelper(
                    ProblemsApplication.getAppContext()
            ));
        }
        return mInstance;
    }

    @Override
    public void deleteAll() {
        mDbOpenHelper.getWritableDatabase().delete(
                DBContract.ProblemEntry.TABLE_PROBLEM, "1=1", null
        );
    }

    @Override
    public void addProblem(@NonNull Problem problem) {
        ContentValues values = getContentValuesFromProblem(problem);

        mDbOpenHelper.getReadableDatabase().
                insert(DBContract.ProblemEntry.TABLE_PROBLEM, null, values);
    }

    @Override
    public Problem getProblem(@NonNull UUID uuid) {
        Cursor cursor = mDbOpenHelper.getWritableDatabase()
                .query(
                        DBContract.ProblemEntry.TABLE_PROBLEM,
                        null,
                        DBContract.ProblemEntry.COLUMN_ID + "=?",
                        new String[]{
                                uuid.toString()
                        },
                        null, null, null
                );

        if (cursor == null) {
            return null;
        }

        Problem problem = null;

        if (cursor.moveToNext()) {
            problem = getProblem(cursor);
        }

        cursor.close();
        return problem;
    }

    @NonNull
    private Problem getProblem(Cursor cursor) {
        Problem problem;
        problem = new Problem();
        problem.setId(UUID.fromString(cursor.getString(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_ID))
                )
        );
        problem.setCreateDate(new Date(cursor.getLong(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_CREATE_DATE)
        )));
        problem.setDescription(cursor.getString(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_DESCRIPTION)
        ));
        problem.setSolution(cursor.getString(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_SOLUTION)
        ));
        problem.setDone(cursor.getInt(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_IS_DONE)
                ) == 1
        );
        problem.setProductName(cursor.getString(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_PRODUCT_NAME)
        ));
        problem.setOrderId(cursor.getString(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_ORDER_ID)
        ));
        problem.setDate(new Date(cursor.getLong(
                cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_DATE)
        )));
        return problem;
    }

    @Override
    public void updateProblem(@NonNull Problem problem) {
        ContentValues values = getContentValuesFromProblem(problem);
        mDbOpenHelper.getWritableDatabase().update(DBContract.ProblemEntry.TABLE_PROBLEM,
                values, DBContract.ProblemEntry.COLUMN_ID + " = ?", new String[]{
                        problem.getId().toString()
                });
    }

    @NonNull
    private ContentValues getContentValuesFromProblem(Problem problem) {
        ContentValues values = new ContentValues();
        values.put(DBContract.ProblemEntry.COLUMN_ID, problem.getId().toString());
        values.put(DBContract.ProblemEntry.COLUMN_DESCRIPTION, problem.getDescription());
        values.put(DBContract.ProblemEntry.COLUMN_SOLUTION, problem.getSolution());
        values.put(DBContract.ProblemEntry.COLUMN_CREATE_DATE, problem.getCreateDate().getTime());
        values.put(DBContract.ProblemEntry.COLUMN_IS_DONE, problem.isDone() ? 1 : 0);
        values.put(DBContract.ProblemEntry.COLUMN_ORDER_ID, problem.getOrderId());
        values.put(DBContract.ProblemEntry.COLUMN_PRODUCT_NAME, problem.getProductName());
        values.put(DBContract.ProblemEntry.COLUMN_DATE, problem.getDate().getTime());
        return values;
    }

    public static class Filter implements IProblemDataSource.Filter {

        private String mTextFilter = "";

        private String mDoneFilter = "";

        private String mDateFilter = "";

        public Filter() {
        }

        /**
         * @param match pass NULL to disable the text filter
         */
        @Override
        public void setTextFilter(String match) {
            if (match == null) {
                mTextFilter = "";
                return;
            }

            match = "'%" + match + "%'";

            mTextFilter = " AND ( " + DBContract.ProblemEntry.COLUMN_DESCRIPTION + " LIKE " + match +
                    " OR " + DBContract.ProblemEntry.COLUMN_SOLUTION + " LIKE " + match +
                    " OR " + DBContract.ProblemEntry.COLUMN_PRODUCT_NAME + " LIKE " + match +
                    " OR " + DBContract.ProblemEntry.COLUMN_ORDER_ID + " LIKE " + match + " ) ";
        }


        /**
         * @param isDone pass NULL to disable the done filter
         */
        @Override
        public void setDoneFilter(boolean isDone) {
            String condition;
            if (isDone) {
                condition = "1";
            } else {
                condition = "0";
            }

            mDoneFilter = " AND ( " + DBContract.ProblemEntry.COLUMN_IS_DONE + " = "
                    + condition + ")";
        }

        @Override
        public void clearDoneFilter() {
            mDoneFilter = "";
        }

        @Override
        public void setDateFilter(Date from, Date to) {
            mDateFilter = " AND " +
                    "( " + DBContract.ProblemEntry.COLUMN_DATE + " >= " + from.getTime() + " ) " +
                    "AND " +
                    "( " + DBContract.ProblemEntry.COLUMN_DATE + " <= " + to.getTime() + " ) ";
        }

        @Override
        public void clearDateFilter() {
            mDateFilter = "";
        }

        @NonNull
        @Override
        public String parseFilter() {
            return mTextFilter + mDoneFilter + mDateFilter;
        }

    }

    @Override
    public List<Problem> getProblems() {
        Cursor cursor = mDbOpenHelper.getReadableDatabase().query(
                DBContract.ProblemEntry.TABLE_PROBLEM,
                null, null, null, null, null,
                getOrderBy()
        );

        List<Problem> problems = new ArrayList<>();

        if (cursor == null) {
            return problems;
        }

        while (cursor.moveToNext()) {
            problems.add(getProblem(cursor));
        }

        cursor.close();
        return problems;
    }


    @Override
    public List<Problem> getProblems(IProblemDataSource.Filter filter) {

        Cursor cursor = mDbOpenHelper.getReadableDatabase()
                .query(
                        DBContract.ProblemEntry.TABLE_PROBLEM,
                        null, "1=1 " + filter.parseFilter(), null, null, null, getOrderBy()
                );

        if (cursor == null) {
            return null;
        }

        List<Problem> problems = new ArrayList<>();

        while (cursor.moveToNext()) {
            problems.add(getProblem(cursor));
        }

        cursor.close();

        return problems;
    }

    @NonNull
    private String getOrderBy() {
        return DBContract.ProblemEntry.COLUMN_DATE + DBContract.ORDER_DESC +
                DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_CREATE_DATE + DBContract.ORDER_DESC +
                "";
    }

    @Override
    public void deleteProblem(UUID mProblemId) {
        mDbOpenHelper.getWritableDatabase()
                .delete(DBContract.ProblemEntry.TABLE_PROBLEM,
                        DBContract.ProblemEntry.COLUMN_ID + "=?",
                        new String[]{
                                mProblemId.toString()
                        });
    }

    @Override
    public List<String> getProductNames() {
        Cursor cursor = mDbOpenHelper.getReadableDatabase()
                .query(true,
                        DBContract.ProblemEntry.TABLE_PROBLEM,
                        new String[]{
                                DBContract.ProblemEntry.COLUMN_PRODUCT_NAME
                        },
                        DBContract.ProblemEntry.COLUMN_PRODUCT_NAME + " NOT LIKE ''",
                        null, null, null,
                        getOrderBy(),
                        null);

        List<String> productNames = new ArrayList<>();

        if (cursor == null) {
            return productNames;
        }

        while (cursor.moveToNext()) {
            String productName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.ProblemEntry.COLUMN_PRODUCT_NAME)
            );

            productNames.add(productName);
        }

        cursor.close();

        return productNames;
    }

    @Override
    public int getCount() {
        return getProblems().size();
    }


}

