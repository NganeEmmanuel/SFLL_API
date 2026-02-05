package com.socialapp.sfll.mapper;

/***
 * Generic Mapper Interface
 * @param <T> Entity Type
 * @param <U> DTO Type
 */
public interface Mapper<T, U> {
    void toDto(T entity);
    void toEntity(T t, U dto);

}
