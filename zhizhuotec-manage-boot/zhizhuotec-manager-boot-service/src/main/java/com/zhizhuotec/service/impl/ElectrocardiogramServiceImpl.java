package com.zhizhuotec.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhizhuotec.entity.Electrocardiogram;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.ElectrocardiogramMapper;
import com.zhizhuotec.service.ElectrocardiogramService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class ElectrocardiogramServiceImpl extends BaseServiceImpl<Electrocardiogram>
		implements ElectrocardiogramService {

	@Override
	@Resource
	public void setMapper(BaseMapper<Electrocardiogram> mapper) {
		super.setMapper(mapper);
	}

	@Resource
	private ElectrocardiogramMapper electrocardiogramMapper;

	public void setElectrocardiogramMapper(ElectrocardiogramMapper electrocardiogramMapper) {
		this.electrocardiogramMapper = electrocardiogramMapper;
	}

}
