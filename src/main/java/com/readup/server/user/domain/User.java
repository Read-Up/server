package com.readup.server.user.domain;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.common.entity.BaseEntity;
import com.readup.server.terms.domain.UserTermsConsent;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private SocialAccount socialAccount;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserTermsConsent> userTermsConsentList;

	public void updateUserTermsConsentList(List<UserTermsConsent> userTermsConsentList) {
		userTermsConsentList.forEach(userTermsConsent -> userTermsConsent.updateUser(this));
		this.userTermsConsentList = userTermsConsentList;
	}

	public void updateSocialAccount(SocialAccount socialAccount) {
		socialAccount.updateUserFromSocialAccount(this);
		this.socialAccount = socialAccount;
	}
}
