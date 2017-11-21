package pl.radomiej.formula;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.radomiej.formula.parser.FormulaFunctionParser;
import pl.radomiej.formula.parser.FormulaParser;
import pl.radomiej.formula.variables.FormulaNumber;

/**
 * Testowanie parsowania funkcji oraz ich dynamicznego tworzenia podczas pracy silnika
 * @author Praca
 *
 */
public class FormulaFunctionTest {

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
	public void testReturnWork() {
//		formulaEngine.addFunctionLine("TEST_fun","return('work ok')");
//		FormulaVariable result = formulaEngine.invoke("TEST_fun");
//		assertEquals("work ok", result.getValue());
	}

//	@Test
	public void testLastLineReturn() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test utworzenia surowej postaci funkcji i zparsowania jej przez dedykowany dla anonimowych funkcji parser
	 */
	@Test
	public void testRawFunction() {
		FormulaFunctionParser functionParser = new FormulaFunctionParser("_p := 'test OK'");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);
		
		assertEquals(FormulaVariableType.STRING, result.getType());
		assertEquals("test OK", result.getValue());
	}
	
	/**
	 * Test utworzenia i wykonania anonimowej funkcji. Anonimowe funkcje pozwalaj¹ na dynamiczne
	 * modyfikowanie kodu podczas jego wykonywania.
	 */
	@Test
	public void testAnonymousFunction() {
		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := \"i:=1\"()");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);
		
		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals("1.0", result.getValue().toString());
	}
	
	@Test
	public void testAnonymousFunctionLikeGlobalVariable() {
		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := \"i:=1\"");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);
		
		assertEquals(FormulaVariableType.FUNCTION, result.getType());
		assertEquals(FormulaFunction.class, result.getValue().getClass());
//		assertEquals("i:=1", result.getValue().toString());
		
		functionParser = new FormulaFunctionParser("p()");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);
		
		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals("1.0", result.getValue().toString());
	}
	
	@Test
	public void testFunctionWithArgumentsLetter() {
		final String firstArgumentText = "ArgumentTest";
		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := _a");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		anonymousFunction.addArgument(new FormulaVariable(firstArgumentText));
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);
		
		assertEquals(FormulaVariableType.STRING, result.getType());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals(firstArgumentText, result.getValue().toString());
	}
	
	@Test
	public void testFunctionWithArguments() {
		final String firstArgumentText = "ArgumentTest";
		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := _[1]");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		anonymousFunction.addArgument(new FormulaVariable(firstArgumentText));
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);
		
		assertEquals(FormulaVariableType.STRING, result.getType());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals(firstArgumentText, result.getValue().toString());
	}
	
	
}
