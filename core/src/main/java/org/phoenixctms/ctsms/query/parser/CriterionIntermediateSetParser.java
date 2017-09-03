package org.phoenixctms.ctsms.query.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.phoenixctms.ctsms.domain.CriterionTie;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.IntermediateSetDetailVO;

public class CriterionIntermediateSetParser extends CriterionParser {

	private final static boolean RESOLVE_CRITERION_VALUES = true;

	private static HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, Long> createCriterionTieIdMap(
			CriterionTieDao criterionTieDao) {
		Collection<CriterionTie> criterionTies = criterionTieDao.loadAllSorted(0, 0);
		HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, Long> tieMap = new HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, Long>(criterionTies.size());
		Iterator<CriterionTie> criterionTieIt = criterionTies.iterator();
		while (criterionTieIt.hasNext()) {
			CriterionTie criterionTie = criterionTieIt.next();
			tieMap.put(criterionTie.getTie(), criterionTie.getId());
		}
		return tieMap;
	}

	private static Boolean getRPNEvalObfuscateCriterions(Object context) {
		return (Boolean) ((Map<String, Object>) context).get("obfuscate");
	}

	private static Integer getRPNEvalPositionDigits(Object context) {
		return (Integer) ((Map<String, Object>) context).get("digits");
	}

	private static Boolean getRPNEvalPrettyPrint(Object context) {
		return (Boolean) ((Map<String, Object>) context).get("pretty");
	}

	private static ArrayList<IntermediateSetDetailVO> getRPNEvalResult(Object context) {
		return (ArrayList<IntermediateSetDetailVO>) ((Map<String, Object>) context).get("result");
	}

	private static Stack<ArrayList<CriterionInstantVO>> getRPNEvalStack(Object context) {
		return (Stack<ArrayList<CriterionInstantVO>>) ((Map<String, Object>) context).get("stack");
	}

	private static void initRPNEvalContext(Object context, int positionDigits, boolean prettyCriterionExpressions, boolean obfuscateCriterions) {
		if (context != null) {
			((Map<String, Object>) context).put("stack", new Stack<ArrayList<CriterionInstantVO>>());
			((Map<String, Object>) context).put("result", new ArrayList<CriteriaInstantVO>());
			((Map<String, Object>) context).put("digits", positionDigits);
			((Map<String, Object>) context).put("pretty", prettyCriterionExpressions);
			((Map<String, Object>) context).put("obfuscate", obfuscateCriterions);
		}
	}

	private HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, Long> tieIdMap;


	public CriterionIntermediateSetParser() {
		super();
	}

	private CriteriaInstantVO createCriteriaInstantVO(ArrayList<CriterionInstantVO> criterions, int positionDigits, boolean prettyCriterionExpressions, boolean obfuscateCriterions)
			throws ServiceException {
		CriteriaInstantVO criteria = new CriteriaInstantVO();
		criteria.setCriterions(criterions);
		if (prettyCriterionExpressions) {
			criteria.setCriterionExpression(getInfixStyleExpressionPrettyString(criterions, positionDigits, RESOLVE_CRITERION_VALUES, obfuscateCriterions));
		} else {
			criteria.setCriterionExpression(getInfixStyleExpressionString(criterions, obfuscateCriterions));
		}
		criteria.setSetOperationExpression(getSetOperationExpressionString(criterions));
		return criteria;
	}

	private CriterionInstantVO createToken(org.phoenixctms.ctsms.enumeration.CriterionTie op, CriterionInstantVO neighbour) {
		CriterionInstantVO token = new CriterionInstantVO();
		token.setTieId(getTieIdMap().get(op));
		token.setPosition(neighbour.getPosition());
		token.setSelectStatementIndex(neighbour.getSelectStatementIndex());
		return token;
	}

	private int getSetOperatorCount(ArrayList<CriterionInstantVO> tokens) {
		int result = 0;
		if (tokens != null && tokens.size() > 0) {
			Iterator<CriterionInstantVO> it = tokens.iterator();
			while (it.hasNext()) {
				if (isSetOperator(it.next())) {
					result++;
				}
			}
		}
		return result;
	}

	private HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, Long> getTieIdMap() {
		if (tieIdMap == null) {
			tieIdMap = createCriterionTieIdMap(criterionTieDao);
		}
		return tieIdMap;
	}

	public ArrayList<IntermediateSetDetailVO> parseCriterions(ArrayList<CriterionInstantVO> sortedCriterions, boolean prettyPrint, boolean obfuscateCriterions)
			throws SyntaxException, ServiceException {
		ArrayList<CriterionInstantVO> tokens = unFoldTokens(sortedCriterions);
		Map<String, Object> context = new HashMap<String, Object>();
		initRPNEvalContext(context, getPositionDigits(tokens), prettyPrint, obfuscateCriterions);
		try {
			checkSyntax(tokens);
			ArrayList<CriterionInstantVO> RPNTokens = infixToRPN(tokens);
			if (RPNTokens.size() > 0) {
				RPNEval(RPNTokens, context);
			}
		} catch (SyntaxException e) {
			throw e;
		}
		return getRPNEvalResult(context);
	}

	protected void RPNEvalFinalize(ValueType resultType, Object context) throws ServiceException {
		if (context != null) {
			Stack<ArrayList<CriterionInstantVO>> stack = getRPNEvalStack(context);
			ArrayList<CriterionInstantVO> result = stack.pop();
			if (WhereTermValueType.VALUE_TYPE.equals(resultType)
					|| (SelectValueType.VALUE_TYPE.equals(resultType) && getSetOperatorCount(result) > 0)) {
				int positionDigits = getRPNEvalPositionDigits(context);
				boolean prettyCriterionExpressions = getRPNEvalPrettyPrint(context);
				boolean obfuscateCriterions = getRPNEvalObfuscateCriterions(context);
				IntermediateSetDetailVO setCriteria = new IntermediateSetDetailVO();
				setCriteria.setA(null);
				setCriteria.setB(null);
				setCriteria.setCriteria(createCriteriaInstantVO(result, positionDigits, prettyCriterionExpressions, obfuscateCriterions));
				setCriteria.setIntersection(null);
				setCriteria.setOperator(null);
				getRPNEvalResult(context).add(setCriteria);
			}
		}
	}

	protected void RPNEvalProcessOperator(CriterionInstantVO operator, ValueType firstOperandType, ValueType secondOperandType, Object context) throws ServiceException {
		if (context != null) {
			Stack<ArrayList<CriterionInstantVO>> stack = getRPNEvalStack(context);
			ArrayList<CriterionInstantVO> secondOperand = stack.pop();
			ArrayList<CriterionInstantVO> firstOperand = stack.pop();
			ArrayList<CriterionInstantVO> tokens = new ArrayList<CriterionInstantVO>(firstOperand.size() + secondOperand.size() + 5);
			if (isSetOperator(operator)) {
				int positionDigits = getRPNEvalPositionDigits(context);
				boolean prettyCriterionExpressions = getRPNEvalPrettyPrint(context);
				boolean obfuscateCriterions = getRPNEvalObfuscateCriterions(context);
				IntermediateSetDetailVO setCriteria = new IntermediateSetDetailVO();
				setCriteria.setA(createCriteriaInstantVO(firstOperand, positionDigits, prettyCriterionExpressions, obfuscateCriterions));
				setCriteria.setB(createCriteriaInstantVO(secondOperand, positionDigits, prettyCriterionExpressions, obfuscateCriterions));
				int aSelectCount = getSetOperatorCount(firstOperand);
				ArrayList<CriterionInstantVO> a = new ArrayList<CriterionInstantVO>(firstOperand.size() + 2);
				if (aSelectCount > 0) {
					a.add(createToken(org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS, firstOperand.get(0)));
				}
				a.addAll(firstOperand);
				if (aSelectCount > 0) {
					a.add(createToken(org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS, firstOperand.get(firstOperand.size() - 1)));
				}
				int bSelectCount = getSetOperatorCount(secondOperand);
				ArrayList<CriterionInstantVO> b = new ArrayList<CriterionInstantVO>(secondOperand.size() + 2);
				if (bSelectCount > 0) {
					b.add(createToken(org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS, secondOperand.get(0)));
				}
				b.addAll(secondOperand);
				if (bSelectCount > 0) {
					b.add(createToken(org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS, secondOperand.get(secondOperand.size() - 1)));
				}
				tokens.addAll(a);
				tokens.add(new CriterionInstantVO(operator));
				tokens.addAll(b);
				ArrayList<CriterionInstantVO> intersection = new ArrayList<CriterionInstantVO>(firstOperand.size() + secondOperand.size() + 5);
				intersection.addAll(a);
				intersection.add(createToken(org.phoenixctms.ctsms.enumeration.CriterionTie.INTERSECT, operator));
				intersection.addAll(b);
				setCriteria.setCriteria(createCriteriaInstantVO(tokens, positionDigits, prettyCriterionExpressions, obfuscateCriterions));
				setCriteria.setIntersection(createCriteriaInstantVO(intersection, positionDigits, prettyCriterionExpressions, obfuscateCriterions));
				setCriteria.setOperator(operator);
				getRPNEvalResult(context).add(setCriteria);
			} else {
				tokens.addAll(firstOperand);
				tokens.add(new CriterionInstantVO(operator));
				tokens.addAll(secondOperand);
			}
			stack.push(tokens);
		}
	}

	protected void RPNEvalProcessValue(CriterionInstantVO token, Object context) throws ServiceException {
		if (context != null) {
			ArrayList<CriterionInstantVO> tokens = new ArrayList<CriterionInstantVO>();
			tokens.add(token);
			getRPNEvalStack(context).push(tokens);
		}
	}
}