package org.group4.dvdshopbackend.security.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.group4.dvdshopbackend.common.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "user_refresh_tokens")
public class UserRefreshToken {

	@Id
	@SequenceGenerator(
			name = "user_refresh_tokens_seq",
			sequenceName = "user_refresh_tokens_seq_tbl",
			allocationSize = 1 // sequence 캐싱 처리, 배포시 50정도 -> 병목시 늘리기
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_refresh_tokens_seq")
	@Column(name = "refresh_token_id")
	private Long id; // 토큰 고유 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member; // 유저 ID join

	@Column(name = "jti")
	private String jti; // jwt ID

	@Column(name = "token_hash")
	private String tokenHash; // jwt token hash

	@Column(name = "created_at")
	private LocalDateTime createdDate; // 생성일자

	@Column(name = "expires_at")
	private LocalDateTime expiresDate; // 만료일자

	@Column(name = "replaced_at")
	private LocalDateTime replacedDate; // 회전일

	@Column(name = "revoked_by")
	private String revokedBy; // 회전 jwt ID


	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "user_agent")
	private String userAgent;

	@Column(name = "ip")
	private String ip;

	@PrePersist
	void prePersist() {
		if (createdDate == null) createdDate = LocalDateTime.now();
	}

	public void rotate(String revokeJti) {
		this.replacedDate = LocalDateTime.now();
		this.revokedBy = revokeJti;
	}
}
