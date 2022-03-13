package com.tcdt.qlnvhang.request.object.khlcnt;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class KhLuaChonNhaThauSearchReq extends BaseRequest {
	private String soQdinh;
	private LocalDate tuNgay;
	private LocalDate denNgay;
	private String noiDung;
}
