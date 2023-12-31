package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /*
    * 新增菜品和对应的口味
    * */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        String[] strings = dishDTO.getImage().split("\\?");
        dishDTO.setImage(strings[0]);
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表插入一条数据
        dishMapper.insert(dish);
        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /*
    * 菜品分页查询
    * */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /*
    * 菜品批量删除
    * */
    @Override
    public void deleteByIds(List<Long> ids) {

        //是否被套餐关联了
        List<Long> setmealIdsByDishIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIdsByDishIds != null && setmealIdsByDishIds.size() > 0){
            //当前菜品被套餐关联了,不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        for(Long id : ids){
            Dish dish = dishMapper.getById(id);
            //起售的菜品不能删除
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

        }

        dishFlavorMapper.deleteByDishId(ids);
        dishMapper.deleteByIds(ids);
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(dish.getId());
        Category category = categoryMapper.selectById(dish.getCategoryId());

        DishVO dishVO = DishVO.builder()
                .id(dish.getId())
                .name(dish.getName())
                .categoryId(dish.getCategoryId())
                .price(dish.getPrice())
                .image(dish.getImage())
                .description(dish.getDescription())
                .status(dish.getStatus())
                .categoryName(category.getName())
                .flavors(dishFlavors)
                .build();

        return dishVO;
    }

    @Override
    public void startStop(Integer status, Long id) {

        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();

        dishMapper.update(dish);
    }

    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        String image=dishDTO.getImage().split("\\?")[0];
        dishDTO.setImage(image);
        BeanUtils.copyProperties(dishDTO,dish);

        Long dishId = dish.getId();
        List<Long> longs = new ArrayList<>();
        longs.add(dishId);

        dishFlavorMapper.deleteByDishId(longs);

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if(flavors != null && flavors.size() > 0){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(flavors);
        }
        dishMapper.update(dish);
    }

    @Override
    public List<Dish> selectByCategoryId(Long categoryId) {
        List<Dish> dishes = dishMapper.selectByCategoryId(categoryId);
        return dishes;
    }


    public List<DishVO> listWithFlavor(Long categoryId) {
        List<Dish> dishList = dishMapper.selectByCategoryIdaAndStart(categoryId);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

}
