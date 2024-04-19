package org.dee.framework.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

public interface IWebService<T> {

    /**
     * 查询分页数据
     * @param param 实体参数
     */
    Page<T> queryPage(T param);

    /**
     * 查询所有数据
     * @param param 实体参数
     */
    List<T> queryList(T param);

    /**
     * 查询单个数据
     * @param id 主键
     */
    T getById(Serializable id);

    /**
     * 新增数据
     * @param t 实体对象
     */
    void add(T t);

    /**
     * 批量新增数据
     * @param entities 实体对象数组
     */
    void addBatch(List<T> entities);

    /**
     * 修改数据
     * @param t 实体对象
     */
    void update(T t);

    /**
     * 批量修改数据
     * @param entities 实体对象数组
     */
    void updateBatch(List<T> entities);

    /**
     * 删除数据
     * @param id 主键列表
     */
    void delete(Serializable id);

    /**
     * 批量删除数据
     * @param ids 主键列表
     */
    void deleteBatch(List<Serializable> ids);

    /**
     * 下载导入模版
     * @param tempCode 模版编码
     * @return 下载导入模版
     */
    void downloadTemplate(String tempCode, HttpServletResponse response);

    /**
     * 导入数据
     * @param tempCode 模版编码
     * @param file 导入文件
     * @param param 实体参数
     * @return 导入数据
     */
    void importExcel(String tempCode, MultipartFile file, T param);

    /**
     * 导出数据
     * @param tempCode 模版编码
     * @param param 实体参数
     * @return 导出结果
     */
    void exportExcel(String tempCode, T param, HttpServletResponse response);

}
