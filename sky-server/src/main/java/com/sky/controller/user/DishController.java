package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    /*@GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);

        // 构造 redis 中的 key ,规则： dish_(分类 id)
        String key = "dish_" + categoryId;

        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);

        // 查询 redis 中是否存在菜品数据
        if (list != null && !list.isEmpty()) {
            // 如果存在，直接返回，无须查询数据库
            return Result.success(list);
        }

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        // 查询起售中的菜品
        dish.setStatus(StatusConstant.ENABLE);

        // 如果不存在，查询数据库，将查询到的数据放入 redis 中
        list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }*/

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);

        // 参数校验
        if (categoryId == null) {
            throw new IllegalArgumentException("分类 id 不能为空");
        }

        // 构造Redis键，规则：dish_(分类id)
        String key = "dish_" + categoryId;

        try {
            // 查询Redis中是否存在菜品数据
            List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);

            if (list != null) { // 包含空列表的情况也从缓存返回，防止缓存穿透
                log.info("从Redis中获取菜品数据，分类id：{}", categoryId);
                return Result.success(list);
            }

            // Redis中不存在，查询数据库
            Dish dish = new Dish();
            dish.setCategoryId(categoryId);
            dish.setStatus(StatusConstant.ENABLE);

            list = dishService.listWithFlavor(dish);

            // 即使查询结果为空也缓存，避免缓存穿透
            redisTemplate.opsForValue().set(key, list, 60, TimeUnit.MINUTES);
            log.info("将菜品数据放入Redis，分类id：{}", categoryId);

            return Result.success(list);
        } catch (Exception e) {
            log.error("查询菜品异常：{}", e.getMessage(), e);
            // Redis异常时直接查询数据库
            Dish dish = new Dish();
            dish.setCategoryId(categoryId);
            dish.setStatus(StatusConstant.ENABLE);
            return Result.success(dishService.listWithFlavor(dish));
        }
    }

}
