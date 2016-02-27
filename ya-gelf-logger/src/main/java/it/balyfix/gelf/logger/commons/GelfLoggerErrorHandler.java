package it.balyfix.gelf.logger.commons;

/**
 * Add an indirection level to handle error
 * 
 * @author fbalicchia
 * @version $Id: $
 */
public interface GelfLoggerErrorHandler {

	void printError(String msg, Exception ex);

}
