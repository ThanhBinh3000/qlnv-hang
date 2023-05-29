package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongDtl;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuKtChatLuongHdrReq {
    private Long id;

    private Integer nam;

    private String maDvi;

    private String maQhns;

    private Long qdDcId;

    private String soQdinhDc;

    private String soPhieu;

    private LocalDate ngayLapPhieu;

    private String nguoiKt;

    private Long nguoiKtId;

    private Long tpNguoiKtId;

    private String tpNguoiKt;

    private String maDiemKho;

    private String tenDiemKho;

    private String maNhaKho;

    private String tenNhaKho;

    private String maNganKho;

    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;

    private String tenThuKho;

    private Long thuKhoId;

    private String soBbLayMau;

    private Long bbLayMauId;

    private LocalDate ngayLayMau;

    private LocalDate ngayKiem;

    private String loaiVthh;

    private String tenLoaiVthh;

    private String cloaiVthh;

    private String tenCloaiVthh;

    private String hinhThucBq;

    private String danhGiaCamQuan;

    private String nhanXetKetLuan;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;

    private String trangThai;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private String lyDoTuChoi;

    private Long thayDoiThuKho;

    private LocalDate ngayDuyetTp;

    private Long nguoiDuyetTp;

    private Long bbTinhKhoId;

    private String soBbTinhKho;

    private LocalDate ngayXuatDocKho;

    private Long bbHaoDoiId;

    private String soBbHaoDoi;

    private List<FileDinhKemReq> bienBanLayMauDinhKem = new ArrayList<>();

    private List<DcnbPhieuKtChatLuongDtl> dcnbPhieuKtChatLuongDtl = new ArrayList<>();
}
