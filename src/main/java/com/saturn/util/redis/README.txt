#基于redis的简单订阅发布
#version 0.1
#==========================================================================================================
2012-12-04
CommonMessage
1. String msg CommonMessage(String msgStr)
2.jdbc CommonMessage(String[] msgs, List<Object[]> args)
msgs sql (SELECT * FROM user WHERE userid=? AND workcity=?)
args :jdbc new Object[]{}
3.mongodb:map 
Map<String,Object> map = new TreeMap<String,Object>();DBObject o = new BasicDBObject("userid",111); map.put(key,o)


#===============================================================================================================


#e.g
#################################################################################
#ApplicationContext context = new ClassPathXmlApplicationContext("redis.xml");
#
#final JedisTemplate jedisTemplate = context.getBean("jedisTemplateTest", JedisTemplate.class);
#final JedisPubSubListener jedisPubSubListener = context.getBean("pubSubListener", JedisPubSubListener.class);
#订阅通道
#        new Thread(new Runnable() {
#
#            @Override
#            public void run() {
#                jedisTemplate.subscribe(jedisPubSubListener, jedisPubSubListener.getChannels());
#            }
#        }).start();

###################################################################################
#发布消息
jedisPubSubListener.publish("test2", "{userid:" + i.get() + "}");
#####################################################################################

#����ҵ����
#################################################################################
#public class LoginRedisMQPoper extends RedisMQPoper {
#
#    public LoginRedisMQPoper(JedisPool jedisPool, int dbIndex) {
#        super(jedisPool, dbIndex);
#    }
#
#    //doWork 业务处理
#    @Override
#    public void doWork(String msg) {
#        System.out.println("测试消息" + msg);
#
#    }
#
#}
###################################################################################
