package pl.radomiej.formula;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.radomiej.formula.parser.FormulaParser;

public class FormulaParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/test.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8");  // or any other encoding
		
		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine);
	}

	@Test
	public void test2() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/testWithLogic.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8");  // or any other encoding
		
		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine);
	}
	
	@Test
	public void testIteration() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/testWithIteration.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8");  // or any other encoding
		
		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine);
	}
	
}
