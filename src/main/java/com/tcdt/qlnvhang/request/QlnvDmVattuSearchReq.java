package com.tcdt.qlnvhang.request;

import lombok.Data;

import java.util.List;

@Data
public class QlnvDmVattuSearchReq extends BaseRequest {
	String ma;
	String ten;
	String dviQly;
	String maDvi;
}