package it.balyfix.gelf.logger.providers;

public abstract class AbstractGelfSenderProvider implements GelfSenderProvider{
	

	private static final String VALID_IPADDRESS_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
	private static final String VALID_HOSTNAME_REGEX = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";

	public String getCanonicalHost(String url) {
		
		String[] split = url.split(":");

		String ipAddress = split[1];

		if (!ipAddress.matches(VALID_IPADDRESS_REGEX)) {
			if (!ipAddress.matches(VALID_HOSTNAME_REGEX)) {
				if (ipAddress.contains("//")) {
					return ipAddress.split("//")[1];
				} else {
					throw new IllegalArgumentException();
				}
			}
		}

		return ipAddress;
	}
	

}
