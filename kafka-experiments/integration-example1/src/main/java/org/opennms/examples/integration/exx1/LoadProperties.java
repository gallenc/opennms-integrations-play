package org.opennms.examples.integration.exx1;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadProperties {
	private static final Logger LOG = LoggerFactory.getLogger(LoadProperties.class);

	private static Properties properties = null;

	public static Properties load() {
		if (properties == null)
			synchronized (LoadProperties.class) {
				if (properties == null) try (final InputStream stream = (LoadProperties.class.getResourceAsStream("/kafkaclient.properties"))) {
					Properties propertiesx = new Properties();
					propertiesx.load(stream);
					properties = propertiesx;
				} catch (Exception ex) {
					LOG.error("cannot load properties: ", ex);
				}
			}
		return properties;

	}

}
