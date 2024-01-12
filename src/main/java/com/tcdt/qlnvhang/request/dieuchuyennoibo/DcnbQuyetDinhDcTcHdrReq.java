package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcTcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcTcTTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenTongCucDtl;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbQuyetDinhDcTcHdrReq implements Serializable {
    private Long id;
    private String loaiDc;
    private String tenLoaiDc;
    private Integer nam;
    private String soQdinh;
    private LocalDate ngayKyQdinh;
    private LocalDate ngayDuyetTc;
    private Long nguoiDuyetTcId;
    private String trichYeu;
    private String maDvi;
    private String tenDvi;
    private String maThop;
    private Long idThop;
    private String maDxuat;
    private Long idDxuat;
    private String type; // loại TH (tổng hợp), DX (đề xuất)
    private String trangThai;
    private String lyDoTuChoi;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String tenTrangThai;
    private String tenLoaiHinhNhapXuat;
    private String tenKieuNhapXuat;
    private String ysKienVuKhac;
    private List<FileDinhKemReq> canCu = new ArrayList<>();
    private List<FileDinhKemReq> quyetDinh = new ArrayList<>();
    private List<DcnbQuyetDinhDcTcTTDtl> danhSachQuyetDinhChiTiet = new ArrayList<>();
    private List<THKeHoachDieuChuyenTongCucDtl> quyetDinhPdDtl = new ArrayList<>();
    private List<DcnbQuyetDinhDcTcDtl> danhSachQuyetDinh = new ArrayList<>();
}
