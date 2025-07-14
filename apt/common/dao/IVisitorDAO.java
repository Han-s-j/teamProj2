package com.project.apt.common.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IVisitorDAO {
    
    void insertVisit(@Param("userId") String userId, @Param("ipAddress") String ipAddress);
}