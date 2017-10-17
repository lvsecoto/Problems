package com.yjy.problems.problem.detail;

import java.util.Date;
import java.util.List;

public interface ProblemDetailContract {

    interface View {

        void setPresenter(Presenter presenter);

        void showDescription(String description);

        String getDescription();

        void showSolution(String solution);

        String getSolution();

        void showDate(String date);

        String getDate();

        void showDone(boolean done);

        boolean isDone();

        void showProductName(String productName);

        String getProductName();

        void showOrderId(String orderId);

        String getOrderId();

        void showDatePicker(Date date);

        void showChooseList(List<String> items);

        void showToast(String text);
    }

    interface Presenter {

        void start();

        void stop();

        void chooseDate();

        void onDateSet(Date date);

        void chooseProduct();

        void onListItemChoose(int which);
    }
}
