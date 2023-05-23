package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauDtl;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBienBanLayMauHdrReq implements Serializable {
    private Long id;

    private String loaiBb;

    private LocalDate thoiHanDieuChuyen;

    private Integer nam;

    private String maDvi;

    private String tenDvi;

    private Long qhnsId;

    private String maQhns;

    private Long qDinhDccId;

    private String soQdinhDcc;

    private String ktvBaoQuan;

    private Long ktvBaoQuanId;

    private String soBbLayMau;

    private LocalDate ngayLayMau;

    private String dViKiemNghiem;

    private String diaDiemLayMau;

    private String loaiVthh;

    private String cloaiVthh;

    private String maDiemKho;

    private String tenDiemKho;

    private String maNhaKho;

    private String tenNhaKho;

    private String maNganKho;

    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;

    private Boolean thayDoiThuKho;

    private Long soLuongMau;

    private String pPLayMau;

    private String chiTieuKiemTra;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;

    private String ketQuaNiemPhong;

    private String diaDiemBanGiao;

    private String loaiDc;

    private String type;

    private String soBbTinhKho;

    private Long bbTinhKhoId;

    private LocalDate ngayXuatDocKho;

    private String soBbHaoDoi;

    private Long bbHaoDoiId;

    private List<FileDinhKemReq> canCu = new ArrayList<>();
    private List<DcnbBienBanLayMauDtl> dcnbBienBanLayMauDtl = new ArrayList<>();
}
