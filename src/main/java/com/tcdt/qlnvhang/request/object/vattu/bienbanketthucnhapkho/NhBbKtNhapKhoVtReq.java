package com.tcdt.qlnvhang.request.object.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBbKtNhapKhoVtReq {
    private Long id;
    private Long qdgnvnxId;
    private Long bbChuanBiKhoId;
    private String maDvi;
    private String capDvi;
    private String soBienBan;
    private LocalDate ngayKetThucKho;
    private String thuTruongDonVi;
    private String keToanDonVi;
    private String kyThuatVien;
    private String thuKho;

    private String maVatTuCha;
    private String maVatTu;

    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maNganLo;

    private LocalDate ngayBatDauNhap;
    private LocalDate ngayKetThucNhap;
    private String trangThai;
    private String lyDoTuChoi;
    private Integer so;
    private Integer nam;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;

    private List<NhBbKtNhapKhoVtCtReq> chiTiets = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();
}
