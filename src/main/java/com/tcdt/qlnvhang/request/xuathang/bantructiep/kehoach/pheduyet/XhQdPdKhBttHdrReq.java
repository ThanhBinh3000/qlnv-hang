package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBttHdrReq extends BaseRequest {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private Long idGoc;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private LocalDate ngayHluc;
    private Long idThHdr;
    private Long idTrHdr;
    private String soTrHdr;
    private String trichYeu;
    private String soQdCc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String loaiHinhNx;
    private String kieuNx;
    private String tchuanCluong;
    private Boolean lastest;
    private Integer slDviTsan;
    private String slHdongDaKy;
    private String phanLoai;
    private String trangThai;
    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
    private List<XhQdPdKhBttDtlReq> children;
    private LocalDate ngayKyQdTu;
    private LocalDate ngayKyQdDen;
    private String dvql;
    private String maCuc;
}
