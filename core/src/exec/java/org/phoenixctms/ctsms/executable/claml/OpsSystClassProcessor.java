package org.phoenixctms.ctsms.executable.claml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;

import org.phoenixctms.ctsms.domain.OpsSyst;
import org.phoenixctms.ctsms.domain.OpsSystBlock;
import org.phoenixctms.ctsms.domain.OpsSystBlockDao;
import org.phoenixctms.ctsms.domain.OpsSystCategory;
import org.phoenixctms.ctsms.domain.OpsSystCategoryDao;
import org.phoenixctms.ctsms.domain.OpsSystDao;
import org.phoenixctms.ctsms.domain.OpsSystModifier;
import org.phoenixctms.ctsms.domain.OpsSystModifierDao;

public class OpsSystClassProcessor extends ClamlClassProcessor {

	@Autowired
	protected OpsSystDao opsSystDao;
	@Autowired
	protected OpsSystBlockDao opsSystBlockDao;
	@Autowired
	protected OpsSystCategoryDao opsSystCategoryDao;
	@Autowired
	protected OpsSystModifierDao opsSystModifierDao;
	private String revision;

	public OpsSystClassProcessor() {
		super();
	}

	private OpsSyst createOpsSyst(
			Element chapterClassElement,
			ArrayList<Element> blockClassElements,
			ArrayList<Element> categoryClassElements,
			String modifiedCode,
			ArrayList<Element> modifierClasses,
			int hash,
			String lang) {
		OpsSyst opsSyst = OpsSyst.Factory.newInstance();
		opsSyst.setChapterCode(getCode(chapterClassElement));
		opsSyst.setChapterPreferredRubricLabel(getPreferredRubricLabel(chapterClassElement, lang));
		HashSet<OpsSystBlock> blocks = new HashSet<OpsSystBlock>(blockClassElements.size()); // LinkedHashSet
		int i = INITIAL_LEVEL;
		Iterator<Element> it = blockClassElements.iterator();
		while (it.hasNext()) {
			blocks.add(createOpsSystBlock(it.next(), i, !it.hasNext(), lang));
			i++;
		}
		opsSyst.setBlocks(blocks);
		HashSet<OpsSystCategory> categories = new HashSet<OpsSystCategory>(categoryClassElements.size()); // LinkedHashSet
		i = INITIAL_LEVEL;
		it = categoryClassElements.iterator();
		while (it.hasNext()) {
			categories.add(createOpsSystCategory(it.next(), i, !it.hasNext(), modifiedCode, modifierClasses, lang));
			i++;
		}
		opsSyst.setCategories(categories);
		opsSyst.setHash(hash);
		opsSyst.setRevision(revision);
		opsSyst = opsSystDao.create(opsSyst);
		return opsSyst;
	}

	private OpsSystBlock createOpsSystBlock(
			Element blockClassElement,
			int level,
			boolean last,
			String lang) {
		OpsSystBlock opsSystBlock = OpsSystBlock.Factory.newInstance();
		opsSystBlock.setCode(getCode(blockClassElement));
		opsSystBlock.setPreferredRubricLabel(getPreferredRubricLabel(blockClassElement, lang));
		opsSystBlock.setLevel(level);
		opsSystBlock.setLast(last);
		opsSystBlock = opsSystBlockDao.create(opsSystBlock);
		return opsSystBlock;
	}

	private OpsSystCategory createOpsSystCategory(
			Element categoryClassElement,
			int level,
			boolean last,
			String modifiedCode,
			ArrayList<Element> modifierClasses,
			String lang) {
		OpsSystCategory opsSystCategory = OpsSystCategory.Factory.newInstance();
		opsSystCategory.setCode((!last || modifiedCode == null) ? getCode(categoryClassElement) : modifiedCode);
		opsSystCategory.setPreferredRubricLabel(getPreferredRubricLabel(categoryClassElement, lang));
		opsSystCategory.setLevel(level);
		opsSystCategory.setLast(last);
		HashSet<OpsSystModifier> modifiers;
		if (last && modifierClasses != null) {
			modifiers = new HashSet<OpsSystModifier>(modifierClasses.size());
			Iterator<Element> it = modifierClasses.iterator();
			int i = INITIAL_LEVEL;
			while (it.hasNext()) {
				Element modifierClass = it.next();
				modifiers.add(createOpsSystModifier(getCode(modifierClass), getPreferredRubricLabel(modifierClass, lang), i));
				i++;
			}
			opsSystCategory.setModifiers(modifiers);
		} else {
			modifiers = new HashSet<OpsSystModifier>();
		}
		opsSystCategory.setModifiers(modifiers);
		opsSystCategory = opsSystCategoryDao.create(opsSystCategory);
		return opsSystCategory;
	}

	private OpsSystModifier createOpsSystModifier(
			String suffix,
			String preferredRubricLabel,
			int level) {
		OpsSystModifier opsSystModifier = OpsSystModifier.Factory.newInstance();
		opsSystModifier.setSuffix(suffix);
		opsSystModifier.setPreferredRubricLabel(preferredRubricLabel);
		opsSystModifier.setLevel(level);
		opsSystModifier = opsSystModifierDao.create(opsSystModifier);
		return opsSystModifier;
	}

	public String getRevision() {
		return revision;
	}

	@Override
	protected int processClass(Element chapterClassElement,
			ArrayList<Element> blockClassElements,
			ArrayList<Element> categoryClassElements, String modifiedCode,
			ArrayList<Element> modifierClasses, String lang) throws Exception {
		int hash = getHashCodeBuilder(chapterClassElement, blockClassElements, categoryClassElements, modifiedCode, modifierClasses, lang).append(revision).toHashCode();
		if (opsSystDao.searchUniqueHash(hash) == null) {
			createOpsSyst(chapterClassElement, blockClassElements, categoryClassElements, modifiedCode, modifierClasses, hash, lang);
			return 1;
		} else {
			return 0;
		}
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}
}
