package org.phoenixctms.ctsms.query.parser;

import java.util.ArrayList;

import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;

public class CriterionSyntaxParser extends CriterionParser {

	private final static boolean DEBUG = false;
	private final static boolean RESOLVE_CRITERION_VALUES = true;

	public CriterionSyntaxParser() {
		super();
	}

	public String parseCriterions(ArrayList<CriterionInstantVO> sortedCriterions, boolean prettyPrint, boolean obfuscateCriterions) throws SyntaxException, ServiceException {
		ArrayList<CriterionInstantVO> tokens = unFoldTokens(sortedCriterions);
		String infixStyleExpression;
		if (prettyPrint) {
			infixStyleExpression = getInfixStyleExpressionPrettyString(tokens, getPositionDigits(tokens), RESOLVE_CRITERION_VALUES, obfuscateCriterions);
		} else {
			infixStyleExpression = getInfixStyleExpressionString(tokens, obfuscateCriterions);
		}
		if (DEBUG) {
			System.out.println("criterions infix expression: " + infixStyleExpression);
		}
		try {
			checkSyntax(tokens);
			ArrayList<CriterionInstantVO> RPNTokens = infixToRPN(tokens);
			if (DEBUG) {
				System.out.println("criterions RPN:");
				System.out.println(getRPNStyleExpressionString(RPNTokens, obfuscateCriterions));
			}
			if (RPNTokens.size() > 0) {
				RPNEval(RPNTokens, null);
				if (DEBUG) {
					System.out.println("OK");
				}
			} else {
				if (DEBUG) {
					System.out.println("emtpy expression");
				}
			}
		} catch (SyntaxException e) {
			if (DEBUG) {
				StringBuilder message = new StringBuilder(e.getError().toString());
				message.append(" ERROR");
				CriterionInstantVO criterion = (CriterionInstantVO) e.getToken();
				if (criterion != null) {
					message.append(": ");
					message.append(tokenToString(criterion));
				}
				System.out.println(message.toString());
			}
			throw e;
		}
		return infixStyleExpression;
	}
}
