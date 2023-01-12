package org.opennms.selenesepoller.test;

import jp.vmi.selenium.runner.converter.Converter;
import jp.vmi.selenium.runner.model.side.SideFile;
import jp.vmi.selenium.runner.model.side.SideProject;
import jp.vmi.selenium.selenese.InvalidSeleneseException;
import jp.vmi.selenium.selenese.Main;
import jp.vmi.selenium.selenese.Parser;
import jp.vmi.selenium.selenese.Runner;
import jp.vmi.selenium.selenese.Selenese;
import jp.vmi.selenium.selenese.TestSuite;
import jp.vmi.selenium.selenese.command.CommandFactory;
import jp.vmi.selenium.selenese.command.ICommandFactory;
import jp.vmi.selenium.selenese.config.DefaultConfig;
import jp.vmi.selenium.selenese.config.IConfig;
import jp.vmi.selenium.selenese.inject.Binder;
import jp.vmi.selenium.selenese.log.CookieFilter;
import jp.vmi.selenium.selenese.log.CookieFilter.FilterType;
import jp.vmi.selenium.selenese.parser.ParserUtils;
import jp.vmi.selenium.selenese.log.LogFilter;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.result.Result.Level;
import jp.vmi.selenium.selenese.utils.CommandDumper;
import jp.vmi.selenium.selenese.utils.LangUtils;
import jp.vmi.selenium.selenese.utils.LoggerUtils;
import jp.vmi.selenium.webdriver.DriverOptions;
import jp.vmi.selenium.webdriver.DriverOptions.DriverOption;
import jp.vmi.selenium.webdriver.WebDriverManager;

import static jp.vmi.selenium.selenese.config.DefaultConfig.*;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.opennms.selenesepoller.Configuration;
import org.opennms.selenesepoller.SelenesePoller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;

/**
 * Unit test for simple App.
 */
public class SelenesePollerTest {
	private static final Logger LOG = LoggerFactory.getLogger(SelenesePollerTest.class);

	String args = "--geckodriver C:\\devel\\gitrepos\\selenize\\geckodriver-v0.32.0-win32\\geckodriver.exe  --firefox \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\"    selenizetest.side";

	String sidecontent = "{" + "  \"id\": \"94bcbc16-11f9-4634-9aad-1f0450eb68c8\"," + "  \"version\": \"2.0\","
			+ "  \"name\": \"tmp\"," + "  \"url\": \"https://www.bbc.co.uk\"," + "  \"tests\": [{"
			+ "    \"id\": \"16718ce9-e596-4223-87fd-32fc9be6a591\"," + "    \"name\": \"bbctest\","
			+ "    \"commands\": [{" + "      \"id\": \"b465a2e6-800c-4c50-a510-c72d8ffc0a52\","
			+ "      \"comment\": \"\"," + "      \"command\": \"open\"," + "      \"target\": \"/\","
			+ "      \"targets\": []," + "      \"value\": \"\"" + "    }, {"
			+ "      \"id\": \"7240cff3-0246-4a97-9305-25ea663da1b9\"," + "      \"comment\": \"\","
			+ "      \"command\": \"setWindowSize\"," + "      \"target\": \"1095x790\"," + "      \"targets\": [],"
			+ "      \"value\": \"\"" + "    }, {" + "      \"id\": \"5811074e-109b-45e9-8f73-336290c977c1\","
			+ "      \"comment\": \"\"," + "      \"command\": \"mouseOver\","
			+ "      \"target\": \"linkText=Search BBC\"," + "      \"targets\": ["
			+ "        [\"linkText=Search BBC\", \"linkText\"],"
			+ "        [\"css=.ssrcss-13qfcv5-NavigationLink-SearchLink\", \"css:finder\"],"
			+ "        [\"xpath=//header[@id='header-content']/nav/div/div/div[3]/div[2]/a\", \"xpath:idRelative\"],"
			+ "        [\"xpath=//a[contains(@href, '/search?d=HOMEPAGE_PS')]\", \"xpath:href\"],"
			+ "        [\"xpath=//div[2]/a\", \"xpath:position\"],"
			+ "        [\"xpath=//a[contains(.,'Search BBC')]\", \"xpath:innerText\"]" + "      ],"
			+ "      \"value\": \"\"" + "    }, {" + "      \"id\": \"30337b33-2470-43f1-8abe-70a7b730639b\","
			+ "      \"comment\": \"\"," + "      \"command\": \"mouseOut\","
			+ "      \"target\": \"linkText=Search BBC\"," + "      \"targets\": ["
			+ "        [\"linkText=Search BBC\", \"linkText\"],"
			+ "        [\"css=.ssrcss-13qfcv5-NavigationLink-SearchLink\", \"css:finder\"],"
			+ "        [\"xpath=//header[@id='header-content']/nav/div/div/div[3]/div[2]/a\", \"xpath:idRelative\"],"
			+ "        [\"xpath=//a[contains(@href, '/search?d=HOMEPAGE_PS')]\", \"xpath:href\"],"
			+ "        [\"xpath=//div[2]/a\", \"xpath:position\"],"
			+ "        [\"xpath=//a[contains(.,'Search BBC')]\", \"xpath:innerText\"]" + "      ],"
			+ "      \"value\": \"\"" + "    }]" + "  }]," + "  \"suites\": [{"
			+ "    \"id\": \"0c1cbefc-3969-4239-923c-62786174659c\"," + "    \"name\": \"Default Suite\","
			+ "    \"persistSession\": false," + "    \"parallel\": false," + "    \"timeout\": 300,"
			+ "    \"tests\": [\"16718ce9-e596-4223-87fd-32fc9be6a591\"]" + "  }],"
			+ "  \"urls\": [\"https://www.bbc.co.uk/\"]," + "  \"plugins\": []" + "}";

	/**
	 * test parsing command line into string array
	 */
	@Test
	public void stestcommandLineParsing() {
		LOG.debug("TEST COMMAND PARSING ");
		
		String command = "--geckodriver C:\\devel\\gitrepos\\selenize\\geckodriver-v0.32.0-win32\\geckodriver.exe  --firefox \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\"     selenizetest.side";
		String[] args = Configuration.splitArgs(command);
		for (String arg : args) {
			LOG.debug(arg);
		}
		LOG.debug("END TEST COMMAND PARSING ");

	}

	@Test
	public void testsidefileParsing() throws IOException {
		LOG.debug("STARTING TEST ");

		InputStream is = new ByteArrayInputStream(sidecontent.getBytes(Charset.forName("UTF-8")));

		String result = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
		LOG.debug("sidecontent : " + sidecontent);
		LOG.debug("final string: " + result);
		assertTrue(sidecontent.equals(result));

		is = new ByteArrayInputStream(sidecontent.getBytes(Charset.forName("UTF-8")));

		Reader r = new InputStreamReader(is, StandardCharsets.UTF_8);
		try {
			SideFile.parse("test.side", is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOG.debug("ENDING TEST ");
	}

	@Test
	public void testsiderunner() throws IOException {
		LOG.debug("STARTING side runner TEST ");

		Main main = new Main();
//
		String[] splitArgs = Configuration.splitArgs(args);
		IConfig config = new DefaultConfig(splitArgs);
		
		
//		
		LOG.info("config gekodriver:" + config.getGeckodriver());
		LOG.info("config firefox:" + config.getFirefox());
//

		Runner runner = new Runner();
//		
		LOG.debug("SETUP command args ");
		runner.setCommandLineArgs(splitArgs); // not needed no junit reesult
//		
		LOG.debug("SETUP RUNNER TEST ");
		main.setupRunner(runner, config, "test.side");
//
//		LOG.debug("SETUP RUNNER TEST end ");
		CommandFactory commandFactory = runner.getCommandFactory();
//
//		LOG.debug("RUNNING TEST ");
		InputStream is = new ByteArrayInputStream(sidecontent.getBytes(Charset.forName("UTF-8")));
		Selenese selenese = Parser.parse("test.side", is, commandFactory);

		LOG.debug("selenese:" + selenese);

		LOG.debug("selenese type:" + selenese.getType());
		TestSuite testSuite;

		switch (selenese.getType()) {
		case TEST_CASE:
			testSuite = Binder.newTestSuite("test.side", selenese.getName());
			testSuite.addSelenese(selenese);
			break;
		case TEST_PROJECT:
		case TEST_SUITE:
			testSuite = (TestSuite) selenese;
			break;
		default:
			// don't reach here.
			throw new RuntimeException("Unknown Selenese object: " + selenese);
		}

		testSuite.execute(null, runner);

		LOG.debug("ENDING side runner TEST ");
	}

	// @Test
	public void testRunningSide() {
		SelenesePoller spoller = new SelenesePoller();

		LOG.debug(sidecontent);

		spoller.runside(args, sidecontent);

	}
}
