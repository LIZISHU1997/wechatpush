package com.lizishu.wechat.wechatpush.controller;


import com.lizishu.wechat.wechatpush.queue.user.UserQueue;
import com.lizishu.wechat.wechatpush.util.AccessTokenUtil;
import com.lizishu.wechat.wechatpush.util.MessageUtil;
import com.lizishu.wechat.wechatpush.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author by lijiacheng
 * @date 2021/4/16.
 * 与微信产生交互的控制器
 */
@RestController
@Slf4j
public class WeChatController {
    @Value("${wechat.menuUrl}")
    private String menuUrl;

    @Value("${wechat.templateMsgUrl}")
    private String templateMsgUrl;

    /**
     *  与微信服务器进行认证的API接口
     * */
    @GetMapping(value = "/wechat")//穿透域名加/wechat
    public Object check (String signature ,
                         String timestamp ,
                         String nonce ,
                         String echostr) {
        return echostr;//返回随机字符串
    }

    /**
     *  处理微信用户的交互行为
     * @param request 请求对象
     * */
    @PostMapping(value = "/wechat" , produces = {"application/xml;charset=utf-8"})
    public Object doRequest (HttpServletRequest request) throws IOException {
        //解析XML文档，判断消息类型，判断事件类型，实现沟通逻辑

        //解析XML文档，转换为Map，可读性更高，获取数据方便
        Map<String , String> map = XMLUtil.getMap(request.getInputStream());

        //获取消息类型
        String msgType = map.get("MsgType");

        //获取消息是谁发的，用户的openid
        String fromUser = map.get("FromUserName");

        //发送给谁，我们的公众号的账号
        String toUser = map.get("ToUserName");

        //要回复的内容
        String reply = "";

        //如果是事件类型的消息
        if (msgType.equals("event")) {
            //获取事件类型
            String event = map.get("Event");
            //如果是关注事件
            if (event.equals("subscribe")) {
                reply = "欢迎您关注智慧养老平台！";

                //在这里实现，获取用户信息，并且信息入库
                //放入阻塞队列
                UserQueue.QUEUE.push(fromUser);

            }else if (event.equals("CLICK")) {
                //如果是点击事件，获取菜单的key值，实现我们自己的业务逻辑
                String key = map.get("EventKey");
                if (key.equals("get-post")) {
                    reply = "功能正在建设中，请期待...";
                }
            }
        }else if (msgType.equals("text")) {

            //用户发送给我们的消息
            String content = map.get("Content");

            if (content.equals("1")) {
                reply = "1";
            }else if (content.equals("2")) {
                reply = "2";
            }else if (content.equals("3")) {
                reply = "3";
            }else {
                reply = "请回复您要解决的问题编号：\n" +
                        "1、1\n" +
                        "2、2\n" +
                        "3、3";
            }
        }

        return MessageUtil.formatMsg(fromUser , toUser , 454245 , "text" , reply);
    }

    /**
     *  创建菜单
     * */
    @GetMapping(value = "/create-menu")
    public Object test () {
        String token = AccessTokenUtil.getToken();
        String paramStr = "{\n" +
                "  \"button\":[\n" +
                "    {\n" +
                "      \"type\":\"click\",\n" +
                "      \"name\":\"我的海报\",\n" +
                "      \"key\":\"get-post\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"智慧服务\",\n" +
                "      \"sub_button\":[\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"平台公告\",\n" +
                "          \"url\":\"https://news.qq.com/\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"智慧养老\",\n" +
                "          \"url\":\"https://news.163.com/\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"智享搜搜乐\",\n" +
                "          \"url\":\"https://www.baidu.com/index.php?tn=monline_3_dg\"\n" +
                "        }]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"可视对讲\",\n" +
                "      \"type\":\"view\",\n" +
                "      \"url\":\"https://yanglao.ananops.com/#/\"\n" +
                "    }]\n" +
                "}\n";


        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8") , paramStr);
            Request request = new Request.Builder().url(menuUrl + "?access_token=" + token).post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("创建菜单成功");
            }
        }catch (Exception e) {
            log.error("创建菜单出错");
        }

        return "success";
    }

    /**
     *  发送模板消息
     * */
    @GetMapping(value = "/test/msg")
    public Object testMsg () throws IOException {
        String jsonBody = "{\n" +
                "  \"touser\":\"oqlzU6JBjCViqbH57oviNMG9T1IQ\",\n" +
                "  \"template_id\":\"bdhBg3HiodvNKXjExAYmBGYiC5VuLdsi4RnFn11ZPJs\",\n" +
                "  \"data\":{\n" +
                "    \"name\": {\n" +
                "      \"value\":\"智慧养老\",\n" +
                "      \"color\":\"#173177\"\n" +
                "    },\n" +
                "    \"username\":{\n" +
                "      \"value\":\"袁德胜\",\n" +
                "      \"color\":\"#173177\"\n" +
                "    },\n" +
                "    \"time\": {\n" +
                "      \"value\":\"2020-03-08\",\n" +
                "      \"color\":\"#173177\"\n" +
                "    },\n" +
                "    \"remark\": {\n" +
                "      \"value\":\"娃哈哈\",\n" +
                "      \"color\":\"#173177\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8") , jsonBody);
        Request request = new Request.Builder().url(templateMsgUrl + "?access_token=" + AccessTokenUtil.getToken()).post(body).build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String result = response.body().string();
            System.out.println(result);
            return "发送成功";
        }
        return "发送失败";
    }
}
