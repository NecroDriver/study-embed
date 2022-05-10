package com.xin.web.pojo;

import org.springframework.util.ObjectUtils;

/**
 * 分页对象
 *
 * @author creator mafh 2018/4/26 10:15
 * @author updater mafh
 * @version 1.0.0
 */
public class Pageable {

    /**
     * 当前页
     */
    private Integer pageNo = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    private static Pageable pageable = new Pageable();

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取分页对象（单例）
     *
     * @param pageNo   当前页
     * @param pageSize 页面记录数
     * @return 对象
     */
    public static Pageable getPageable(Integer pageNo, Integer pageSize) {
        if (!ObjectUtils.isEmpty(pageNo)) {
            pageable.setPageNo(pageNo);
        }
        if (!ObjectUtils.isEmpty(pageSize)) {
            pageable.setPageSize(pageSize);
        }
        return pageable;
    }

}
