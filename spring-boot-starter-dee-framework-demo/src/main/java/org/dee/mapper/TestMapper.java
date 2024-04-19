package org.dee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dee.entity.TestEntity;

@Mapper
public interface TestMapper extends BaseMapper<TestEntity> {
}
