package com.xiaotian.redis.task;

import com.alibaba.fastjson.JSONObject;

import com.xiaotian.redis.msmq.RedisMQ;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @decription MsgTask
 * <p>发送消息</p>
 * @author xiaotian
 * @date 2018/2/9 18:04
 */
@EnableScheduling
@Component
public class MsgTask {

    @Resource private RedisMQ redisMQ;
    // @Value("${mq.list.first}") private String MQ_LIST_FIRST;

    @Scheduled(cron="*/5 * * * * *")
    //@Scheduled(fixedRate  = 5000)
    public void sendMsg() {
        // 消费
        List<String> msgs = redisMQ.consume(redisMQ.getRoutes().get(0).getList());
        int len;
        if (null != msgs && 0 < (len = msgs.size())) {
            // 将每一条消息转为JSONObject
            JSONObject jObj;
            for (int i = 0; i < len; i++) {
                if (!StringUtils.isEmpty(msgs.get(i))) {
                    jObj = JSONObject.parseObject(msgs.get(i));
                    // 取出消息
                    System.out.println(jObj.toJSONString());
                }
            }
        }
    }
}
