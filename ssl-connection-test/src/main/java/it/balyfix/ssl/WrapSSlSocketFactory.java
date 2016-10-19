package it.balyfix.ssl;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.ssl.KeyMaterial;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;

public class WrapSSlSocketFactory extends SSLSocketFactory {

	private final SSLContext sslContext = SSLContext.getInstance("TLS");

	public WrapSSlSocketFactory(KeyStore keyStore, String keystorePassword)
			throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {

		super(keyStore);

		// trust everyone!
		X509TrustManager tm = new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		};

		if (keyStore != null) {
			KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmfactory.init(keyStore, keystorePassword != null ? keystorePassword.toCharArray() : null);
			KeyManager[] keymanagers = kmfactory.getKeyManagers();
			sslContext.init(keymanagers, new TrustManager[] { tm }, null);
		} else {
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	}

//	@Override
//	public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress,
//			HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
//
//		String sslConfig = (String) params.getParameter(SoapUIHttpRoute.SOAPUI_SSL_CONFIG);
//
//		SSLSocketFactory factory = factoryMap.get(sslConfig);
//
//		if (factory != null) {
//			if (factory == this) {
//				return sslContext.getSocketFactory().createSocket();
//			} else {
//				return factory.createSocket(params);
//			}
//		}
//
//		try {
//			// try to create new factory for specified config
//			int ix = sslConfig.lastIndexOf(' ');
//			String keyStore = sslConfig.substring(0, ix);
//			String pwd = sslConfig.substring(ix + 1);
//
//			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//
//			if (keyStore.trim().length() > 0) {
//				File f = new File(keyStore);
//
//				if (f.exists()) {
//
//					try {
//						KeyMaterial km = new KeyMaterial(f, pwd.toCharArray());
//						ks = km.getKeyStore();
//					} catch (Exception e) {
//
//						pwd = null;
//					}
//				}
//			}
//
////			factory = new SoapUISSLSocketFactory(ks, pwd);
////			factoryMap.put(sslConfig, factory);
//
//			return factory.createSocket(params);
//		} catch (Exception gse) {
//			return super.createSocket(params);
//		}
//
//	}
//
//	@Override
//	public Socket createLayeredSocket(final Socket socket, final String host, final int port, final boolean autoClose)
//			throws IOException, UnknownHostException {
//		SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//		// if( getHostnameVerifier() != null )
//		// {
//		// getHostnameVerifier().verify( host, sslSocket );
//		// }
//		// verifyHostName() didn't blowup - good!
//		return sslSocket;
//	}

}
