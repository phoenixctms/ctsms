package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.util.ArrayList;
import java.util.List;

/**
 * State of one background query agent run, shared between the worker thread and the polling view.
 */
public class QueryTextRun {

	public enum Status {
		RUNNING, DONE, FAILED, CANCELLED
	}

	private final long startMillis = System.currentTimeMillis();
	private final List<String> steps = new ArrayList<String>();
	private volatile Status status = Status.RUNNING;
	private volatile long endMillis;
	private volatile String answer;
	private volatile String lastValidText;
	private volatile String error;
	private volatile boolean applied;
	private volatile Thread thread;

	public void addStep(String step) {
		synchronized (steps) {
			steps.add(step);
		}
	}

	public synchronized void cancel() {
		if (status == Status.RUNNING) {
			status = Status.CANCELLED;
			endMillis = System.currentTimeMillis();
			addStep("cancelled");
			Thread worker = thread;
			if (worker != null) {
				worker.interrupt();
			}
		}
	}

	synchronized void done(String answer, String lastValidText) {
		if (status == Status.RUNNING) {
			this.answer = answer;
			this.lastValidText = lastValidText;
			status = Status.DONE;
			endMillis = System.currentTimeMillis();
		}
	}

	synchronized void failed(String error) {
		if (status == Status.RUNNING) {
			this.error = error;
			status = Status.FAILED;
			endMillis = System.currentTimeMillis();
		}
	}

	public String getAnswer() {
		return answer;
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
