/**
 * Copyright 2012 Richemont
 */
package com.hal.utils;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

/**
 * Implements a namespace context for all email xsds
 * 
 * @author smorei
 */
public class EsiNamespaceContext implements NamespaceContext {

	enum EsiNamespaceType {
		common, nameAndPhone, address, email, asset, product, catalog, customer, order, rot, contactus, sendfriend, linebreak, sendretailer,
		addressCar, commonCar, contactusCar, sendfriendCar, nameAndPhoneCar, sendfriendMultipleProducts, appointment;
	}

	public static final String URN_ESVC_RIC_NAMESPACE_CATALOG_1_0 = "urn:esvc:ric:namespace:catalog/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_PRODUCT_1_0 = "urn:esvc:ric:namespace:product/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_ASSET_1_0 = "urn:esvc:ric:namespace:asset/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_ORDER_1_0 = "urn:esvc:ric:namespace:order/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_CUSTOMER_1_0 = "urn:esvc:ric:namespace:customer/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_ROT_1_0 = "urn:esvc:ric:namespace:rot/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_EMAIL_1_0 = "urn:esvc:ric:namespace:email/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_SWSE_1_0 = "urn:esvc:ric:namespace:swse/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_CONTACTUS_1_0 = "urn:esvc:ric:namespace:contactus/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_SENDFRIEND_1_0 = "urn:esvc:ric:namespace:sendfriend/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_LINEBREAK_1_0 = "urn:esvc:ric:namespace:linebreak/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_SENDRETAILER_1_0 = "urn:esvc:ric:namespace:sendretailer/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_APPOINTMENT_1_0 = "urn:esvc:ric:namespace:appointment/1.0";

	public static final String URN_ESVC_RIC_NAMESPACE_CONTACTUS_CAR_1_0 = "urn:esvc:ric:namespace:contactus:car/1.0";
	public static final String URN_ESVC_RIC_NAMESPACE_SENDFRIEND_CAR_1_0 = "urn:esvc:ric:namespace:sendfriend:car/1.0";

	public static final String URN_ESVC_RIC_NAMESPACE_SENDFRIEND_MULTIPLE_PRODUCTS_1_0 = "urn:esvc:ric:namespace:sendfriendmultipleproducts/1.0";

	@Override
	public String getNamespaceURI(String prefix) {
		String uri;

		if (prefix.equals(EsiNamespaceType.email.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_EMAIL_1_0;
		} else if (prefix.equals(EsiNamespaceType.asset.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_ASSET_1_0;
		} else if (prefix.equals(EsiNamespaceType.product.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_PRODUCT_1_0;
		} else if (prefix.equals(EsiNamespaceType.catalog.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_CATALOG_1_0;
		} else if (prefix.equals(EsiNamespaceType.rot.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_ROT_1_0;
		} else if (prefix.equals(EsiNamespaceType.order.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_ORDER_1_0;
		} else if (prefix.equals(EsiNamespaceType.customer.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_CUSTOMER_1_0;
		} else if (prefix.equals(EsiNamespaceType.contactus.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_CONTACTUS_1_0;
		} else if (prefix.equals(EsiNamespaceType.sendfriend.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_SENDFRIEND_1_0;
		} else if (prefix.equals(EsiNamespaceType.linebreak.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_LINEBREAK_1_0;
		} else if (prefix.equals(EsiNamespaceType.sendretailer.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_SENDRETAILER_1_0;
		} else if (prefix.equals(EsiNamespaceType.contactusCar.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_CONTACTUS_CAR_1_0;
		} else if (prefix.equals(EsiNamespaceType.sendfriendCar.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_SENDFRIEND_CAR_1_0;
		} else if (prefix.equals(EsiNamespaceType.sendfriendMultipleProducts.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_SENDFRIEND_MULTIPLE_PRODUCTS_1_0;
		} else if (prefix.equals(EsiNamespaceType.appointment.name())) {
			uri = URN_ESVC_RIC_NAMESPACE_APPOINTMENT_1_0;
		} else {
			uri = null;
		}
		return uri;
	}

	// Dummy implementation - not used!
	@Override
	public Iterator getPrefixes(String val) {
		return null;
	}

	// Dummy implemenation - not used!
	@Override
	public String getPrefix(String uri) {
		return null;
	}
}
