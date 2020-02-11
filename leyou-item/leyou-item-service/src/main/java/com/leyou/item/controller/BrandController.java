package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;


    /**
     * 分页查询
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page" , defaultValue = "1") Integer page,
            @RequestParam(value = "rows" , defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy" , required = false) String sortBy,
            @RequestParam(value = "desc" , defaultValue = "false") boolean desc,
            @RequestParam(value = "key" , required = false) String key
    ){
        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,sortBy,desc,key));
    }

    /**
     * 保存品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping()
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        try{
            this.brandService.saveBrand(brand,cids);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }


    /**
     * 删除brand
     * @param id
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteBrand(@RequestParam("id") Long id){
        try {
            this.brandService.deleteBrand(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     *分局分类id查询品牌列表
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid")Long cid){
        List<Brand> list =  this.brandService.queryBrandByCid(cid);
        if (CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }


}
