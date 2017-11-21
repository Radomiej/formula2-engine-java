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
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaNumber;
import pl.radomiej.formula.variables.FormulaObject;

public class FormulaShortObjectCreatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBrecketClamra() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testShortObjectCreator.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "test");
		FormulaVariable result = engine.invokeFunction("test1@test");
		
		System.out.println("result: " + result);
//		assertEquals(FormulaVariableType.NUMBER, result.getType());
//		assertEquals(FormulaNumber.class, result.getValue().getClass());
//		assertEquals(new FormulaNumber("1"), result.getValue());
	}
	
	@Test
	public void testShortestConstructorInNativeMethod() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testShortObjectCreator.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "test");
		FormulaVariable result = engine.invokeFunction("test2@test");
		
		System.out.println("result: " + result);
//		assertEquals(FormulaVariableType.NUMBER, result.getType());
//		assertEquals(FormulaNumber.class, result.getValue().getClass());
//		assertEquals(new FormulaNumber("1"), result.getValue());
	}
	
	@Test
	public void testShortestConstructorWithSpecialCharaters() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testShortObjectCreator.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "test");
		FormulaVariable result = engine.invokeFunction("test3@test");
		
		assertEquals(FormulaVariableType.STRING, result.getType());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("!@#$%^&*()_+/[]{}<>?\\|", result.getValue());
	}
	
	@Test
	public void testShortestConstructorWithSpecialCharatersProblematicExample() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testShortObjectCreator.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "test");
		FormulaVariable result = engine.invokeFunction("test4@test");
		
		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertEquals(FormulaArray.class, result.getValue().getClass());
		
		FormulaArray resultArray = result.getValueLikeArray();
		
		FormulaVariable one = resultArray.getElements().get(0);		
		assertEquals(FormulaVariableType.STRING, one.getType());
		assertEquals(String.class, one.getValue().getClass());
		assertEquals("*.png", one.getValue());
		
		FormulaVariable two = resultArray.getElements().get(1);		
		assertEquals(FormulaVariableType.STRING, two.getType());
		assertEquals(String.class, two.getValue().getClass());
		assertEquals("*.jpg", two.getValue());
		
		FormulaVariable three = resultArray.getElements().get(2);		
		assertEquals(FormulaVariableType.STRING, three.getType());
		assertEquals(String.class, three.getValue().getClass());
		assertEquals("*.jpng", three.getValue());
	}

	@Test
	public void testShortestConstructorArrayWithSpecialCharatersProblematicExample() throws URISyntaxException, IOException {
		URL url = this.getClass().getResource("/testShortObjectCreator.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, StandardCharsets.UTF_8); // or

		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "test");
		FormulaVariable result = engine.invokeFunction("test5@test");
		
		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertEquals(FormulaArray.class, result.getValue().getClass());
		
		FormulaArray resultArray = result.getValueLikeArray();
		
		FormulaVariable one = resultArray.getElements().get(0);		
		assertEquals(FormulaVariableType.OBJECT, one.getType());
		assertEquals(FormulaObject.class, one.getValue().getClass());
		
		FormulaVariable two = resultArray.getElements().get(1);		
		assertEquals(FormulaVariableType.OBJECT, two.getType());
		assertEquals(FormulaObject.class, two.getValue().getClass());
		
		FormulaVariable three = resultArray.getElements().get(2);		
		assertEquals(FormulaVariableType.OBJECT, three.getType());
		assertEquals(FormulaObject.class, three.getValue().getClass());
	}
}
