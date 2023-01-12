package org.opennms.selenesepoller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.vmi.selenium.selenese.config.DefaultConfig;
import jp.vmi.selenium.selenese.config.IConfig;

public class Configuration {

	private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

	// these commands match documentation at
	// https://github.com/vmi/selenese-runner-java for seleneze runner 4.2.0
	// but not all options have a default
	// --driver (-d) <driver> : firefox (default) | chrome | ie | edge | safari |
	// htmlunit | remote | appium | FQCN-of-WebDriverFactory
	private String driver = "firefox";

	// --headless : use headless mode if driver is supported (currently, Chrome and
	// Firefox)
	private boolean headless = false;

	// --proxy-type <proxy-type> : proxy type (manual (default if set --proxy) | pac
	// | autodetect | system)
	private String proxyType = null;

	// --proxy <proxy> : [manual] proxy host and port (HOST:PORT) (excepting IE) /
	// [pac] PAC URL
	private String proxy = null;

	// --proxy-user <user> : proxy username (HtmlUnit only *2)
	// --proxy-password <password> : proxy password (HtmlUnit only *2)
	// --no-proxy <hosts> : no-proxy hosts
	// --cli-args <arg> : add command line arguments at starting up driver
	// (multiple)

	// --firefox <path> : path to 'firefox' binary. (implies '--driver firefox')
	private String firefox = null;

	// --geckodriver <path> : path to 'geckodriver' binary. (implies '--driver
	// firefox')
	private String geckodriver = null;

	// --chromedriver <path> : path to 'chromedriver' binary. (implies '--driver
	// chrome')
	private String chromedriver = null;

	// --timeout (-t) <timeout> : set timeout (ms) for waiting. (default: 30000 ms)
	private String timeout = "30000";
	
	public Configuration() {
		// will pick up default values at startup
		geckodriver = System.getProperty("opennms.selenium.geckodriver");
		chromedriver = System.getProperty("opennms.selenium.chromedriver");
		firefox = System.getProperty("opennms.selenium.firefox");
	}

	/**
	 * 
	 * @return returns the default configuration
	 */
	private DefaultConfig getDefaultConfiguration() {
		// to create parser
		DefaultConfig config = new DefaultConfig("--timeout=30000");

		if (driver != null)
			config.setDriver(driver);
		
		config.setHeadless(headless);

		if (proxyType != null)
			config.setProxyType(proxyType);

		if (proxy != null)
			config.setProxy(proxy);

		if (firefox != null)
			config.setFirefox(firefox);

		if (geckodriver != null)
			config.setGeckodriver(geckodriver);

		if (chromedriver != null)
			config.setChromedriver(chromedriver);

		if (timeout != null)
			config.setTimeout(timeout);

		return config;
	}
	
	public IConfig getConfiguration() {
		return  getDefaultConfiguration();
	}

	/**
	 * 
	 * @param command additional arguments specified in the same command string used
	 *             for selenese-runner. note that not all arguments are applicable
	 *             https://github.com/vmi/selenese-runner-java for seleneze runner
	 *             4.2.0
	 * @return returns the default configuration with any canges or ammendments
	 *         overlayed from args string
	 */
	public IConfig getConfiguration(String command) {
		
		DefaultConfig config = getDefaultConfiguration();
		
		String[] splitArgs = Configuration.splitArgs(command);
		config.parseCommandLine(splitArgs);

		return config;
	}



	/**
	 * splits command string around spaces except if in quotes
	 * 
	 * @param cmd
	 * @return separate command components
	 */
	public static String[] splitArgs(String cmd) {
		List<String> listCmd = new ArrayList<String>();
		// see
		// https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cmd);
		while (m.find())
			listCmd.add(m.group(1).replace("\"", "").trim());

		String[] arr = new String[listCmd.size()];
		return listCmd.toArray(arr);
	}

}
