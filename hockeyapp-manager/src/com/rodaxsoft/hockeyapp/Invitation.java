/*
	Invitation.java
	
	Created by John Boyer on Aug 10, 2015
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
package com.rodaxsoft.hockeyapp;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.validator.routines.EmailValidator;

import com.rodaxsoft.hockeyapp.user.Role;

/**
 * Invitation class
 * @author John Boyer
 * @version 2015-08-16
 * @since 0.1
 * 
 */
public final class Invitation {

	/**
	 * Invite parameters
	 * @see http://support.hockeyapp.net/kb/api/api-teams-app-users
	 */
	private final Map<String, String> parameters;

	/**
	 * Constructor
	 */
	public Invitation() {
		this.parameters = new HashMap<String, String>();
	}
	
	/**
	 * Constructor sets the <code>email</code>, <code>firstName</code>, 
	 * and <code>lastName</code> from the given <code>Address</code> object.
	 * @param address The address object
	 */
	public Invitation(Address address) {
		this();
		
		InternetAddress internetAddr;
		try {
			internetAddr = new InternetAddress(address.toString());
		} catch (AddressException e) {
			throw new ContextedRuntimeException(e)
			           .addContextValue("pos", e.getPos())
			           .addContextValue("ref", e.getRef());

		}
		
		this.setEmail(internetAddr.getAddress());
		String name = internetAddr.getPersonal();
		this.setFirstName(StringUtils.substringBefore(name, " "));
		this.setLastName(StringUtils.substringAfter(name, " "));
	}


	/**
	 * @return The parameters map
	 */
	Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Set the email address (required)
	 * @param email The email address to set
	 * @return This instance
	 * @throws ContextedRuntimeException if the email is invalid
	 */
	public Invitation setEmail(String email) {
		EmailValidator validator;
		validator = EmailValidator.getInstance();
		if (validator.isValid(email)) {
			parameters.put("email", email);
		} else {
			throw new ContextedRuntimeException("Invalid email address")
					.addContextValue("email", email);
		}

		return this;
	}

	/**
	 * Set the first name (optional)
	 * 
	 * @param firstName
	 *            The first name to set
	 * @return This instance
	 */
	public Invitation setFirstName(String firstName) {
		parameters.put("first_name", firstName);
		return this;
	}

	/**
	 * Set the last name (optional)
	 * 
	 * @param lastName
	 *            The last name to set
	 * @return This instance
	 */
	public Invitation setLastName(String lastName) {
		parameters.put("last_name", lastName);
		return this;
	}

	/**
	 * Set to {@link Role#DEVELOPER}, {@link Role#MEMBER}, or
	 * {@link Role#TESTER} (default)
	 * 
	 * @param role
	 *            The role to set
	 * @return This instance
	 * @throws ContextedRuntimeException if the role value is set to <code>OWNER</code>
	 */
	public Invitation setRole(Role role) {
		
		if (role != Role.OWNER) {
			parameters.put("role", role.getIndex().toString());
		}

		else {
			throw new ContextedRuntimeException("Role value cannot be set to OWNER")
					.addContextValue("name", role.toString())
					.addContextValue("value", role.getIndex());
		}

		return this;
	}

	/**
	 * Set tags (optional)
	 * 
	 * @param tags
	 *            The tags to set
	 * @return This instance
	 */
	public Invitation setTags(String tags) {
		parameters.put("tags", tags);
		return this;
	}

	/**
	 * Set the message
	 * @param message The message to set
	 * @return This instance
	 */
	public Invitation setMessage(String message) {
		parameters.put("message", message);
		return this;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Invitation [parameters=" + parameters + "]";
	}

}
