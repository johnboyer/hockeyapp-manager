/*
	HockeyAppManager.java
	
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.exception.ContextedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rodaxsoft.hockeyapp.converters.UserConverter;
import com.rodaxsoft.hockeyapp.user.User;

/**
 * HockeyAppManager class
 * @author John Boyer
 * @version 2015-08-12
 * @since 0.1
 * 
 */
public final class HockeyAppManager {

	/**
	 * HockeyApp Base URI
	 */
	private static final String BASE_URI = "https://rink.hockeyapp.net/api/2/apps";

	/**
	 * Hockey App Token Header Key
	 */
	private static final String HOCKEY_APP_TOKEN_KEY = "X-HockeyAppToken";

	/**
	 * Logging object
	 */
	private static final Log LOG = LogFactory.getLog(HockeyAppManager.class);
	
	/**
	 * Debugs the response object
	 * <i>Note: The response stream will be closed after calling this method.</i>
	 * @param response The response object to debug
	 */
	private static void debugEntity(Response response) {

		if (response.hasEntity() && 
				(LOG.isDebugEnabled() || LOG.isTraceEnabled())) {
			String entity = response.readEntity(String.class);
			LOG.debug("Response: \n" + entity);
		}
	}

	/**
	 * Handles a boolean response
	 * @param response The response object 
	 * @return A boolean value of <code>true</code> if successful; 
	 *         otherwise <code>false</code>.
	 * @throws ContextedException if the response status is unknown
	 */
	private static boolean handleBooleanResponse(Response response) 
			                                 throws ContextedException {
		final StatusType statusInfo = response.getStatusInfo();
		final Family family = statusInfo.getFamily();
		
		final int statusCode = statusInfo.getStatusCode();
		final String reasonPhrase = statusInfo.getReasonPhrase();
		final String familyName = family.name();
		
		boolean success;
		
		switch (family) {
		
		//Success
		case SUCCESSFUL:
			success = true;
			break;
		
	    //Unknown error
		case INFORMATIONAL:
		case SERVER_ERROR:
		case REDIRECTION:
		case OTHER:
			
			throw new ContextedException("Invalid status code")
			                         .addContextValue("statusCode", statusCode)
			                         .addContextValue("reason", reasonPhrase)
			                         .addContextValue("name", familyName);
		
		
		//Failure/Not found
		case CLIENT_ERROR:
		default:
			success = false;
			break;
		}
		return success;
	}

	/**
	 * HockeyApp API token 
	 */
	private String apiToken;

	/**
	 * HockeyApp App ID
	 */
	private String appId;

	/**
	 * Constructor
	 */
	private HockeyAppManager() {
		//Register converters
		ConvertUtils.register(new UserConverter(), User.class);
	}

	/**
	 * Constructor
	 * @param apiToken HockeyApp API token
	 * @param appId HockeyApp App ID
	 */
	public HockeyAppManager(String apiToken, String appId) {
		this();
		this.setApiToken(apiToken);
		this.setAppId(appId);
	}

	/**
	 * @return the apiToken
	 */
	public String getApiToken() {
		return apiToken;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * Returns a list of all app users
	 * @return A list of all app users
	 * @see #getAppId()
	 */
	public List<User> getAllAppUsers() {
		String resource = "app_users";
		String path = appId + "/" + resource;
		LOG.debug("Path: " + path);
		
		Response response = ClientBuilder.newClient()
				                         .target(BASE_URI)
                                         .path(path)
                                         .request(MediaType.APPLICATION_JSON_TYPE)
                                         .header(HOCKEY_APP_TOKEN_KEY, apiToken)
                                         .get();
		
		List<User> users = null;
		
		final Family family = response.getStatusInfo().getFamily();
		if(family == Family.SUCCESSFUL) {
			
			String entity = response.readEntity(String.class);
			
			JSONObject jsonObj = JSONObject.fromObject(entity);
			JSONArray jsonArray = jsonObj.getJSONArray("app_users");
			
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iter = jsonArray.iterator();
			
			while(iter.hasNext()) {
				
				JSONObject jsonUser = iter.next();
				
				if(null == users) {
					users = new ArrayList<>();
				}
				
				User user = (User) ConvertUtils.convert(jsonUser, User.class);
				users.add(user);
				
			}

		}
		
		return users;
	}
	
	/**
	 * Invites a new user to the app 
	 * @param invitation The user invitation
	 * @return A boolean value of <code>true</code> if the user was 
	 *         successfully added; otherwise, <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 * @see #getAppId()
	 */
	public boolean inviteUser(Invitation invitation) throws ContextedException {

		String resource = "app_users";
		String path = appId + "/" + resource;
		LOG.debug("Path: " + path);
		
		//Create the path
		final Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(BASE_URI).path(path);
		
		//Transform parameters to Form object
		Form form;
		form = new Form(new MultivaluedHashMap<>(invitation.getParameters()));
		
		//Invoke a POST
		Response response;
		response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
				            .header(HOCKEY_APP_TOKEN_KEY, apiToken)
				            .post(Entity.form(form));


		LOG.debug("Status: " + response.getStatus());

		debugEntity(response);

		return handleBooleanResponse(response);
	}
	
	/**
	 * Returns <code>true</code> if the email address matches a member of the app.
	 * @param email The email address to check for membership
	 * @param secret The app's secret string
	 * @return A boolean value of <code>true</code> if the email address 
	 *         matches a member of the app; otherwise, <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 * @see #getAppId()
	 */
	public boolean isAppMember(String email, String secret) 
			                   throws ContextedException {
		
		String resource = "app_users/check";
		String path = appId + "/" + resource;
		LOG.debug("Path: " + path);

		WebTarget webTarget = ClientBuilder.newClient().target(BASE_URI)
				                           .path(path)
	                                       .queryParam("email", email)
	                                       .queryParam("secret", secret);
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
				                     .get();
		
		LOG.debug("Status: " + response.getStatus());

		debugEntity(response);
		
		return handleBooleanResponse(response);
	}

	/**
	 * @param apiToken
	 *            the apiToken to set
	 */
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * Returns all the non-pending app users 
	 * @param appUsers All of the app's uers
	 * @return A new list of all the non-pending app users 
	 */
	public static List<User> getNonPendingAppUsers(List<User> appUsers) {
		
		List<User> copyUsers = new ArrayList<>(appUsers);
		
		CollectionUtils.filter(copyUsers, new Predicate<User>() {

			@Override
			public boolean evaluate(User object) {
				return !object.isPending();
			}
		});
		
		return copyUsers;		
	}

}
