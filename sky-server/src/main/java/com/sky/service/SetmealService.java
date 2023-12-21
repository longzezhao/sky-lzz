package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void save(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void delete(List<Long> ids);

    SetmealVO selectById(Long id);

    void update(SetmealDTO setmealDTO);

    void startStop(int status, Long id);
}
