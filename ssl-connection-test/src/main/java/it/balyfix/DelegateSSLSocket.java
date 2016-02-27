package it.balyfix;

import java.io.IOException;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class DelegateSSLSocket extends SSLSocket {

	private final SSLSocket delegate;
	private static final String[] ACCEPT_PROTOCOL = new String[]{"TLSv1.1","TLSv1.2"};

	public DelegateSSLSocket(SSLSocket delegate) {
		this.delegate = delegate;
	}

	@Override
	public void addHandshakeCompletedListener(
			HandshakeCompletedListener listener) {
		delegate.addHandshakeCompletedListener(listener);
	}

	@Override
	public boolean getEnableSessionCreation() {
		return delegate.getEnableSessionCreation();
	}

	@Override
	public String[] getEnabledCipherSuites() {

		return delegate.getEnabledCipherSuites();
	}

	@Override
	public String[] getEnabledProtocols() {
		return delegate.getEnabledProtocols();
	}

	@Override
	public boolean getNeedClientAuth() {
		return delegate.getNeedClientAuth();
	}

	@Override
	public SSLSession getSession() {
		return delegate.getSession();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return delegate.getSupportedCipherSuites();
	}

	@Override
	public String[] getSupportedProtocols() {
		return delegate.getSupportedProtocols();
	}

	@Override
	public boolean getUseClientMode() {
		return delegate.getUseClientMode();
	}

	@Override
	public boolean getWantClientAuth() {
		return delegate.getWantClientAuth();
	}

	@Override
	public void removeHandshakeCompletedListener(
			HandshakeCompletedListener listener) {
		delegate.removeHandshakeCompletedListener(listener);
	}

	@Override
	public void setEnableSessionCreation(boolean flag) {
		delegate.setEnableSessionCreation(flag);
	}

	@Override
	public void setEnabledCipherSuites(String[] suites) {
		delegate.setEnabledCipherSuites(suites);
	}

	@Override
	public void setEnabledProtocols(String[] protocols) {
		delegate.setEnabledProtocols(ACCEPT_PROTOCOL);
	}

	@Override
	public void setNeedClientAuth(boolean need) {
		delegate.setNeedClientAuth(need);
	}

	@Override
	public void setUseClientMode(boolean mode) {
		delegate.setUseClientMode(mode);

	}

	@Override
	public void setWantClientAuth(boolean want) {
		delegate.setWantClientAuth(want);

	}

	@Override
	public void startHandshake() throws IOException {
		delegate.startHandshake();

	}

}
