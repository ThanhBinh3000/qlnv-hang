package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhDxuatKhMttHdrReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    @NotNull(message = "Không được để trống")
    private String loaiHinhNx;
    @NotNull(message = "Không được để trống")
    private String kieuNx;
    private String tenDuAn;
    private Integer namKh;
    @NotNull(message = "Không được để trống")
    private String soDxuat;
    @NotNull(message = "Không được để trống")
    private String trichYeu;
    private String soQd;
    private String trangThai;
    private String trangThaiTh;
    private Date ngayTao;
    private String nguoiTao;
    private Date ngaySua;
    private  String nguoiSua;
    private String ldoTuchoi;
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
    private Date ngayPduyet;
    private String nguoiPduyet;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private String giaMua;
    private String giaChuaThue;
    private String giaCoThue;
    private String thueGtgt;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;


    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();


    private List<HhDxuatKhMttSlddReq> soLuongDiaDiemList = new ArrayList<>();


    private List<HhDxuatKhMttCcxdgReq> ccXdgList = new ArrayList<>();


}
