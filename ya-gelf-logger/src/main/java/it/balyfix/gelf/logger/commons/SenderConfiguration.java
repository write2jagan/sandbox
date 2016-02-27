package it.balyfix.gelf.logger.commons;

import java.util.Map;

public interface SenderConfiguration extends BaseHostHostProvider {
	public Map<String, Object> getExtraconfiguration();

	public GelfLoggerErrorHandler getError();

}
