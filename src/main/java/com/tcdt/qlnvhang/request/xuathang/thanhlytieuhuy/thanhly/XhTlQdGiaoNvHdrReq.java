package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlQdGiaoNvHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soBbQd;
    private LocalDate ngayKy;
    private LocalDate ngayKyQd;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDviTsan;
    private String toChucCaNhan;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenVthh;
    private BigDecimal soLuong;
    private String donViTinh;
    private LocalDate thoiGianGiaoNhan;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String trangThaiXh;
    private List<XhTlQdGiaoNvDtl> quyetDinhDtl = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
    private LocalDate ngayKyQdTu;
    private LocalDate ngayKyQdDen;
}