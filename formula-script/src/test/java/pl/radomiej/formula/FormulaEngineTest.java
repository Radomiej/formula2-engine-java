package pl.radomiej.formula;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.radomiej.formula.parser.FormulaFunctionParser;
import pl.radomiej.formula.parser.FormulaParser;
import pl.radomiej.formula.variables.FormulaDatetime;
import pl.radomiej.formula.variables.FormulaNumber;

public class FormulaEngineTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void testConsole() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/testConsole.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8"); // or any
																		// other
																		// encoding

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine);
		FormulaVariable result = engine.invokeFunction("test");
		System.out.println("result: " + result);
	}

	// @Test
	public void testLogic() throws IOException, URISyntaxException {
		String fileName = "testWithLogic";
		String content = ResourceHelper.getFormulaScriptFromResource(fileName);

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, fileName);
		FormulaVariable result = engine.invokeFunction("test@" + fileName);
		System.out.println("result: " + result);
	}

	// @Test
	public void testNumbers() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/testNumbers.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8"); // or any
																		// other
																		// encoding

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine);
		FormulaVariable result = engine.invokeFunction("test");
		System.out.println("result: " + result);
	}

	// @Test
	public void testAnd() throws IOException, URISyntaxException {
		String fileName = "testAnd";
		String content = ResourceHelper.getFormulaScriptFromResource(fileName);

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, fileName);
		FormulaVariable result = engine.invokeFunction("test@" + fileName);
		System.out.println("result: " + result);
	}

	@Test
	public void testDebuger() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/testDebuger.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8"); // or any
																		// other
																		// encoding

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine);
		FormulaVariable result = engine.invokeFunction("test@");
		System.out.println("result: " + result);
	}
}
