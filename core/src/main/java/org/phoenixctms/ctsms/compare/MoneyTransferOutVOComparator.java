package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Date;

import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;

public class MoneyTransferOutVOComparator implements Comparator<MoneyTransferOutVO> {

	@Override
	public int compare(MoneyTransferOutVO a, MoneyTransferOutVO b) {
		if (a != null && b != null) {
			Date tsA = a.getTransactionTimestamp();
			Date tsB = b.getTransactionTimestamp();
			if (tsA != null && tsB != null) {
				int tsComparison = tsA.compareTo(tsB);
				if (tsComparison != 0) {
					return tsComparison;
				}
			} else if (tsA == null && tsB != null) {
				return -1;
			} else if (tsA != null && tsB == null) {
				return 1;
			}
			if (a.getId() > b.getId()) {
				return 1;
			} else if (a.getId() < b.getId()) {
				return -1;
			} else {
				return 0;
			}
		} else if (a == null && b != null) {
			return -1;
		} else if (a != null && b == null) {
			return 1;
		} else {
			return 0;
		}
	}
}