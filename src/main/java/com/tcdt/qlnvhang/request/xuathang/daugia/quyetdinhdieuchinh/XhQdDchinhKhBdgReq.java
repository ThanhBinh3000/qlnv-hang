//package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;
//
//import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
//import com.tcdt.qlnvhang.request.BaseRequest;
//import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//public class XhQdDchinhKhBdgReq extends BaseRequest {
//    private Long id;
//    private String maDvi;
//    private Integer nam;
//    private String soCongVan;
//    private LocalDate ngayTaoCongVan;
//    private String trichYeu;
//    private Long idQdPd;
//    private String soQdPd;
//    private Integer lanDieuChinh;
//    private LocalDate ngayKyQd;
//    private String soQdCc;
//    private String soQdDc;
//    private String soQdCanDc;
//    private Long idDcGoc;
//    private LocalDate ngayKyDc;
//    private LocalDate ngayHlucDc;
//    private String noiDungDieuChinh;
//    private String loaiHinhNx;
//    private String kieuNx;
//    private String loaiVthh;
//    private String cloaiVthh;
//    private String moTaHangHoa;
//    private String tchuanCluong;
//    private Integer slDviTsan;
//    private Integer slHdongDaKy;
//    private Integer thoiHanGiaoNhan;
//    private String trangThai;
//    private List<FileDinhKemJoinTable> fileToTrinh = new ArrayList<>();
//    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
//    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
//    private List<XhQdPdKhBdgDtlReq> children;
//    private LocalDate ngayKyDcTu;
//    private LocalDate ngayKyDcDen;
//    private String dvql;
//}