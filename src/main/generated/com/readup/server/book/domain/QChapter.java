package com.readup.server.book.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChapter is a Querydsl query type for Chapter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChapter extends EntityPathBase<Chapter> {

    private static final long serialVersionUID = -800024659L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChapter chapter = new QChapter("chapter");

    public final com.readup.server.common.entity.QBaseEntity _super = new com.readup.server.common.entity.QBaseEntity(this);

    public final QBook book;

    public final NumberPath<Integer> chapterNumber = createNumber("chapterNumber", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QChapter(String variable) {
        this(Chapter.class, forVariable(variable), INITS);
    }

    public QChapter(Path<? extends Chapter> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChapter(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChapter(PathMetadata metadata, PathInits inits) {
        this(Chapter.class, metadata, inits);
    }

    public QChapter(Class<? extends Chapter> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
    }

}

