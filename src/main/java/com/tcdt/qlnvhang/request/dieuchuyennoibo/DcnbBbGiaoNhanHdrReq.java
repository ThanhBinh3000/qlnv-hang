package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanTTDtl;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBbGiaoNhanHdrReq extends BaseRequest {

    private Long id;
    private String loaiQdinh;
    private String loaiDc;
    private String typeQd;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private LocalDate ngayLap;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private String soBbKtNhapKho;
    private Long idBbKtNhapKho;
    private Long idKeHoachDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String soHoSoKyThuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String dviTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQdDcCuc;
    private String ghiChu;
    private String ketLuan;
    private Long idCanBo;
    private Long idLanhDao;
    private String trangThai;
    private String lyDoTuChoi;
    private Boolean thayDoiThuKho;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang;
    private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();

    private List<DcnbBbGiaoNhanDtl> danhSachDaiDien = new ArrayList<>();
    private List<DcnbBbGiaoNhanTTDtl> danhSachBangKe = new ArrayList<>();

    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;
    private LocalDate tuNgayThoiHanNhap;
    private LocalDate denNgayThoiHanNhap;
}
