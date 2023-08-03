package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoDtl;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBbNhapDayKhoHdrReq extends BaseRequest {
    private Long id;
    private String loaiQdinh;
    private Boolean thayDoiThuKho;
    private String typeQd;
    private String loaiDc;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private LocalDate ngayLap;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private Long idDiaDiemKho;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String dviTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQdDcCuc;
    private Long idThuKho;
    private Long idKyThuatVien;
    private Long idKeToan;
    private Long idLanhDao;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private List<DcnbBbNhapDayKhoDtl> children = new ArrayList<>();
    private LocalDate tuNgayBdNhap;
    private LocalDate denNgayBdNhap;
    private LocalDate tuNgayKtNhap;
    private LocalDate denNgayKtNhap;
    private LocalDate tuNgayThoiHanNh;
    private LocalDate denNgayThoiHanNh;

    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
}
