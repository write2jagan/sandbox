package it.balyfix.ssl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
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

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpConnectionParams;
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

	private static SSLSocket enableSocket(SSLSocket socket) {
		return socket;
	}

	@Override
	public Socket createSocket(HttpParams params) throws IOException {
	//	String sslConfig = (String) params.getParameter(SoapUIHttpRoute.SOAPUI_SSL_CONFIG);

		//if (StringUtils.isEmpty(sslConfig)) {
			return enableSocket((SSLSocket) sslContext.getSocketFactory().createSocket());
	//	}

//		SSLSocketFactory factory = factoryMap.get(sslConfig);
//
//		if (factory != null) {
//			if (factory == this) {
//				return enableSocket((SSLSocket) sslContext.getSocketFactory().createSocket());
//			} else {
//				return enableSocket((SSLSocket) factory.createSocket(params));
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
//					log.info("Initializing Keystore from [" + keyStore + "]");
//
//					try {
//						KeyMaterial km = new KeyMaterial(f, pwd.toCharArray());
//						ks = km.getKeyStore();
//					} catch (Exception e) {
//						SoapUI.logError(e);
//						pwd = null;
//					}
//				}
//			}
//
//			factory = new SoapUISSLSocketFactory(ks, pwd);
//			factoryMap.put(sslConfig, factory);
//
//			return enableSocket((SSLSocket) factory.createSocket(params));
//		} catch (Exception gse) {
//			return enableSocket((SSLSocket) super.createSocket(params));
//		}
	}

	/**
	 * @since 4.1
	 */
	@Override
	public Socket connectSocket(final Socket socket, final InetSocketAddress remoteAddress,
			final InetSocketAddress localAddress, final HttpParams params)
			throws IOException, UnknownHostException, ConnectTimeoutException {
		if (remoteAddress == null) {
			throw new IllegalArgumentException("Remote address may not be null");
		}
		if (params == null) {
			throw new IllegalArgumentException("HTTP parameters may not be null");
		}
		Socket sock = socket != null ? socket : new Socket();
		if (localAddress != null) {
			sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
			sock.bind(localAddress);
		}

		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);

		try {
			sock.setSoTimeout(soTimeout);
			sock.connect(remoteAddress, connTimeout);
		} catch (SocketTimeoutException ex) {
			throw new ConnectTimeoutException(
					"Connect to " + remoteAddress.getHostName() + "/" + remoteAddress.getAddress() + " timed out");
		}
		SSLSocket sslsock;
		// Setup SSL layering if necessary
		if (sock instanceof SSLSocket) {
			sslsock = (SSLSocket) sock;
		} else {
			sslsock = (SSLSocket) sslContext.getSocketFactory().createSocket(sock, remoteAddress.getHostName(),
					remoteAddress.getPort(), true);
			sslsock = enableSocket(sslsock);
		}
		// do we need it? trust all hosts
		// if( getHostnameVerifier() != null )
		// {
		// try
		// {
		// getHostnameVerifier().verify( remoteAddress.getHostName(), sslsock );
		// // verifyHostName() didn't blowup - good!
		// }
		// catch( IOException iox )
		// {
		// // close the socket before re-throwing the exception
		// try
		// {
		// sslsock.close();
		// }
		// catch( Exception x )
		// { /* ignore */
		// }
		// throw iox;
		// }
		// }
		return sslsock;
	}

	/**
	 * @since 4.1
	 */
	@Override
	public Socket createLayeredSocket(final Socket socket, final String host, final int port, final boolean autoClose)
			throws IOException, UnknownHostException {
		SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		sslSocket = enableSocket(sslSocket);
		// if( getHostnameVerifier() != null )
		// {
		// getHostnameVerifier().verify( host, sslSocket );
		// }
		// verifyHostName() didn't blowup - good!
		return sslSocket;
	}

}
