package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DishService {

    /*
    * 新增菜品和对应的口味
    * */
    void saveWithFlavor(DishDTO dishDTO);

    /*
    * 菜品分页查询
    * */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteByIds(List<Long> ids);

    DishVO getById(Long id);

    void startStop(Integer status, Long id);

    void update(DishDTO dishDTO);

    List<Dish> selectByCategoryId(Long categoryId);


    List<DishVO> listWithFlavor(Long categoryId);
}
