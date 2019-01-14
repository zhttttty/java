package com.zhizhuotec.service.impl;

import javax.annotation.Resource;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhizhuotec.entity.Sleeping;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.SleepingMapper;
import com.zhizhuotec.service.SleepingService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class SleepingServiceImpl extends BaseServiceImpl<Sleeping> implements SleepingService {

	@Override
	@Resource
	public void setMapper(BaseMapper<Sleeping> mapper){
		super.setMapper(mapper);
	}
	
	@Resource
	private SleepingMapper sleepingMapper;

	public void setSleepingMapper(SleepingMapper sleepingMapper) {
		this.sleepingMapper = sleepingMapper;
	}
	
}
