# wechatpush
 微信公众号推送
## 使用到的工具
redis处于运行状态

natapp网址：https://natapp.cn/

微信测试号系统：https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login
 使用natapp进行内网穿透然后在测试系统里填写相关信息。如图：
 ![image](https://user-images.githubusercontent.com/65590773/114986293-6af3ae00-9ec6-11eb-874c-cd2130ec12dd.png)
 
## 更改模块中的内容

将自己的appid和appsecret进行替换

![image](https://user-images.githubusercontent.com/65590773/114986843-1270e080-9ec7-11eb-9a34-50db2c8ecaa3.png)

WeChatController中将用户的openid进行替换就可以发送相关消息

![image](https://user-images.githubusercontent.com/65590773/114987307-99be5400-9ec7-11eb-9159-dcadd2016d6f.png)

## 消息模板的添加

在此处添加消息模板获得template_id

![image](https://user-images.githubusercontent.com/65590773/114987446-bd819a00-9ec7-11eb-9c9c-1cf418364f3d.png)

并WeChatController中将替换template_id可以发送自己的内容模板



