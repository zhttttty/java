package com.zhizhuotec.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhizhuotec.entity.BloodPressure;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.BloodPressureMapper;
import com.zhizhuotec.service.BloodPressureService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class BloodPressureServiceImpl extends BaseServiceImpl<BloodPressure> implements BloodPressureService {

	@Override
	@Resource
	public void setMapper(BaseMapper<BloodPressure> mapper) {
		super.setMapper(mapper);
	}

	@Resource
	private BloodPressureMapper bloodPressureMapper;

	public void setBloodPressureMapper(BloodPressureMapper bloodPressureMapper) {
		this.bloodPressureMapper = bloodPressureMapper;
	}

}
