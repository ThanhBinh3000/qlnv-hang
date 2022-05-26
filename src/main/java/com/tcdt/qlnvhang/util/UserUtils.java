package com.tcdt.qlnvhang.util;

import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.UserInfo;

public class UserUtils {
	public static UserInfo getUserInfo() throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Can not get user info");

		return userInfo;
	}

}
