/*
	App.java
	
	Created by John Boyer on Aug 14, 2015
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

import org.apache.commons.beanutils.DynaBean;

/**
 * App bean class
 * @author John Boyer
 * @version 2015-08-14
 * @since 0.1
 * 
 */
public class App {
	/**
	 * The app ID
	 */
	private String appId;
	/**
	 * API token
	 */
	private String apiToken;
	/**
	 * Secret
	 */
	private String secret;
	
	/**
	 * Default constructor
	 */
	public App() {
	}

	/**
	 * Constructs app object from a {@link DynaBean}
	 * @param bean The dynamic bean
	 */
	public App(DynaBean bean) {
		this.appId = (String) bean.get("appId");
		this.apiToken = (String) bean.get("apiToken");
		this.secret = (String) bean.get("secret");
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the apiToken
	 */
	public String getApiToken() {
		return apiToken;
	}

	/**
	 * @param apiToken the apiToken to set
	 */
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	

}
