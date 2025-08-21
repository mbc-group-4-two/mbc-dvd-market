package org.group4.dvdshopbackend.security.auth.record;

import java.security.Principal;

public record RefreshPrincipal(Long id, String jti, Long version) implements Principal {
	@Override
	public String getName() {
		return String.valueOf(id);
	}
}