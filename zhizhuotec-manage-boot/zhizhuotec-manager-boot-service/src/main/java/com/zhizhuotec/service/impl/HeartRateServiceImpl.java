package com.zhizhuotec.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhizhuotec.entity.HeartRate;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.HeartRateMapper;
import com.zhizhuotec.service.HeartRateService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class HeartRateServiceImpl extends BaseServiceImpl<HeartRate> implements HeartRateService {

	@Override
	@Resource
	public void setMapper(BaseMapper<HeartRate> mapper) {
		super.setMapper(mapper);
	}

	@Resource
	private HeartRateMapper heartRateMapper;

	public void setHeartRateMapper(HeartRateMapper heartRateMapper) {
		this.heartRateMapper = heartRateMapper;
	}
}
