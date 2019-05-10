package org.phoenixctms.ctsms.query.parser;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.query.parser.OperandConfiguration.Associativity;
import org.phoenixctms.ctsms.query.parser.SyntaxException.SyntaxErrors;

public abstract class ExpressionParser<T> { // Binary operator precedence parser

	// Shunting Yard expression parsing...
	// http://www.technical-recipes.com/2011/a-mathematical-expression-parser-in-java/#comment-946
	private enum TokenTypes {
		LEFT_PARENTHESIS, RIGHT_PARENTHESIS, OPERATOR, VALUE
	}

	// http://stackoverflow.com/questions/3291992/shunting-yard-missing-argument-to-operator?rq=1
	protected final void checkSyntax(ArrayList<T> inputTokens) throws SyntaxException {
		int leftParenthesisCount = 0;
		int rightParenthesisCount = 0;
		if (inputTokens != null) {
			TokenTypes previousToken = null;
			Iterator<T> it = inputTokens.iterator();
			while (it.hasNext()) {
				T token = it.next();
				if (token != null) {
					if (isLeftParenthesis(token)) {
						if (previousToken == null) {
							// OK
						} else if (TokenTypes.LEFT_PARENTHESIS.equals(previousToken)) {
							// OK
						} else if (TokenTypes.RIGHT_PARENTHESIS.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.LEFT_PARENTHESIS_AFTER_RIGHT_PARETHESIS, token);
						} else if (TokenTypes.OPERATOR.equals(previousToken)) {
							// OK
						} else if (TokenTypes.VALUE.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.LEFT_PARENTHESIS_AFTER_VALUE, token);
						}
						if (!it.hasNext()) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.LEFT_PARENTHESIS_AT_END, token);
						}
						leftParenthesisCount++;
						previousToken = TokenTypes.LEFT_PARENTHESIS;
					} else if (isRightParenthesis(token)) {
						if (previousToken == null) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.RIGHT_PARENTHESIS_AT_BEGIN, token);
						} else if (TokenTypes.LEFT_PARENTHESIS.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.RIGHT_PARENTHESIS_AFTER_LEFT_PARENTHESIS, token);
						} else if (TokenTypes.RIGHT_PARENTHESIS.equals(previousToken)) {
							// OK
						} else if (TokenTypes.OPERATOR.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.RIGHT_PARENTHESIS_AFTER_OPERATOR, token);
						} else if (TokenTypes.VALUE.equals(previousToken)) {
							// OK
						}
						if (!it.hasNext()) {
							// OK
						}
						rightParenthesisCount++;
						previousToken = TokenTypes.RIGHT_PARENTHESIS;
					} else if (isOperator(token)) {
						if (previousToken == null) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.OPERATOR_AT_BEGIN, token);
						} else if (TokenTypes.LEFT_PARENTHESIS.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.OPERATOR_AFTER_LEFT_PARENTHESIS, token);
						} else if (TokenTypes.RIGHT_PARENTHESIS.equals(previousToken)) {
							// OK
						} else if (TokenTypes.OPERATOR.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.OPERATOR_AFTER_OPERATOR, token);
						} else if (TokenTypes.VALUE.equals(previousToken)) {
							// OK
						}
						if (!it.hasNext()) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.OPERATOR_AT_END, token);
						}
						previousToken = TokenTypes.OPERATOR;
					} else {
						if (previousToken == null) {
							// OK
						} else if (TokenTypes.LEFT_PARENTHESIS.equals(previousToken)) {
							// OK
						} else if (TokenTypes.RIGHT_PARENTHESIS.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.VALUE_AFTER_RIGHT_PARENTHESIS, token);
						} else if (TokenTypes.OPERATOR.equals(previousToken)) {
							// OK
						} else if (TokenTypes.VALUE.equals(previousToken)) {
							// ERROR
							throw new SyntaxException(SyntaxErrors.VALUE_AFTER_VALUE, token);
						}
						if (!it.hasNext()) {
							// OK
						}
						previousToken = TokenTypes.VALUE;
					}
				}
			}
		}
		if (leftParenthesisCount != rightParenthesisCount) {
			throw new SyntaxException(SyntaxErrors.PARENTHESIS_MISSING); // no_operator message has no token/position associated
		}
	}

	private int cmpPrecedence(T operatorA, T operatorB) throws SyntaxException {
		if (!isOperator(operatorB)) {
			throw new SyntaxException(SyntaxErrors.NO_OPERATOR, operatorB);
		}
		if (!isOperator(operatorA)) {
			throw new SyntaxException(SyntaxErrors.NO_OPERATOR, operatorA);
		}
		return getOperandConfiguration(operatorA).getPrecedence() - getOperandConfiguration(operatorB).getPrecedence();
	}

	protected final String getInfixStyleExpressionString(ArrayList<T> inputTokens, Object... args) {
		StringBuilder result = new StringBuilder();
		if (inputTokens != null) {
			Iterator<T> it = inputTokens.iterator();
			while (it.hasNext()) {
				T token = it.next();
				if (token != null) {
					if (result.length() > 0) {
						result.append(" ");
					}
					if (isLeftParenthesis(token)) {
						result.append("(");
					} else if (isRightParenthesis(token)) {
						result.append(")");
					} else {
						result.append(tokenToString(token, args));
					}
				}
			}
		}
		return result.toString();
	}

	protected abstract OperandConfiguration getOperandConfiguration(T operator);

	protected final String getRPNStyleExpressionString(ArrayList<T> inputTokens, Object... args) {
		StringBuilder result = new StringBuilder();
		if (inputTokens != null) {
			Iterator<T> it = inputTokens.iterator();
			while (it.hasNext()) {
				T token = it.next();
				if (token != null) {
					if (result.length() > 0) {
						result.append("\n");
					}
					if (isLeftParenthesis(token)) {
						result.append("\t(");
					} else if (isRightParenthesis(token)) {
						result.append("\t)");
					} else {
						result.append("\t");
						result.append(tokenToString(token, args));
					}
				}
			}
		}
		return result.toString();
	}

	protected abstract ValueType getValueType(T operand);

	// Convert infix expression format into reverse Polish notation
	protected final ArrayList<T> infixToRPN(ArrayList<T> inputTokens)
			throws SyntaxException {
		ArrayList<T> out = new ArrayList<T>();
		Stack<T> stack = new Stack<T>();
		// For each tokens
		if (inputTokens != null) {
			Iterator<T> it = inputTokens.iterator();
			while (it.hasNext()) {
				T token = it.next();
				// If token is an operator
				if (isOperator(token)) {
					// While stack not empty AND stack top element
					// is an operator
					while (!stack.empty() && isOperator(stack.peek())) {
						if ((isAssociative(token,
								OperandConfiguration.Associativity.LEFT)
								&& cmpPrecedence(
										token, stack.peek()) <= 0)
								|| (isAssociative(
										token,
										OperandConfiguration.Associativity.RIGHT)
										&& cmpPrecedence(
												token, stack.peek()) < 0)) {
							out.add(stack.pop());
							continue;
						}
						break;
					}
					// Push the new operator on the stack
					stack.push(token);
				}
				// If token is a left bracket '('
				else if (isLeftParenthesis(token)) {
					stack.push(token); //
				}
				// If token is a right bracket ')'
				else if (isRightParenthesis(token)) {
					while (!stack.empty()
							&& !isLeftParenthesis(stack.peek())) {
						out.add(stack.pop());
					}
					try {
						stack.pop();
					} catch (EmptyStackException e) {
						throw new SyntaxException(SyntaxErrors.MISSING_LEFT_PARENTHESIS, token, e);
					}
				}
				// If token is a number
				else {
					out.add(token);
				}
			}
		}
		while (!stack.empty()) {
			out.add(stack.pop());
		}
		return out;
	}

	private boolean isAssociative(T operator, Associativity associativity) throws SyntaxException {
		if (!isOperator(operator)) {
			throw new SyntaxException(SyntaxErrors.NO_OPERATOR, operator);
		}
		if (getOperandConfiguration(operator).getAssociativity().equals(associativity)) {
			return true;
		}
		return false;
	}

	protected abstract boolean isLeftParenthesis(T token);

	protected abstract boolean isOperator(T token);

	protected abstract boolean isRightParenthesis(T token);

	protected boolean isValue(T token) {
		return !(isOperator(token) || isLeftParenthesis(token) || isRightParenthesis(token));
	}

	protected final void RPNEval(ArrayList<T> RPNtokens, Object context)
			throws SyntaxException, ServiceException {
		// For each tokens
		if (RPNtokens != null) {
			Stack<ValueType> stack = new Stack<ValueType>();
			Iterator<T> it = RPNtokens.iterator();
			while (it.hasNext()) {
				T token = it.next();
				try {
					// If the token is a value push it onto the stack
					if (!isOperator(token)) {
						stack.push(getValueType(token));
						RPNEvalProcessValue(token, context);
					} else {
						// Token is an operator: pop top two entries
						ValueType secondOperand = stack.pop();
						ValueType firstOperand = stack.pop();
						OperandConfiguration configuration = getOperandConfiguration(token);
						if (!configuration.checkFirstOperandType(firstOperand)) {
							throw new SyntaxException(
									SyntaxErrors.INCOMPATIBLE_FIRST_OPERAND_TYPE,
									token);
						}
						if (!configuration
								.checkSecondOperandType(secondOperand)) {
							throw new SyntaxException(
									SyntaxErrors.INCOMPATIBLE_SECOND_OPERAND_TYPE,
									token);
						}
						stack.push(configuration.getResultType());
						RPNEvalProcessOperator(token, firstOperand, secondOperand, context);
					}
				} catch (EmptyStackException e) {
					throw new SyntaxException(SyntaxErrors.MISSING_OPERAND,
							token, e);
				}
			}
			try {
				RPNEvalFinalize(stack.pop(), context);
			} catch (EmptyStackException e) {
				throw new SyntaxException(SyntaxErrors.MISSING_OPERATOR, e);
			}
			if (!stack.isEmpty()) {
				throw new SyntaxException(SyntaxErrors.MISSING_OPERATOR);
			}
		}
	}

	protected void RPNEvalFinalize(ValueType resultType, Object context) throws ServiceException {
	}

	protected void RPNEvalProcessOperator(T operator, ValueType firstOperandType, ValueType secondOperandType, Object context) throws ServiceException {
	}

	protected void RPNEvalProcessValue(T token, Object context) throws ServiceException {
	}

	protected abstract String tokenToString(T token, Object... args);

	protected ArrayList<T> unFoldTokens(ArrayList<T> inputTokens) {
		ArrayList<T> unfolded;
		if (inputTokens != null && inputTokens.size() > 0) {
			unfolded = new ArrayList<T>(inputTokens.size());
			Iterator<T> it = inputTokens.iterator();
			while (it.hasNext()) {
				T token = it.next();
				if (token != null) {
					unfolded.add(token);
				}
			}
		} else {
			unfolded = new ArrayList<T>();
		}
		return unfolded;
	}
}
