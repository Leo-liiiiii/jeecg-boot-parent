package org.jeecg.modules.test;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;

public class DingTest {

    private static Long AGENTLD = 1544553096L;//微应用ID
    private static String APPSECRET ="Sa2sG57vcTFXIt9VVgYbgfQWChNXVDr-f4oTUjQCVahefZZzjDx3PqlQuT5m8JUe";//企业应用凭证密匙
    private static String CORPID = "ding221fa3daa51500a0bc961a6cb783455b";//企业ID
    private static String APPKEY = "dingdior3nxknipuiry9";//企业凭证唯一标识key
    public static void main(String[] args) throws ApiException {

        //获取token  appkey+appsecret通过下面接口就能获取到token
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        //Appkey
        request.setAppkey(APPKEY);
        //Appsecret
        request.setAppsecret(APPSECRET);
        /*请求方式*/
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        //获取到token
        String AccessToken = response.getAccessToken();
        //获取接收者ID接口
        DingTalkClient clientUserId = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
        OapiUserGetuserinfoRequest reqUID = new OapiUserGetuserinfoRequest();
        reqUID.setCode("");
        reqUID.setHttpMethod("GET");
        OapiUserGetuserinfoResponse rspUID = clientUserId.execute(reqUID, AccessToken);
        //获取到接收者的ID
        String userId = rspUID.getUserid();
        //发送消息接口
        DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        req.setAgentId(AGENTLD);//微应用ID
        req.setToAllUser(false);//是否发给所有人 false否 ,否的情况下要获取接受者id 可以是多个和接收者部门id
        req.setUseridList(userId);//接收者ID

        //---------消息体------------
        OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        obj1.setMsgtype("oa");//消息类型
        OapiMessageCorpconversationAsyncsendV2Request.Text obj2 = new OapiMessageCorpconversationAsyncsendV2Request.Text();
        obj2.setContent("测试");//文本消息
        obj1.setText(obj2);
        OapiMessageCorpconversationAsyncsendV2Request.OA obj3 = new OapiMessageCorpconversationAsyncsendV2Request.OA();
        OapiMessageCorpconversationAsyncsendV2Request.Body obj4 = new OapiMessageCorpconversationAsyncsendV2Request.Body();
        obj4.setContent("测试消息");//发送消息内容
        obj3.setBody(obj4);
        OapiMessageCorpconversationAsyncsendV2Request.Head obj5 = new OapiMessageCorpconversationAsyncsendV2Request.Head();//消息头
        obj5.setText("测试11");//消息头内容
        obj3.setHead(obj5);
        obj1.setOa(obj3);
        req.setMsg(obj1);
        //发送消息
        OapiMessageCorpconversationAsyncsendV2Response rsp = client1.execute(req, AccessToken);
        System.out.println(rsp.getBody());

    }
}
