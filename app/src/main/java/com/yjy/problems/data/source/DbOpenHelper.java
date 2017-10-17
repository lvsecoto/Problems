package com.yjy.problems.data.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings("WeakerAccess")
public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(Context context) {
        super(context, "Problem.db", null, 1);
    }

    public DbOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                DBContract.ProblemEntry.TABLE_PROBLEM + DBContract.START_PARAMETER +
                DBContract.ProblemEntry.COLUMN_CREATE_DATE + DBContract.TYPE_INT + DBContract.CONSTRAINT_NOT_NULL + DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_ID + DBContract.TYPE_TEXT + DBContract.CONSTRAINT_PRIMARY_KEY + DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_DESCRIPTION + DBContract.TYPE_TEXT + DBContract.CONSTRAINT_NOT_NULL + DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_SOLUTION + DBContract.TYPE_TEXT + DBContract.CONSTRAINT_NOT_NULL + DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_DATE + DBContract.TYPE_INT + DBContract.CONSTRAINT_NOT_NULL + DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_ORDER_ID + DBContract.TYPE_TEXT + DBContract.CONSTRAINT_NOT_NULL + DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_PRODUCT_NAME + DBContract.TYPE_TEXT + DBContract.CONSTRAINT_NOT_NULL + DBContract.NEXT_PARAMETER +
                DBContract.ProblemEntry.COLUMN_IS_DONE + DBContract.TYPE_INT + DBContract.CONSTRAINT_NOT_NULL + DBContract.END_PARAMETER
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
