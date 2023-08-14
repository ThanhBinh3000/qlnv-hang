package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;

import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBdgReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soQdDc;
    private LocalDate ngayKyDc;
    private LocalDate ngayHlucDc;
    private String soQdGoc;
    private Long idQdGoc;
    private LocalDate ngayKyQdGoc;
    private String trichYeu;
    private String soQdCc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String loaiHinhNx;
    private String kieuNx;
    private String tchuanCluong;
    private Integer slDviTsan;
    private String trangThai;
    private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem = new ArrayList<>();
    private List<FileDKemJoinHoSoKyThuatDtl> fileCanCu = new ArrayList<>();
    private List<XhQdDchinhKhBdgDtlReq> children = new ArrayList<>();
    private LocalDate ngayKyDcTu;
    private LocalDate ngayKyDcDen;
    private String dvql;
}

