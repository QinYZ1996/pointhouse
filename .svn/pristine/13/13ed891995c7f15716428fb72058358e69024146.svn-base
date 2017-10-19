package com.pointhouse.chiguan.k1_2;

import com.pointhouse.chiguan.db.CourseDownloadInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ljj on 2017/7/29.
 */

public class DownloadStateUtil {
    /**
     * //0未下载，1下载中，2暂停中，3等待中，4下载结束，5下载失败
     * 说明 ： 前提
     *         点开始就设定成等待。
     *         失败到开始也是，暂停到开始也是。
     *         暂停除了下载结束其他都设定成暂停，失败未下载的设定成暂停，系统启动时所有未完成除了失败设定成暂停。
     *  结果   0未下载肯定不在数据库。
     *         1存在下载中一定是下载状态。
     *         2暂停只要没有下载失败就是暂停。
     *         3等待只要没有下载中就是等待。
     *         4下载结束只要没有别的状态就是下载全部结束。
     *         5失败只要存在失败就是失败。
     *   注意 删除文件课程时必须删除数据库。
     * @param courseDownloadInfoList
     * @return
     */
    public static int getDownloadState(List<CourseDownloadInfo> courseDownloadInfoList){

        int downloadState = -1;
        Set<Integer> set = new HashSet<>();
        if (courseDownloadInfoList != null && courseDownloadInfoList.size() != 0) {
            for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                set.add(courseDownloadInfo.getState());
            }
        }

        if(set.contains(1) && !set.contains(5)) {
            downloadState = 1;
        } else if(set.contains(2) && !set.contains(5)){
            downloadState = 2;
        } else if(set.contains(5)){
            downloadState = 5;
        } else if(set.contains(3) && !set.contains(1) && !set.contains(5)){
            downloadState = 3;
        } else if(set.contains(4) && !set.contains(1)&& !set.contains(2)&& !set.contains(3)&& !set.contains(5)){
            downloadState = 4;
        } else if (set.size() == 0){
            downloadState = 0;
        }

        return downloadState;
    }
}
