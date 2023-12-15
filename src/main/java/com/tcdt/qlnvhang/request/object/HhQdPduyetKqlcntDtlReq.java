package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdPduyetKqlcntDtlReq {
	private Long id;
	private Long idGoiThau;
	private Long idQdPdHdr;
	private Long idNhaThau;
	private BigDecimal donGiaVat;
	private BigDecimal thanhTienNhaThau;
	private String trangThai;
	private String type;
	private String tenNhaThau;
	private String dienGiaiNhaThau;
}
