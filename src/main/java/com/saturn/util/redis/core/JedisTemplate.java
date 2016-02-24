package com.saturn.util.redis.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

/**
 * redis客户端 jedis简单
 */
public class JedisTemplate implements JedisCommands {

    private static final Logger log = LoggerFactory.getLogger(JedisTemplate.class);

    private static JedisTemplate systemJedisTemplate;
    private static ResourceBundle rb;

    /**
     * 默认redis 配置，用来维护所有redis 中注册的key
     */
    static {
        // rb = ResourceBundle.getBundle("tauren/jedis/core/defaultredis");
        // JedisPoolConfig config = new JedisPoolConfig();
        // // 是否注册key
        // boolean b = Boolean.parseBoolean(rb.getString("default.reg"));
        // if (b) {
        // config.setMaxTotal(Integer.parseInt(rb.getString("jedispool.maxActive")));
        // config.setMaxIdle(Integer.parseInt(rb.getString("jedispool.maxIdle")));
        // config.setMaxWaitMillis(Integer.parseInt(rb.getString("jedispool.maxWait")));
        // config.setTestOnBorrow(Boolean.parseBoolean(rb.getString("jedispool.testOnBorrow")));
        // config.setTestOnReturn(Boolean.parseBoolean(rb.getString("jedispool.testOnReturn")));
        // JedisPool pool = new JedisPool(config, rb.getString("default.host"), Integer.parseInt(rb.getString("default.port")), Integer.parseInt(rb.getString("default.timeout")));
        // systemJedisTemplate = new JedisTemplate(pool);
        // }
    }

    public JedisTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisTemplate(JedisPool jedisPool, String pwd, boolean isAuth) {
        this.jedisPool = jedisPool;
        this.pwd = pwd;
    }

    /**
     * 把要使用到的key规则注册起来，方便查阅
     * 
     * @param keyPrefixs
     * @throws JedisException
     */
    public void registerKey(String... keyPrefixs) throws JedisException {
        if (systemJedisTemplate == null) {
            throw new JedisException("System JedisTemplate null.");
        }
        Client c = systemJedisTemplate.getJedisPool().getResource().getClient();
        String host = c.getHost();
        int port = c.getPort();
        for (String keyPrefix : keyPrefixs) {
            String key = keyPrefix.split("-")[0].trim();
            String describe = keyPrefix.split("-")[1].trim();
            if (!systemJedisTemplate.hexists("registerkey", "key :host:port:db" + key + ":" + host + ":" + port + ":" + dbIndex)) {
                systemJedisTemplate.hset("registerkey", "key :host:port:db" + key + ":" + host + ":" + port + ":" + dbIndex, "describe=" + describe);
            } else {

                log.warn("key :host:port:" + key + ":" + host + ":" + port + ":" + dbIndex + "  is exists,please change new key.");
            }
        }
    }

    private JedisPool jedisPool;
    private int dbIndex;
    private String pwd;

    public Jedis getJedis() {
        Jedis j = jedisPool.getResource();
        if (pwd != null && !pwd.isEmpty()) {
            j.auth(pwd);
        }
        if (!j.isConnected()) {
            log.info("try reconnect jedis:" + j);
            j.connect();
        }
        return j;
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public Long lpushx(String arg0, String arg1) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpushx(arg0, arg1);
        }
    }

    public Long rpushx(String arg0, String arg1) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpushx(arg0, arg1);
        }
    }

    @Override
    public Long zcount(String arg0, String arg1, String arg2) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcount(arg0, arg1, arg2);
        }
    }

    @Override
    public Set<String> zrangeByScore(String arg0, String arg1, String arg2, int arg3, int arg4) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScore(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Set<String> zrangeByScore(String arg0, String arg1, String arg2) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScore(arg0, arg1, arg2);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, String arg1, String arg2, int arg3, int arg4) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, String arg1, String arg2) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScoreWithScores(arg0, arg1, arg2);
        }
    }

    @Override
    public Long zremrangeByScore(String arg0, String arg1, String arg2) {
        try (Jedis jedis = getJedis()) {
            return jedis.zremrangeByScore(arg0, arg1, arg2);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, String arg1, String arg2, int arg3, int arg4) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScore(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, String arg1, String arg2) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScore(arg0, arg1, arg2);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, String arg1, String arg2, int arg3, int arg4) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, String arg1, String arg2) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScoreWithScores(arg0, arg1, arg2);
        }
    }

    @Override
    public String set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value);
        }
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    @Override
    public Boolean exists(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        }
    }

    @Override
    public String type(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.type(key);
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, seconds);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        try (Jedis jedis = getJedis()) {
            return jedis.expireAt(key, unixTime);
        }
    }

    @Override
    public Long ttl(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.ttl(key);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setbit(key, offset, value);
        }
    }

    @Override
    public Boolean getbit(String key, long offset) {
        try (Jedis jedis = getJedis()) {
            return jedis.getbit(key, offset);
        }
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setrange(key, offset, value);
        }
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        try (Jedis jedis = getJedis()) {
            return jedis.getrange(key, startOffset, endOffset);
        }
    }

    @Override
    public String getSet(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.getSet(key, value);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setnx(key, value);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setex(key, seconds, value);
        }
    }

    @Override
    public Long decrBy(String key, long integer) {
        try (Jedis jedis = getJedis()) {
            return jedis.decrBy(key, integer);
        }
    }

    @Override
    public Long decr(String key) {

        try (Jedis jedis = getJedis()) {
            return jedis.decr(key);
        }
    }

    @Override
    public Long incrBy(String key, long integer) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(key, integer);
        }
    }

    @Override
    public Long incr(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.incr(key);
        }
    }

    @Override
    public Long append(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.append(key, value);
        }
    }

    @Override
    public String substr(String key, int start, int end) {
        try (Jedis jedis = getJedis()) {
            return jedis.substr(key, start, end);
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hset(key, field, value);
        }
    }

    @Override
    public String hget(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, field);
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hsetnx(key, field, value);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        try (Jedis jedis = getJedis()) {
            return jedis.hmset(key, hash);
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        try (Jedis jedis = getJedis()) {
            return jedis.hmget(key, fields);
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hincrBy(key, field, value);
        }
    }

    @Override
    public Boolean hexists(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hexists(key, field);
        }
    }

    @Override
    public Long hdel(String key, String... field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hdel(key, field);
        }
    }

    @Override
    public Long hlen(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hlen(key);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(key);
        }
    }

    @Override
    public List<String> hvals(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hvals(key);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hgetAll(key);
        }
    }

    @Override
    public Long rpush(String key, String... value) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpush(key, value);
        }
    }

    @Override
    public Long lpush(String key, String... string) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpush(key, string);
        }
    }

    @Override
    public Long llen(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.llen(key);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrange(key, start, end);
        }
    }

    @Override
    public String ltrim(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.ltrim(key, start, end);
        }
    }

    @Override
    public String lindex(String key, long index) {
        try (Jedis jedis = getJedis()) {
            return jedis.lindex(key, index);
        }
    }

    @Override
    public String lset(String key, long index, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lset(key, index, value);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrem(key, count, value);
        }
    }

    @Override
    public String lpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpop(key);
        }
    }

    @Override
    public String rpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpop(key);
        }
    }

    @Override
    public Long sadd(String key, String... member) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key, member);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.smembers(key);
        }
    }

    @Override
    public Long srem(String key, String... member) {
        try (Jedis jedis = getJedis()) {
            return jedis.srem(key, member);
        }
    }

    @Override
    public String spop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.spop(key);
        }
    }

    @Override
    public Long scard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        }
    }

    @Override
    public Boolean sismember(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.sismember(key, member);
        }
    }

    @Override
    public String srandmember(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.srandmember(key);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, score, member);
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrange(key, start, end);
        }
    }

    @Override
    public Long zrem(String key, String... member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrem(key, member);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zincrby(key, score, member);
        }
    }

    @Override
    public Long zrank(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrank(key, member);
        }
    }

    @Override
    public Long zrevrank(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrank(key, member);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrange(key, start, end);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeWithScores(key, start, end);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeWithScores(key, start, end);
        }
    }

    @Override
    public Long zcard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcard(key);
        }
    }

    @Override
    public Double zscore(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zscore(key, member);
        }
    }

    @Override
    public List<String> sort(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.sort(key);
        }
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        try (Jedis jedis = getJedis()) {
            return jedis.sort(key, sortingParameters);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcount(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScore(key, max, min);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScore(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zremrangeByRank(key, start, end);
        }
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zremrangeByScore(key, start, end);
        }
    }

    @Override
    public Long linsert(String key, Client.LIST_POSITION where, String pivot, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.linsert(key, where, pivot, value);
        }
    }

    public Set<String> keys(String pattern) {
        try (Jedis jedis = getJedis()) {
            return jedis.keys(pattern);
        }
    }

    public Long del(String keys) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(keys);
        }
    }

    public Set<String> sinter(final String... keys) {
        try (Jedis jedis = getJedis()) {
            return jedis.sinter(keys);
        }
    }

    public Long sinterstore(final String dstkey, final String... keys) {
        try (Jedis jedis = getJedis()) {
            return jedis.sinterstore(dstkey, keys);
        }
    }

    public void rename(final String oldKey, final String newKey) {
        try (Jedis jedis = getJedis()) {
            jedis.rename(oldKey, newKey);
        }
    }

    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        try (Jedis jedis = getJedis()) {
            jedis.psubscribe(jedisPubSub, patterns);
        }
    }

    /**
     * 订阅
     */
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        try (Jedis jedis = getJedis()) {
            jedis.subscribe(jedisPubSub, channels);
        }
    }

    public long publish(String channel, String message) {
        try (Jedis jedis = getJedis()) {
            return jedis.publish(channel, message);
        }
    }

    /**
     * pipeline操作
     * 
     * @param keys
     * @param fields
     * @return
     */
    public List<String> pipelinedHget(List<String> keys, List<String> fields) {
        if (keys.size() != fields.size()) {
            throw new IllegalArgumentException("pipeline keys size not eq fields size.");
        }

        try (Jedis jedis = getJedis()) {
            Pipeline pipeline = jedis.pipelined();
            // pipeline.multi();
            for (int i = 0; i < keys.size(); i++) {
                pipeline.hget(keys.get(i), fields.get(i));
            }
            List<Object> list = pipeline.syncAndReturnAll();
            List<String> result = new ArrayList<String>();
            for (Object o : list) {
                if (o == null) {
                    continue;
                }
                result.add(o.toString());
            }
            return result;
            // pipeline.sync();
            // List<Object> list = resp.get();
            // return list;
        }
    }

    public List<String> pipelinedLindex(List<String> keys, List<Integer> index) {
        if (keys.size() != index.size()) {
            throw new IllegalArgumentException("pipeline keys size not eq fields size.");
        }

        try (Jedis jedis = getJedis()) {
            Pipeline pipeline = jedis.pipelined();
            // pipeline.multi();
            for (int i = 0; i < keys.size(); i++) {
                pipeline.lindex(keys.get(i), index.get(i));
            }
            List<Object> list = pipeline.syncAndReturnAll();
            List<String> result = new ArrayList<String>();
            for (Object o : list) {
                if (o == null) {
                    continue;
                }
                result.add(o.toString());
            }
            return result;
            // pipeline.sync();
            // List<Object> list = resp.get();
            // return list;
        }
    }

    @Override
    public Long bitcount(String arg0, long arg1, long arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long bitcount(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> blpop(int arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> blpop(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> brpop(int arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> brpop(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String echo(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long lpushx(String arg0, String... arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long move(String arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long persist(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long pfadd(String arg0, String... arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long pfcount(String arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Long rpushx(String arg0, String... arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String set(String arg0, String arg1, String arg2, String arg3, long arg4) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean setbit(String arg0, long arg1, String arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> srandmember(String arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanResult<String> sscan(String arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanResult<String> sscan(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long strlen(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long zadd(String arg0, Map<String, Double> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long zlexcount(String arg0, String arg1, String arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> zrangeByLex(String arg0, String arg1, String arg2, int arg3, int arg4) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> zrangeByLex(String arg0, String arg1, String arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long zremrangeByLex(String arg0, String arg1, String arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(String arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Double incrByFloat(String key, double value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> spop(String key, long count) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        // TODO Auto-generated method stub
        return null;
    }
}
