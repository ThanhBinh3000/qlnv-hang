package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBBKetThucNKReq extends BaseRequest {
    private Long id;
    private String loaiDc;
    private String loaiQdinh;
    private String loaiVthh;
    private String cloaiVthh;
    private Integer nam;
    private String soBb;
    private LocalDate ngayLap;
    private String maDvi;
    private Long qhnsId;
    private String maQhns;
    private Long qDinhDccId;
    private String soQdinhDcc;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDaDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private LocalDate ngayBatDauNhap;
    private LocalDate ngayKetThucNhap;
    private BigDecimal tongSlTheoQd;
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private Long ktvBQuan;
    private String tenKtvBQuan;
    private Long keToanTruong;
    private String tenKeToanTruong;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyetKtv;

    private LocalDate ngayPDuyetKtv;
    private Long nguoiPDuyetKt;

    private LocalDate ngayPDuyetKt;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;
    private String type;
    private List<DcnbBBKetThucNKDtl> bcnbBBKetThucNKDtl = new ArrayList<>();
    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;
    private LocalDate tuNgayThoiHanNhap;
    private LocalDate denNgayThoiHanNhap;
}
