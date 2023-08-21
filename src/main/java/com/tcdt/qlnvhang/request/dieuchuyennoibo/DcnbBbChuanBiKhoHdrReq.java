package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoDtl;
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
public class DcnbBbChuanBiKhoHdrReq extends BaseRequest {

    private Long id;
    private String loaiDc;
    private String typeQd;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private BigDecimal soLuongQdDcCuc;
    private String soBban;
    private LocalDate ngayLap;
    private LocalDate ngayKetThucNt;
    private Long idKeHoachDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String loaiHinhKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private BigDecimal tichLuong;
    private Long idPhieuNhapKho;
    private String soPhieuNhapKho;
    private BigDecimal soLuongPhieuNhapKho;
    private String hthucKlot;
    private String pthucBquan;
    private BigDecimal dinhMucGiao;
    private BigDecimal dinhMucThucTe;
    private String nhanXet;
    private String trangThai;
    private String lyDoTuChoi;
    private Boolean thayDoiThuKho;
    private String loaiQdinh;
    private List<DcnbBbChuanBiKhoDtl> children = new ArrayList<>();
    private LocalDate tuNgayLapBb;
    private LocalDate denNgayLapBb;
    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;

    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
}
