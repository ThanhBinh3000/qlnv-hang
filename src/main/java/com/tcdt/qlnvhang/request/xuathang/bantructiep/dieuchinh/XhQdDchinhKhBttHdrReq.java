package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBttHdrReq extends BaseRequest {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String soQdDc;
    private LocalDate ngayKyDc;
    private LocalDate ngayHluc;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private Long idQdGoc;
    private String soQdGoc;
    private LocalDate ngayKyQdGoc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String trangThai;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    List<XhQdDchinhKhBttDtlReq> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayKyDcTu;
    private LocalDate ngayKyDcDen;
}
