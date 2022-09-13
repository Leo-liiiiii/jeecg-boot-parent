//package org.jeecg.modules.dingsend;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.extension.exceptions.ApiException;
//import com.dingtalk.api.DefaultDingTalkClient;
//import com.dingtalk.api.DingTalkClient;
//import com.dingtalk.api.request.OapiDepartmentListIdsRequest;
//import com.dingtalk.api.request.OapiGettokenRequest;
//import com.dingtalk.api.response.OapiDepartmentListIdsResponse;
//import com.dingtalk.api.response.OapiGettokenResponse;
//
//public class DingSendUtil {
//
//    private static String APPKEY = "*************";//(这里是应用的AppKey)
//    private static String APPSECRET = "*************";//(这里是应用的APPSECRET )
//    private static AccessTokenlin Atl;
//
//    private static String getAccessToken() throws Exception {
//        DefaultDingTalkClient client = new 					DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
//        OapiGettokenRequest request = new OapiGettokenRequest();
//        request.setAppkey(APPKEY);
//        request.setAppsecret(APPSECRET);
//        request.setHttpMethod("GET");
//        OapiGettokenResponse response = client.execute(request);
//        String accessToken = response.getAccessToken();
//        //将获取的AccessToken和它的过期时间封装到一个AccessToken类中
//        Atl = new AccessTokenlin(accessToken, "7200");
//        return accessToken;
//    }
//
//    public static String GetAccessToken() throws Exception {
//        //这里我们需要判断一下Accesstoken是否过期，然后才将其返回
//        if(Atl==null||Atl.isExpired()) {
//            getAccessToken();
//        }
//        return Atl.getAccessToken();
//    }
//
//    /**
//     *获取子部门列表
//     */
//    public static void getDeptList() {
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list_ids");
//        OapiDepartmentListIdsRequest request = new OapiDepartmentListIdsRequest();
//        request.setId("1");
//        request.setHttpMethod("GET");
//        try {
//            OapiDepartmentListIdsResponse response = client.execute(request, GetAccessToken());
//            //这里引用了JSONObject，需要导入相关的jar包，这里我是直接输出相关的信息到控制台获取一个部门id
//            JSONObject jsonObject = JSONObject.fromObject(response);
//            System.out.println(jsonObject);
//        } catch (ApiException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//
//}
