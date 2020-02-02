package org.phoenixctms.ctsms.security;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.LdapEntryVO;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;

public class LdapService {

	private final static String USERNAME_ATTRIBUTE_ID = "cn";
	private LdapTemplate ldapTemplate;
	private String baseFormat;
	private String authFilterFormat;
	private String searchFilterFormat;
	private String[] searchResultAttributes;

	public LdapService() {
	}

	public boolean authenticate(AuthenticationVO auth) {
		return ldapTemplate.authenticate(getBase(), getAuthFilter(auth.getUsername()), auth.getPassword());
	}

	private AttributesMapper getAttributeMapper(final Object... baseArgs) {
		return new AttributesMapper() {

			@Override
			public Object mapFromAttributes(Attributes attrs) throws NamingException {
				LdapEntryVO entry = new LdapEntryVO();
				entry.setUsername((String) attrs.get(USERNAME_ATTRIBUTE_ID).get());
				DistinguishedName dn = new DistinguishedName(getBase(baseArgs));
				dn.add(USERNAME_ATTRIBUTE_ID, entry.getUsername());
				entry.setAbsoluteDn(dn.encode());
				Map<String, Object> attributes = new LinkedHashMap<String, Object>();
				if (searchResultAttributes != null) {
					for (int i = 0; i < searchResultAttributes.length; i++) {
						Attribute attr = attrs.get(searchResultAttributes[i]);
						if (attr != null) {
							attributes.put(searchResultAttributes[i], attr.get());
						}
					}
				}
				entry.setAttributes(attributes);
				return entry;
			}
		};
	}

	private String getAuthFilter(Object... args) {
		return MessageFormat.format(authFilterFormat, args);
	}

	private String getBase(Object... args) {
		return MessageFormat.format(baseFormat, args);
	}

	private String getSearchFilter(Object... args) {
		return MessageFormat.format(searchFilterFormat, args);
	}

	public List<LdapEntryVO> search(final Integer limit, String... filterArgs) {
		SearchControls searchControls = new SearchControls();
		Integer defaultLimit;
		if (limit != null) {
			if (limit >= 0) {
				searchControls.setCountLimit(limit);
			} else {
				searchControls.setCountLimit(0l);
			}
		} else if ((defaultLimit = Settings.getIntNullable(SettingCodes.LDAP_ENTRIES_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT, Bundle.SETTINGS,
				DefaultSettings.LDAP_ENTRIES_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT)) != null) {
			if (defaultLimit >= 0) {
				searchControls.setCountLimit(defaultLimit);
			} else {
				searchControls.setCountLimit(0l);
			}
		} else {
			searchControls.setCountLimit(0l);
		}
		return ldapTemplate.search(getBase(), getSearchFilter(filterArgs), searchControls, getAttributeMapper());
	}

	public List<LdapEntryVO> searchAuth(String username) {
		return ldapTemplate.search(getBase(), getAuthFilter(username), getAttributeMapper());
	}

	public void setAuthFilterFormat(String authFilterFormat) {
		this.authFilterFormat = authFilterFormat;
	}

	public void setBaseFormat(String baseFormat) {
		this.baseFormat = baseFormat;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public void setSearchFilterFormat(String searchFilterFormat) {
		this.searchFilterFormat = searchFilterFormat;
	}

	public void setSearchResultAttributes(String[] searchResultAttributes) {
		this.searchResultAttributes = searchResultAttributes;
	}
}
