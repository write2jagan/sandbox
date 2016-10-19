package it.balyfix;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.balyfix.ssl.WrapSSlSocketFactory;

public class NewTest {

	private static Logger log = LoggerFactory.getLogger(NewTest.class);

	public static void main(String[] args) throws Exception {

		SchemeRegistry registry = new SchemeRegistry();
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream f = new FileInputStream("");
		keyStore.load(f, "".toCharArray());
		WrapSSlSocketFactory wrapSSlSocketFactory = new WrapSSlSocketFactory(keyStore, "");
		registry.register(new Scheme("https", 443, wrapSSlSocketFactory));
		ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(registry);

		DefaultHttpClient httplClient = null;

		try {

			httplClient = new DefaultHttpClient(connectionManager);

			BasicHttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost("");
			httpPost.addHeader("SOAPAction", "");
			httpPost.addHeader("Accept-Encoding", "gzip,deflate");
			FileEntity entity = new FileEntity(new File("/home/fbalicchia/Documents/app18/request.xml"),
					"text/xml;charset=UTF-8");
			httpPost.setEntity(entity);

			log.info("+++++++Header+++++");
			Header[] allHeaders = httpPost.getAllHeaders();

			for (Header header : allHeaders) {
				log.info(header.toString());
			}

			HttpResponse response = httplClient.execute(httpPost, httpContext);
			InputStream content = response.getEntity().getContent();
			String responseStr = IOUtils.toString(content, "UTF-8");
			log.info("-----------------Response-----------");
			log.info(responseStr);

		}

		catch (Exception e) {
			throw new IllegalArgumentException(e);
		} finally {
			httplClient.getConnectionManager().shutdown();
		}

	}

}
