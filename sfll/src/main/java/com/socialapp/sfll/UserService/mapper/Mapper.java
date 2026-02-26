package com.socialapp.sfll.UserService.mapper;

/***
 * Generic Mapper Interface
 * @param <T> Entity Type
 * @param <U> DTO Type
 */
public interface Mapper<T, U> {
    U toDto(T entity);
    void toEntity(T t, U dto);

}
