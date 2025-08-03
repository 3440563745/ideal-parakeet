package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    List<Setmeal> list(Setmeal setmeal);
    @Select("select sd.name, sd.copies, d.image, d.description " +  // 去掉开头的\"
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")  // 注意：SQL结尾不需要分号，MyBatis会自动处理
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
