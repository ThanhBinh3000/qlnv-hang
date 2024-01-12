package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhBbLayMauRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String loaiBienBan;
    private String maQhNs;
    private Long idQdGiaoNvXh;
    private String soQdGiaoNvXh;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private LocalDate thoiGianXuat;
    private Integer soLanLm;
    private String soBienBan;
    private LocalDate ngayLayMau;
    private LocalDate ngayXuatLayMau;
    private String dviKiemNghiem;
    private String diaDiemLayMau;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private String maDviTsan;
    private Integer namNhap;
    private String maDiaDiem;
    private Integer soLuongMau;
    private Integer slTonKho;
    private String ppLayMau;
    private String chiTieuKiemTra;
    private Boolean ketQuaNiemPhong;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<XhXkVtBhBbLayMauDtl> bbLayMauDtl = new ArrayList<>();

    //search params
    private LocalDate ngayLayMauTu;
    private LocalDate ngayLayMauDen;
    private String dvql;
}
