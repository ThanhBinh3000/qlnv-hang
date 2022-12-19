package com.tcdt.qlnvhang.request.object;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhDxuatKhLcntDsgthauDtlCtietVtReq {

	Long id;

	Integer soLuong;

	String maDvi;

	List<HhDxuatKhLcntDsgthauDtlCtietVt1Req> children = new ArrayList<>();

}
