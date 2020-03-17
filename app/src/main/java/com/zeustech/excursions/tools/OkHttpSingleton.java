package com.zeustech.excursions.tools;

import android.content.Context;

import com.zeustech.excursions.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class OkHttpSingleton {

    private static volatile OkHttpSingleton INSTANCE = null;
    private final OkHttpClient client;

    private OkHttpSingleton(Context context) throws Exception {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        Certificate b2b_cer;

        try (InputStream fis = context.getResources().openRawResource(R.raw.development_server_certificate)) {
            b2b_cer = cf.generateCertificate(fis);
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();

        KeyStore keyStore = KeyStore.getInstance(keyStoreType);

        // New stream, no password
        keyStore.load(null, null);

        keyStore.setCertificateEntry("b2b_cer", b2b_cer);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init(keyStore);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }

        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(null, new TrustManager[] { trustManager }, null);

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    public static synchronized OkHttpSingleton getInstance(Context context) {
        if(INSTANCE == null) {
            try {
                INSTANCE = new OkHttpSingleton(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    public OkHttpClient getClient() {
        return client;
    }
}

// https://developer.android.com/training/articles/security-ssl
