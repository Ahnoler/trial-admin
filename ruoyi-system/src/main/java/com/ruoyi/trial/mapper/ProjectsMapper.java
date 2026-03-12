package com.ruoyi.trial.mapper;

import java.util.List;
import com.ruoyi.trial.domain.Projects;

/**
 * 项目管理Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface ProjectsMapper 
{
    /**
     * 查询项目管理
     * 
     * @param id 项目管理主键
     * @return 项目管理
     */
    public Projects selectProjectsById(Long id);

    /**
     * 查询项目管理列表
     * 
     * @param projects 项目管理
     * @return 项目管理集合
     */
    public List<Projects> selectProjectsList(Projects projects);

    /**
     * 新增项目管理
     * 
     * @param projects 项目管理
     * @return 结果
     */
    public int insertProjects(Projects projects);

    /**
     * 修改项目管理
     * 
     * @param projects 项目管理
     * @return 结果
     */
    public int updateProjects(Projects projects);

    /**
     * 删除项目管理
     * 
     * @param id 项目管理主键
     * @return 结果
     */
    public int deleteProjectsById(Long id);

    /**
     * 批量删除项目管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectsByIds(Long[] ids);
}
