package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父节点查询子节点
     * @param pid
     * @return
     */
    public List<Category> queryCategoriesByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);
        return this.categoryMapper.select(category);
    }


    /**
     * 根据品牌id查询分类
     * @param bid
     * @return
     */
    public List<Category> queryByBrandId(Long bid){
        return this.categoryMapper.queryByBrandId(bid);
    }


    /**
     * 根据ids查询列表
     * @param ids
     * @return
     */
    public List<String> queryNameByIds(List<Long> ids){
        List<Category> list = this.categoryMapper.selectByIdList(ids);
        return list.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
