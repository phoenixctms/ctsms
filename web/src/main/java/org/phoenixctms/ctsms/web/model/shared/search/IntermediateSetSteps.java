package org.phoenixctms.ctsms.web.model.shared.search;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.enumeration.CriterionTie;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;
import org.phoenixctms.ctsms.vo.IntermediateSetDetailVO;
import org.phoenixctms.ctsms.vo.IntermediateSetSummaryVO;

/**
 * The set operations of a query as the intermediate sets analysis counts them: the single result sets (A, B, ...),
 * one step per ∪, ∩ or ∖ in evaluation order, and the total. The query agent's checkSets tool and the Intermediate Sets tab read the same steps.
 */
public class IntermediateSetSteps {

	public enum Warning {
		EMPTY_OPERAND,
		EMPTY_RESULT,
		ADDS_NOTHING,
		REMOVES_NOTHING,
		REMOVES_ALL
	}

	/**
	 * A remark on a step, about one of its operands (the operand's short name) or, if that is null, about its result.
	 */
	public static class StepWarning {

		private final Warning warning;
		private final String operand;

		private StepWarning(Warning warning, String operand) {
			this.warning = warning;
			this.operand = operand;
		}

		public Warning getWarning() {
			return warning;
		}

		public String getOperand() {
			return operand;
		}
	}

	/**
	 * One result set of the query, i.e. one SQL select: its label such as A, its count and its criteria.
	 */
	public static class ResultSet {

		private final String label;
		private final long count;
		private final String expression;
		private final Collection<CriterionInstantVO> criterions;

		private ResultSet(String label, long count, CriteriaInstantVO criteria) {
			this.label = label;
			this.count = count;
			this.expression = criteria.getCriterionExpression();
			this.criterions = criteria.getCriterions();
		}

		public String getLabel() {
			return label;
		}

		public long getCount() {
			return count;
		}

		public String getExpression() {
			return expression;
		}

		public Collection<CriterionInstantVO> getCriterions() {
			return criterions;
		}
	}

	/**
	 * One set operation: a tie b, with the counts of a, b, their intersection and the result.
	 */
	public static class Step {

		private final int number;
		// the position in the sets of the summary, which the chart iterates
		private final int index;
		private final CriterionTie tie;
		private final String tieName;
		private final String expression;
		private final String a;
		private final String b;
		private final long aCount;
		private final long bCount;
		private final long intersectionCount;
		private final long count;
		private final ArrayList<StepWarning> warnings = new ArrayList<StepWarning>();

		private Step(int number, int index, CriterionTie tie, String tieName, String expression, String a, String b, long aCount, long bCount, long intersectionCount,
				long count) {
			this.number = number;
			this.index = index;
			this.tie = tie;
			this.tieName = tieName;
			this.expression = expression;
			this.a = a;
			this.b = b;
			this.aCount = aCount;
			this.bCount = bCount;
			this.intersectionCount = intersectionCount;
			this.count = count;
			addWarnings();
		}

		private void addWarnings() {
			boolean emptyOperand = false;
			if (aCount == 0) {
				warnings.add(new StepWarning(Warning.EMPTY_OPERAND, a));
				emptyOperand = true;
			}
			if (bCount == 0) {
				warnings.add(new StepWarning(Warning.EMPTY_OPERAND, b));
				emptyOperand = true;
			}
			if (emptyOperand) {
				// an empty operand already explains the rest
				if (count == 0 && aCount + bCount > 0) {
					warnings.add(new StepWarning(Warning.EMPTY_RESULT, null));
				}
				return;
			}
			if (tie == null) {
				return;
			}
			switch (tie) {
				case UNION:
					if (intersectionCount == bCount) {
						warnings.add(new StepWarning(Warning.ADDS_NOTHING, b));
					} else if (intersectionCount == aCount) {
						warnings.add(new StepWarning(Warning.ADDS_NOTHING, a));
					}
					break;
				case INTERSECT:
					if (count == 0) {
						warnings.add(new StepWarning(Warning.EMPTY_RESULT, null));
					} else if (count == aCount) {
						warnings.add(new StepWarning(Warning.REMOVES_NOTHING, b));
					} else if (count == bCount) {
						warnings.add(new StepWarning(Warning.REMOVES_NOTHING, a));
					}
					break;
				case EXCEPT:
					if (intersectionCount == 0) {
						warnings.add(new StepWarning(Warning.REMOVES_NOTHING, b));
					} else if (intersectionCount >= aCount) {
						warnings.add(new StepWarning(Warning.REMOVES_ALL, b));
					}
					break;
				default:
					break;
			}
		}

		public int getNumber() {
			return number;
		}

		public int getIndex() {
			return index;
		}

		public CriterionTie getTie() {
			return tie;
		}

		/**
		 * The short form, such as step 3 ∪ D, when operands are earlier steps.
		 */
		public String getShortExpression(String stepReference) {
			return operandName(a, stepReference) + " " + tieName + " " + operandName(b, stepReference);
		}

		/**
		 * The whole set expression, such as ((A ∪ B) ∩ C).
		 */
		public String getExpression() {
			return expression;
		}

		public String getA() {
			return a;
		}

		public String getB() {
			return b;
		}

		public String getTieName() {
			return tieName;
		}

		public long getACount() {
			return aCount;
		}

		public long getBCount() {
			return bCount;
		}

		public long getIntersectionCount() {
			return intersectionCount;
		}

		public long getCount() {
			return count;
		}

		public List<StepWarning> getWarnings() {
			return Collections.unmodifiableList(warnings);
		}

		public boolean isWarning() {
			return !warnings.isEmpty();
		}
	}

	// an earlier step as operand is named by its number with this prefix
	private static final String STEP_OPERAND_PREFIX = "#";
	private final ArrayList<Step> steps = new ArrayList<Step>();
	private final LinkedHashMap<String, ResultSet> sets = new LinkedHashMap<String, ResultSet>();
	private String expression;
	private long count;

	public IntermediateSetSteps(IntermediateSetSummaryVO summary, Map<Long, CriterionTieVO> ties) {
		Collection<IntermediateSetDetailVO> details = summary == null ? null : summary.getSets();
		if (details == null) {
			return;
		}
		// the step whose result an operand is, by the operand's set expression
		HashMap<String, Integer> stepNumbers = new HashMap<String, Integer>();
		int index = 0;
		Iterator<IntermediateSetDetailVO> it = details.iterator();
		while (it.hasNext()) {
			IntermediateSetDetailVO detail = it.next();
			if (detail.getOperator() != null) {
				CriterionTieVO tieVO = ties == null ? null : ties.get(detail.getOperator().getTieId());
				String a = operand(detail.getA(), detail.getACount(), stepNumbers);
				String b = operand(detail.getB(), detail.getBCount(), stepNumbers);
				Step step = new Step(steps.size() + 1, index, tieVO == null ? null : tieVO.getTie(), tieVO == null ? "?" : tieVO.getName(),
						detail.getCriteria() == null ? "" : detail.getCriteria().getSetOperationExpression(), a, b, count(detail.getACount()), count(detail.getBCount()),
						count(detail.getIntersectionCount()), detail.getCriteriaCount());
				steps.add(step);
				if (detail.getCriteria() != null && detail.getCriteria().getSetOperationExpression() != null) {
					stepNumbers.put(detail.getCriteria().getSetOperationExpression(), step.getNumber());
				}
			} else {
				// the whole query, after the steps
				CriteriaInstantVO criteria = detail.getCriteria();
				expression = criteria == null ? null : criteria.getSetOperationExpression();
				count = detail.getCriteriaCount();
				if (steps.isEmpty() && criteria != null && !CommonUtil.isEmptyString(expression) && isSingle(criteria)) {
					sets.put(expression, new ResultSet(expression, count, criteria));
				}
			}
			index++;
		}
	}

	private static long count(Long count) {
		return count == null ? 0L : count.longValue();
	}

	// the name of an operand: the label of a single result set, or the number of the step it results from
	private String operand(CriteriaInstantVO criteria, Long count, Map<String, Integer> stepNumbers) {
		String expression = criteria == null ? null : criteria.getSetOperationExpression();
		if (expression == null) {
			return "?";
		}
		if (isSingle(criteria)) {
			if (!sets.containsKey(expression)) {
				sets.put(expression, new ResultSet(expression, count(count), criteria));
			}
			return expression;
		}
		Integer number = stepNumbers.get(expression);
		return number != null ? STEP_OPERAND_PREFIX + number : expression;
	}

	private static boolean isSingle(CriteriaInstantVO criteria) {
		Integer selectCount = criteria.getSelectStatementCount();
		if (selectCount != null) {
			return selectCount.intValue() <= 1;
		}
		// a single result set is just its label
		return criteria.getSetOperationExpression() != null && criteria.getSetOperationExpression().trim().indexOf(' ') < 0;
	}

	/**
	 * Names an earlier step as operand with the given pattern, such as "step {0}" or "Schritt {0}".
	 */
	public static String operandName(String operand, String stepReference) {
		if (operand != null && operand.startsWith(STEP_OPERAND_PREFIX) && stepReference != null) {
			return MessageFormat.format(stepReference, operand.substring(STEP_OPERAND_PREFIX.length()));
		}
		return operand;
	}

	public List<Step> getSteps() {
		return Collections.unmodifiableList(steps);
	}

	public Collection<ResultSet> getSets() {
		return Collections.unmodifiableCollection(sets.values());
	}

	/**
	 * The set expression of the whole query.
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * The record count of the whole query.
	 */
	public long getCount() {
		return count;
	}

	public boolean isWarning() {
		Iterator<Step> it = steps.iterator();
		while (it.hasNext()) {
			if (it.next().isWarning()) {
				return true;
			}
		}
		return false;
	}
}
