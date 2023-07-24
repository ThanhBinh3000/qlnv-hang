package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBbKtNhapKhoRequest extends BaseRequest {
    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String soBienBan;
    private String maQhns;
    private LocalDate ngayLapBienBan;
    private String soCanCu;
    private Long idCanCu;
    private String maDiaDiem; // mã địa điểm (mã lô/ngăn kho)
    private String loaiVthh;
    private String cloaiVthh;
    private String canBoLapBb;
    private String ldChiCuc;
    private String ktvBaoQuan;
    private String keToanTruong;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private LocalDate ngayBatDauNhap;
    private LocalDate ngayKetThucNhap;
    private LocalDate ngayHetHanLuuKho;
    // Số báo cáo kết quả kd mẫu
    private String soBbLayMau;
    private Long idBbLayMau;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    //search params
    private LocalDate ngayKetThucNhapKhoTu;
    private LocalDate ngayKetThucNhapKhoDen;
    private String dvql;
}
