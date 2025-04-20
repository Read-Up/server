package com.readup.server.terms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTermsVersion is a Querydsl query type for TermsVersion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTermsVersion extends EntityPathBase<TermsVersion> {

    private static final long serialVersionUID = -1998143303L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTermsVersion termsVersion = new QTermsVersion("termsVersion");

    public final com.readup.server.common.entity.QBaseEntity _super = new com.readup.server.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final DateTimePath<java.time.LocalDateTime> effectiveDate = createDateTime("effectiveDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRequired = createBoolean("isRequired");

    public final QTerms terms;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public final StringPath version = createString("version");

    public QTermsVersion(String variable) {
        this(TermsVersion.class, forVariable(variable), INITS);
    }

    public QTermsVersion(Path<? extends TermsVersion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTermsVersion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTermsVersion(PathMetadata metadata, PathInits inits) {
        this(TermsVersion.class, metadata, inits);
    }

    public QTermsVersion(Class<? extends TermsVersion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.terms = inits.isInitialized("terms") ? new QTerms(forProperty("terms")) : null;
    }

}

