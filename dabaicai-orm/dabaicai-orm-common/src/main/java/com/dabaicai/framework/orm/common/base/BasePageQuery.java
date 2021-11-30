package com.dabaicai.framework.orm.common.base;



/**
 * @author zhangyanbing
 * @Description: 分页条件查询基类
 * @date 2021/1/14 10:35
 */
public class BasePageQuery {

    private Integer pageNum;

    private Integer pageSize;


    public BasePageQuery(){
        initPageQuery();
    }

    public void initPageQuery(){
        if (pageNum == null){
            this.pageNum = 1;
        }
        if (pageSize == null){
            this.pageSize = 30;
        }
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
