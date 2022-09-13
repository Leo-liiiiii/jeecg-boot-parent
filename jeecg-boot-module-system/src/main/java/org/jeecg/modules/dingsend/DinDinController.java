package org.jeecg.modules.dingsend;


import com.dingtalk.api.request.OapiMessageMassSendRequest;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Body;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Head;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Msg;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.OA;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Text;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉发送消息接口
 * @author
 *
 */
@Slf4j
@Api(tags="钉钉推送消息")
@RestController
@RequestMapping("/DinDinController")
public class DinDinController {


    private static Long AGENTLD = 1544553096L;//微应用ID
    private static String APPSECRET ="Sa2sG57vcTFXIt9VVgYbgfQWChNXVDr-f4oTUjQCVahefZZzjDx3PqlQuT5m8JUe";//企业应用凭证密匙
    private static String CORPID = "ding221fa3daa51500a0bc961a6cb783455b";//企业ID
    private static String APPKEY = "dingdior3nxknipuiry9";//企业凭证唯一标识key

    //发送消息
    @AutoLog(value = "钉钉推送消息")
    @ApiOperation(value="钉钉推送消息", notes="钉钉推送消息")
    @RequestMapping("/Message")                                         //requestAuthCode 免密登陆授权码前端获取传进来
    public void DDMessage(@RequestParam String msgcontent,@RequestParam String requestAuthCode) throws ApiException {
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
        reqUID.setCode(requestAuthCode);
        reqUID.setHttpMethod("GET");
        OapiUserGetuserinfoResponse rspUID = clientUserId.execute(reqUID, AccessToken);
        //获取到接收者的ID
        String userId = rspUID.getUserid();
        //发送消息接口
        DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        req.setAgentId(AGENTLD);//微应用ID
        req.setToAllUser(true);//是否发给所有人 false否 ,否的情况下要获取接受者id 可以是多个和接收者部门id
        req.setUseridList(userId);//接收者ID

        //---------消息体------------
        Msg obj1 = new Msg();
        obj1.setMsgtype("oa");//消息类型
        Text obj2 = new Text();
        obj2.setContent("测试");//文本消息
        obj1.setText(obj2);
        OA obj3 = new OA();
        Body obj4 = new Body();
        obj4.setContent(msgcontent);//发送消息内容
        obj3.setBody(obj4);
        Head obj5 = new Head();//消息头
        obj5.setText("测试11");//消息头内容
        obj3.setHead(obj5);
        obj1.setOa(obj3);
        req.setMsg(obj1);
        //发送消息
        OapiMessageCorpconversationAsyncsendV2Response rsp = client1.execute(req, AccessToken);
        System.out.println(rsp.getBody());

    }





}

