package cn.leixd.dao;

import cn.leixd.entity.po.CompileInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompileInfoMapper {

    /**
     * 查询编译错误信息
     * @param solutionId 提交编号
     * @return
     */
    @Select("select * from compile_info where solution_id = #{solution_id}")
    CompileInfo findCompileInfoBySolutionId(@Param("solution_id") String solutionId);

    @Insert("insert into compile_info (solution_id ,title,compile_info,) " +
            "values (#{compile_info.solution_id},#{compile_info.compile_info})")
    int insertCompileInfoBySolutionId(@Param("solution_id") String solutionId, @Param("compile_info") String compile_info);
}
