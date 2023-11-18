package com.tcdt.qlnvhang.request.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class HhPreviewHopDongDTO extends BaseRequest {


	private Integer namHd;

	private String tenLoaiVthh;

	private String tenCloaiVthh;

	private String donViTinh;

	private Double soLuong;

	private String moTaHangHoa;

	String tgianNkho;
	private Date tgianGiaoThucTe;
	private String tongSoLuongStr;
	private String tongThanhTienStr;

	private List<HhPreviewHopDongDtlDTO> details;

}
