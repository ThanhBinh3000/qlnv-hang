package com.tcdt.qlnvhang.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPheduyetKhMttDxReq {
    @ApiModelProperty(notes = "bắt buộc set phải đối với updata")
    private Long id;
    private Long idDxuat;
    private Long idPduyetHdr;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private BigDecimal giaMua;
    private BigDecimal giaChuaThue;
    private BigDecimal giaCoThue;
    private BigDecimal thueGtgt;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private String nguonVon;
    private String tenChuDt;
    private List<HhQdPheduyetKhMttSLDDReq> hhQdPheduyetKhMttSLDDList = new ArrayList<>();

}
