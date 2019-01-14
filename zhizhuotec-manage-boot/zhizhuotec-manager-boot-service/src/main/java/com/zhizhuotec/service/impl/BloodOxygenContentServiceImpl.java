package com.zhizhuotec.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhizhuotec.entity.BloodOxygenContent;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.BloodOxygenContentMapper;
import com.zhizhuotec.service.BloodOxygenContentService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class BloodOxygenContentServiceImpl extends BaseServiceImpl<BloodOxygenContent>
		implements BloodOxygenContentService {

	@Override
	@Resource
	public void setMapper(BaseMapper<BloodOxygenContent> mapper) {
		super.setMapper(mapper);
	}

	@Resource
	private BloodOxygenContentMapper bloodOxygenContentMapper;

	public void setBloodOxygenContentMapper(BloodOxygenContentMapper bloodOxygenContentMapper) {
		this.bloodOxygenContentMapper = bloodOxygenContentMapper;
	}

}
