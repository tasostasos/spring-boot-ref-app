package com.example.demo.dto.keycloak.rest.response.users;

import java.util.List;
import lombok.Data;

@Data
public class User {
	private boolean totp;
	private String lastName;
	private Access access;
	private long createdTimestamp;
	private boolean enabled;
	private int notBefore;
	private List<Object> disableableCredentialTypes;
	private boolean emailVerified;
	private String firstName;
	private List<Object> requiredActions;
	private String id;
	private String email;
	private String username;
}