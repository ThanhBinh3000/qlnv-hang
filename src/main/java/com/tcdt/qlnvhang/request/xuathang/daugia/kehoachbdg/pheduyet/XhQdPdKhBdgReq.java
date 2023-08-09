package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private LocalDate ngayHluc;
    private Long idThHdr;
    private String soTrHdr;
    private Long idTrHdr;
    private String trichYeu;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String tchuanCluong;
    private Boolean lastest;
    private String phanLoai;
    private Long idGoc;
    private String soQdCc;
    private Integer slDviTsan;
    private String loaiHinhNx;
    private String kieuNx;
    private String trangThai;
    private List<XhQdPdKhBdgDtlReq> children;
    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
    private LocalDate ngayKyQdTu;
    private LocalDate ngayKyQdDen;
    private String dvql;
}

