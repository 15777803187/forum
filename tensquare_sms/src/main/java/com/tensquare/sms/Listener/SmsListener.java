package com.tensquare.sms.Listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {
    /**
     * 监听短信微服务的消息队列，当sms消息队列中有消息，则这个工程就获取其中的手机号和验证码，然后通过阿里云短信发送接口将短信发个客户
     * @param map
     */
    @RabbitHandler
    public void sendSms(Map map){
        System.out.println("mobile:"+map.get("mobile"));
        System.out.println("code:"+map.get("code"));

    }
}
