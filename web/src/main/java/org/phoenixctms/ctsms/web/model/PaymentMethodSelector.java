package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class PaymentMethodSelector {

	private PaymentMethodSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> paymentMethods;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public PaymentMethodSelector(PaymentMethodSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		paymentMethods = WebUtil.getPaymentMethods();
	}

	public PaymentMethodSelectorListener getBean() {
		return bean;
	}

	public String getPaymentMethod() {
		if (bean != null) {
			PaymentMethod paymentMethod = bean.getPaymentMethod(property);
			if (paymentMethod != null) {
				return paymentMethod.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public ArrayList<SelectItem> getPaymentMethods() {
		return paymentMethods;
	}

	public void setBean(PaymentMethodSelectorListener bean) {
		this.bean = bean;
	}

	public void setPaymentMethod(String name) {
		if (bean != null) {
			PaymentMethod paymentMethod;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					paymentMethod = PaymentMethod.fromString(name);
				} catch (Exception e) {
					paymentMethod = PaymentMethodSelectorListener.NO_SELECTION_PAYMENT_METHOD;
				}
			} else {
				paymentMethod = PaymentMethodSelectorListener.NO_SELECTION_PAYMENT_METHOD;
			}
			bean.setPaymentMethod(property, paymentMethod);
		}
	}
}
