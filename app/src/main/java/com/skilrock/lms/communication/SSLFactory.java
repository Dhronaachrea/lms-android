package com.skilrock.lms.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by stpl on 5/2/2016.
 */
public class SSLFactory extends SSLSocketFactory {
    SSLContext context;
    SSLSocketFactory factory;

    public SSLFactory() {
        try {
            init();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    void init() throws KeyManagementException, NoSuchAlgorithmException {
        context = SSLContext.getInstance("TLS");
        context.init(null, TrustAllSSLSocketFactory.getAllTrustManager(), new SecureRandom());
        factory = context.getSocketFactory();
    }


    @Override
    public String[] getDefaultCipherSuites() {
        return factory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return factory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {

        SSLSocket sslSocket = (SSLSocket) factory.createSocket(s, host, port, autoClose);
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
        return sslSocket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        SSLSocket sslSocket = (SSLSocket) factory.createSocket(host, port);
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
        return sslSocket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        SSLSocket sslSocket = (SSLSocket) factory.createSocket(host, port, localHost, localPort);
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
        return sslSocket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocket sslSocket = (SSLSocket) factory.createSocket(host, port);
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
        return sslSocket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocket sslSocket = (SSLSocket) factory.createSocket(address, port, localAddress, localPort);
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
        return sslSocket;
    }
}
