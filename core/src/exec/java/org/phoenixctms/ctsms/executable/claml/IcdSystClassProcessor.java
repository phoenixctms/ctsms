package org.phoenixctms.ctsms.executable.claml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;

import org.phoenixctms.ctsms.domain.IcdSyst;
import org.phoenixctms.ctsms.domain.IcdSystBlock;
import org.phoenixctms.ctsms.domain.IcdSystBlockDao;
import org.phoenixctms.ctsms.domain.IcdSystCategory;
import org.phoenixctms.ctsms.domain.IcdSystCategoryDao;
import org.phoenixctms.ctsms.domain.IcdSystDao;
import org.phoenixctms.ctsms.domain.IcdSystModifier;
import org.phoenixctms.ctsms.domain.IcdSystModifierDao;

public class IcdSystClassProcessor extends ClamlClassProcessor {

	@Autowired
	protected IcdSystDao icdSystDao;
	@Autowired
	protected IcdSystBlockDao icdSystBlockDao;
	@Autowired
	protected IcdSystCategoryDao icdSystCategoryDao;
	@Autowired
	protected IcdSystModifierDao icdSystModifierDao;
	private String revision;

	public IcdSystClassProcessor() {
		super();
	}

	private IcdSyst createIcdSyst(
			Element chapterClassElement,
			ArrayList<Element> blockClassElements,
			ArrayList<Element> categoryClassElements,
			String modifiedCode,
			ArrayList<Element> modifierClasses,
			int hash,
			String lang) {
		IcdSyst icdSyst = IcdSyst.Factory.newInstance();
		icdSyst.setChapterCode(getCode(chapterClassElement));
		icdSyst.setChapterPreferredRubricLabel(getPreferredRubricLabel(chapterClassElement, lang));
		HashSet<IcdSystBlock> blocks = new HashSet<IcdSystBlock>(blockClassElements.size()); // LinkedHashSet
		int i = INITIAL_LEVEL;
		Iterator<Element> it = blockClassElements.iterator();
		while (it.hasNext()) {
			blocks.add(createIcdSystBlock(it.next(), i, !it.hasNext(), lang));
			i++;
		}
		icdSyst.setBlocks(blocks);
		HashSet<IcdSystCategory> categories = new HashSet<IcdSystCategory>(categoryClassElements.size()); // LinkedHashSet
		i = INITIAL_LEVEL;
		it = categoryClassElements.iterator();
		while (it.hasNext()) {
			categories.add(createIcdSystCategory(it.next(), i, !it.hasNext(), modifiedCode, modifierClasses, lang));
			i++;
		}
		icdSyst.setCategories(categories);
		icdSyst.setHash(hash);
		icdSyst.setRevision(revision);
		icdSyst = icdSystDao.create(icdSyst);
		return icdSyst;
	}

	private IcdSystBlock createIcdSystBlock(
			Element blockClassElement,
			int level,
			boolean last,
			String lang) {
		IcdSystBlock icdSystBlock = IcdSystBlock.Factory.newInstance();
		icdSystBlock.setCode(getCode(blockClassElement));
		icdSystBlock.setPreferredRubricLabel(getPreferredRubricLabel(blockClassElement, lang));
		icdSystBlock.setLevel(level);
		icdSystBlock.setLast(last);
		icdSystBlock = icdSystBlockDao.create(icdSystBlock);
		return icdSystBlock;
	}

	private IcdSystCategory createIcdSystCategory(
			Element categoryClassElement,
			int level,
			boolean last,
			String modifiedCode,
			ArrayList<Element> modifierClasses,
			String lang) {
		IcdSystCategory icdSystCategory = IcdSystCategory.Factory.newInstance();
		icdSystCategory.setCode((!last || modifiedCode == null) ? getCode(categoryClassElement) : modifiedCode);
		icdSystCategory.setPreferredRubricLabel(getPreferredRubricLabel(categoryClassElement, lang));
		icdSystCategory.setLevel(level);
		icdSystCategory.setLast(last);
		HashSet<IcdSystModifier> modifiers;
		if (last && modifierClasses != null) {
			modifiers = new HashSet<IcdSystModifier>(modifierClasses.size());
			Iterator<Element> it = modifierClasses.iterator();
			int i = INITIAL_LEVEL;
			while (it.hasNext()) {
				Element modifierClass = it.next();
				modifiers.add(createIcdSystModifier(getCode(modifierClass), getPreferredRubricLabel(modifierClass, lang), i));
				i++;
			}
			icdSystCategory.setModifiers(modifiers);
		} else {
			modifiers = new HashSet<IcdSystModifier>();
		}
		icdSystCategory.setModifiers(modifiers);
		icdSystCategory = icdSystCategoryDao.create(icdSystCategory);
		return icdSystCategory;
	}

	private IcdSystModifier createIcdSystModifier(
			String suffix,
			String preferredRubricLabel,
			int level) {
		IcdSystModifier icdSystModifier = IcdSystModifier.Factory.newInstance();
		icdSystModifier.setSuffix(suffix);
		icdSystModifier.setPreferredRubricLabel(preferredRubricLabel);
		icdSystModifier.setLevel(level);
		icdSystModifier = icdSystModifierDao.create(icdSystModifier);
		return icdSystModifier;
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
		if (icdSystDao.searchUniqueHash(hash) == null) {
			createIcdSyst(chapterClassElement, blockClassElements, categoryClassElements, modifiedCode, modifierClasses, hash, lang);
			return 1;
		} else {
			return 0;
		}
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}
}
