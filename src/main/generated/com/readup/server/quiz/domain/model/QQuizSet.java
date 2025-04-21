package com.readup.server.quiz.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuizSet is a Querydsl query type for QuizSet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuizSet extends EntityPathBase<QuizSet> {

    private static final long serialVersionUID = 1270482812L;

    public static final QQuizSet quizSet = new QQuizSet("quizSet");

    public final com.readup.server.common.entity.QBaseEntity _super = new com.readup.server.common.entity.QBaseEntity(this);

    public final NumberPath<Long> chapterId = createNumber("chapterId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Quiz, QQuiz> quizList = this.<Quiz, QQuiz>createList("quizList", Quiz.class, QQuiz.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QQuizSet(String variable) {
        super(QuizSet.class, forVariable(variable));
    }

    public QQuizSet(Path<? extends QuizSet> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuizSet(PathMetadata metadata) {
        super(QuizSet.class, metadata);
    }

}

