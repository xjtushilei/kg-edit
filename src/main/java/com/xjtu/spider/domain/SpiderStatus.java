package com.xjtu.spider.domain;

/**
 * 正在爬取课程的状态对象，不需要建立表格，不用注释为@Entity
 * Created by yuanhao on 2017/3/18.
 */
public class SpiderStatus {

    private String className;

    private int termSum;

    private int errorTermNum;

    private int alreadyTermNum;

    public SpiderStatus() {

    }

    public SpiderStatus(String className, int termSum, int errorTermNum, int alreadyTermNum) {
        this.className = className;
        this.termSum = termSum;
        this.errorTermNum = errorTermNum;
        this.alreadyTermNum = alreadyTermNum;
    }

    @Override
    public String toString() {
        return "SpiderStatus{" +
                "className='" + className + '\'' +
                ", termSum=" + termSum +
                ", errorTermNum=" + errorTermNum +
                ", alreadyTermNum=" + alreadyTermNum +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTermSum() {
        return termSum;
    }

    public void setTermSum(int termSum) {
        this.termSum = termSum;
    }

    public int getErrorTermNum() {
        return errorTermNum;
    }

    public void setErrorTermNum(int errorTermNum) {
        this.errorTermNum = errorTermNum;
    }

    public int getAlreadyTermNum() {
        return alreadyTermNum;
    }

    public void setAlreadyTermNum(int alreadyTermNum) {
        this.alreadyTermNum = alreadyTermNum;
    }
}
