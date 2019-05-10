package org.phoenixctms.ctsms.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperandConfiguration {

	public enum Associativity {
		LEFT, RIGHT
	}

	private int precedence;
	private Associativity associativity;
	private List<ValueType> firstOperandTypes;
	private List<ValueType> secondOperandTypes;
	private ValueType resultType;

	public OperandConfiguration(int precedence, Associativity associativity,
			ValueType[] firstOperandTypes,
			ValueType[] secondOperandTypes, ValueType resultType) {
		this.precedence = precedence;
		this.associativity = associativity;
		this.firstOperandTypes = Arrays.asList(firstOperandTypes);
		this.secondOperandTypes = Arrays.asList(secondOperandTypes);
		this.resultType = resultType;
	}

	public boolean checkFirstOperandType(ValueType firstOperand) {
		if (firstOperandTypes == null) {
			firstOperandTypes = new ArrayList<ValueType>();
		}
		return firstOperandTypes.contains(firstOperand);
	};

	public boolean checkSecondOperandType(ValueType secondOperand) {
		if (secondOperandTypes == null) {
			secondOperandTypes = new ArrayList<ValueType>();
		}
		return secondOperandTypes.contains(secondOperand);
	}

	public Associativity getAssociativity() {
		return associativity;
	}

	public int getPrecedence() {
		return precedence;
	}

	public ValueType getResultType() {
		return resultType;
	}
}
