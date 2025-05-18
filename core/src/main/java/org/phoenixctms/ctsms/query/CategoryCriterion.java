package org.phoenixctms.ctsms.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class CategoryCriterion {

	public enum EmptyPrefixModes {
		NON_EMPTY_ROWS, //for an empty/null filter, return items with NOT empty/null category
		EMPTY_ROWS, //for an empty/null filter, return items with empty/null category
		ALL_ROWS //for an empty/null filter, return all items
	}

	public static void apply(Criteria criteria, CategoryCriterion categoryCriterion) {
		applyCategoryCriterion(criteria, categoryCriterion);
	}

	public static void applyAnd(Criteria criteria, CategoryCriterion... categoryCriterions) {
		applyCategoryCriterions(criteria, Restrictions.conjunction(), categoryCriterions);
	}
	//public static void applyAnd(Criteria criteria, List<CategoryCriterion> categoryCriterions) {
	//	applyAnd(criteria, categoryCriterions.toArray(new CategoryCriterion[0]));
	//}

	private static boolean applyCategoryCriterion(Object criteriaJunction, CategoryCriterion categoryCriterion) {
		Criterion restriction = getCategoryCriterionRestriction(categoryCriterion);
		if (restriction != null) {
			if (criteriaJunction instanceof Criteria) {
				((Criteria) criteriaJunction).add(restriction);
				return true;
			} else if (criteriaJunction instanceof Junction) {
				((Junction) criteriaJunction).add(restriction);
				return true;
			}
		}
		return false;
	}

	private static void applyCategoryCriterions(Criteria criteria, Junction junction, CategoryCriterion... categoryCriterions) {
		if (criteria != null && junction != null && categoryCriterions != null) {
			if (categoryCriterions.length > 0) {
				boolean applied = false;
				for (int i = 0; i < categoryCriterions.length; i++) {
					applied |= applyCategoryCriterion(junction, categoryCriterions[i]);
				}
				if (applied) {
					criteria.add(junction);
				}
			}
		}
	}

	public static void applyOr(Criteria criteria, CategoryCriterion... categoryCriterions) {
		applyCategoryCriterions(criteria, Restrictions.disjunction(), categoryCriterions);
	}
	//public static void applyOr(Criteria criteria, List<CategoryCriterion> categoryCriterions) {
	//	applyOr(criteria, categoryCriterions.toArray(new CategoryCriterion[0]));
	//}

	public static void applyAnd(Criteria criteria, List<CategoryCriterion>... categoryCriterions) {
		if (criteria != null && categoryCriterions != null && categoryCriterions != null) {
			Disjunction or = Restrictions.disjunction();
			boolean orApplied = false;
			for (int i = 0; i < categoryCriterions.length; i++) {
				Conjunction and = Restrictions.conjunction();
				boolean andApplied = false;
				for (int j = 0; j < categoryCriterions[i].size(); j++) {
					andApplied |= applyCategoryCriterion(and, categoryCriterions[i].get(j));
				}
				if (andApplied) {
					or.add(and);
					orApplied |= true;
				}
			}
			if (orApplied) {
				criteria.add(or);
			}
		}
	}

	public static void applyOr(Criteria criteria, List<CategoryCriterion>... categoryCriterions) {
		if (criteria != null && categoryCriterions != null && categoryCriterions != null) {
			Conjunction and = Restrictions.conjunction();
			boolean andApplied = false;
			for (int i = 0; i < categoryCriterions.length; i++) {
				Disjunction or = Restrictions.disjunction();
				boolean orApplied = false;
				for (int j = 0; j < categoryCriterions[i].size(); j++) {
					orApplied |= applyCategoryCriterion(or, categoryCriterions[i].get(j));
				}
				if (orApplied) {
					and.add(or);
					andApplied |= true;
				}
			}
			if (andApplied) {
				criteria.add(and);
			}
		}
	}

	public static Criterion getCategoryCriterionRestriction(CategoryCriterion categoryCriterion) {
		Criterion restriction = null;
		if (categoryCriterion.prefix != null && categoryCriterion.prefix.length() > 0) {
			if (categoryCriterion.caseSensitive) {
				restriction = Restrictions.like(categoryCriterion.field, categoryCriterion.prefix, categoryCriterion.matchMode);
			} else {
				restriction = Restrictions.ilike(categoryCriterion.field, categoryCriterion.prefix, categoryCriterion.matchMode);
			}
		} else if (EmptyPrefixModes.NON_EMPTY_ROWS.equals(categoryCriterion.emptyPrefixMode)) {
			restriction = Restrictions.not(Restrictions.or(Restrictions.eq(categoryCriterion.field, ""), Restrictions.isNull(categoryCriterion.field)));
		} else if (EmptyPrefixModes.EMPTY_ROWS.equals(categoryCriterion.emptyPrefixMode)) {
			restriction = Restrictions.or(Restrictions.eq(categoryCriterion.field, ""), Restrictions.isNull(categoryCriterion.field));
		}
		return restriction;
	}

	private String prefix;
	private String field;
	private MatchMode matchMode;
	private EmptyPrefixModes emptyPrefixMode;
	private boolean caseSensitive;

	public CategoryCriterion(String prefix, String field, MatchMode matchMode) {
		this(prefix, field, matchMode, EmptyPrefixModes.NON_EMPTY_ROWS);
	}

	public CategoryCriterion(String prefix, String field, MatchMode matchMode, EmptyPrefixModes emptyPrefixMode) {
		this(prefix, field, matchMode, emptyPrefixMode, false);
	}

	public CategoryCriterion(String prefix, String field, MatchMode matchMode,
			EmptyPrefixModes emptyPrefixMode, boolean caseSensitive) {
		this.prefix = prefix;
		this.field = field;
		this.matchMode = matchMode;
		this.emptyPrefixMode = emptyPrefixMode;
		this.caseSensitive = caseSensitive;
	}

	public Criterion getRestriction() {
		return getCategoryCriterionRestriction(this);
	}
}
