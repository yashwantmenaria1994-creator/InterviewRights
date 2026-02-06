package com.example.interviewrights.Utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.interviewrights.entity.UserInvite;

import jakarta.persistence.criteria.Predicate;

public class UserInviteSpecification {

    public static Specification<UserInvite> filter(
            String email,
            String status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (email != null && !email.isEmpty()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                    )
                );
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(
                    cb.equal(root.get("status"), status)
                );
            }

            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}