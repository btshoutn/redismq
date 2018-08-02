package com.xiaotian.redis.mq;

import com.alibaba.fastjson.JSONObject;
import com.xiaotian.redis.msmq.Message;
import com.xiaotian.redis.msmq.RedisMQ;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @decription TestMQ
 * <p>测试</p>
 * @author xiaotian
 * @date 2018/2/9 18:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMQ {

    @Resource
    private RedisMQ redisMQ;
    @Value("${mq.queue.first}")
    private String MQ_QUEUE_FIRST;

    @Test
    public void testMq() {

        JSONObject jObj = new JSONObject();
        jObj.put("msg", "这是一条短信11111");

        String seqId = UUID.randomUUID().toString();

        // 将有效信息放入消息队列和消息池中
        Message message = new Message();
        message.setBody(jObj.toJSONString());
        // 可以添加延迟配置
        message.setDelay(20);
        message.setTopic("SMS");
        message.setCreateTime(System.currentTimeMillis());
        message.setId(seqId);
        message.setTtl(20 * 60);
        message.setStatus(0);
        message.setPriority(0);
        redisMQ.addMsgPool(message);
        redisMQ.enMessage(MQ_QUEUE_FIRST,
                message.getCreateTime() + message.getDelay() + message.getPriority(), message.getId());
    }
}
