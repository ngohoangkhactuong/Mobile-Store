package com.seo.mobilestore.common.util.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.seo.mobilestore.common.util.Validation;
@Component
public class ValidationImpl implements Validation {
	private String PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";
	private Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
	@Override
	public boolean passwordValid(String password) {
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
	

}
