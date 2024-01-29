package com.tcdt.qlnvhang.request.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HhQdGiaoNvuNhapxuatDdNhapReq {

	private String maCuc;

	private String maChiCuc;

	private String maDiemKho;

	private String maNhaKho;

	private String maNganKho;

	private String maLoKho;

	private BigDecimal soLuong;

	private BigDecimal soLuongDiemKho;
	private BigDecimal slTon;

}
