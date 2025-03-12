package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFIll;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 新增套餐
     *
     * @param setmeal
     */
    @AutoFIll(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 根据分类 id 查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据 id 查询套餐
     *
     * @param id
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 修改套餐
     *
     * @param setmeal
     */
    @AutoFIll(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据 id 删除套餐
     * @param id
     */
    // @Delete("delete from setmeal where id = #{id}")
    // void deleteById(Long id);

    /**
     * 动态条件查询套餐
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     *
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
