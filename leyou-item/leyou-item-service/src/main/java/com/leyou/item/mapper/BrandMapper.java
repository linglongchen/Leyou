package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Administrator
 */
public interface BrandMapper extends Mapper<Brand> {

    /**
     *
     * @param cid
     * @param bid
     */
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据分类id查询品牌列表
     * @param cid
     * @return
     */
    List<Brand> selectBrandByCid(Long cid);
}
