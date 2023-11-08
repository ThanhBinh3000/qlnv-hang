package com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdNvXhBttHdrReq extends BaseRequest {
    private Long id;
    private String maDvi;
    private Integer namKh;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String soBangKeBanLe;
    private Long idQdPd;
    private String soQdPd;
    private Long idQdDc;
    private String soQdDc;
    private Long idChaoGia;
    private String maDviTsan;
    private String tenBenMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private LocalDate tgianGiaoNhan;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private Long idTinhKho;
    private String soTinhKho;
    private Long idHaoDoi;
    private String soHaoDoi;
    private String trangThaiXh;
    private String trangThai;
    private String lyDoTuChoi;
    private List<String> listMaDviTsan = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhQdNvXhBttDtlReq> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayTaoTu;
    private LocalDate ngayTaoDen;
    private String soBienBan;
    private LocalDate ngayLayMauTu;
    private LocalDate ngayLayMauDen;
    private String maDviCon;
}