package pl.radomiej.formula.operations;

public enum FormulaOperationType {
	ATTRIBUTION(AttributionOperation.INSTANCE), 
	ADD(AddOperation.INSTANCE), 
	SUBTRACTION(SubtractionOperation.INSTANCE), 
	DIVIDE(DivideOperation.INSTANCE), 
	MULTIPLICATION(MultiplicationOperation.INSTANCE), 
	EQUALS(EqualsOperation.INSTANCE),
	NOT_EQUALS(NotEqualsOperation.INSTANCE), 
	LESS(LessOperation.INSTANCE), 
	MORE(MoreOperation.INSTANCE), 
	CONVERT_TO_STRING(ConvertToStringOperation.INSTANCE), 
	CONVERT_TO_NUMBER(ConvertToNumberOperation.INSTANCE), 
	AND(LogicOperation.INSTANCE), 
	OR(LogicOperation.INSTANCE), 
	REST_FROM_DIVISION(RestFromDivisionOperation.INSTANCE), 
	EQUALS_LESS(EqualsLessOperation.INSTANCE), 
	EQUALS_MORE(EqualsMoreOperation.INSTANCE), 
	ATTRIBUTION_ADD(AttributionAddOperation.INSTANCE), 
	ATTRIBUTION_SUBTRACTION(AttributionSubtractionOperation.INSTANCE), 
	ATTRIBUTION_MULTIPLICATION(AttributionMultiplicationOperation.INSTANCE), 
	ATTRIBUTION_DIVIDE(AttributionDivideOperation.INSTANCE), INSTRUCTION(InstructionOperation.INSTANCE), INVERT(InvertOperation.INSTANCE);

	private FormulaOperation operationExecuter;

	private FormulaOperationType(FormulaOperation operationExecuter) {
		this.operationExecuter = operationExecuter;
	}

	public FormulaOperation getOperationExecuter() {
		return operationExecuter;
	}
}
