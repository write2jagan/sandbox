package it.balyfix;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.BasicHttpContext;

import it.balyfix.ssl.WrapSSlSocketFactory;

public class NewTest {

	public static void main(String[] args) throws Exception {

		SchemeRegistry registry = new SchemeRegistry();
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		
		WrapSSlSocketFactory wrapSSlSocketFactory = new WrapSSlSocketFactory(keyStore, "m3D0T4aM");
		registry.register(new Scheme("https", 443, wrapSSlSocketFactory));
		ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(registry);

		DefaultHttpClient httplClient = null;
		
		try {

			httplClient = new DefaultHttpClient(connectionManager);

			BasicHttpContext httpContext = new BasicHttpContext();
			
			httpPost.setEntity(entity);

			System.out.println(httpPost.toString());
			HttpResponse execute = httplClient.execute(httpPost, httpContext);

			System.out.println(execute.toString());

		} finally {
			httplClient.getConnectionManager().shutdown();
		}

	}

}
