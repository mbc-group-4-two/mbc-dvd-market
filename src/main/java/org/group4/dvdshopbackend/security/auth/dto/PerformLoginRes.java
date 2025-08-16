package org.group4.dvdshopbackend.security.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformLoginRes {
	Long memberId;
	String accessToken;
}
