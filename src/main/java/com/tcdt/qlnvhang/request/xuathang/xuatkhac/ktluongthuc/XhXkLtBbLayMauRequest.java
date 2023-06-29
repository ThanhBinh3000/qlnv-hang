package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtBbLayMauDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkLtBbLayMauRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String loaiBienBan;
    private String maQhNs;
    private Long idTongHop;
    private String maDanhSach;
    private String tenDanhSach;
    private String ktvBaoQuan;
    private String soBienBan;
    private LocalDate ngayLayMau;
    private String dviKiemNghiem;
    private String diaDiemLayMau;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private Integer soLuongMau;
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

    private String dvql;
    private String ngayLayMauTu;
    private String ngayLayMauDen;

    private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();

    private List<FileDinhKemReq> canCu =new ArrayList<>();


    private List<FileDinhKemReq> fileDinhKemNiemPhong =new ArrayList<>();

    private List<XhXkLtBbLayMauDtl> nguoiLienQuan = new ArrayList<>();

}
