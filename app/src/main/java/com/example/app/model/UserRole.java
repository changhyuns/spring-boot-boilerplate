package com.example.app.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {

	USER(ROLES.USER, ROLES.ADMIN),
	ADMIN(ROLES.ADMIN, null);

	private final String roleName;

	private final String parentName;

	public static class ROLES {

		public static final String ADMIN = "ROLE_ADMIN";
		public static final String USER = "ROLE_USER";

		private ROLES() {

		}
	}

	public static String getRoleHierarchy() {
		StringBuilder sb = new StringBuilder();

		for (UserRole role : UserRole.values()) {
			if (role.parentName != null) {
				sb.append(role.parentName);
				sb.append(" > ");
				sb.append(role.roleName);
				sb.append("\n");
			}
		}

		return sb.toString();
	}

}
