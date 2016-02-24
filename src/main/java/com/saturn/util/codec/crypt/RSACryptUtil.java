package com.saturn.util.codec.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import org.slf4j.Logger;
import com.saturn.util.CloseableHelper;
import com.saturn.util.log.Log;

/**
 * RSA算法的调用工具
 */
public class RSACryptUtil {

    /**
     * RSA加密类池
     */
    private static final Logger log = Log.getLogger();

    public static ThreadLocal<RSACrypt> getRsaCryptFactory(final byte[] rsaPrivateKey) {
        return new ThreadLocal<RSACrypt>() {

            @Override
            protected synchronized RSACrypt initialValue() {
                try {
                    RSACrypt rsa = new RSACrypt();
                    rsa.setPrivateKey(rsaPrivateKey);
                    return rsa;
                } catch (Exception e) {
                    log.error("", e);
                }
                return null;
            }
        };
    }

    public static ThreadLocal<RSACrypt> getRsaCryptFactoryPublic(final byte[] rsaPublicKey) {
        return new ThreadLocal<RSACrypt>() {

            @Override
            protected synchronized RSACrypt initialValue() {
                try {
                    RSACrypt rsa = new RSACrypt();
                    rsa.setPublicKey(rsaPublicKey);
                    return rsa;
                } catch (Exception e) {
                    log.error("", e);
                }
                return null;
            }
        };
    }

    public static ThreadLocal<RSACrypt> getRsaCryptFactory(final PrivateKey rsaPrivateKey, final String transformation) {
        return new ThreadLocal<RSACrypt>() {

            @Override
            protected synchronized RSACrypt initialValue() {
                try {
                    RSACrypt rsa = new RSACrypt(transformation);
                    rsa.setPrivateKey(rsaPrivateKey);
                    return rsa;
                } catch (Exception e) {
                    log.error("", e);
                }
                return null;
            }
        };
    }

    /**
     * 根据公钥生成加密池
     * 
     * @param publicKey
     * @param transformation
     * @return
     */
    public static ThreadLocal<RSACrypt> getRsaCryptFactoryByPubKey(final PublicKey publicKey, final String transformation) {
        return new ThreadLocal<RSACrypt>() {

            @Override
            protected synchronized RSACrypt initialValue() {
                try {
                    RSACrypt rsa = new RSACrypt(transformation);
                    rsa.setPublicKey(publicKey);
                    return rsa;
                } catch (Exception e) {
                    log.error("", e);
                }
                return null;
            }
        };
    }

    /**
     * 以PKCS2类型获取私钥
     * 
     * @param alias
     * @param pwd
     * @param privateKeyPath 私钥存放路径
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws URISyntaxException
     */
    public static PrivateKey getPrivateKey(String alias, String pwd, String privateKeyPath) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
            UnrecoverableKeyException, URISyntaxException {
        InputStream inStream = null;
        // 以PKCS2类型打开密钥库
        try {
            KeyStore store = KeyStore.getInstance("PKCS12");
            URL url = RSACryptUtil.class.getClassLoader().getResource(privateKeyPath);
            inStream = Files.newInputStream(Paths.get((url.toURI())));
            store.load(inStream, pwd.toCharArray());

            // 从密钥库中提取密钥
            PrivateKey pKey = (PrivateKey) store.getKey(alias, pwd.toCharArray());
            return pKey;
        } finally {
            CloseableHelper.close(inStream);
        }
    }


    /**
     * 以x509类型获取私钥
     * 
     * @param alias
     * @param pwd
     * @param privateKeyPath 公钥存放路径
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws URISyntaxException
     */
    public static PublicKey getPublicKey(String publicKeyPath) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, URISyntaxException {
        InputStream inStream = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("x509");
            URL url = RSACryptUtil.class.getClassLoader().getResource(publicKeyPath);
            inStream = Files.newInputStream(Paths.get((url.toURI())));
            Certificate cerCert = cf.generateCertificate(inStream);
            PublicKey publicKey = cerCert.getPublicKey();
            return publicKey;
        } finally {
            CloseableHelper.close(inStream);
        }
    }
}
