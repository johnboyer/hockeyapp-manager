/*
	UserConverter.java
	
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

package com.rodaxsoft.hockeyapp.converters;

import java.util.Arrays;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.joda.time.DateTime;

import com.rodaxsoft.hockeyapp.user.Role;
import com.rodaxsoft.hockeyapp.user.User;
import com.rodaxsoft.mail.Address;

/**
 * UserConverter converts {@link JSONObject}Object and {@link Address} instances 
 * into a {@link User} object
 * @author John Boyer
 * @version 2015-08-15
 * @since 0.1
 * 
 */
public final class UserConverter implements Converter {

	/* (non-Javadoc)
	 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T convert(Class<T> type, Object value) {
		
		User user = null;
		
		if(value instanceof JSONObject) {
			JSONObject obj = (JSONObject) value;
			user = convertJSONObject(obj);
		}
		
		else if(value instanceof Address) {
			Address addr = (Address) value;
			user = convertAddress(addr);
		}
		
		return type.cast(user);
	}

	/**
	 * @param addr
	 * @return
	 */
	private User convertAddress(Address addr) {
		User user;
		user = new User();
		user.setEmail(addr.getEmail());
		user.setFullName(addr.getName());
		return user;
	}

	/**
	 * @param obj
	 * @return
	 */
	private User convertJSONObject(JSONObject obj) {
		User user;
		user = new User();
		user.setCreated(new DateTime(obj.get("created_at")));
		user.setEmail(obj.getString("email"));
		user.setFullName(obj.getString("full_name"));
		user.setId(obj.getInt("id"));
		
		final String invited = obj.optString("invited_at");
		if (!StringUtils.isEmpty(invited)) {
			user.setInvited(new DateTime(invited));
		}
		
		user.setPending(obj.optBoolean("pending"));
		
		//Set the Role
		final Integer roleIndex = obj.getInt("role");
		Predicate<Role> predicate = new Predicate<Role>() {

			@Override
			public boolean evaluate(Role object) {
				return object.getIndex().equals(roleIndex);
			}
		};
		
		Role role = CollectionUtils.find(Arrays.asList(Role.values()), predicate);
		if(null == role) {
			throw new ContextedRuntimeException("Invalid role value")
			                       .addContextValue("role", roleIndex);
		}
		else {
			user.setRole(role);
		}

		user.setTags(obj.getString("tags"));
		user.setUserId(obj.getInt("user_id"));
		return user;
	}

}
