package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import com.tcdt.qlnvhang.request.BaseRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QlnvSoKhoSearchReq extends BaseRequest{
	String soKho;
	String maDvi;
	String maKho;
	Date tuNgayLap;
	Date denNgayLap;
}
