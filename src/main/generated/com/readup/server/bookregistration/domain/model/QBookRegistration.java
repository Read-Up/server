package com.readup.server.bookregistration.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBookRegistration is a Querydsl query type for BookRegistration
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookRegistration extends EntityPathBase<BookRegistration> {

    private static final long serialVersionUID = -1446007808L;

    public static final QBookRegistration bookRegistration = new QBookRegistration("bookRegistration");

    public final com.readup.server.common.entity.QBaseEntity _super = new com.readup.server.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isbn = createString("isbn");

    public final NumberPath<Long> processorId = createNumber("processorId", Long.class);

    public final EnumPath<RegistrationStatus> registrationStatus = createEnum("registrationStatus", RegistrationStatus.class);

    public final NumberPath<Long> requesterId = createNumber("requesterId", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QBookRegistration(String variable) {
        super(BookRegistration.class, forVariable(variable));
    }

    public QBookRegistration(Path<? extends BookRegistration> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBookRegistration(PathMetadata metadata) {
        super(BookRegistration.class, metadata);
    }

}

