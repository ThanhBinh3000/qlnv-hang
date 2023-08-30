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
    private LocalDate ngayQdNv;
    private Long idHd;
    private String soHd;
    private LocalDate ngayKyHd;
    private Long idQdPd;
    private String soQdPd;
    private Long idChaoGia;
    private String maDviTsan;
    private String tenTccn;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private BigDecimal soLuongBanTrucTiep;
    private String donViTinh;
    private LocalDate tgianGnhan;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String trangThaiXh;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private String trangThai;
    private List<String> listMaDviTsan = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhQdNvXhBttDtlReq> children = new ArrayList<>();
    private String dvql;
    private String maChiCuc;
    private LocalDate ngayTaoTu;
    private LocalDate ngayTaoDen;
    private String soBienBan;
    private LocalDate ngayLayMauTu;
    private LocalDate ngayLayMauDen;
}

