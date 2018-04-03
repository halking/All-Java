/*package com.hal.xml;


import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.hal.utils.XPathUtilities;
import com.hal.utils.XmlUtilities;

public class TestXml {
     
	Logger log = Logger.getLogger(TestXml.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document emailDocument = XmlUtilities.parseDocument();
		// VALIDATE XSD
		
		String templateExp = "//email:Email/email:Service/email:Template";
		NodeList nodeList = XPathUtilities.getNodeList(emailDocument,
				templateExp);
		String templateUrn = null;
		EmailTemplateData emailTemplateData = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			templateUrn = item.getTextContent();
			auditService.put(AuditService.TEMPLATE_URN, templateUrn);
			EsvUrn emailUrn = EsvUrn.valueOf(templateUrn);
			Assert.isTrue(emailUrn.getBrand().equalsIgnoreCase(brand),
					"Template brand " + emailUrn.getBrand()
							+ " is not the same as the email brand " + brand);
			Assert.isTrue(
					EsvUrnType.email_template.equals(emailUrn.getType()),
					"Email template type should be  "
							+ EsvUrnType.email_template.getType() + " and not "
							+ emailUrn.getTypeValue());
			String templateName = emailUrn.getIdentifierParent();
			auditService.put(AuditService.TEMPLATE_NAME, templateName);
			String locale = emailUrn.getIdentifierChild();
			auditService.put(AuditService.TEMPLATE_LOCALE, locale);
			// Assert.notNull(locale);
			EmailTemplateCode code = new EmailTemplateCode(brand, templateName,
					locale);
			auditService.put(AuditService.EMAIL_CODE, code);
			if (!emailTemplateLocatorService.existsTemplateWithFallback(code)) {
				throw new EmailTemplateNotFoundException();
			}
			emailTemplateData = emailTemplateLocatorService
					.findTemplateWithFallback(code);
			auditService.put(AuditService.EMAIL_TEMPLATE, emailTemplateData);
			if (emailTemplateData != null) {
				log.info(uuid + ":finded "
						+ emailTemplateData.getEmailTemplateCode()
						+ " for original requested code " + code);
				break;
			}
		}
		if (emailTemplateData == null) {
			throw new EmailTemplateNotFoundException();
		}
		//

		Email email = new Email(null, uuid, emailTemplateData, emailDocument);
		email.setEmailStatus(EmailStatus.RECEIVED);
	}

}
*/