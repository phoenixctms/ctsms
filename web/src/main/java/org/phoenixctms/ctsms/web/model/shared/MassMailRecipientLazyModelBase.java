package org.phoenixctms.ctsms.web.model.shared;

import java.util.HashMap;

import org.phoenixctms.ctsms.vo.EmailMessageVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class MassMailRecipientLazyModelBase extends LazyDataModelBase {

	protected HashMap<Long, EmailMessageVO> emailMessageCache;

	public MassMailRecipientLazyModelBase() {
		super();
		emailMessageCache = new HashMap<Long, EmailMessageVO>();
	}

	public EmailMessageVO getEmailMessage(MassMailRecipientOutVO recipient) {
		if (recipient != null) {
			if (emailMessageCache.containsKey(recipient.getId())) {
				return emailMessageCache.get(recipient.getId());
			} else {
				EmailMessageVO emailMessage = WebUtil.getEmailMessage(recipient.getId());
				emailMessageCache.put(recipient.getId(), emailMessage);
				return emailMessage;
			}
		}
		return null;
	}

	@Override
	protected MassMailRecipientOutVO getRowElement(Long id) {
		return WebUtil.getMassMailRecipient(id);
	}
}
