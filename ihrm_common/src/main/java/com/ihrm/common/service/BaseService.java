package com.ihrm.common.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * @Author: 846602483
 * @Date: 2019-8-4 12:31
 * @Version 1.0
 */
public class BaseService<T> {
    protected Specification<T> getSpecification(String companyId){
        return new Specification<T>(){
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
            }
        };
    }
}
