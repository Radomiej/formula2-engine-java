package pl.radomiej.formula;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.radomiej.formula.operations.FormulaOperationType;
import pl.radomiej.formula.parser.FormulaParser;
import pl.radomiej.formula.variables.FormulaNumber;

public class FormulaMacroTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	 @Test
	public void testBasicMacro() {
		FormulaLine line1, line2, line3 = null;
		FormulaLeaf operation = null;
		FormulaMacro macro = null;

		line1 = new FormulaLine("p:='moc'");
		line2 = new FormulaLine("p:='moc'");
		line3 = new FormulaLine("p:='moc'");
		operation = line1.getOperationLeaf();
		System.out.println("operation: " + operation);

		macro = new FormulaMacro();
		macro.addInstruction(line1);
		macro.addInstruction(line2);
		macro.addInstruction(line3);

		// assertEquals("p:='moc'", parser.getOperationLeaf().getQuery());
		// assertEquals("p", parser.getOperationLeaf().getLeft().getQuery());
		// assertEquals("'moc'",
		// parser.getOperationLeaf().getRight().getQuery());

	}

	@Test
	public void testMacroWithParentMemory() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testMacroWithParentMemory.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8"); // or any
																		// other
																		// encoding

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "test");
		FormulaVariable result = engine.invokeFunction("test@test");

		assertEquals(FormulaVariableType.UNDEFINED, result.getType());
		assertNull("Obiekt z zagnie¿dzonego scope powinien byc UNDEFINED", result.getValue());

		result = engine.invokeFunction("test2@test");

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("Moc", result.getValue());
	}

	@Test
	public void testMacroWithLetterArguments() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testArguments.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8"); // or any
																		// other
																		// encoding

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "testArguments");

		List<FormulaVariable> args = new ArrayList<FormulaVariable>();
		args.add(new FormulaVariable("TestArg"));
		FormulaVariable result = engine.invokeFunction("test@testArguments", args);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("TestArg", result.getValue());
	}

	@Test
	public void testIteration() throws IOException, URISyntaxException {
		String fileName = "testWithIteration";
		String content = ResourceHelper.getFormulaScriptFromResource(fileName);

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, fileName);
		FormulaVariable result = null;

		result = engine.invokeFunction("test@" + fileName);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("100"), result.getValue());

		result = engine.invokeFunction("test2@" + fileName);
		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("100"), result.getValue());

		result = engine.invokeFunction("test3@" + fileName);
		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("51"), result.getValue());

		result = engine.invokeFunction("test4@" + fileName);
		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("10"), result.getValue());

		result = engine.invokeFunction("test5@" + fileName);
		assertEquals(FormulaVariableType.UNDEFINED, result.getType());
		assertNull(result.getValue());
		
		result = engine.invokeFunction("test6@" + fileName);
		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("100"), result.getValue());
	}
	
	@Test
	public void testBadIteration() throws IOException, URISyntaxException {
		String fileName = "testBadIteration";
		String content = ResourceHelper.getFormulaScriptFromResource(fileName);
		FormulaEngine engine = new FormulaEngine();

		try {
			FormulaParser parser = new FormulaParser(content, engine, fileName);
			fail("Powinien zostaæ zg³oszony wyj¹tek");
		} catch (UnsupportedOperationException ex) {}
		
	}
	
	@Test
	public void testIterationDynamicCounterChange() throws IOException, URISyntaxException {
		String fileName = "testWithIterationCounterChange";
		String content = ResourceHelper.getFormulaScriptFromResource(fileName);

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, fileName);
		FormulaVariable result = null;

		result = engine.invokeFunction("test@" + fileName);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("50"), result.getValue());

	}
}
