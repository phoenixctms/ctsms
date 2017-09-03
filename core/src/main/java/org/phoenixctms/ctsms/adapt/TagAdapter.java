package org.phoenixctms.ctsms.adapt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;

public abstract class TagAdapter<ROOT, TAG, TAGVALUE, VO> {

	protected abstract ROOT checkRootId(Long rootId) throws ServiceException;

	protected abstract void checkRootTagConstraints(ROOT root, TAG tag, VO tagValueIn) throws ServiceException;

	protected abstract TAG checkTagId(Long tagId) throws ServiceException;

	public final void checkTagValueInput(VO tagValueIn) throws ServiceException {
		// referential checks
		ROOT root = checkRootId(getRootId(tagValueIn));
		TAG tag = checkTagId(getTagId(tagValueIn));
		// other input checks
		if (root != null && tag != null) {
			checkRootTagConstraints(root, tag, tagValueIn);
			Iterator<TAGVALUE> it = getTagValues(root).iterator();
			int tagCount = 0;
			while (it.hasNext()) {
				TAGVALUE tagValue = it.next();
				if (tag.equals(getTag(tagValue)) &&
						!same(tagValue, tagValueIn)) {
					tagCount++;
				}
			}
			Integer maxOccurence = getMaxOccurrence(tag);
			if (maxOccurence != null && maxOccurence >= 0 && tagCount >= maxOccurence) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TAG_MAX_NUMBER_EXCEEDED, getNameL10nKey(tag));
			}
			if (checkValueAgainstRegExp(tagValueIn)) {
				String regExp = getRegExp(tag);
				if (regExp != null && regExp.length() > 0) {
					java.util.regex.Pattern tagValuePattern = null;
					try {
						tagValuePattern = Pattern.compile(regExp);
					} catch (PatternSyntaxException e) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.TAG_INVALID_REGEXP_PATTERN, getNameL10nKey(tag), e.getMessage());
					}
					if (tagValuePattern != null && !tagValuePattern.matcher(getValue(tagValueIn)).matches()) {
						String mismatchMsgL10nKey = getMismatchL10nKey(tag);
						if (mismatchMsgL10nKey != null && mismatchMsgL10nKey.length() > 0) {
							throw L10nUtil.initServiceException(mismatchMsgL10nKey, getValue(tagValueIn), regExp);
						} else {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.TAG_VALUE_INVALID, getValue(tagValueIn), regExp);
						}
					}
				}
			}
		}
	}

	protected abstract boolean checkValueAgainstRegExp(VO tagValueIn);

	protected abstract Collection<TAG> findTagsIncludingId(ROOT root, Long tagId);

	public final Collection getAvailableTags(Long rootId, Long tagId) throws Exception {
		Collection tags = null;
		if (rootId != null) {
			ROOT root = checkRootId(rootId);
			tags = new ArrayList<TAG>();
			if (root != null) {
				TAG appendTag = null;
				if (tagId != null) {
					appendTag = checkTagId(tagId);
				}
				Collection<TAGVALUE> tagValues = getTagValues(root);
				Iterator<TAG> tagIt = findTagsIncludingId(root, tagId).iterator();
				HashMap<TAG, Integer> counts = new HashMap<TAG, Integer>();
				while (tagIt.hasNext()) {
					TAG tag = tagIt.next();
					counts.put(tag, 0);
					Iterator<TAGVALUE> tagValuesIt = tagValues.iterator();
					while (tagValuesIt.hasNext()) {
						if (tag.equals(getTag(tagValuesIt.next()))) {
							counts.put(tag, counts.get(tag) + 1);
						}
					}
				}
				Iterator<Map.Entry<TAG, Integer>> it = counts.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<TAG, Integer> count = it.next();
					TAG tag = count.getKey();
					Integer maxOccurrence = getMaxOccurrence(tag);
					if (maxOccurrence == null || maxOccurrence < 0 || count.getValue() < maxOccurrence || (appendTag != null && tag.equals(appendTag))) {
						tags.add(count.getKey());
					}
				}
			}
		} else {
			if (tagId != null) {
				checkTagId(tagId);
			}
			tags = findTagsIncludingId(null, tagId);
		}
		toTagVOCollection(tags);
		return tags;
	}

	protected abstract Integer getMaxOccurrence(TAG tag);

	protected abstract String getMismatchL10nKey(TAG tag);

	protected abstract String getNameL10nKey(TAG tag);

	protected abstract String getRegExp(TAG tag);

	protected abstract Long getRootId(VO tagValueIn);

	protected abstract TAG getTag(TAGVALUE tagValue);

	protected abstract Long getTagId(VO tagValueIn);

	protected abstract Collection<TAGVALUE> getTagValues(ROOT root);

	protected abstract String getValue(VO tagValueIn);

	protected abstract boolean same(TAGVALUE tagValue, VO tagValueIn);

	protected abstract void toTagVOCollection(Collection<?> tags);
}
