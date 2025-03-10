package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品 id 查询套餐 id
     * @param dishId
     * @return
     */
    List<Long> getSetmealIdsByDishId(List<Long> dishId);
}
