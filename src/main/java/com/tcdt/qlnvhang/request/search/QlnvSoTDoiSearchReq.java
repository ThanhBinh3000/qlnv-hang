package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import com.tcdt.qlnvhang.request.BaseRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QlnvSoTDoiSearchReq extends BaseRequest{
	String soTheodoi;
	String maDvi;
	String maKho;
	Date tuNgayLap;
	Date denNgayLap;
}
