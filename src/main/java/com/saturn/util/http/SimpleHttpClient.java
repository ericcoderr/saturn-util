package com.saturn.util.http;

import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * 线程安全的HttpClient
 */
public class SimpleHttpClient extends DefaultHttpClient {

    private int connectionTimeout = 5000;
    private int soTimeout = 5000;
    private int socketBufferSize = 8192;
    private boolean tcpNoDelay = true;
    private boolean useExpectContinue = false;
    private Charset contentCharset = Charset.forName("UTF-8");
    private HttpVersion version = HttpVersion.HTTP_1_1;

    public SimpleHttpClient() {
    }

    public SimpleHttpClient(int timeout) {
        this.soTimeout = connectionTimeout = timeout;
    }

    public SimpleHttpClient(int soTimeout, int connectionTimeout) {
        this.soTimeout = soTimeout;
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * ThreadSafeClientConnManager
     */
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        PoolingClientConnectionManager manager = null;
        try {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", getPort(), PlainSocketFactory.getSocketFactory()));
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            registry.register(new Scheme("https", 443, ssf));
            manager = new PoolingClientConnectionManager(registry);
        } catch (Exception e) {
        }
        return manager;
    }

    @Override
    protected HttpParams createHttpParams() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, version);
        HttpProtocolParams.setContentCharset(params, contentCharset.name());
        HttpProtocolParams.setUseExpectContinue(params, useExpectContinue);
        HttpConnectionParams.setTcpNoDelay(params, tcpNoDelay);
        HttpConnectionParams.setSocketBufferSize(params, socketBufferSize);
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, soTimeout);
        HttpConnectionParams.setSoKeepalive(params, true);
        return params;
    }

    public int getPort() {
        return 80;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }
}
