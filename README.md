1. 设计方案
2. 代码实现
2.1 技术说明
2.2 核心代码
2.2.1 Message对象
2.2.2 Route(消息路由器)
2.2.3 RedisMq(消息队列)
2.2.4 RedisMq消息队列配置：
2.2.5 消费者
4. 测试
3. 总结
4. 源码连接
本文以redis为数据结构基础，配合Spring管理机制，使用java实现了一个轻量级、可配置的消息队列。适合的项目特点：

Spring框架管理对象
有消息需求，但不想维护mq中间件
有使用redis
对消息持久化并没有很苛刻的要求
需要使用rabbitmq实现延迟消息请参考这里

1. 设计方案

设计主要包含以下几点：

将整个Redis当做消息池，以kv形式存储消息
使用ZSET做优先队列，按照score维持优先级
使用LIST结构，以先进先出的方式消费
zset和list存储消息地址（对应消息池的每个key）
自定义路由对象，存储zset和list名称，以点对点的方式将消息从zset路由到正确的list
使用定时器维持路由
根据TTL规则实现消息延迟
