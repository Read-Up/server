package com.readup.server.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.common.entity.BaseEntity;
import com.readup.server.terms.domain.UserTermsConsent;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")

public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private SocialAccount socialAccount;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserTermsConsent> userTermsConsentList  = new ArrayList<>();

	@Column
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column
	private UserStatus status;

	@Builder(access = AccessLevel.PRIVATE)
	private User(String nickname, SocialAccount socialAccount, String imageUrl, UserStatus status) {
		this.nickname = nickname;
		this.socialAccount = socialAccount;
		this.imageUrl = imageUrl;
		this.status = status;
	}

	// 회원가입
	public static User register(String nickname, SocialAccount socialAccount, String imageUrl) {
		return User.builder()
				.nickname(nickname)
				.socialAccount(socialAccount)
				.imageUrl(imageUrl)
				.status(UserStatus.ACTIVE)
				.build();
	}

	// 약관추가
	public void addUserTermsConsentList(List<UserTermsConsent> userTermsConsentList) {
		userTermsConsentList.forEach(userTermsConsent -> userTermsConsent.updateUser(this));
		this.userTermsConsentList.addAll(userTermsConsentList);
	}

	// 회원수정
	public void update(String nickname, String imageUrl) {
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}

//	// 회원삭제
//	public void delete() {
//		this.status = UserStatus.DELETED;
//		markDeletedAt();
//	}
}
