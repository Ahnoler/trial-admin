package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.ProjectsMapper;
import com.ruoyi.trial.domain.Projects;
import com.ruoyi.trial.service.IProjectsService;

/**
 * 项目管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@Service
public class ProjectsServiceImpl implements IProjectsService 
{
    @Autowired
    private ProjectsMapper projectsMapper;

    /**
     * 查询项目管理
     * 
     * @param id 项目管理主键
     * @return 项目管理
     */
    @Override
    public Projects selectProjectsById(Long id)
    {
        return projectsMapper.selectProjectsById(id);
    }

    /**
     * 查询项目管理列表
     * 
     * @param projects 项目管理
     * @return 项目管理
     */
    @Override
    public List<Projects> selectProjectsList(Projects projects)
    {
        return projectsMapper.selectProjectsList(projects);
    }

    /**
     * 新增项目管理
     * 
     * @param projects 项目管理
     * @return 结果
     */
    @Override
    public int insertProjects(Projects projects)
    {
        projects.setCreateTime(DateUtils.getNowDate());
        return projectsMapper.insertProjects(projects);
    }

    /**
     * 修改项目管理
     * 
     * @param projects 项目管理
     * @return 结果
     */
    @Override
    public int updateProjects(Projects projects)
    {
        projects.setUpdateTime(DateUtils.getNowDate());
        return projectsMapper.updateProjects(projects);
    }

    /**
     * 批量删除项目管理
     * 
     * @param ids 需要删除的项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProjectsByIds(Long[] ids)
    {
        return projectsMapper.deleteProjectsByIds(ids);
    }

    /**
     * 删除项目管理信息
     * 
     * @param id 项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProjectsById(Long id)
    {
        return projectsMapper.deleteProjectsById(id);
    }
}
