package org.group4.dvdshopbackend.security.auth.record;

import java.security.Principal;

public record LoginUser(Long id, String role) implements Principal {
	@Override
	public String getName() {
		return String.valueOf(id);
	}
}