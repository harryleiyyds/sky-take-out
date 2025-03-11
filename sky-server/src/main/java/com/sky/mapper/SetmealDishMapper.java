package com.sky.mapper;

import com.sky.annotation.AutoFIll;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
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

    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes
     */
    @AutoFIll(value = OperationType.INSERT)
    void insertBatch(List<SetmealDish> setmealDishes);
}
