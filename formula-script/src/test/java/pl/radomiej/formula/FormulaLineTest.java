package pl.radomiej.formula;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.radomiej.formula.operations.FormulaOperationType;
import pl.radomiej.formula.parser.FormulaFunctionParser;
import pl.radomiej.formula.variables.FormulaDatetime;
import pl.radomiej.formula.variables.FormulaNumber;

/**
 * Test bazowej jednostki jêzyka. Pozwalaj¹cy na parsowanie pojedyñczej lini
 * kodu. Kod rozbijany jest na Leaf(liœcie) które tworz¹ graf wykoanania
 * poszczególnych instrukcji linii.
 * 
 * @author Radomiej
 *
 */
public class FormulaLineTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDivideLine() {
		FormulaLine parser = null;
		FormulaLeaf operation = null;

		parser = new FormulaLine("p:='moc'");
		operation = parser.getOperationLeaf();
		System.out.println("operation: " + operation);

		assertEquals("p:='moc'", parser.getOperationLeaf().getQuery());
		assertEquals("p", parser.getOperationLeaf().getLeft().getQuery());
		assertEquals("'moc'", parser.getOperationLeaf().getRight().getQuery());

		parser = new FormulaLine("p:='moc' + 'koc'");
		operation = parser.getOperationLeaf();
		System.out.println("operation: " + operation);

		assertEquals("p:='moc' + 'koc'", parser.getOperationLeaf().getQuery());
		assertEquals("p", parser.getOperationLeaf().getLeft().getQuery());
		assertEquals("'moc' + 'koc'", parser.getOperationLeaf().getRight().getQuery());
		assertEquals("'moc'", parser.getOperationLeaf().getRight().getLeft().getQuery());
		assertEquals("'koc'", parser.getOperationLeaf().getRight().getRight().getQuery());

		// parser = new FormulaLineParser("p:='moc' + 'koc' + 'test3'");
		// operation = parser.getOperation();
		// System.out.println("operation: " + operation);
		//
		parser = new FormulaLine("p:=2 * 6 + 3");
		operation = parser.getOperationLeaf();
		System.out.println("operation: " + operation);

		assertEquals("p:=2 * 6 + 3", parser.getOperationLeaf().getQuery());
		assertEquals("p", parser.getOperationLeaf().getLeft().getQuery());
		assertEquals("2 * 6 + 3", parser.getOperationLeaf().getRight().getQuery());
		assertEquals("2 * 6", parser.getOperationLeaf().getRight().getLeft().getQuery());
		assertEquals("3", parser.getOperationLeaf().getRight().getRight().getQuery());
		assertEquals("2", parser.getOperationLeaf().getRight().getLeft().getLeft().getQuery());
		assertEquals("6", parser.getOperationLeaf().getRight().getLeft().getRight().getQuery());

		parser = new FormulaLine("p:=2 * (6 + 3)");
		operation = parser.getOperationLeaf();
		System.out.println("operation: " + operation);

		assertEquals("p", parser.getOperationLeaf().getLeft().getQuery());
		assertEquals("2 * (6 + 3)", parser.getOperationLeaf().getRight().getQuery());
		assertEquals("2", parser.getOperationLeaf().getRight().getLeft().getQuery());
		assertEquals("6 + 3", parser.getOperationLeaf().getRight().getRight().getQuery());
		assertEquals("6", parser.getOperationLeaf().getRight().getRight().getLeft().getQuery());
		assertEquals("3", parser.getOperationLeaf().getRight().getRight().getRight().getQuery());

		parser = new FormulaLine("p:=-2");
		operation = parser.getOperationLeaf();
		System.out.println("operation: " + operation);

		assertEquals("p", parser.getOperationLeaf().getLeft().getQuery());
		assertEquals("-2", parser.getOperationLeaf().getRight().getQuery());
		assertEquals(FormulaOperationType.ATTRIBUTION, parser.getOperationLeaf().getType());

		parser = new FormulaLine("p:=-2 + sin(_b + _c)");
		operation = parser.getOperationLeaf();
		System.out.println("operation: " + operation);

		assertEquals("p", parser.getOperationLeaf().getLeft().getQuery());
		assertEquals("-2 + sin(_b + _c)", parser.getOperationLeaf().getRight().getQuery());
		assertEquals(FormulaOperationType.ATTRIBUTION, parser.getOperationLeaf().getType());

		assertEquals("-2", parser.getOperationLeaf().getRight().getLeft().getQuery());
		assertEquals("sin(_b + _c)", parser.getOperationLeaf().getRight().getRight().getQuery());
		assertEquals(FormulaOperationType.ADD, parser.getOperationLeaf().getRight().getType());
		assertEquals(true, parser.getOperationLeaf().getRight().getRight().isSimpleQuery());

	}

	@Test
	public void testInvokeLine() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;

		parser = new FormulaLine("p:='moc'");
		leaf = parser.getOperationLeaf();
		FormulaVariable leftVar = FormulaLeafHelper.getVariableFromLeaf(leaf.getLeft(), engine, function);
		FormulaVariable rightVar = FormulaLeafHelper.getVariableFromLeaf(leaf.getRight(), engine, function);

		FormulaVariable result = leaf.getType().getOperationExecuter().invoke(leftVar, rightVar);
		System.out.println("result: " + result);

		parser = new FormulaLine("p:=2");
		leaf = parser.getOperationLeaf();
		leftVar = FormulaLeafHelper.getVariableFromLeaf(leaf.getLeft(), engine, function);
		rightVar = FormulaLeafHelper.getVariableFromLeaf(leaf.getRight(), engine, function);

		result = leaf.getType().getOperationExecuter().invoke(leftVar, rightVar);
		System.out.println("result: " + result);
	}

	@Test
	public void testInvokeAnoymousFunction() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;

		parser = new FormulaLine("\"console('anonymous OK')\"()");
		leaf = parser.getOperationLeaf();
		FormulaVariable result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		System.out.println("result: " + result);

	}

	@Test
	public void testOperatorMore() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("1.0000000000000001 > 1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("1 > 1.0000000000000001");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("'b' > 'a'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("'a' > 'b'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

	}

	@Test
	public void testOperatorEqualsOrMore() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("1.0000000000000001 >= 1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("1 >= 1.0000000000000001");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("1 >= 1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("'b' >= 'a'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("'a' >= 'b'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("'a' >= 'a'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

	}

	@Test
	public void testOperatorLess() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("1.0000000000000001 < 1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("1 < 1.0000000000000001");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("'b' < 'a'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("'a' < 'b'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

	}

	@Test
	public void testOperatorEqualsOrLess() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("1.0000000000000001 <= 1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("1 <= 1.0000000000000001");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("1 <= 1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("'b' <= 'a'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("'a' <= 'b'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		parser = new FormulaLine("'a' <= 'a'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());
	}

	@Test
	public void testOperationAddAndRemoveOnString() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("1-'Test'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("est", result.getValue());

		parser = new FormulaLine("1+'Test'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("T", result.getValue());

		parser = new FormulaLine("'Test'+1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("t", result.getValue());

		parser = new FormulaLine("'Test'-1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("Tes", result.getValue());

		parser = new FormulaLine("5+'ABCDEFGHIJK'+1");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("E", result.getValue());
	}

	@Test
	public void testOperationMultiplication() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("2 * 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("4"), result.getValue());

		parser = new FormulaLine("2 * 'Test'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.STRING, result.getType());
		assertNotNull(result.getValue());
		assertEquals(String.class, result.getValue().getClass());
		assertEquals("TestTest", result.getValue());

		parser = new FormulaLine("'Test' * 'es'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("2"), result.getValue());

		parser = new FormulaLine("'Test' * 'NotFind'");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());
	}

	@Test
	public void testOperationDivision() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("2 / 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1"), result.getValue());

		try {
			parser = new FormulaLine("2 / 'Test'");
			leaf = parser.getOperationLeaf();
			result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);
			fail("Wyj¹tek niezgodnoœci typów powinien byæ zg³oszony");
		} catch (RuntimeException ex) {

		}

		parser = new FormulaLine("9 / 4");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("2.25"), result.getValue());

		parser = new FormulaLine("9 / 0");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.UNDEFINED, result.getType());
		assertNull(result.getValue());
	}

	@Test
	public void testArthmeticsOperation() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("2 + 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("4"), result.getValue());

		parser = new FormulaLine("2 + 2.004");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("4.004"), result.getValue());

		parser = new FormulaLine("2 - 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0"), result.getValue());

		parser = new FormulaLine("2 - 2.004");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("-0.004"), result.getValue());

		parser = new FormulaLine("2 * 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("4.0"), result.getValue());

		parser = new FormulaLine("2 * 2.004");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("4.008"), result.getValue());
	}

	@Test
	public void testAttributionMathOperations() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("result := 4");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("4"), result.getValue());

		parser = new FormulaLine("result *= 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("8.0"), result.getValue());

		parser = new FormulaLine("result /= 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("4"), result.getValue());

		parser = new FormulaLine("result += 2");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("6"), result.getValue());
		
		parser = new FormulaLine("result -= 4");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("2"), result.getValue());
	}
	
	@Test
	public void testGetArrayElementWithCustomLiteral() {
		FormulaEngine formulaEngine = new FormulaEngine();

		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := \"i:=1\"");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("a := obj_new(2)");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("a[1] := 1");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("a[p()]");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals("1.0", result.getValue().toString());
	}

	@Test
	public void testCustomOperationInLiteral() {
		FormulaEngine formulaEngine = new FormulaEngine();

		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := \"i:=1\"");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("a := obj_new(2)");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("a[1] := 1");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("a[p()]");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals("1.0", result.getValue().toString());
	}

	@Test
	public void testDatetime() {
		FormulaEngine formulaEngine = new FormulaEngine();

		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := datetime()");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.DATE_TIME, result.getType());
		assertEquals(FormulaDatetime.class, result.getValue().getClass());
		System.out.println("Current time: " + result.getValue().toString());
		// assertEquals("1", result.getValue().toString());

		functionParser = new FormulaFunctionParser("p := datetime(2017, 6, 20, 19, 16, 30)");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.DATE_TIME, result.getType());
		assertEquals(FormulaDatetime.class, result.getValue().getClass());
		System.out.println("Current time: " + result.getValue().toString());
	}

	@Test
	public void testGetArrayFromObjectPath() {
		FormulaEngine formulaEngine = new FormulaEngine();

		FormulaFunctionParser functionParser = new FormulaFunctionParser("p := obj_new()");
		FormulaFunction anonymousFunction = functionParser.getFunction();
		FormulaVariable result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("p.tab := obj_new(0)");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("array_put(p.tab, 10)");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		functionParser = new FormulaFunctionParser("_result := p.tab[1]");
		anonymousFunction = functionParser.getFunction();
		result = formulaEngine.invokeAnonymousFunction(anonymousFunction);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals("10.0", result.getValue().toString());
	}
}
