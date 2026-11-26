package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.phoenixctms.ctsms.vo.CriterionInVO;

/**
 * State of one background query agent run, shared between the worker thread and the polling view.
 */
public class QueryTextRun {

	public enum Status {
		RUNNING, DONE, FAILED, CANCELLED
	}

	private final long startMillis = System.currentTimeMillis();
	private final List<String> steps = new ArrayList<String>();
	// the user's request in natural language, null if a query was given to improve
	private final String naturalLanguageRequest;
	private volatile Status status = Status.RUNNING;
	private volatile long endMillis;
	private volatile String answer;
	private volatile String note;
	private volatile String lastValidText;
	private volatile Collection<CriterionInVO> lastValidCriterions;
	private volatile String error;
	private volatile boolean applied;
	private volatile Thread thread;

	public QueryTextRun(String naturalLanguageRequest) {
		this.naturalLanguageRequest = naturalLanguageRequest;
	}

	public String getNaturalLanguageRequest() {
		return naturalLanguageRequest;
	}

	public void addStep(String step) {
		synchronized (steps) {
			steps.add(step);
		}
	}

	public synchronized void cancel() {
		if (status == Status.RUNNING) {
			// end time first: the unsynchronized getElapsedMillis() reads it once the status is final
			endMillis = System.currentTimeMillis();
			status = Status.CANCELLED;
			addStep("cancelled");
			Thread worker = thread;
			if (worker != null) {
				worker.interrupt();
			}
		}
	}

	synchronized void done(String answer, String note, String lastValidText, Collection<CriterionInVO> lastValidCriterions) {
		if (status == Status.RUNNING) {
			this.answer = answer;
			this.note = note;
			this.lastValidText = lastValidText;
			this.lastValidCriterions = lastValidCriterions;
			endMillis = System.currentTimeMillis();
			status = Status.DONE;
		}
	}

	synchronized void failed(String error) {
		if (status == Status.RUNNING) {
			this.error = error;
			endMillis = System.currentTimeMillis();
			status = Status.FAILED;
		}
	}

	public String getAnswer() {
		return answer;
	}

	/**
	 * Short model note about omitted criteria or why the query is empty, if any.
	 */
	public String getNote() {
		return note;
	}

	public long getElapsedMillis() {
		return (status == Status.RUNNING ? System.currentTimeMillis() : endMillis) - startMillis;
	}

	public String getError() {
		return error;
	}

	public String getLastValidText() {
		return lastValidText;
	}

	public Collection<CriterionInVO> getLastValidCriterions() {
		return lastValidCriterions;
	}

	public Status getStatus() {
		return status;
	}

	public List<String> getSteps() {
		synchronized (steps) {
			return new ArrayList<String>(steps);
		}
	}

	public boolean isApplied() {
		return applied;
	}

	public boolean isRunning() {
		return status == Status.RUNNING;
	}

	/**
	 * Marks the finished run as handed to the view, so the result is applied only once.
	 */
	public synchronized boolean markApplied() {
		if (status == Status.RUNNING || applied) {
			return false;
		}
		applied = true;
		return true;
	}

	void setThread(Thread thread) {
		this.thread = thread;
	}
}
