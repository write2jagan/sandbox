package it.balyfix;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class WrapSSLSocket {

	public static class WrapSSLSocketFactory extends SSLSocketFactory {

		private final SSLSocketFactory delegate;

		public WrapSSLSocketFactory() {
			delegate = HttpsURLConnection.getDefaultSSLSocketFactory();
		}

		private static Socket makeSocketFake(Socket socket) {
			if (socket instanceof SSLSocket) {
				socket = new DelegateSSLSocket(((SSLSocket) socket));
			}
			return socket;
		}

		@Override
		public Socket createSocket(Socket s, String host, int port,
				boolean autoClose) throws IOException {
			return null;
		}

		@Override
		public String[] getDefaultCipherSuites() {
			return delegate.getDefaultCipherSuites();
		}

		@Override
		public String[] getSupportedCipherSuites() {
			return delegate.getSupportedCipherSuites();
		}

		@Override
		public Socket createSocket(String host, int port) throws IOException,
				UnknownHostException {
			return makeSocketFake(delegate.createSocket(host, port));
		}

		@Override
		public Socket createSocket(InetAddress host, int port)
				throws IOException {
			return makeSocketFake(delegate.createSocket(host, port));
		}

		@Override
		public Socket createSocket(String host, int port,
				InetAddress localHost, int localPort) throws IOException,
				UnknownHostException {
			return makeSocketFake(delegate.createSocket());
		}

		@Override
		public Socket createSocket(InetAddress address, int port,
				InetAddress localAddress, int localPort) throws IOException {
			return makeSocketFake(delegate.createSocket(address, port,
					localAddress, localPort));
		}

	}

}
