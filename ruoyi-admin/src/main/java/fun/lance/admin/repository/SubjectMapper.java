package fun.lance.admin.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.lance.admin.domain.entity.Subject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {
}
