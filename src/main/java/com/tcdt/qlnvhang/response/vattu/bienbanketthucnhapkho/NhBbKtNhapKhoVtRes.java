package com.tcdt.qlnvhang.response.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBbKtNhapKhoVtRes extends SoBienBanPhieuRes {
    private Long id;
    private Long qdgnvnxId;
    private String soQuyetDinhNhap;
    private Long bbChuanBiKhoId;
    private String soBbChuanBiKho;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String capDvi;
    private String soBienBan;
    private LocalDate ngayKetThucKho;
    private String thuTruongDonVi;
    private String keToanDonVi;
    private String kyThuatVien;
    private String thuKho;

    private String maVatTuCha;
    private String tenVatTuCha;
    private String maVatTu;
    private String tenVatTu;

    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNganLo;
    private String tenNganLo;

    private LocalDate ngayBatDauNhap;
    private LocalDate ngayKetThucNhap;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private String lyDoTuChoi;
    private Integer so;
    private Integer nam;

    private List<NhBbKtNhapKhoVtCtRes> chiTiets = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
