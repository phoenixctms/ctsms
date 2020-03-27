package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class TeamMemberOutVOComparator extends AlphanumComparatorBase implements Comparator<TeamMemberOutVO> {

	@Override
	public int compare(TeamMemberOutVO a, TeamMemberOutVO b) {
		if (a != null && b != null) {
			TeamMemberRoleVO roleA = a.getRole();
			TeamMemberRoleVO roleB = b.getRole();
			if (roleA != null && roleB != null) {
				int roleComparison = comp(roleA.getName(), roleB.getName());
				if (roleComparison != 0) {
					return roleComparison;
				}
			} else if (roleA == null && roleB != null) {
				return -1;
			} else if (roleA != null && roleB == null) {
				return 1;
			}
			StaffOutVO staffA = a.getStaff();
			StaffOutVO staffB = b.getStaff();
			if (staffA != null && staffB != null) {
				if (staffA.isPerson() == true && staffB.isPerson() == true) {
					int lastNameComparison = comp(staffA.getLastName(), staffB.getLastName());
					if (lastNameComparison != 0) {
						return lastNameComparison;
					} else {
						TrialOutVO trialA = a.getTrial();
						TrialOutVO trialB = b.getTrial();
						if (trialA != null && trialB != null) {
							int trialNameComparison = comp(trialA.getName(), trialB.getName());
							if (trialNameComparison != 0) {
								return trialNameComparison;
							}
						} else if (trialA == null && trialB != null) {
							return -1;
						} else if (trialA != null && trialB == null) {
							return 1;
						}
					}
				} else if (staffA.isPerson() == false && staffB.isPerson() == false) {
					int organisationNameComparison = comp(staffA.getOrganisationName(), staffB.getOrganisationName());
					if (organisationNameComparison != 0) {
						return organisationNameComparison;
					}
				} else if (staffA.isPerson() == true) {
					return 1;
				} else if (staffA.isPerson() == false) {
					return -1;
				}
			} else if (staffA == null && staffB != null) {
				return -1;
			} else if (staffA != null && staffB == null) {
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