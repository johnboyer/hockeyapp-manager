/*
	UserAddress.java
	
	Created by John Boyer on Aug 16, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package com.rodaxsoft.hockeyapp.user;

import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * UserAddress class
 * @author John Boyer
 * @version 2015-08-16
 * @since 0.1
 * 
 */
final class UserAddress extends Address {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 4996642206006384111L;
	/**
	 * Internet address
	 */
	private InternetAddress address;
	
	/**
	 * Constructor
	 * @param user
	 */
	UserAddress(User user) {
		try {
			address = new InternetAddress(user.getEmail(), user.getFullName());
		} catch (UnsupportedEncodingException e) {
			throw new ContextedRuntimeException(e)
			          .addContextValue("address", user.getEmail());
		}
	}


	/* (non-Javadoc)
	 * @see javax.mail.Address#getType()
	 */
	@Override
	public String getType() {
		return address.getType();
	}

	/* (non-Javadoc)
	 * @see javax.mail.Address#toString()
	 */
	@Override
	public String toString() {
		return address.toString();
	}

	/* (non-Javadoc)
	 * @see javax.mail.Address#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object address) {
		return address.equals(address);
	}

}
