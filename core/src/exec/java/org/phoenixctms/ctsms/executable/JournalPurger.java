package org.phoenixctms.ctsms.executable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.TableSizes;
import org.phoenixctms.ctsms.util.ChunkedRemoveAll;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.ExecDefaultSettings;
import org.phoenixctms.ctsms.util.ExecSettingCodes;
import org.phoenixctms.ctsms.util.ExecSettings;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.springframework.beans.factory.annotation.Autowired;

public class JournalPurger {

	@Autowired
	protected JournalEntryDao journalEntryDao;

	private JobOutput jobOutput;

	public JournalPurger() {
	}

	private SearchParameter[] getSearchParameters(Date before, JournalModule module, Long id, boolean systemMessagesOnly) {
		ArrayList<SearchParameter> searchParams = new ArrayList<SearchParameter>();
		searchParams.add(new SearchParameter("modifiedTimestamp", CommonUtil.dateToTimestamp(before), SearchParameter.LESS_THAN_COMPARATOR));
		jobOutput.println("limit records to drop to journal records before "
				+ CommonUtil.formatDate(before, ExecSettings.getString(ExecSettingCodes.DATETIME_PATTERN, ExecDefaultSettings.DATETIME_PATTERN)));
		if (module != null) {
			if (systemMessagesOnly) {
				searchParams.add(new SearchParameter("systemMessageModule", module, SearchParameter.EQUAL_COMPARATOR));
				searchParams.add(new SearchParameter("systemMessage", true, SearchParameter.EQUAL_COMPARATOR));
				jobOutput.println("limit records to drop to system messages: " + module.toString());
			}
			if (id != null) {
				switch (module) {
					case INVENTORY_JOURNAL:
						searchParams.add(new SearchParameter("inventory.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of inventory ID " + id);
						break;
					case STAFF_JOURNAL:
						searchParams.add(new SearchParameter("staff.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of staff ID " + id);
						break;
					case COURSE_JOURNAL:
						searchParams.add(new SearchParameter("course.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of course ID " + id);
						break;
					case USER_JOURNAL:
						searchParams.add(new SearchParameter("user.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of user ID " + id);
						break;
					case TRIAL_JOURNAL:
						searchParams.add(new SearchParameter("trial.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of trial ID " + id);
						break;
					case PROBAND_JOURNAL:
						searchParams.add(new SearchParameter("proband.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of proband ID " + id);
						break;
					case CRITERIA_JOURNAL:
						searchParams.add(new SearchParameter("criteria.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of criteria ID " + id);
						break;
					case INPUT_FIELD_JOURNAL:
						searchParams.add(new SearchParameter("inputField.id", id, SearchParameter.EQUAL_COMPARATOR));
						jobOutput.println("limit records to drop to journal records of inputfield ID " + id);
						break;
					default:
						// not supported for now...
						throw new IllegalArgumentException(MessageFormat.format(DefaultMessages.UNSUPPORTED_JOURNAL_MODULE, module.toString()));
				}
			}
		} else {
			if (systemMessagesOnly) {
				searchParams.add(new SearchParameter("systemMessage", true, SearchParameter.EQUAL_COMPARATOR));
				jobOutput.println("limit records to drop to system messages");
			}
		}
		return searchParams.toArray(new SearchParameter[0]);
	}

	public long removeJournalEntries(VariablePeriod period, Long explicitDays, JournalModule module, Long id) throws Exception {
		Date now = new Date();
		jobOutput.println("keep journal records of the last " + (VariablePeriod.EXPLICIT.equals(period) ? explicitDays + " days" : period.toString()));
		Date before = DateCalc.subIntervals(now, period, explicitDays, 1);
		ChunkedRemoveAll journalRemover = new ChunkedRemoveAll(journalEntryDao, new Search(getSearchParameters(before, module, id, true)));
		jobOutput.println("journal records to remove: " + journalRemover.getTotalCount());
		long count = journalRemover.remove(TableSizes.BIG);
		jobOutput.println(count + " journal records removed");
		return count;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
