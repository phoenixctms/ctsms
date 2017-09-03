package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.PaymentMethod;

public interface PaymentMethodSelectorListener {

	public final static PaymentMethod NO_SELECTION_PAYMENT_METHOD = null;

	public PaymentMethod getPaymentMethod(int property);

	public void setPaymentMethod(int property, PaymentMethod paymentMethod);
}
