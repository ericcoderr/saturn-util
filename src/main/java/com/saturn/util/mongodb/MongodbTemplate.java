/** filename:MongodbTemplate.java */
package com.saturn.util.mongodb;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;

/**
 * <pre>
 * mongodb基本操作类
 * mongodb 时间操作是用默认标准时区，存入bson时不会保存本地时区，所以寸时间时要转换
 *  加上当前服务器时区
 *   TimeZone tz = TimeZone.getTimeZone("Asia/Shenzhen");
 *       TimeZone.setDefault(tz);
 * </pre>
 * 
 * @author : ericcoderr@gmail.com
 * @date : 2012-11-30
 */
public class MongodbTemplate {

    public static final int DEFAULT_LIMIT = 100;

    public static final int DEFAULT_NUM = 300; // 地理位置查询默认返回条数

    private MongoClient mongoClient;

    public MongodbTemplate() {
    }

    public MongodbTemplate(MongoClient mongoClient) {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(100); // 客户端最大链接数
        builder.threadsAllowedToBlockForConnectionMultiplier(5); // 5* 100 允许最大线程等待队列或去链接
        // builder.socketKeepAlive(false);
        MongoClientOptions options = builder.build();
        mongoClient = new MongoClient(mongoClient.getAllAddress(), options);
        this.mongoClient = mongoClient;
    }

    /**
     * @param isSlaveOk true : secondary 节点可读
     */
    public MongodbTemplate(MongoClient mongoClient, boolean isSlaveOk) {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(200);
        builder.threadsAllowedToBlockForConnectionMultiplier(5);
        // builder.socketKeepAlive(false);
        MongoClientOptions options = builder.build();
        mongoClient = new MongoClient(mongoClient.getAllAddress(), options);
        this.mongoClient = mongoClient;
        if (isSlaveOk) {
            mongoClient.setReadPreference(ReadPreference.secondaryPreferred());
        }
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * 如果是多db，多collection 必需每次返回新的实例
     * 
     * @param dbname
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> getDBCollection(String dbname, String collectionName) {
        return mongoClient.getDatabase(dbname).getCollection(collectionName); // 如果不存在库表会自动创建.
    }

    // **************************************************************修改相关************************************************************

}
