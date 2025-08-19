package org.group4.dvdshopbackend.security.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.group4.dvdshopbackend.common.enums.Role;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformLoginRes {
	Long memberId;
	Role memberRole;
	String accessToken;
}
