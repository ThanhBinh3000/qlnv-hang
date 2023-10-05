package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;


import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHaoDoiDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlHaoDoiHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soBbHaoDoi;
    private LocalDate ngayLapBienBan;
    private Long idQdXh;
    private String soQdXh;
    private LocalDate ngayQdXh;
    private Long idDsHdr;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private Long idBbTinhKho;
    private String soBbTinhKho;
    private BigDecimal tongSlNhap;
    private LocalDate tgianKetThucNhap;
    private BigDecimal tongSlXuat;
    private BigDecimal tgianKetThucXuat;
    private BigDecimal slHaoThucTe;
    private String tiLeHaoThucTe;
    private BigDecimal slHaoVuotMuc;
    private String tiLeHaoVuotMuc;
    private BigDecimal slHaoThanhLy;
    private String tiLeHaoThanhLy;
    private BigDecimal slHaoDuoiDmuc;
    private String tiLeHaoDuoiDmuc;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private List<FileDinhKemReq> fileDinhKemReq;
}