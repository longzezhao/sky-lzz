package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /*
     * 根据菜品id查询对应的套餐id
     * */
    //select setmeal_id from setmeal_dish where dish_id in(1,2,3,4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    void insertBatch(List<SetmealDish> setmealDishs);


    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> selectBySetmealId(Long setmealId);
}
