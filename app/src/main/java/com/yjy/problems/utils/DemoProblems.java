package com.yjy.problems.utils;

import com.yjy.problems.data.Problem;
import com.yjy.problems.data.source.IProblemDataSource;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DemoProblems {

    private static DemoProblems mInstance;

    public static DemoProblems getInstance() {
        if (mInstance == null) {
            mInstance = new DemoProblems();
        }
        return mInstance;
    }

    public void loadDemoProblems(IProblemDataSource problemDataSource) {
        loadFirstUseProblems(problemDataSource);

        Calendar calendar = Calendar.getInstance();
        Problem problem;

        problem = new Problem();
        problem.setId(UUID.randomUUID());
        problem.setDescription("真空吸盘过一段时间就很容易掉下来");
        problem.setSolution("先在吸盘涂抹和墙面上涂抹洗洁精，压紧待干了再后使用,据说牙膏效果会更好");
        problem.setProductName("吸盘");
        problem.setOrderId("abc1234567");
        calendar.set(2017, 9, 1);
        problem.setCreateDate(new Date());
        problem.setDate(calendar.getTime());
        problem.setDone(true);

        problemDataSource.addProblem(
                problem
        );

        problem = new Problem();
        problem.setId(UUID.randomUUID());
        problem.setDescription("摇头灯无法通过控制台打开灯泡");
        problem.setSolution("确保灯泡已经冷却超过一分钟。先把控制通道设置为0，再把通道值255");
        problem.setProductName("MH440");
        problem.setOrderId("AHD20170901");
        calendar.set(2017, 8, 15);
        problem.setCreateDate(new Date());
        problem.setDate(calendar.getTime());
        problem.setDone(true);

        problemDataSource.addProblem(
                problem
        );

        problem = new Problem();
        problem.setId(UUID.randomUUID());
        problem.setDescription("无线网络经常过一阵子就无法连接，并显示黄色感叹号");
        problem.setSolution("可能是无线网卡发热导致无法正常工作，也有可能是无线路由离得太远信号太差，一般三格以下就会出问题");
        problem.setProductName("网络");
        problem.setOrderId("");
        calendar.set(2017, 6, 15);
        problem.setCreateDate(new Date());
        problem.setDate(calendar.getTime());
        problem.setDone(true);

        problemDataSource.addProblem(
                problem
        );

    }

    public void loadFirstUseProblems(IProblemDataSource problemDataSource) {

        Problem problem;

        problem = new Problem();
        problem.setId(UUID.randomUUID());
        problem.setDescription("遇到了不会解决的问题");
        problem.setSolution("使用 Problems APP 搜索有无问题的解决方法，如无，百度或询问你的上司。" +
                "最后，记得在Problems记下您的问题和解决方法哦~");
        problem.setProductName("Problems");
        problem.setOrderId("00000000");
        problem.setCreateDate(new Date());
        problem.setDate(new Date());
        problem.setDone(false);

        problemDataSource.addProblem(
                problem
        );

    }
}
