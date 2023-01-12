package org.opennms.selenesepoller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.vmi.selenium.selenese.Main;
import jp.vmi.selenium.selenese.Runner;
import jp.vmi.selenium.selenese.config.DefaultConfig;
import jp.vmi.selenium.selenese.config.IConfig;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.result.Result.Level;
import jp.vmi.selenium.webdriver.WebDriverManager;

public class SelenesePoller {
	private static final Logger LOG = LoggerFactory.getLogger(SelenesePoller.class);


	public void runside(String args, String sidecontent) {

		Level exitLevel = Level.UNEXECUTED;
		try {

			// parse command line
			Main main = new Main();

			String[] splitArgs = Configuration.splitArgs(args);
			IConfig config = new DefaultConfig(splitArgs);
			
			LOG.info("config gekodriver:"+config.getGeckodriver());
			LOG.info("config firefox:"+config.getFirefox());
			// String[] filenames = config.getArgs();
			// if (filenames.length == 0)
			// help();
			// log.info("Start: " + PROG_TITLE + " {}", getVersion());
			Runner runner = new Runner();
			runner.setCommandLineArgs(splitArgs); // not needed no junit reesult
			main.setupRunner(runner, config, "test.side");

			InputStream is = new ByteArrayInputStream(sidecontent.getBytes(Charset.forName("UTF-8")));

			// file name needed with .side
			Result totalResult = runner.run("test.side", is);
			runner.finish();
			exitLevel = totalResult.getLevel();
		} catch (IllegalArgumentException e) {
			LOG.error("failed to run side command",e);
		} catch (Throwable t) {
			t.printStackTrace();
			exitLevel = Level.FATAL;
		} finally {
			WebDriverManager.quitDriversOnAllManagers();
		}
		// exit(exitLevel);
	}

}
