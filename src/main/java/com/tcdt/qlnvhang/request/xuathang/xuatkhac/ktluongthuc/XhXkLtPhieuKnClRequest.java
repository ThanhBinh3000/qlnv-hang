package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkLtPhieuKnClRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soPhieu;
    private LocalDate ngayLapPhieu;
    private LocalDate ngayKnMau;
    private Long idBienBan;
    private String soBienBan;
    private LocalDate ngayLayMau;
    private Long idTongHop;
    private String maDanhSach;
    private String tenDanhSach;
    private String nguoiKn;
    private String truongPhong;
    private String thuKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String maDiaDiem;
    private String hinhThucBq;
    private String noiDung;
    private String ketLuan;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;
    private boolean KqThamDinh;

    private String dvql;
    private LocalDate ngayKnMauTu;
    private LocalDate ngayKnMauDen;

    private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();

    private List<XhXkLtPhieuKnClDtl> phieuKnClDtl= new ArrayList<>();
}
