package org.phoenixctms.ctsms.security;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public class IPAddressValidation {

	private final static Map<String, String> TRUSTED_HOSTS_IP_RANGES = Settings.getBundleMap(Bundle.TRUSTED_HOSTS, false);
	private final static java.util.regex.Pattern RANGES_SPLIT_PATTERN_REGEXP = Pattern.compile("\\s*,\\s*");
	private final static java.util.regex.Pattern RANGE_SPLIT_PATTERN_REGEXP = Pattern.compile("\\s*-\\s*");

	private static long ipToLong(InetAddress ip) {
		byte[] octets = ip.getAddress();
		long result = 0;
		for (byte octet : octets) {
			result <<= 8;
			result |= octet & 0xff;
		}
		return result;
	}

	private static boolean isValidIpRange(String ipToCheck) {
		if (CommonUtil.isEmptyString(ipToCheck)) {
			return false;
		}
		try {
			long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
			return (ipToTest > 0);
		} catch (UnknownHostException e) {
			return false;
		}
	}

	private static boolean isValidIpRange(String ipStart, String ipEnd) {
		if (CommonUtil.isEmptyString(ipStart) || CommonUtil.isEmptyString(ipEnd)) {
			return false;
		}
		try {
			long ipLo = ipToLong(InetAddress.getByName(ipStart));
			long ipHi = ipToLong(InetAddress.getByName(ipEnd));
			return (ipHi >= ipLo);
		} catch (UnknownHostException e) {
			return false;
		}
	}

	public static boolean isValidIpRange(String[] ipRange) {
		if (ipRange.length == 1) {
			return isValidIpRange(ipRange[0]);
		} else if (ipRange.length == 2) {
			return isValidIpRange(ipRange[0], ipRange[1]);
		}
		return false;
	}

	private static boolean isWithinIpRange(String ip,
			String ipToCheck) {
		try {
			long ipRef = ipToLong(InetAddress.getByName(ip));
			long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
			return (ipToTest == ipRef);
		} catch (UnknownHostException e) {
			return false;
		}
	}

	private static boolean isWithinIpRange(String ipStart, String ipEnd,
			String ipToCheck) {
		try {
			long ipLo = ipToLong(InetAddress.getByName(ipStart));
			long ipHi = ipToLong(InetAddress.getByName(ipEnd));
			long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
			return (ipToTest >= ipLo && ipToTest <= ipHi);
		} catch (UnknownHostException e) {
			return false;
		}
	}

	public static boolean isWithinIpRange(String[] ipRange,
			String ipToCheck) {
		if (ipRange.length == 1) {
			return isWithinIpRange(ipRange[0], ipToCheck);
		} else if (ipRange.length == 2) {
			return isWithinIpRange(ipRange[0], ipRange[1], ipToCheck);
		}
		return false;
	}

	public static ArrayList<String[]> splitIpRanges(String ranges) {
		return splitIpRanges(ranges, new HashSet<String>(TRUSTED_HOSTS_IP_RANGES.size()));
	}

	private static ArrayList<String[]> splitIpRanges(String ranges, HashSet<String> dupeCheck) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		if (!CommonUtil.isEmptyString(ranges)) {
			String[] ipRanges = RANGES_SPLIT_PATTERN_REGEXP.split(ranges);
			String[] ips;
			if (ipRanges != null && ipRanges.length > 0) {
				for (int i = 0; i < ipRanges.length; i++) {
					ips = RANGE_SPLIT_PATTERN_REGEXP.split(ipRanges[i]);
					if (ips != null) {
						if (ips.length == 1) {
							if (TRUSTED_HOSTS_IP_RANGES.containsKey(ips[0])) {
								if (!dupeCheck.contains(ips[0])) {
									dupeCheck.add(ips[0]);
									result.addAll(splitIpRanges(TRUSTED_HOSTS_IP_RANGES.get(ips[0]), dupeCheck));
								} else {
									result.add(ips);
								}
							} else {
								result.add(ips);
							}
						} else {
							result.add(ips);
						}
					}
				}
			}
		}
		return result;
	}
}
