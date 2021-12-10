package com.tcdt.qlnvhang.define;

import java.util.HashMap;
import java.util.Map;

public final class ErrorMessage {

	public static final int SUCCESS = 0;
	public static final int PERMISSION_DENIED = 2;
	public static final int NOT_EXIST = 302;

	public static Map<Integer, String> info = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 7957693488024072535L;

		{

			put(ErrorMessage.SUCCESS, "Thành công");
			put(ErrorMessage.PERMISSION_DENIED, "Bạn không có quyền truy cập chức năng này");
			put(ErrorMessage.NOT_EXIST, "Không tồn tại bản ghi");

		}
	};

	public static String getStateInfo(int key) {
		return ErrorMessage.info.get(key);
	}

}
