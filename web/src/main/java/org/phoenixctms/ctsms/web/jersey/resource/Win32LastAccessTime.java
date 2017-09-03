package org.phoenixctms.ctsms.web.jersey.resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "Win32LastAccessTime", namespace = "urn:schemas-microsoft-com:")
public final class Win32LastAccessTime {

	@XmlValue
	private String content;

	/**
	 * @return Client specific string which is not to be further parsed, according to Microsoft's documentation.
	 * @see <a href="http://msdn.microsoft.com/en-us/library/cc250144(PROT.10).aspx">Chapter 2.2.10.8 "Z:Win32LastAccessTime Property" of MS-WDVME "Web Distributed Authoring and Versioning (WebDAV) Protocol: Microsoft Extensions"</a>
	 */
	public final String getContent() {
		return this.content;
	}

	/**
	 * @return "Win32LastAccesstime (content)"
	 */
	@Override
	public final String toString() {
		return String.format("Win32LastAccessTime (%s)", this.content);
	}
}