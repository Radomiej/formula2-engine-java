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

import pl.radomiej.formula.operations.FormulaOperationType;
import pl.radomiej.formula.parser.FormulaFunctionParser;
import pl.radomiej.formula.parser.FormulaParser;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaDatetime;
import pl.radomiej.formula.variables.FormulaNumber;

/**
 * Testy na sprawdzenie zachowania siê operacji na tablicach
 * @author Radomiej
 *
 */
public class FormulaArraysOperationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddOperationBetweenNumberAndArray() throws URISyntaxException, IOException, FormulaException {
		URL url = this.getClass().getResource("/testArrays.fml");
		File myFile = new File(url.toURI());
		String content = FileUtils.readFileToString(myFile, "UTF-8"); // or any
																		// other
																		// encoding
		// TEST 1
		FormulaEngine engine = new FormulaEngine();
		FormulaParser parser = new FormulaParser(content, engine, "test");
		FormulaVariable result = engine.invokeFunction("test1@test");

		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertNotNull("Zwrócony obiekt powinien istnieæ", result.getValue());
		assertEquals("Zwrócony obiekt powinien byæ FORMULA ARRAY", FormulaArray.class, result.getValue().getClass());
		
		FormulaArray resutlArray = result.getValueLikeArray();
		assertEquals(2, resutlArray.getSize());
		assertEquals(new FormulaNumber("1"), resutlArray.getVariable(1).getValue());
		assertEquals(new FormulaNumber("2"), resutlArray.getVariable(2).getValue());
		
		// TEST 2
		result = engine.invokeFunction("test2@test");
		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertNotNull("Zwrócony obiekt powinien istnieæ", result.getValue());
		assertEquals("Zwrócony obiekt powinien byæ FORMULA ARRAY", FormulaArray.class, result.getValue().getClass());
		
		resutlArray = result.getValueLikeArray();
		assertEquals(2, resutlArray.getSize());
		assertEquals(new FormulaNumber("9"), resutlArray.getVariable(1).getValue());
		assertEquals(new FormulaNumber("10"), resutlArray.getVariable(2).getValue());
		
		
		// TEST 3
		result = engine.invokeFunction("test3@test");
		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertNotNull("Zwrócony obiekt powinien istnieæ", result.getValue());
		assertEquals("Zwrócony obiekt powinien byæ FORMULA ARRAY", FormulaArray.class, result.getValue().getClass());
		
		resutlArray = result.getValueLikeArray();
		assertEquals(8, resutlArray.getSize());
		assertEquals(new FormulaNumber("3"), resutlArray.getVariable(1).getValue());
		assertEquals(new FormulaNumber("4"), resutlArray.getVariable(2).getValue());
		assertEquals(new FormulaNumber("5"), resutlArray.getVariable(3).getValue());
		assertEquals(new FormulaNumber("6"), resutlArray.getVariable(4).getValue());
		assertEquals(new FormulaNumber("7"), resutlArray.getVariable(5).getValue());
		assertEquals(new FormulaNumber("8"), resutlArray.getVariable(6).getValue());
		assertEquals(new FormulaNumber("9"), resutlArray.getVariable(7).getValue());
		assertEquals(new FormulaNumber("10"), resutlArray.getVariable(8).getValue());
		// TEST 4
		result = engine.invokeFunction("test4@test");
		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertNotNull("Zwrócony obiekt powinien istnieæ", result.getValue());
		assertEquals("Zwrócony obiekt powinien byæ FORMULA ARRAY", FormulaArray.class, result.getValue().getClass());
		
		resutlArray = result.getValueLikeArray();
		assertEquals(8, resutlArray.getSize());
		assertEquals(new FormulaNumber("1"), resutlArray.getVariable(1).getValue());
		assertEquals(new FormulaNumber("2"), resutlArray.getVariable(2).getValue());
		assertEquals(new FormulaNumber("3"), resutlArray.getVariable(3).getValue());
		assertEquals(new FormulaNumber("4"), resutlArray.getVariable(4).getValue());
		assertEquals(new FormulaNumber("5"), resutlArray.getVariable(5).getValue());
		assertEquals(new FormulaNumber("6"), resutlArray.getVariable(6).getValue());
		assertEquals(new FormulaNumber("7"), resutlArray.getVariable(7).getValue());
		assertEquals(new FormulaNumber("8"), resutlArray.getVariable(8).getValue());
	}
		
}
