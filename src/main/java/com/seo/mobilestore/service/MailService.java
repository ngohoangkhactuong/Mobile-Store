package com.seo.mobilestore.service;

import com.seo.mobilestore.common.MailResponse;
import com.seo.mobilestore.common.MessageResponse;

public interface MailService {
	void send(MailResponse mailResponse);

	MessageResponse sendMailActive(String email);

	MessageResponse sendMailForgotPassword(String email);
}
