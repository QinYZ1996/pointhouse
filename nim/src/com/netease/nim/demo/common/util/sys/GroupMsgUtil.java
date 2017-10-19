package com.netease.nim.demo.common.util.sys;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

import static com.netease.nimlib.sdk.NIMClient.getService;


/**
 * 群组未读消息
 * Created by Maclaine on 2017/9/25.
 */

public class GroupMsgUtil {
    private final static String TAG="GroupMessage";

    public static Integer read(String tid){
        Integer count=0;
        List<RecentContact> recentContacts = getService(MsgService.class).queryRecentContactsBlock();
        for(RecentContact r:recentContacts){
            if(r.getContactId().equals(tid)){
                count=r.getUnreadCount();
                break;
            }
        }
        return count;
    }

    public static boolean hasMsg(){
        int count=NIMClient.getService(MsgService.class).getTotalUnreadCount();
        return count > 0;
    }
}
