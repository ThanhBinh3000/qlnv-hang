package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhKqBttHdrReq extends BaseRequest {
    private Long id;
    private Integer namKh;
    private String soQdKq;
    private LocalDate ngayKy;
    private LocalDate ngayHluc;
    private Long idQdPd;
    private String soQdPd;
    private Long idChaoGia;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String maDvi;
    private String diaDiemChaoGia;
    private LocalDate ngayMkho;
    private LocalDate ngayKthuc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ghiChu;
    private Integer slHdDaKy;
    private Integer slHdChuaKy;
    private BigDecimal tongSoLuong;
    private BigDecimal tongGiaTriHdong;
    private String trangThai;
    private String trangThaiHd;
    private String trangThaiXh;
    private Long idQdDc;
    private String soQdDc;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDaKy = new ArrayList<>();
    private List<XhQdPdKhBttDviReq> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayCgiaTu;
    private LocalDate ngayCgiaDen;
}