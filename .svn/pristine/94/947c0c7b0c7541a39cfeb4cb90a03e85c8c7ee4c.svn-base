package com.pointhouse.chiguan.k1_2;

/**
 * 下载信息
 */

public class DownloadInfo {
    public static final long TOTAL_ERROR = -1;//获取进度失败
    private String url;
    private long total;
    private long progress;
    private String fileName;
    private String courseSubKey;
    /**
     * 0未开始，1下载中，2暂停中，3下载结束,4下载失败
     */
    private int State;

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getcourseSubKey() {
        return courseSubKey;
    }

    public void setcourseSubKey(String courseSubKey) {
        this.courseSubKey = courseSubKey;
    }

    public DownloadInfo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }
}
