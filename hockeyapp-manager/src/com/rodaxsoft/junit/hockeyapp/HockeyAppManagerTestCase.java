/*
	HockeyAppManagerTestCase.java
	
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
package com.rodaxsoft.junit.hockeyapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ContextedException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rodaxsoft.hockeyapp.HockeyAppManager;
import com.rodaxsoft.hockeyapp.Invitation;
import com.rodaxsoft.hockeyapp.user.User;

/**
 * HockeyAppManagerTestCase class
 * @author John Boyer
 * @version 2015-08-12
 * @since 0.1
 * 
 */
public class HockeyAppManagerTestCase {
	
	/**
	 * Configuration 
	 */
	private Configuration config;
	/**
	 * HockeyApp API token
	 */
	private String apiToken;
	/**
	 * HockeyApp App ID
	 */
	private String appId;

	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		PropertiesConfiguration propConfig = new PropertiesConfiguration();

		final String propFile = "HockeyAppManagerTestCase.properties";
		propConfig.load(getClass().getResourceAsStream(propFile));
		config = propConfig;

		this.apiToken = config.getString("hockeyapp.api.token");
		this.appId = config.getString("hockeyapp.app.id");
		
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link HockeyAppManager#isAppMember(String, String)}.
	 */
	@Test
	public void testIsAppMember() {
		
		final String member = config.getString("hockeyapp.member.email");
		final String secret = config.getString("hockeyapp.app.secret");
		final String nonmember = config.getString("hockeyapp.nonmember.email");
		
		try {
			
			HockeyAppManager mgr = new HockeyAppManager(apiToken, appId);
			boolean success = mgr.isAppMember(member, secret);
			assertTrue(success);
			
			success = mgr.isAppMember(nonmember, secret);
			assertFalse(success);
			
		} catch (ContextedException e) {
			fail(e.getMessage());
		}
		
	}

	/**
	 * Test method for {@link HockeyAppManager#inviteUser(Invitation)}.
	 */
	@Test
	public void testInviteUser() {

		String email = config.getString("hockeyapp.invitee.email");
		String first = config.getString("hockeyapp.invitee.first.name");
		String last = config.getString("hockeyapp.invitee.last.name");
		String msg = config.getString("hockeyapp.invitee.message");
		
		try {
			
			Invitation invitation = new Invitation()
			                              .setEmail(email)
			                              .setFirstName(first)
			                              .setLastName(last)
			                              .setMessage(msg);
			
			HockeyAppManager mgr = new HockeyAppManager(apiToken, appId);
			boolean success = mgr.inviteUser(invitation);
			assertTrue(success);
			
		} catch (ContextedException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test method for {@link HockeyAppManager#getAppUsers()}
	 */
	@Test
	public void testGetAppUsers() {
		HockeyAppManager mgr = new HockeyAppManager(apiToken, appId);
		List<User> users = mgr.getAppUsers();
		assertNotNull(users);
	}

}
