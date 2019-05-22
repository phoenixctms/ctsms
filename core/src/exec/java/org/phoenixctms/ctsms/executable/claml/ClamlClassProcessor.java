package org.phoenixctms.ctsms.executable.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jdom2.Element;
import org.jdom2.Namespace;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JobOutput;

public abstract class ClamlClassProcessor {

	private static void addModifiedBy(ArrayList<String> modifiedBy, ArrayList<Element> classElements) {
		Iterator<Element> it = classElements.iterator();
		while (it.hasNext()) {
			Element classElement = it.next();
			addModifiedBy(modifiedBy, classElement);
			removeModifiedBy(modifiedBy, classElement);
		}
	}

	private static void addModifiedBy(ArrayList<String> modifiedBy, Element classElement) {
		Iterator<Element> it = classElement.getChildren(ClamlConstants.MODIFIED_BY_ELEMENT).iterator();
		while (it.hasNext()) {
			modifiedBy.add(getCode(it.next()));
		}
	}

	private static HashMap<String, ArrayList<Element>> applyModifiers(Element classElement, ArrayList<String> modifiedBy, Map<String, Map<String, Element>> modifierClasses) {
		HashMap<String, ArrayList<Element>> result = new HashMap<String, ArrayList<Element>>();
		result.put(getCode(classElement), null);
		Iterator<String> it = modifiedBy.iterator();
		while (it.hasNext()) {
			String modifier = it.next();
			if (modifierClasses.containsKey(modifier)) {
				result = modifierCartesianProduct(result, modifierClasses.get(modifier));
			} else {
				throw new IllegalArgumentException(ClamlConstants.MODIFIER_ATTR + " '" + modifier + "' unknown");
			}
		}
		return result;
	}

	private static Element getBlock(Element categoryClassElement, Map<String, Map<String, Element>> classKinds) throws Exception {
		Element superClass = categoryClassElement.getChild(ClamlConstants.SUPERCLASS_ELEMENT); // single parent assumed
		if (superClass == null) {
			throw new IllegalArgumentException(getKind(categoryClassElement) + " " + getCode(categoryClassElement) + ": no " + ClamlConstants.SUPERCLASS_ELEMENT);
		}
		String code = getCode(superClass);
		if (!CommonUtil.isEmptyString(code)) {
			return classKinds.get(ClamlConstants.CLASS_KIND_BLOCK_ATTR).get(code);
		} else {
			throw new IllegalArgumentException(getKind(categoryClassElement) + " " + getCode(categoryClassElement) + ": no " + ClamlConstants.SUPERCLASS_ELEMENT + " code");
		}
	}

	private static Element getCategory(Element subCategoryClassElement, Map<String, Map<String, Element>> classKinds) throws Exception {
		Element superClass = subCategoryClassElement.getChild(ClamlConstants.SUPERCLASS_ELEMENT); // single parent assumed
		if (superClass == null) {
			throw new IllegalArgumentException(getKind(subCategoryClassElement) + " " + getCode(subCategoryClassElement) + ": no " + ClamlConstants.SUPERCLASS_ELEMENT);
		}
		String code = getCode(superClass);
		if (!CommonUtil.isEmptyString(code)) {
			return classKinds.get(ClamlConstants.CLASS_KIND_CATEGORY_ATTR).get(code);
		} else {
			throw new IllegalArgumentException(getKind(subCategoryClassElement) + " " + getCode(subCategoryClassElement) + ": no " + ClamlConstants.SUPERCLASS_ELEMENT + " code");
		}
	}

	private static Element getChapter(Element blockClassElement, Map<String, Map<String, Element>> classKinds) throws Exception {
		Element superClass = blockClassElement.getChild(ClamlConstants.SUPERCLASS_ELEMENT); // single parent assumed
		if (superClass == null) {
			throw new IllegalArgumentException(getKind(blockClassElement) + " " + getCode(blockClassElement) + ": no " + ClamlConstants.SUPERCLASS_ELEMENT);
		}
		String code = getCode(superClass);
		if (!CommonUtil.isEmptyString(code)) {
			return classKinds.get(ClamlConstants.CLASS_KIND_CHAPTER_ATTR).get(code);
		} else {
			throw new IllegalArgumentException(getKind(blockClassElement) + " " + getCode(blockClassElement) + ": no " + ClamlConstants.SUPERCLASS_ELEMENT + " code");
		}
	}

	protected static String getCode(Element classElement) {
		return classElement.getAttributeValue(ClamlConstants.CODE_ATTR);
	}

	private static ArrayList<String> getModifiedBy(ArrayList<Element> categories) {
		ArrayList<String> modifiedBy = new ArrayList<String>();
		addModifiedBy(modifiedBy, categories);
		return modifiedBy;
	}

	protected int maxBlocks;
	protected int maxCategories;
	protected JobOutput jobOutput;
	protected final static int INITIAL_LEVEL = 0;

	private static String getKind(Element classElement) {
		return classElement.getAttributeValue(ClamlConstants.KIND_ATTR);
	}

	protected static String getPreferredRubricLabel(Element classElement, String lang) {
		Iterator<Element> it = classElement.getChildren(ClamlConstants.RUBRIC_ELEMENT).iterator();
		String label = null;
		while (it.hasNext() && label == null) {
			Element rubric = it.next();
			if (ClamlConstants.RUBRIC_KIND_PREFFERD_ATTR.equals(rubric.getAttributeValue(ClamlConstants.KIND_ATTR))) {
				Element labelElement = rubric.getChild(ClamlConstants.LABEL_ELEMENT);
				if (labelElement != null) {
					if (CommonUtil.isEmptyString(lang) || lang.equals(labelElement.getAttributeValue(ClamlConstants.XML_LANG, Namespace.XML_NAMESPACE))) {
						label = labelElement.getTextTrim();
					}
				} else {
					throw new IllegalArgumentException(getKind(classElement) + " " + getCode(classElement) + ": " + ClamlConstants.RUBRIC_ELEMENT + " without "
							+ ClamlConstants.LABEL_ELEMENT);
				}
			}
		}
		return label;
	}

	private static HashMap<String, ArrayList<Element>> modifierCartesianProduct(HashMap<String, ArrayList<Element>> codes, Map<String, Element> suffixMap) {
		HashMap<String, ArrayList<Element>> result = new HashMap<String, ArrayList<Element>>(codes.size() * suffixMap.size());
		Iterator<String> codesIt = codes.keySet().iterator();
		while (codesIt.hasNext()) {
			String code = codesIt.next();
			ArrayList<Element> modifierClasses = codes.get(code);
			Iterator<String> suffixIt = suffixMap.keySet().iterator();
			while (suffixIt.hasNext()) {
				StringBuilder newCode = new StringBuilder(code);
				String suffix = suffixIt.next();
				newCode.append(suffix);
				ArrayList<Element> newModifierClasses;
				if (modifierClasses != null) {
					newModifierClasses = new ArrayList<Element>(modifierClasses);
				} else {
					newModifierClasses = new ArrayList<Element>();
				}
				newModifierClasses.add(suffixMap.get(suffix));
				result.put(newCode.toString(), newModifierClasses);
			}
		}
		return result;
	}

	private static void removeModifiedBy(ArrayList<String> modifiedBy, Element classElement) {
		Iterator<Element> it = classElement.getChildren(ClamlConstants.EXCLUDE_MODIFIER_ELEMENT).iterator();
		while (it.hasNext()) {
			modifiedBy.remove(getCode(it.next()));
		}
	}

	public ClamlClassProcessor() {
		reset();
	}

	protected HashCodeBuilder getHashCodeBuilder(Element chapterClassElement, ArrayList<Element> blockClassElements, ArrayList<Element> categoryClassElements, String modifiedCode,
			ArrayList<Element> modifierClasses, String lang) {
		String chapterCode = getCode(chapterClassElement);
		String chapterPreferredRubricLabel = getPreferredRubricLabel(chapterClassElement, lang);
		HashCodeBuilder hashCodeBuilder = (new HashCodeBuilder(1249046965, -82296885))
				.append(chapterCode)
				.append(chapterPreferredRubricLabel);
		int i = INITIAL_LEVEL;
		Iterator<Element> it = blockClassElements.iterator();
		while (it.hasNext()) {
			Element blockClassElement = it.next();
			hashCodeBuilder.append(getCode(blockClassElement))
					.append(getPreferredRubricLabel(blockClassElement, lang))
					.append(i)
					.append(!it.hasNext());
			i++;
		}
		i = INITIAL_LEVEL;
		it = categoryClassElements.iterator();
		while (it.hasNext()) {
			Element categoryClassElement = it.next();
			boolean last = !it.hasNext();
			String code = (!last || modifiedCode == null) ? getCode(categoryClassElement) : modifiedCode;
			if (last) {
				if (modifierClasses != null) {
					Iterator<Element> modifierIt = modifierClasses.iterator();
					int j = INITIAL_LEVEL;
					while (modifierIt.hasNext()) {
						Element modifierClass = modifierIt.next();
						hashCodeBuilder.append(getCode(modifierClass))
								.append(getPreferredRubricLabel(modifierClass, lang))
								.append(j);
						j++;
					}
				}
			}
			hashCodeBuilder.append(code)
					.append(getPreferredRubricLabel(categoryClassElement, lang))
					.append(i)
					.append(last);
			i++;
		}
		return hashCodeBuilder;
	}

	public void postProcess() {
		jobOutput.println("max blocks: " + maxBlocks);
		jobOutput.println("max categories: " + maxCategories);
	}

	protected int processClass(Element chapterClassElement, ArrayList<Element> blockClassElements, ArrayList<Element> categoryClassElements, String lang) throws Exception {
		return processClass(chapterClassElement, blockClassElements, categoryClassElements, null, null, lang);
	}

	protected abstract int processClass(Element chapterClassElement, ArrayList<Element> blockClassElements, ArrayList<Element> categoryClassElements, String modifiedCode,
			ArrayList<Element> modifierClasses, String lang) throws Exception;

	public int processClass(String kind, String code, Element classElement, Map<String, Map<String, Element>> classKinds, Map<String, Map<String, Element>> modifierClasses,
			String lang) throws Exception {
		if (ClamlConstants.CLASS_KIND_CATEGORY_ATTR.equals(kind)) {
			if (classElement == null) {
				throw new IllegalArgumentException(kind + " " + code + ": no " + ClamlConstants.CLASS_KIND_CATEGORY_ATTR + " class element");
			}
			ArrayList<Element> categories = new ArrayList<Element>();
			Element categoryClassElement = classElement;
			do {
				categories.add(categoryClassElement);
				categoryClassElement = getCategory(categoryClassElement, classKinds);
			} while (categoryClassElement != null && categoryClassElement.getChildren(ClamlConstants.SUBCLASS_ELEMENT).size() > 0);
			ArrayList<Element> blocks = new ArrayList<Element>();
			Element blockClassElement = getBlock(categories.get(categories.size() - 1), classKinds);
			if (blockClassElement == null) {
				throw new IllegalArgumentException(kind + " " + code + ": no " + ClamlConstants.CLASS_KIND_BLOCK_ATTR + " class element found");
			}
			do {
				blocks.add(blockClassElement);
				blockClassElement = getBlock(blockClassElement, classKinds);
			} while (blockClassElement != null);
			Element chapterClassElement = getChapter(blocks.get(blocks.size() - 1), classKinds);
			if (chapterClassElement == null) {
				throw new IllegalArgumentException(kind + " " + code + ": no " + ClamlConstants.CLASS_KIND_CHAPTER_ATTR + " class element found");
			}
			maxCategories = Math.max(maxCategories, categories.size());
			maxBlocks = Math.max(maxBlocks, blocks.size());
			Collections.reverse(categories);
			Collections.reverse(blocks);
			StringBuilder icdSystLine = new StringBuilder(ClamlConstants.CLASS_KIND_CHAPTER_ATTR + "\t" + getCode(chapterClassElement));
			Iterator<Element> it = blocks.iterator();
			while (it.hasNext()) {
				icdSystLine.append("\t" + ClamlConstants.CLASS_KIND_BLOCK_ATTR + "\t" + getCode(it.next()));
			}
			it = categories.iterator();
			while (it.hasNext()) {
				icdSystLine.append("\t" + ClamlConstants.CLASS_KIND_CATEGORY_ATTR + "\t" + getCode(it.next()));
			}
			int count = 0;
			ArrayList<String> modifiedBy = getModifiedBy(categories);
			if (modifiedBy.size() > 0) {
				HashMap<String, ArrayList<Element>> modifiedCodes = applyModifiers(categories.get(categories.size() - 1), modifiedBy, modifierClasses);
				Iterator<String> codeIt = modifiedCodes.keySet().iterator();
				while (codeIt.hasNext()) {
					String modifiedCode = codeIt.next();
					count += processClass(chapterClassElement, blocks, categories, modifiedCode, modifiedCodes.get(modifiedCode), lang);
					icdSystLine.append("\tmodified code\t" + modifiedCode);
				}
			} else {
				count += processClass(chapterClassElement, blocks, categories, lang);
			}
			jobOutput.println(icdSystLine.toString());
			return count;
		}
		return 0;
	}

	public void reset() {
		maxBlocks = 0;
		maxCategories = 0;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
