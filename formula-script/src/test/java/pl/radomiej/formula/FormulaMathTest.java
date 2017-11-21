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
public class FormulaMathTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testArthmeticsOperation() throws FormulaException {
		FormulaEngine engine = new FormulaEngine();
		FormulaFunction function = new FormulaFunction();
		FormulaLine parser = null;
		FormulaLeaf leaf = null;
		FormulaVariable result = null;

		parser = new FormulaLine("sin(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0.479425538604203"), result.getValue());

		parser = new FormulaLine("asin(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0.5235987755982989"), result.getValue());
		
		parser = new FormulaLine("sinh(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0.5210953054937474"), result.getValue());
		
		//COS		
		parser = new FormulaLine("cos(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0.8775825618903728"), result.getValue());

		parser = new FormulaLine("cosh(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1.1276259652063807"), result.getValue());
		
		//TAN
		parser = new FormulaLine("tan(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0.5463024898437905"), result.getValue());
		
		parser = new FormulaLine("atan(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0.4636476090008061"), result.getValue());

		parser = new FormulaLine("atan2(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("0.0"), result.getValue());
		
		parser = new FormulaLine("acos(0.5)");
		leaf = parser.getOperationLeaf();
		result = FormulaLeafHelper.getVariableFromLeaf(leaf, engine, function);

		assertEquals(FormulaVariableType.NUMBER, result.getType());
		assertNotNull(result.getValue());
		assertEquals(FormulaNumber.class, result.getValue().getClass());
		assertEquals(new FormulaNumber("1.0471975511965979"), result.getValue());
	
		
	}
}
