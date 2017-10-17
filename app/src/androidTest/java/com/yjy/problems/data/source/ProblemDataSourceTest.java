package com.yjy.problems.data.source;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.yjy.problems.data.Problem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ProblemDataSourceTest {

    private ProblemDataSource mProblemDataSource;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        SQLiteOpenHelper dbHelper = new DbOpenHelper(context, "test.db");
        mProblemDataSource = new ProblemDataSource(dbHelper);
        mProblemDataSource.deleteAll();
    }

    @Test
    public void testDeleteAll() throws Exception {
        mProblemDataSource.deleteAll();
        assertProblemSize(0);

        for (int i = 0; i < 10; i++) {
            mProblemDataSource.addProblem(createTestProblem());
        }

        assertProblemSize(10);

        mProblemDataSource.deleteAll();
        assertProblemSize(0);

    }

    private void assertProblemSize(int expected) {
        assertEquals(expected, mProblemDataSource.getProblems().size());
    }

    private Problem createTestProblem() {
        Problem problem = new Problem();
        problem.setId(UUID.randomUUID());
        problem.setCreateDate(new Date());
        problem.setDate(new Date());
        problem.setOrderId(getRandomString());
        problem.setDescription(getRandomString());
        problem.setSolution(getRandomString());
        problem.setProductName(getRandomString());
        return problem;
    }

    @NonNull
    private String getRandomString() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    @Test
    public void testAddProblem() throws Exception {
        Problem problem = createTestProblem();
        int before = mProblemDataSource.getProblems().size();
        mProblemDataSource.addProblem(problem);

        assertProblemSize(1 + before);
    }

    @Test
    public void testGetProblem() throws Exception {
        Problem problem = createTestProblem();
        mProblemDataSource.addProblem(problem);

        Problem getProblem = mProblemDataSource.getProblem(problem.getId());

        assertEquals(problem, getProblem);
    }

    @Test
    public void testUpdateProblem() throws Exception {
        Problem problem = createTestProblem();
        mProblemDataSource.addProblem(problem);

        problem.setDescription(getRandomString());
        mProblemDataSource.updateProblem(problem);

        Problem getProblem = mProblemDataSource.getProblem(problem.getId());

        assertEquals(problem, getProblem);

    }

    @Test
    public void testTextFilter() throws Exception {
        String matchString = getRandomString();
        List<Problem> problems = createTextFilterTestProblems(matchString);

        ProblemDataSource.Filter filter = new ProblemDataSource.Filter();
        filter.setTextFilter(matchString);
        List<Problem> problemsInDataResource = mProblemDataSource.getProblems(filter);


        assertTrue(problemsInDataResource.containsAll(
                problems.subList(0, problems.size() - 1)
        ));

        assertFalse(problemsInDataResource.contains(
                problems.get(problems.size() - 1)
        ));

    }

    private List<Problem> createTextFilterTestProblems(String matchString) {
        mProblemDataSource.deleteAll();

        List<Problem> problems = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            problems.add(createTestProblem());
        }

        problems.get(0).setDescription(getRandomString() + matchString + getRandomString());
        problems.get(1).setProductName(getRandomString() + matchString + getRandomString());
        problems.get(2).setOrderId(getRandomString() + matchString + getRandomString());
        //the last problem do not process

        for (Problem problem : problems) {
            mProblemDataSource.addProblem(problem);
        }

        return problems;
    }

    @Test
    public void testNullTextFilter() throws Exception {
        List<Problem> problems = createTextFilterTestProblems(getRandomString());

        ProblemDataSource.Filter filter = new ProblemDataSource.Filter();

        assertEquals(
                problems.size(), mProblemDataSource.getProblems(filter).size()
        );

    }

    @After
    public void tearDown() throws Exception {

    }

}