package com.yjy.problems.data.source;

@SuppressWarnings("WeakerAccess")
final class DBContract {

    public static final String TYPE_INT = " INT";

    public static final String TYPE_TEXT = " TEXT";

    public static final String CONSTRAINT_PRIMARY_KEY = " PRIMARY KEY";

    public static final String CONSTRAINT_NOT_NULL = " NOT NULL";

    public static final String ORDER_DESC = " DESC";

    public static final String ORDER_ASC = " ASC";

    public static final String NEXT_PARAMETER = ", ";

    public static final String END_PARAMETER = ")";

    public static final String START_PARAMETER = " ( ";

    public static final String AND = " and ";

    class ProblemEntry {

        public static final String TABLE_PROBLEM = "problem";

        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_CREATE_DATE = "create_date";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_SOLUTION = "solution";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_ORDER_ID = "order_id";

        public static final String COLUMN_PRODUCT_NAME = "product_name";

        public static final String COLUMN_IS_DONE = "is_done";
    }
}
