package com.example.demo.dto.keycloak.rest.response.users;

import java.util.List;
import lombok.Data;

@Data
public class Response{
	private List<Object> disableableCredentialTypes;
	private boolean totp;
	private boolean emailVerified;
	private List<Object> requiredActions;
	private Access access;
	private long createdTimestamp;
	private String id;
	private boolean enabled;
	private int notBefore;
	private String username;
}