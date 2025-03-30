package com.readup.server.quiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Quiz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	// TODO: Chapter Entity 추가 후 변경 필요
	@Column(name = "chapter_id")
	private Long chapterId;

	@Column(columnDefinition = "TEXT")
	private String question;

	@Column(columnDefinition = "TEXT")
	private String description;

}
