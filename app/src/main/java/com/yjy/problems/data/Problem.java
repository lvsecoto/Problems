package com.yjy.problems.data;

import java.util.Date;
import java.util.UUID;

public class Problem {

    private UUID mId;

    private Date mCreateDate;

    private String mDescription;

    private String mSolution;

    private Date mDate;

    private boolean mIsResolved;

    private String mProductName;

    private String mOrderId;

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getSolution() != null ? getSolution().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (isDone() ? 1 : 0);
        result = 31 * result + (getProductName() != null ? getProductName().hashCode() : 0);
        result = 31 * result + (getOrderId() != null ? getOrderId().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Problem)) return false;

        Problem problem = (Problem) o;

        if (isDone() != problem.isDone()) return false;
        if (getId() != null ? !getId().equals(problem.getId()) : problem.getId() != null)
            return false;
        if (getCreateDate() != null ? !getCreateDate().equals(problem.getCreateDate()) : problem.getCreateDate() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(problem.getDescription()) : problem.getDescription() != null)
            return false;
        if (getSolution() != null ? !getSolution().equals(problem.getSolution()) : problem.getSolution() != null)
            return false;
        if (getDate() != null ? !getDate().equals(problem.getDate()) : problem.getDate() != null)
            return false;
        if (getProductName() != null ? !getProductName().equals(problem.getProductName()) : problem.getProductName() != null)
            return false;
        return getOrderId() != null ? getOrderId().equals(problem.getOrderId()) : problem.getOrderId() == null;

    }

    @Override
    public String toString() {
        return "Problem{" +
                "mId=" + mId +
                ", mCreateDate=" + mCreateDate +
                ", mDescription='" + mDescription + '\'' +
                ", mSolution='" + mSolution + '\'' +
                ", mDate=" + mDate +
                ", mIsResolved=" + mIsResolved +
                ", mProductName='" + mProductName + '\'' +
                ", mOrderId='" + mOrderId + '\'' +
                '}';
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getSolution() {
        return mSolution;
    }

    public void setSolution(String solution) {
        mSolution = solution;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isDone() {
        return mIsResolved;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public void setDone(boolean resolved) {
        mIsResolved = resolved;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
