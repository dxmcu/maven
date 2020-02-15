package com.kaifantech.component.service.agv.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kaifantech.bean.info.agv.AgvInfoBean;
import com.kaifantech.component.dao.agv.info.AgvInfoDao;
import com.kaifantech.init.sys.qualifier.UdfQualifier;

@Service
public class AgvInfoService {

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_AGV_INFO_DAO)
	private AgvInfoDao agvDao;

	public Integer checkAgvId(Integer agvId) {
		if (agvId == null) {
			agvId = -1;
		}
		if (agvId.equals(0)) {
			return 0;
		}
		AgvInfoBean bean = agvDao.get(agvId);
		if (bean == null) {
			return -1;
		}
		return agvId;
	}

}
