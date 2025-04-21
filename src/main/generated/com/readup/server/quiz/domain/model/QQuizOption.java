package com.readup.server.quiz.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuizOption is a Querydsl query type for QuizOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuizOption extends EntityPathBase<QuizOption> {

    private static final long serialVersionUID = 1597386523L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuizOption quizOption = new QQuizOption("quizOption");

    public final com.readup.server.common.entity.QBaseEntity _super = new com.readup.server.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCorrect = createBoolean("isCorrect");

    public final QQuiz quiz;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QQuizOption(String variable) {
        this(QuizOption.class, forVariable(variable), INITS);
    }

    public QQuizOption(Path<? extends QuizOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuizOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuizOption(PathMetadata metadata, PathInits inits) {
        this(QuizOption.class, metadata, inits);
    }

    public QQuizOption(Class<? extends QuizOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.quiz = inits.isInitialized("quiz") ? new QQuiz(forProperty("quiz"), inits.get("quiz")) : null;
    }

}

