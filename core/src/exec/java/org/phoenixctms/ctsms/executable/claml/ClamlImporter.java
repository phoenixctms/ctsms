package org.phoenixctms.ctsms.executable.claml;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.domain.IcdSystBlockDao;
import org.phoenixctms.ctsms.domain.IcdSystCategoryDao;
import org.phoenixctms.ctsms.domain.IcdSystDao;
import org.phoenixctms.ctsms.domain.IcdSystModifierDao;
import org.phoenixctms.ctsms.domain.OpsSystBlockDao;
import org.phoenixctms.ctsms.domain.OpsSystCategoryDao;
import org.phoenixctms.ctsms.domain.OpsSystDao;
import org.phoenixctms.ctsms.domain.OpsSystModifierDao;
import org.phoenixctms.ctsms.util.ChunkedRemoveAll;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.springframework.beans.factory.annotation.Autowired;

public class ClamlImporter {

	private static Search getRevisionSearch(String revision) {
		return new Search(new SearchParameter[] {
				new SearchParameter("revision", revision, SearchParameter.EQUAL_COMPARATOR) });
	}

	@Autowired
	protected IcdSystDao icdSystDao;
	@Autowired
	protected IcdSystBlockDao icdSystBlockDao;
	@Autowired
	protected IcdSystCategoryDao icdSystCategoryDao;
	@Autowired
	protected IcdSystClassProcessor icdSystClassProcessor;
	@Autowired
	protected IcdSystModifierDao icdSystModifierDao;
	@Autowired
	protected OpsSystDao opsSystDao;
	@Autowired
	protected OpsSystBlockDao opsSystBlockDao;
	@Autowired
	protected OpsSystCategoryDao opsSystCategoryDao;
	@Autowired
	protected OpsSystClassProcessor opsSystClassProcessor;
	@Autowired
	protected OpsSystModifierDao opsSystModifierDao;
	private Map<String, Map<String, Element>> classKinds; // treemap //chapter-category-block -> code -> object(class, preferred label etc)
	private Map<String, Map<String, Element>> modifierClasses;
	protected JobOutput jobOutput;

	public ClamlImporter() {
		classKinds = new HashMap<String, Map<String, Element>>();
		modifierClasses = new HashMap<String, Map<String, Element>>();
	}

	public long loadIcdSyst(String fileName, boolean removeAllBeforeInsert, String revision, String lang) throws Exception {
		reset();
		icdSystClassProcessor.setJobOutput(jobOutput);
		if (CommonUtil.isEmptyString(revision)) {
			revision = ExecUtil.removeExtension((new File(fileName)).getName());
			jobOutput.println("no icd systematics revision specified, using " + revision);
		} else {
			jobOutput.println("using icd systematics revision " + revision);
		}
		icdSystClassProcessor.setRevision(revision);
		long diagnosisCount = icdSystDao.getDiagnosisCount(revision);
		if (diagnosisCount > 0) {
			throw new IllegalArgumentException("icd systematics of revision " + revision + " are referenced by alpha ids used by " + diagnosisCount + " diagnoses");
		}
		if (removeAllBeforeInsert) {
			removeIcdSystRecords(revision);
			jobOutput.println("icd systemtics revision " + revision + " cleared");
		}
		return parse(fileName, lang, icdSystClassProcessor);
	}

	public long loadOpsSyst(String fileName, boolean removeAllBeforeInsert, String revision, String lang) throws Exception {
		reset();
		opsSystClassProcessor.setJobOutput(jobOutput);
		if (CommonUtil.isEmptyString(revision)) {
			revision = ExecUtil.removeExtension((new File(fileName)).getName());
			jobOutput.println("no ops systematics revision specified, using " + revision);
		} else {
			jobOutput.println("using ops systematics revision " + revision);
		}
		opsSystClassProcessor.setRevision(revision);
		long procedureCount = opsSystDao.getProcedureCount(revision);
		if (procedureCount > 0) {
			throw new IllegalArgumentException("ops systematics of revision " + revision + " are referenced by ops codes used by " + procedureCount + " procedures");
		}
		if (removeAllBeforeInsert) {
			removeOpsSystRecords(revision);
			jobOutput.println("ops systemtics revision " + revision + " cleared");
		}
		return parse(fileName, lang, opsSystClassProcessor);
	}

	private long parse(String fileName, String lang, ClamlClassProcessor processor) throws Exception {
		processor.reset();
		jobOutput.println("reading from file " + fileName);
		Element root = (new SAXBuilder()).build(new File(fileName)).getRootElement();
		parseClassKinds(root);
		parseModifiers(root);
		parseModifierClasses(root);
		parseClasses(root);
		long categoryClassCount = 0l;
		Iterator<String> kindsIt = classKinds.keySet().iterator();
		while (kindsIt.hasNext()) {
			String kind = kindsIt.next();
			Map<String, Element> codeMap = classKinds.get(kind);
			Iterator<String> codesIt = codeMap.keySet().iterator();
			while (codesIt.hasNext()) {
				String code = codesIt.next();
				categoryClassCount += processor.processClass(kind, code, codeMap.get(code), classKinds, modifierClasses, lang);
			}
		}
		jobOutput.println(categoryClassCount + " ClaML categories processed");
		processor.postProcess();
		return categoryClassCount;
	}

	private void parseClasses(Element root) {
		Iterator<Element> it = root.getChildren(ClamlConstants.CLASS_ELEMENT).iterator();
		Long count = 0l;
		while (it.hasNext()) {
			Element classElement = it.next();
			String code = classElement.getAttributeValue(ClamlConstants.CODE_ATTR);
			String kind = classElement.getAttributeValue(ClamlConstants.KIND_ATTR);
			if (!CommonUtil.isEmptyString(kind)) {
				if (classKinds.containsKey(kind)) {
					Map<String, Element> codeMap = classKinds.get(kind);
					if (!CommonUtil.isEmptyString(kind)) {
						if (!codeMap.containsKey(code)) {
							codeMap.put(code, classElement);
							count++;
						} else {
							throw new IllegalArgumentException(ClamlConstants.CLASS_ELEMENT + " " + ClamlConstants.KIND_ATTR + " '" + kind + "' - duplicate code " + code);
						}
					} else {
						throw new IllegalArgumentException(ClamlConstants.CLASS_ELEMENT + " " + ClamlConstants.CODE_ATTR + " empty");
					}
				} else {
					throw new IllegalArgumentException(ClamlConstants.CLASS_ELEMENT + " " + ClamlConstants.KIND_ATTR + " '" + kind + "' not declared");
				}
			} else {
				throw new IllegalArgumentException(ClamlConstants.CLASS_ELEMENT + " " + ClamlConstants.KIND_ATTR + " empty");
			}
		}
		jobOutput.println(ClamlConstants.CLASS_ELEMENT + ": " + count + " elements");
	}

	private void parseClassKinds(Element root) {
		Iterator<Element> it = root.getChild(ClamlConstants.CLASS_KINDS_ELEMENT).getChildren(ClamlConstants.CLASS_KIND_ELEMENT).iterator();
		while (it.hasNext()) {
			String name = it.next().getAttributeValue(ClamlConstants.NAME_ATTR);
			if (!CommonUtil.isEmptyString(name)) {
				classKinds.put(name, new HashMap<String, Element>());
				jobOutput.println(ClamlConstants.CLASS_KIND_ELEMENT + " " + name);
			} else {
				throw new IllegalArgumentException(ClamlConstants.CLASS_KIND_ELEMENT + " " + ClamlConstants.NAME_ATTR + " empty");
			}
		}
	}

	private void parseModifierClasses(Element root) {
		Iterator<Element> it = root.getChildren(ClamlConstants.MODIFIER_CLASS_ELEMENT).iterator();
		Long count = 0l;
		while (it.hasNext()) {
			Element modifierClassElement = it.next();
			String suffix = modifierClassElement.getAttributeValue(ClamlConstants.CODE_ATTR);
			Element superClass = modifierClassElement.getChild(ClamlConstants.SUPERCLASS_ELEMENT);
			if (superClass != null) {
				String modifier = superClass.getAttributeValue(ClamlConstants.CODE_ATTR);
				if (!CommonUtil.isEmptyString(modifier)) {
					if (modifierClasses.containsKey(modifier)) {
						Map<String, Element> suffixMap = modifierClasses.get(modifier);
						if (!CommonUtil.isEmptyString(suffix)) {
							if (!suffixMap.containsKey(suffix)) {
								suffixMap.put(suffix, modifierClassElement);
								count++;
							} else {
								throw new IllegalArgumentException(ClamlConstants.MODIFIER_CLASS_ELEMENT + " " + ClamlConstants.CODE_ATTR + " '" + modifier
										+ "' - duplicate suffix " + suffix);
							}
						} else {
							throw new IllegalArgumentException(ClamlConstants.MODIFIER_CLASS_ELEMENT + " " + ClamlConstants.CODE_ATTR + " empty");
						}
					} else {
						jobOutput.println(ClamlConstants.MODIFIER_CLASS_ELEMENT + " " + ClamlConstants.CODE_ATTR + " '" + modifier + "' not declared");
						modifier = modifierClassElement.getAttributeValue(ClamlConstants.MODIFIER_ATTR);
						if (modifierClasses.containsKey(modifier)) {
							Map<String, Element> suffixMap = modifierClasses.get(modifier);
							if (!CommonUtil.isEmptyString(suffix)) {
								if (!suffixMap.containsKey(suffix)) {
									suffixMap.put(suffix, modifierClassElement);
									count++;
								} else {
									throw new IllegalArgumentException(ClamlConstants.MODIFIER_CLASS_ELEMENT + " " + ClamlConstants.MODIFIER_ATTR + " '" + modifier
											+ "' - duplicate suffix " + suffix);
								}
							} else {
								throw new IllegalArgumentException(ClamlConstants.MODIFIER_CLASS_ELEMENT + " " + ClamlConstants.MODIFIER_ATTR + " empty");
							}
						} else {
							throw new IllegalArgumentException(ClamlConstants.MODIFIER_CLASS_ELEMENT + " " + ClamlConstants.MODIFIER_ATTR + " '" + modifier + "' not declared");
						}
					}
				} else {
					throw new IllegalArgumentException(ClamlConstants.MODIFIER_CLASS_ELEMENT + " " + ClamlConstants.CODE_ATTR + " empty");
				}
			} else {
				throw new IllegalArgumentException(ClamlConstants.MODIFIER_CLASS_ELEMENT + " no " + ClamlConstants.SUPERCLASS_ELEMENT);
			}
		}
		jobOutput.println(ClamlConstants.MODIFIER_CLASS_ELEMENT + ": " + count + " elements");
	}

	private void parseModifiers(Element root) {
		Iterator<Element> it = root.getChildren(ClamlConstants.MODIFIER_ELEMENT).iterator();
		long count = 0l;
		while (it.hasNext()) {
			String code = it.next().getAttributeValue(ClamlConstants.CODE_ATTR);
			if (!CommonUtil.isEmptyString(code)) {
				modifierClasses.put(code, new HashMap<String, Element>());
				count++;
			} else {
				throw new IllegalArgumentException(ClamlConstants.MODIFIER_ELEMENT + " " + ClamlConstants.CODE_ATTR + " empty");
			}
		}
		jobOutput.println(ClamlConstants.MODIFIER_ELEMENT + ": " + count + " elements");
	}

	private void removeIcdSystRecords(String revision) throws Exception {
		ChunkedRemoveAll<IcdSystDao> icdSystRemover = new ChunkedRemoveAll<IcdSystDao>(
				icdSystDao, getRevisionSearch(revision)) {

			@Override
			protected Collection<Object> convertPage(Collection<Object> page) {
				return new HashSet(page);
			}

			@Override
			protected Method getRemoveMethod(Class dao) throws NoSuchMethodException, SecurityException {
				return dao.getMethod("removeTxn", Long.class);
			}

			@Override
			protected Method getRemovePageMethod(Class dao) throws NoSuchMethodException, SecurityException {
				return dao.getMethod("removeAllTxn", Set.class);
			}

			@Override
			protected boolean removePageDone(int pageSize, Object removePageResult) {
				jobOutput.println(pageSize + " icd syst records removed");
				return true;
			}
		};
		icdSystRemover.remove();
	}

	private void removeOpsSystRecords(String revision) throws Exception {
		ChunkedRemoveAll<OpsSystDao> opsSystRemover = new ChunkedRemoveAll<OpsSystDao>(
				opsSystDao, getRevisionSearch(revision)) {

			@Override
			protected Collection<Object> convertPage(Collection<Object> page) {
				return new HashSet(page);
			}

			@Override
			protected Method getRemoveMethod(Class dao) throws NoSuchMethodException, SecurityException {
				return dao.getMethod("removeTxn", Long.class);
			}

			@Override
			protected Method getRemovePageMethod(Class dao) throws NoSuchMethodException, SecurityException {
				return dao.getMethod("removeAllTxn", Set.class);
			}

			@Override
			protected boolean removePageDone(int pageSize, Object removePageResult) {
				jobOutput.println(pageSize + " ops syst records removed");
				return true;
			}
		};
		opsSystRemover.remove();
	}

	public void reset() {
		classKinds.clear();
		modifierClasses.clear();
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
