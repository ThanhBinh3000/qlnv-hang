package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhBbHdoiBttHdrReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soBbHaoDoi;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private Long idHd;

    private String soHd;

    private LocalDate ngayKyHd;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private Long idBbTinhKho;

    private String soBbTinhKho;

    private BigDecimal tongSlNhap;

    private LocalDate ngayKthucNhap;

    private BigDecimal tongSlXuat;

    private LocalDate ngayBdauXuat;

    private LocalDate ngayKthucXuat;

    private BigDecimal slHaoThucTe;

    private String tiLeHaoThucTe;

    private BigDecimal slHaoVuotMuc;

    private String tiLeHaoVuotMuc;

    private BigDecimal slHaoThanhLy;

    private String tiLeHaoThanhLy;

    private BigDecimal slHaoDuoiDinhMuc;

    private String tiLeHaoDuoiDinhMuc;

    private String nguyenNhan;

    private String kienNghi;

    private String ghiChu;

    private Long idThuKho;

    private Long idKtv;

    private Long idKeToan;

    private LocalDate ngayTaoTu;

    private LocalDate ngayTaoDen;

    private LocalDate ngayBdauXuatTu;

    private LocalDate ngayBdauXuatDen;

    private LocalDate ngayKthucXuatTu;

    private LocalDate ngayKthucXuatDen;

    private LocalDate ngayQdNvTu;

    private LocalDate ngayQdNvDen;

    @Transient
    private List<XhBbHdoiBttDtlReq> children = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

}
