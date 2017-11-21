package pl.radomiej.formula;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.radomiej.formula.parser.FormulaFunctionParser;
import pl.radomiej.formula.parser.FormulaParser;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaNumber;
import pl.radomiej.formula.variables.FormulaObject;

/**
 * Testowanie parsowania funkcji oraz ich dynamicznego tworzenia podczas pracy
 * silnika
 * 
 * @author Praca
 *
 */
public class FormulaNativeFunctionTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private FormulaEngine formulaEngine;

	@Before
	public void setUp() throws Exception {
		formulaEngine = new FormulaEngine();
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testFunctionWithReturn() {
		FormulaFunctionParser functionParser = new FormulaFunctionParser("return(1)");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), ((FormulaNumber) result.getValue()));
	}

//	@Test
	public void testFunctionReturnWithoutBracketAndReturnVoid() {
		FormulaFunctionParser functionParser = new FormulaFunctionParser("return");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.VOID, result.getType());
		assertNull(result.getValue());
	}

//	@Test
	public void testFunctionCreateArray() {
		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := obj_new(2)");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaArray.class, result.getValue().getClass());

		functionParser = new FormulaFunctionParser("p[1] := 'Test'");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("Test", result.getValue());

		functionParser = new FormulaFunctionParser("p[2] := 1234");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1234"), result.getValue());
	}

//	@Test
	public void testFunctionCreateObject() {
		// Utworzenie obiektu
		FormulaFunctionParser functionParser = new FormulaFunctionParser(
				"p := obj_new('fieldOne', 'Field2', 'Field3')");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.OBJECT, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaObject.class, result.getValue().getClass());

		// Przypisanie zmiennej do obiektu poprzez operator wy³uskania
		functionParser = new FormulaFunctionParser("p.fieldOne := 'TEST'");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("TEST", result.getValue());

		// Przypisanie zmiennej do obiektu przez dostêp tablicowy z kluczem
		// STRING
		functionParser = new FormulaFunctionParser("p['Field2'] := 1234");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1234"), result.getValue());

		// Przypisanie zmiennej do obiektu przez dostêp tablicowy 
		functionParser = new FormulaFunctionParser("p[3] := 12345678");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("12345678"), result.getValue());

		// Pobranie obiektu
		functionParser = new FormulaFunctionParser("p");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);
		assertEquals(FormulaVariableType.OBJECT, result.getType());
		assertNotNull(result.getValue());
	}
	
//	@Test
	public void testFunctionSplitText() throws FormulaException {
		FormulaFunctionParser functionParser = new FormulaFunctionParser(
				"p := spli_str('teraz;lubie;komputer;wertyk',';')");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.ARRAY, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaArray.class, result.getValue().getClass());
		
		FormulaArray array = (FormulaArray) result.getValue();
		assertEquals("teraz", array.getVariable(1).getValue());
		assertEquals("lubie", array.getVariable(2).getValue());
		assertEquals("komputer", array.getVariable(3).getValue());
		assertEquals("wertyk", array.getVariable(4).getValue());
		assertEquals(4, array.getSize());
	}
	@Test
	public void testFunctionConsole() {
		FormulaFunctionParser functionParser = new FormulaFunctionParser("console('WWW: ' + $1234)");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(new FormulaNumber("1"), result.getValue());
	}
}
