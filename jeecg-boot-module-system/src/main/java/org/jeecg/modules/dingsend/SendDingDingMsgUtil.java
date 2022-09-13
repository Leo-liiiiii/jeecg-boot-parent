package org.jeecg.modules.dingsend;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserSimplelistRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserSimplelistResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 钉钉发送工作消息
 */

public class SendDingDingMsgUtil {

    private static String APPKEY = "dingmm15br1cvzqz0mr8";

    private static String APPSECRET = "Ikm8U-3NZ4D7RgGQP7M4oYlFLFlyr92xpDWvY6kuvRIDACINLj6IjLb6ZfkgfJaN";

    private static Long AGENT_ID = 1526986705l;

    private static Map<String, String> map = new HashMap<>(16);

    public static void main(String[] args) throws Exception {
        initUserList(getAccessToken());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if("18250490339".equals(entry.getKey())){
                send("18250490339","陈登步");
            }
            if("17720748129".equals(entry.getKey())){
                send("17720748129","杨恩赐");
            }
        }

    }


    /**
     * 根据手机号码发送工作消息
     * @param mobile
     * @param content
     */
    public static void send(String mobile, String content) {
        try {
            String accessToken = getAccessToken();
            if (map == null) {
                initUserList(accessToken);
            }
            int userListCount = getUserListCount(accessToken);
            if (userListCount > map.size()) {
                initUserList(accessToken);
            }
            senWorkMsg(accessToken, map.get(mobile), content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取 accessToken
     *
     * @return
     * @throws Exception
     */
    private static String getAccessToken() throws Exception {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(APPKEY);
        request.setAppsecret(APPSECRET);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        String accessToken = response.getAccessToken();
        return accessToken;
    }


    /**
     * 发送工作消息
     *
     * @param accessToken
     * @param userId
     * @param content
     * @throws Exception
     */
    private static void senWorkMsg(String accessToken, String userId, String content) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userId);
        request.setAgentId(AGENT_ID);
        request.setToAllUser(false);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
        msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
        msg.getOa().getHead().setText("head");
        msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
        msg.getOa().getBody().setContent(content);
        msg.setMsgtype("oa");
        request.setMsg(msg);

        OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, accessToken);
        System.out.println(response.getBody());

    }

    /**
     * 部门用户列表
     *
     * @param accessToken
     * @throws Exception
     */
    private static void initUserList(String accessToken) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/simplelist");
        OapiUserSimplelistRequest request = new OapiUserSimplelistRequest();
        request.setDepartmentId(1L);//根部门
        request.setOffset(0L);// 不设置分页获取所有用户
        request.setSize(10L);//不设置分页获取所有用户
        request.setHttpMethod("GET");

        OapiUserSimplelistResponse response = client.execute(request, accessToken);
        List<OapiUserSimplelistResponse.Userlist> userlist = response.getUserlist();
        for (OapiUserSimplelistResponse.Userlist user : userlist) {
            String userId = user.getUserid();
            String mobile = getMobile(accessToken, userId);
            map.put(mobile, userId);
        }
    }

    /**
     * 获取部门用户数量
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    private static int getUserListCount(String accessToken) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/simplelist");
        OapiUserSimplelistRequest request = new OapiUserSimplelistRequest();
        request.setDepartmentId(1L);
        request.setOffset(0L);
        request.setSize(10L);
        request.setHttpMethod("GET");

        OapiUserSimplelistResponse response = client.execute(request, accessToken);
        List<OapiUserSimplelistResponse.Userlist> userlist = response.getUserlist();
        return userlist.size();
    }

    /**
     * 根据 userId 获取手机号
     *
     * @param accessToken
     * @param userId
     * @return
     * @throws Exception
     */
    private static String getMobile(String accessToken, String userId) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        OapiUserGetResponse response = client.execute(request, accessToken);
        return response.getMobile();
    }

}
