package it.balyfix;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



public class ShootHttps {

	private static final String URL = "https://api-3t.sandbox.paypal.com/nvp";

	public static void main(String[] args) throws Exception {

		TrustManager[] trustAllCerts = new TrustManager[] { fakeX509TrustManager() };

		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
				.getSocketFactory());

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Commentare se si vuole che durante handshake venga utilizzato il
		// meccasimo di verifica della veridcita dell' host
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		// Se per troubleshooting è necessario utilizzare una propia
		// sslConnection bisogna configurare la DelegateSSLSocket e costruirla
		// tramite la chiamata
		// al Wrap SSL
		HttpsURLConnection.setDefaultSSLSocketFactory(HttpsURLConnection
				.getDefaultSSLSocketFactory());

		URL url = new URL(URL);
		URLConnection con = url.openConnection();

		printCertificate((HttpsURLConnection) con);

		if (con.getInputStream().available() > 0) {
			System.out.println("Successfully connected");
		}

	}

	// fbalicchia No trust fake chain
	private static X509TrustManager fakeX509TrustManager() {
		return new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};
	}

	private static void printCertificate(HttpsURLConnection httpsURLConnection)
			throws IOException {

		System.out.println("responce code : "
				+ httpsURLConnection.getResponseCode());

		System.out.println("encoding : "
				+ httpsURLConnection.getContentEncoding());

		System.out.println("content type : "
				+ httpsURLConnection.getContentType());

		System.out.println(httpsURLConnection.getHeaderFields());
		System.out.println();

		Certificate[] localCertificates = httpsURLConnection
				.getServerCertificates();

		for (Certificate cert : localCertificates) {
			System.out.println("Cert Type : " + cert.getType());
			System.out.println("Cert Public Key Algorithm : "
					+ cert.getPublicKey().getAlgorithm());
			System.out.println("Cert Public Key Format : "
					+ cert.getPublicKey().getFormat());
			System.out.println("\n");

		}

	}

}