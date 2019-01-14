package com.zhizhuotec.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.Family;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.FamilyMapper;
import com.zhizhuotec.service.FamilyService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class FamilyServiceImpl extends BaseServiceImpl<Family> implements FamilyService {

	@Override
	@Resource
	public void setMapper(BaseMapper<Family> mapper) {
		super.setMapper(mapper);
	}

	private FamilyMapper familyMapper;

	@Resource
	public void setFamilyMapper(FamilyMapper familyMapper) {
		this.familyMapper = familyMapper;
	}

	@Override
	public List<Map<String, Object>> getNotificationMsg(List<Map<String, Object>> list) {
		return familyMapper.getNotificationMsg(list);
	}

	@Override
	public List<Map<String, Object>> getFamilyMsg(List<Map<String, Object>> list) {
		return familyMapper.getFamilyMsg(list);
	}

	@Override
	public List<Family> findFamilyById(String id) {
		return familyMapper.findFamilyById(id);
	}

	@Override
	public boolean addFamily(String uId, String familysId) {
		boolean re = false, updateSelf = true, insertSelf = false, updateFamily = true;
		List<Family> list = findFamilyById(uId);
		List<Map<String, Object>> familyList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		Family family = new Family();
		family.setUserId(uId);
		if (list.size() > 0) {
			// 用户家庭成员列表
			familyList = JSON.parseObject(list.get(0).getFamilyList(), new TypeReference<List<Map<String, Object>>>() {
			});
			for (Map<String, Object> map2 : familyList) {
				// 查询id是否已经是用户的家庭成员
				if (familysId.equals(map2.get("id"))) {
					// 已经是家庭成员，不用添加列表
					updateSelf = false;
					break;
				}
			}
			if (updateSelf) {
				map.put("id", familysId);
				// 不是家庭成员，添加列表
				familyList.add(map);
				family.setFamilyList(JSON.toJSONString(familyList, SerializerFeature.DisableCircularReferenceDetect));
			}
		} else {
			// 不是家庭成员，添加列表(第一次添加列表)
			family.setId(Utils.getUUID());
			map.put("id", familysId);
			familyList.add(map);
			family.setFamilyList(JSON.toJSONString(familyList, SerializerFeature.DisableCircularReferenceDetect));
			insertSelf = true;
		}
		if (updateSelf || insertSelf) {
			// 查找对方家庭成员列表(逻辑同上)
			list = findFamilyById(familysId);
			familyList = new ArrayList<Map<String, Object>>();
			map = new HashMap<String, Object>();
			if (list.size() > 0) {
				familyList = JSON.parseObject(list.get(0).getFamilyList(),
						new TypeReference<List<Map<String, Object>>>() {
						});
				for (Map<String, Object> map2 : familyList) {
					// 查询id是否已经是用户的家庭成员
					if (family.getUserId().equals(map2.get("id"))) {
						// 已经是家庭成员，不用添加列表
						updateFamily = false;
						break;
					}
				}
				if (updateFamily) {
					if (updateSelf && !insertSelf) {
						// 更新用户自己的家庭成员列表
						if (familyMapper.updateFamily(family) > 0) {
							re = true;
						}
					} else {
						// 第一次添加用户自己的家庭成员列表
						if (familyMapper.insertFamily(family) > 0) {
							re = true;
						}
					}
					if (re) {
						map.put("id", family.getUserId());
						familyList.add(map);
						family.setUserId(familysId);
						family.setFamilyList(
								JSON.toJSONString(familyList, SerializerFeature.DisableCircularReferenceDetect));
						// 更新对方的家庭成员列表
						if (familyMapper.updateFamily(family) > 0) {
							re = true;
						} else {
							re = false;
						}
					}
				}
			} else {
				// 逻辑同上
				if (updateSelf && !insertSelf) {
					if (familyMapper.updateFamily(family) > 0) {
						re = true;
					}
				} else {
					if (familyMapper.insertFamily(family) > 0) {
						re = true;
					}
				}
				if (re) {
					family.setId(Utils.getUUID());
					map.put("id", family.getUserId());
					familyList.add(map);
					family.setUserId(familysId);
					family.setFamilyList(
							JSON.toJSONString(familyList, SerializerFeature.DisableCircularReferenceDetect));
					if (familyMapper.insertFamily(family) > 0) {
						re = true;
					} else {
						re = false;
					}
				}
			}
		}
		return re;
	}

	@Override
	public int updateRemark(String uId, String familysId, String remark) {
		int res = 0;
		List<Family> list = findFamilyById(uId);
		if (list.size() > 0) {
			// 获取家庭成员列表
			List<Map<String, Object>> familyList = JSON.parseObject(list.get(0).getFamilyList(),
					new TypeReference<List<Map<String, Object>>>() {
					});
			int size = familyList.size();
			if (size > 0) {
				boolean re = false;
				for (int i = 0; i < size; i++) {
					if (familysId.equals(familyList.get(i).get("id"))) {
						// 修改备注
						familyList.get(i).put("remark", remark);
						re = true;
						break;
					}
				}
				if (re) {
					Family family = new Family();
					family.setUserId(uId);
					// 更新列表
					family.setFamilyList(
							JSON.toJSONString(familyList, SerializerFeature.DisableCircularReferenceDetect));
					if (familyMapper.updateFamily(family) > 0) {
						// 备注修改成功
						res = 1;
					} else {
						// 备注修改失败
						res = 0;
					}
				} else {
					// 你还不是他的家庭成员
					res = 3;
				}
			} else {
				// 用户还没有家庭成员
				res = 2;
			}
		} else {
			// 用户还没有家庭成员
			res = 2;
		}
		return res;
	}

	@Override
	public int deletFamily(String uId, String familysId) {
		boolean deletSelf = false;
		boolean deletFamily = false;
		int res = 0;
		List<Family> list = findFamilyById(uId);
		if (list.size() > 0) {
			// 获取家庭成员列表
			List<Map<String, Object>> familyList = JSON.parseObject(list.get(0).getFamilyList(),
					new TypeReference<List<Map<String, Object>>>() {
					});
			int size = familyList.size();
			if (size > 0) {
				Family family = new Family();
				family.setUserId(uId);
				for (int i = 0; i < size; i++) {
					if (familysId.equals(familyList.get(i).get("id"))) {
						// 将对方从自己的列表删除
						familyList.remove(i);
						family.setFamilyList(
								JSON.toJSONString(familyList, SerializerFeature.DisableCircularReferenceDetect));
						deletSelf = true;
						break;
					}
				}
				if (deletSelf) {
					// 获取对方的家庭成员列表
					list = findFamilyById(familysId);
					familyList = JSON.parseObject(list.get(0).getFamilyList(),
							new TypeReference<List<Map<String, Object>>>() {
							});
					size = familyList.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							if (family.getUserId().equals(familyList.get(i).get("id"))) {
								// 将自己从对方的列表删除
								familyList.remove(i);
								deletFamily = true;
								break;
							}
						}
						if (deletFamily) {
							if (familyMapper.updateFamily(family) > 0) {
								family.setUserId(familysId);
								// 更新列表
								family.setFamilyList(JSON.toJSONString(familyList,
										SerializerFeature.DisableCircularReferenceDetect));
								if (familyMapper.updateFamily(family) > 0) {
									// 家庭成员删除成功
									res = 1;
								} else {
									// 家庭成员删除失败
									res = 0;
								}
							} else {
								// 家庭成员删除失败
								res = 0;
							}
						} else {
							// 你还不是他的家庭成员
							res = 3;
						}
					} else {
						// 你还不是他的家庭成员
						res = 3;
					}
				} else {
					// 你还不是他的家庭成员
					res = 3;
				}
			} else {
				// 用户还没有家庭成员
				res = 2;
			}
		} else {
			// 用户还没有家庭成员
			res = 2;
		}
		return res;
	}

}
