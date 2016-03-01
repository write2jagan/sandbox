package it.balyfix.gelf.logger.commons;

import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import it.balyfix.gelf.logger.providers.AMQPProvider;
import it.balyfix.gelf.logger.providers.DefaultSenderProvider;
import it.balyfix.gelf.logger.providers.GelfSenderProvider;
import it.balyfix.gelf.logger.providers.KafkaProvider;
import it.balyfix.gelf.logger.sender.GelfSender;

public final class GelfSenderFactory {

	public static final GelfSender getSender(BaseHostHostProvider baseHostConf,
			Map<String, Object> specificSenderConfiguration, GelfLoggerErrorHandler errorHandler) {

		SenderConfiguration conf = new SenderConfiguration() {

			@Override
			public Integer getPort() {

				return baseHostConf.getPort();
			}

			@Override
			public String getHost() {
				return baseHostConf.getHost();
			}

			@Override
			public Map<String, Object> getExtraconfiguration() {
				return specificSenderConfiguration;
			}

			@Override
			public GelfLoggerErrorHandler getError() {
				return errorHandler;
			}

		};

		return createSender(conf);

	}

	public static GelfSender createSender(SenderConfiguration senderConfiguration) {
		GelfLoggerErrorHandler errorReporter = senderConfiguration.getError();
		if (senderConfiguration.getHost() == null) {
			senderConfiguration.getError().printError("GELF server hostname is empty!", null);
		} else {
			try {

				for (GelfSenderProvider provider : SenderProviderHolder.getSenderProvider()) {
					if (provider.supports(senderConfiguration.getHost())) {
						return provider.create(senderConfiguration);
					}
				}
				senderConfiguration.getError().printError("No sender found for host " + senderConfiguration.getHost(),
						null);
				return null;
			} catch (UnknownHostException e) {
				errorReporter.printError("Unknown GELF server hostname:" + senderConfiguration.getHost(), e);
			} catch (SocketException e) {
				errorReporter.printError("Socket exception: " + e.getMessage(), e);
			} catch (IOException | KeyManagementException | NoSuchAlgorithmException | URISyntaxException e) {
				errorReporter.printError("exception: " + e.getMessage(), e);
			}
		}

		return null;
	}

	public static void addGelfSenderProvider(GelfSenderProvider provider) {
		SenderProviderHolder.addSenderProvider(provider);
	}

	public static void removeGelfSenderProvider(GelfSenderProvider provider) {
		SenderProviderHolder.removeSenderProvider(provider);
	}

	public static void removeAllAddedSenderProviders() {
		SenderProviderHolder.removeAllAddedSenderProviders();
	}

	// For thread safe lazy intialization of provider list
	private static class SenderProviderHolder {

		private static ServiceLoader<GelfSenderProvider> gelfSenderProvider = ServiceLoader
				.load(GelfSenderProvider.class);

		private static List<GelfSenderProvider> providerList = new ArrayList<GelfSenderProvider>();

		private static List<GelfSenderProvider> addedProviders = new ArrayList<GelfSenderProvider>();

		static {
			Iterator<GelfSenderProvider> iter = gelfSenderProvider.iterator();
			while (iter.hasNext()) {
				providerList.add(iter.next());
			}
			providerList.add(new DefaultSenderProvider());
			providerList.add(new AMQPProvider());
			providerList.add(new KafkaProvider());
		}

		static List<GelfSenderProvider> getSenderProvider() {
			return providerList;
		}

		static void addSenderProvider(GelfSenderProvider provider) {
			synchronized (providerList) {
				addedProviders.add(provider);
				if (!providerList.contains(provider)) {
					providerList.add(0, provider); // To take precedence over
													// built-in providers
				}
			}
		}

		static void removeAllAddedSenderProviders() {
			synchronized (providerList) {
				providerList.removeAll(addedProviders);
				addedProviders.clear();
			}
		}

		static void removeSenderProvider(GelfSenderProvider provider) {
			synchronized (providerList) {
				addedProviders.remove(provider);
				providerList.remove(provider);
			}
		}
	}

}
