package com.readup.server.terms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserTermsConsent is a Querydsl query type for UserTermsConsent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserTermsConsent extends EntityPathBase<UserTermsConsent> {

    private static final long serialVersionUID = -1327212698L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserTermsConsent userTermsConsent = new QUserTermsConsent("userTermsConsent");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isConsent = createBoolean("isConsent");

    public final QTerms terms;

    public final QTermsVersion termsVersion;

    public final com.readup.server.user.domain.QUser user;

    public QUserTermsConsent(String variable) {
        this(UserTermsConsent.class, forVariable(variable), INITS);
    }

    public QUserTermsConsent(Path<? extends UserTermsConsent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserTermsConsent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserTermsConsent(PathMetadata metadata, PathInits inits) {
        this(UserTermsConsent.class, metadata, inits);
    }

    public QUserTermsConsent(Class<? extends UserTermsConsent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.terms = inits.isInitialized("terms") ? new QTerms(forProperty("terms")) : null;
        this.termsVersion = inits.isInitialized("termsVersion") ? new QTermsVersion(forProperty("termsVersion"), inits.get("termsVersion")) : null;
        this.user = inits.isInitialized("user") ? new com.readup.server.user.domain.QUser(forProperty("user")) : null;
    }

}

