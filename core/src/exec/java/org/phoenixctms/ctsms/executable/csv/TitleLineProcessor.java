package org.phoenixctms.ctsms.executable.csv;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.Title;
import org.phoenixctms.ctsms.domain.TitleDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class TitleLineProcessor extends LineProcessor {

	private final static int TITLE_COLUMN_INDEX = 0;
	@Autowired
	protected TitleDao titleDao;
	private int titleColumnIndex;

	public TitleLineProcessor() {
		super();

	}

	private Title createTitle(String[] values) {
		Title title = Title.Factory.newInstance();
		title.setTitle(getTitle(values));
		title = titleDao.create(title);
		return title;
	}

	private String getTitle(String[] values) {
		return values[titleColumnIndex];
	}

	public int getTitleColumnIndex() {
		return titleColumnIndex;
	}

	@Override
	public void init() {
		super.init();
		titleColumnIndex = TITLE_COLUMN_INDEX;
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
		.append(getTitle(values))
		.toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		createTitle(values);
		return 1;
	}

	public void setTitleColumnIndex(int titleColumnIndex) {
		this.titleColumnIndex = titleColumnIndex;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getTitle(values))) {
			jobOutput.println("line " + lineNumber + ": empty title");
			return false;
		}
		return true;
	}
}
