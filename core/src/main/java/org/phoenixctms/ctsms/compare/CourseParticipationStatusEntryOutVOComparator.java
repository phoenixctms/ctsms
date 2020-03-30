package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Date;

import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;

public class CourseParticipationStatusEntryOutVOComparator implements Comparator<CourseParticipationStatusEntryOutVO> {

	@Override
	public int compare(CourseParticipationStatusEntryOutVO a, CourseParticipationStatusEntryOutVO b) {
		if (a != null && b != null) {
			TrainingRecordSectionVO trainingRecordSectionA = a.getTrainingRecordSection();
			TrainingRecordSectionVO trainingRecordSectionB = b.getTrainingRecordSection();
			if (trainingRecordSectionA != null && trainingRecordSectionB != null) {
				if (trainingRecordSectionA.getPosition() < trainingRecordSectionB.getPosition()) {
					return -1;
				} else if (trainingRecordSectionA.getPosition() > trainingRecordSectionB.getPosition()) {
					return 1;
				} else {
					Date dateA = a.getCourse().getStop();
					Date dateB = b.getCourse().getStop();
					if (dateA != null && dateB != null) {
						return dateA.compareTo(dateB);
					} else if (dateA == null && dateB != null) {
						return 1;
					} else if (dateA != null && dateB == null) {
						return -1;
					} else {
						return 0;
					}
				}
			} else if (trainingRecordSectionA == null && trainingRecordSectionB != null) {
				return -1;
			} else if (trainingRecordSectionA != null && trainingRecordSectionB == null) {
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