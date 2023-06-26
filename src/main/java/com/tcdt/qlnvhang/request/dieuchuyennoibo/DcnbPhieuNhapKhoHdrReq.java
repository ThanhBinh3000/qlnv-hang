package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
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
public class DcnbPhieuNhapKhoHdrReq extends BaseRequest {

    private Long id;
    private String loaiQdinh;
    private String loaiDc;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayLap;
    private BigDecimal soNo;
    private BigDecimal soCo;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private Long idDiaDiemKho;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String soPhieuKtraCluong;
    private Long idPhieuKtraCluong;
    private String loaiVthh;
    private String cloaiVthh;
    private Long idThuKho;
    private Long idLanhDao;
    private Long idKyThuatVien;
    private String keToanTruong;
    private String hoVaTenNguoiGiao;
    private String cmndNguoiGiao;
    private String donViNguoiGiao;
    private String diaChi;
    private LocalDate tgianGiaoNhanHang;
    private String loaiHinhNx;
    private String kieuNx;
    private String bbNghiemThuBqld;
    private BigDecimal soLuongQdDcCuc;
    private String soBangKeCanHang;
    private Long idBangKeCanHang;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private List<FileDinhKem> chungTuDinhKem = new ArrayList<>();

    private List<DcnbPhieuNhapKhoDtl> children = new ArrayList<>();
}
