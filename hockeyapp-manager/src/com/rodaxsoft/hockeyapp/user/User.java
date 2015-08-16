/*
	User.java
	
	Created by John Boyer on Aug 12, 2015
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

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * User class
 * @author John Boyer
 * @version 2015-08-12
 * @since 0.1
 * 
 */
public final class User {
	/**
	 * Created date
	 */
	private DateTime created;
	/**
	 * Email
	 */
	private String email;
	/**
	 * Full name
	 */
	private String fullName;
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * Invited date (optional)
	 */
	private DateTime invited;
	/**
	 * Pending status (optional)
	 */
	private Boolean pending;
	/**
	 * Role [default: {@link Role#TESTER}]
	 */
	private Role role = Role.TESTER;
	/**
	 * Tags (optional)
	 */
	private String tags;
	/**
	 * User ID
	 */
	private Integer userId;
	
	/**
	 * @return the created
	 */
	public DateTime getCreated() {
		return created;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return The name <i>before</i> the first space character in the 
	 * full-name or <code>null</code>.
	 */
	public String getFirstName() {
		String firstName;
		if(fullName != null) {
			firstName = StringUtils.substringBefore(fullName, " ");
		}
		else {
			firstName = null;
		}
		return firstName;
	}
	
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @return the invited
	 */
	public DateTime getInvited() {
		return invited;
	}
	/**
	 * @return The name <i>after</i> the first space character in the 
	 * full-name or <code>null</code>.
	 */
	public String getLastName() {
		String lastName;
		if(fullName != null) {
			lastName = StringUtils.substringAfter(fullName, " ");
		}
		else {
			lastName = null;
		}
		return lastName;
	}
	/**
	 * Returns the role [default: {@link Role#TESTER}]
	 * @return The role
	 */
	public Role getRole() {
		return role;
	}
	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * Returns <code>true</code> if the user's membership to an app is pending; 
	 * otherwise, <code>false</code>.
	 * @return A value of <code>true</code> if the user's membership to an app 
	 *         is pending.
	 */
	public Boolean isPending() {
		return pending;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(DateTime created) {
		this.created = created;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @param invited the invited to set
	 */
	public void setInvited(DateTime invited) {
		this.invited = invited;
	}
	/**
	 * @param pending the pending to set
	 */
	public void setPending(Boolean pending) {
		this.pending = pending;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [created=" + created + ", email=" + email + ", fullName="
				+ fullName + ", id=" + id + ", invited=" + invited
				+ ", pending=" + pending + ", role=" + role + ", tags=" + tags
				+ ", userId=" + userId + "]";
	}

}
