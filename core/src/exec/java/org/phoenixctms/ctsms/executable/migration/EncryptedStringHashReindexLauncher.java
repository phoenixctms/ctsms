package org.phoenixctms.ctsms.executable.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.phoenixctms.ctsms.PrincipalStore;
import org.phoenixctms.ctsms.UserContext;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.ExecDefaultSettings;
import org.phoenixctms.ctsms.util.ExecSettingCodes;
import org.phoenixctms.ctsms.util.ExecSettings;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EncryptedStringHashReindexLauncher extends EncryptedFieldInitializer {

	@Autowired
	private ProbandContactParticularsHashReindexInitializer probandContactParticularsHashReindexInitializer;
	@Autowired
	private ProbandAddressHashReindexInitializer probandAddressHashReindexInitializer;
	@Autowired
	private BankAccountHashReindexInitializer bankAccountHashReindexInitializer;
	@Autowired
	private FileHashReindexInitializer fileHashReindexInitializer;
	@Autowired
	private JournalEntryHashReindexInitializer journalEntryHashReindexInitializer;
	@Autowired
	private ProbandTagValueHashReindexInitializer probandTagValueHashReindexInitializer;
	@Autowired
	private ProbandContactDetailValueHashReindexInitializer probandContactDetailValueHashReindexInitializer;
	@Autowired
	private ProcedureHashReindexInitializer procedureHashReindexInitializer;
	@Autowired
	private MedicationHashReindexInitializer medicationHashReindexInitializer;
	@Autowired
	private DiagnosisHashReindexInitializer diagnosisHashReindexInitializer;
	@Autowired
	private MoneyTransferHashReindexInitializer moneyTransferHashReindexInitializer;
	@Autowired
	private ProbandStatusEntryHashReindexInitializer probandStatusEntryHashReindexInitializer;
	@Autowired
	private ProbandListStatusEntryHashReindexInitializer probandListStatusEntryHashReindexInitializer;

	public EncryptedStringHashReindexLauncher() {
	}

	@Override
	public long update(AuthenticationVO auth) throws Exception {
		authenticate(auth);
		UserContext userContext = CoreUtil.getUserContext().copyForThread();
		List<EncryptedStringHashReindexInitializer<?, ?>> initializers = getHashReindexInitializers();
		prepareInitializers(initializers);
		int threads = getHashReindexThreads();
		long updated;
		if (threads < 2) {
			updated = reindexSequential(initializers);
		} else {
			jobOutput.println("reindex threads: " + threads);
			updated = reindexParallel(initializers, userContext, threads);
		}
		jobOutput.println("total rows updated: " + updated);
		return updated;
	}

	private long reindexSequential(List<EncryptedStringHashReindexInitializer<?, ?>> initializers) throws Exception {
		long updated = 0l;
		for (EncryptedStringHashReindexInitializer<?, ?> initializer : initializers) {
			updated += initializer.reindexLogged();
		}
		return updated;
	}

	private long reindexParallel(List<EncryptedStringHashReindexInitializer<?, ?>> initializers, UserContext userContext, int threads) throws Exception {
		int poolSize = Math.min(threads, initializers.size());
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);
		boolean interrupted = false;
		try {
			List<Future<Long>> futures = new ArrayList<Future<Long>>(initializers.size());
			for (final EncryptedStringHashReindexInitializer<?, ?> initializer : initializers) {
				futures.add(executor.submit(() -> reindexInThread(initializer, userContext)));
			}
			long updated = 0l;
			for (Future<Long> future : futures) {
				try {
					updated += getFutureResult(future);
				} catch (InterruptedException e) {
					interrupted = true;
					break;
				}
			}
			if (interrupted) {
				throw new InterruptedException();
			}
			return updated;
		} finally {
			shutdownExecutor(executor, interrupted || Thread.currentThread().isInterrupted());
		}
	}

	private void shutdownExecutor(ExecutorService executor, boolean interrupt) throws InterruptedException {
		if (interrupt) {
			executor.shutdownNow();
		} else {
			executor.shutdown();
		}
		try {
			if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
			throw e;
		}
	}

	private long reindexInThread(EncryptedStringHashReindexInitializer<?, ?> initializer, UserContext userContext) throws Exception {
		JobOutput.setLinePrefixThreadNumber(JobOutput.getCurrentThreadNumber());
		PrincipalStore.set(userContext.copyForThread());
		try {
			return initializer.reindexLogged();
		} finally {
			PrincipalStore.set(null);
			JobOutput.clearLinePrefixThreadNumber();
		}
	}

	private long getFutureResult(Future<Long> future) throws Exception {
		try {
			return future.get().longValue();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw e;
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof Exception) {
				throw (Exception) cause;
			}
			if (cause instanceof Error) {
				throw (Error) cause;
			}
			throw e;
		}
	}

	private int getHashReindexThreads() {
		Integer threads = ExecSettings.getIntNullable(ExecSettingCodes.HASH_REINDEX_THREADS, ExecDefaultSettings.HASH_REINDEX_THREADS);
		if (threads == null) {
			return ExecDefaultSettings.HASH_REINDEX_THREADS;
		}
		return threads.intValue();
	}

	private List<EncryptedStringHashReindexInitializer<?, ?>> getHashReindexInitializers() {
		return Arrays.<EncryptedStringHashReindexInitializer<?, ?>> asList(
				probandContactParticularsHashReindexInitializer,
				probandAddressHashReindexInitializer,
				bankAccountHashReindexInitializer,
				fileHashReindexInitializer,
				journalEntryHashReindexInitializer,
				probandTagValueHashReindexInitializer,
				probandContactDetailValueHashReindexInitializer,
				procedureHashReindexInitializer,
				medicationHashReindexInitializer,
				diagnosisHashReindexInitializer,
				moneyTransferHashReindexInitializer,
				probandStatusEntryHashReindexInitializer,
				probandListStatusEntryHashReindexInitializer);
	}

	private void prepareInitializers(List<EncryptedStringHashReindexInitializer<?, ?>> initializers) {
		for (EncryptedStringHashReindexInitializer<?, ?> initializer : initializers) {
			initializer.setJobOutput(jobOutput);
		}
	}
}
