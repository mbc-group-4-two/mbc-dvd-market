package org.group4.dvdshopbackend.common.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "jti")
	private String jti;

	@Column(name = "token_hash")
	private String tokenHash;

	@Column(name = "created_at")
	private LocalDateTime createdDate;

	@Column(name = "expires_at")
	private LocalDateTime expiresDate;

	@Column(name = "replaced_at")
	private LocalDateTime replacedDate;

	@Column(name = "replaced_by")
	private String replacedBy;


	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "user_agent")
	private String userAgent;

	@Column(name = "ip")
	private String ip;

}
