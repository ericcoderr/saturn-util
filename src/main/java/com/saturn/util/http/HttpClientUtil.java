package com.saturn.util.http;

import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.saturn.util.CharsetUtil;
import com.saturn.util.HumanReadableUtil;


public class HttpClientUtil {

    private static final HttpClient httpClient = new SimpleHttpClient(30000, 30000);
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final Logger slowlog = LoggerFactory.getLogger("slow");
    private static final Logger errlog = LoggerFactory.getLogger("error");
    private static final int slow_threshold = 1000;

    public static int httpHead(String url) {
        return httpHead(url, CharsetUtil.UTF_8.name());
    }

    public static int httpHead(String url, String defaultCharset) {
        return httpHead(httpClient, url, defaultCharset);
    }

    public static int httpHead(HttpClient client, String url, String defaultCharset) {
        HttpHead httpHead = null;
        StatusLine sl = null;
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpHead = new HttpHead(url);
            HttpResponse response = client.execute(httpHead);
            sl = response.getStatusLine();
            return sl.getStatusCode();
        } catch (Exception e) {
            ex = e;
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpHead -ERROR- [{}]url:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, ex });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpHead -SLOW-  [{}]url:{}", new Object[] { HumanReadableUtil.timeSpan(span), url });
            } else {
                log.debug("httpHead -OK-    [{}]url:{}", new Object[] { HumanReadableUtil.timeSpan(span), url });
            }
            if (null != httpHead) {
                httpHead.abort();
                // 如果使用了PoolingClientConnectionManager，只有调用此方法才能完全关闭连接
                // client.getConnectionManager().shutdown(); // 关闭链接
            }
        }
        return Integer.MIN_VALUE;//
    }

    public static String httpGet(HttpClient client, String url, String defaultCharset) {
        HttpGet httpGet = null;
        String result = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpGet = new HttpGet(url);

            // 发送非keepalive请求
            httpGet.setHeader("Connection", "close");
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, defaultCharset);
            return result;
        } catch (Exception e) {
            ex = e;
        } finally {
            // if (result.length() > 120) {
            // result = result.substring(0, 50) + "...(" + result.length() + ")..." + result.substring(result.length() - 50, result.length());
            // }
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpGet  -ERROR- [{}]url:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, ex });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpGet  -SLOW-  [{}]url:{}", new Object[] { HumanReadableUtil.timeSpan(span), url });
            } else {
                log.debug("httpGet  -OK-    [{}]url:{}", new Object[] { HumanReadableUtil.timeSpan(span), url });
            }
            if (null != httpGet) {
                httpGet.abort();
                // client.getConnectionManager().shutdown(); // 关闭链接
            }
        }
        return null;
    }

    public static String httpGet(String url) {
        return httpGet(url, CharsetUtil.UTF_8.name());
    }

    public static String httpGet(String url, String defaultCharset) {
        return httpGet(httpClient, url, defaultCharset);
    }

    public static String httpPost(HttpClient client, String url, String postContent, String defaultCharset) {
        HttpPost httpPost = null;
        String content = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpPost = new HttpPost(url);
            HttpEntity entity = new StringEntity(postContent, defaultCharset);
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), defaultCharset);
            return content;
        } catch (Exception e) {
            ex = e;
        } finally {
            // if (content.length() > 120) {
            // content = content.substring(0, 50) + "...(" + content.length() + ")..." + content.substring(content.length() - 50, content.length());
            // }
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpPost -ERROR- [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, postContent, ex });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpPost -SLOW-  [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, postContent });
            } else {
                log.debug("httpPost -OK-  [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, postContent });
            }
            if (null != httpPost) {
                httpPost.abort();
                // client.getConnectionManager().shutdown(); // 关闭链接池
            }
        }
        return null;
    }

    public static String httpPostWithHeader(HttpClient client, String url, String postContent, Map<String, String> header, String defaultCharset) {
        HttpPost httpPost = null;
        String content = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpPost = new HttpPost(url);
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            HttpEntity entity = new StringEntity(postContent, defaultCharset);
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), defaultCharset);
            return content;
        } catch (Exception e) {
            ex = e;
        } finally {
            // if (content.length() > 120) {
            // content = content.substring(0, 50) + "...(" + content.length() + ")..." + content.substring(content.length() - 50, content.length());
            // }
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpPost -ERROR- [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, postContent, ex });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpPost -SLOW-  [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, postContent });
            } else {
                log.debug("httpPost -OK-  [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, postContent });
            }
            if (null != httpPost) {
                httpPost.abort();
                // client.getConnectionManager().shutdown(); // 关闭链接池
            }
        }
        return null;
    }

    public static String httpPost(HttpClient client, String url, HttpEntity entity, Map<String, String> param, String defaultCharset) throws Exception {
        HttpPost httpPost = null;
        String content = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpPost = new HttpPost(url);
            // httpPost.setHeader("Connection", "close");

            // header添加此元素会导致POST报错
            // httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Expect", "100-Continue");
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), defaultCharset);
            return content;
        } catch (Exception e) {
            ex = e;
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpPost -ERROR- [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, param, ex });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpPost -SLOW-  [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, param, });
            } else {
                log.debug("httpPost -OK-  [{}]url:{},postContent:{}", new Object[] { HumanReadableUtil.timeSpan(span), url, param });
            }
            if (null != httpPost) {
                httpPost.abort(); // 表示请求终止
                // client.getConnectionManager().shutdown(); // 关闭链接池
            }
        }
        return null;
    }

    public static String httpPost(String url, String postContent) {
        return httpPost(httpClient, url, postContent, CharsetUtil.UTF_8.name());
    }

    public static String httpPost(String url, String postContent, String defaultCharset) {
        return httpPost(httpClient, url, postContent, defaultCharset);
    }

    public static String httpPost(String url, HttpEntity httpEntity, Map<String, String> param, String defaultCharset) throws Exception {
        return httpPost(httpClient, url, httpEntity, param, defaultCharset);
    }

    public static String httpPostWithHeader(String url, String content, Map<String, String> header, String defaultCharset) throws Exception {
        return httpPostWithHeader(httpClient, url, content, header, defaultCharset);
    }
}
