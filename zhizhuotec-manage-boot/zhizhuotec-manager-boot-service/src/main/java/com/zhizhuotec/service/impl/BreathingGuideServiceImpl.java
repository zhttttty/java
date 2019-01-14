package com.zhizhuotec.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhizhuotec.entity.BreathingGuide;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.BreathingGuideMapper;
import com.zhizhuotec.service.BreathingGuideService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class BreathingGuideServiceImpl extends BaseServiceImpl<BreathingGuide> implements BreathingGuideService {

	@Override
	@Resource
	public void setMapper(BaseMapper<BreathingGuide> mapper) {
		super.setMapper(mapper);
	}

	@Resource
	private BreathingGuideMapper breathingGuideMapper;

	public void setBreathingGuideMapper(BreathingGuideMapper breathingGuideMapper) {
		this.breathingGuideMapper = breathingGuideMapper;
	}

}
