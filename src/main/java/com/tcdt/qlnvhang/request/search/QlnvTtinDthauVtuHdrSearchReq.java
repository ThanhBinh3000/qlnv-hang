package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QlnvTtinDthauVtuHdrSearchReq extends BaseRequest{

	String soQdKh;
	String maVtu;
	String tenVtu;
}
