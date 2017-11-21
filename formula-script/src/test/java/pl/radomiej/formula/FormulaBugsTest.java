package pl.radomiej.formula;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.radomiej.formula.operations.FormulaOperationType;
import pl.radomiej.formula.parser.FormulaParser;
import pl.radomiej.formula.variables.FormulaNumber;

public class FormulaBugsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBugs1() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testBugs.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "testBugs");
		FormulaVariable result = engine.invokeFunction("testBug1@testBugs");
		
		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());
	}
	
	@Test
	public void testBugs2() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testBugs.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "testBugs");
		FormulaVariable result = engine.invokeFunction("testBug2@testBugs");

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());
	}
	
	@Test
	public void testBugs3() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testBugs.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "testBugs");
		FormulaVariable result = engine.invokeFunction("testBug3@testBugs");

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());
	}
}
