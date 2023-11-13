package com.example.demo.dto.keycloak.rest.response.users;

import lombok.Data;

/**
 * Subelement dto for keycloak REST API's /user response.
 * (Response returns an array of {@link User} elements).
 */
@Data
public class Access{
	private boolean manageGroupMembership;
	private boolean view;
	private boolean mapRoles;
	private boolean impersonate;
	private boolean manage;
}