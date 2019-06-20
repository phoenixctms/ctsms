package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class InquiryValueOutVOComparator implements Comparator<InquiryValueOutVO> {

	@Override
	public int compare(InquiryValueOutVO a, InquiryValueOutVO b) {
		if (a != null && b != null) {
			InquiryOutVO inquiryA = a.getInquiry();
			InquiryOutVO inquiryB = b.getInquiry();
			if (inquiryA != null && inquiryB != null) {
				TrialOutVO trialA = inquiryA.getTrial();
				TrialOutVO trialB = inquiryB.getTrial();
				if (trialA.getId() > trialB.getId()) {
					return 1;
				} else if (trialA.getId() < trialB.getId()) {
					return -1;
				} else {
					String categoryA = inquiryA.getCategory();
					String categoryB = inquiryB.getCategory();
					if (categoryA != null && categoryB != null) {
						int categoryComparison = categoryA.compareTo(categoryB);
						if (categoryComparison != 0) {
							return categoryComparison;
						}
					} else if (categoryA == null && categoryB != null) {
						return -1;
					} else if (categoryA != null && categoryB == null) {
						return 1;
					}
					if (inquiryA.getPosition() < inquiryB.getPosition()) {
						return -1;
					} else if (inquiryA.getPosition() > inquiryB.getPosition()) {
						return 1;
					} else {
						return 0;
					}
				}
			} else if (inquiryA == null && inquiryB != null) {
				return -1;
			} else if (inquiryA != null && inquiryB == null) {
				return 1;
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
