package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class XhThopDxKhBdgDtlReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String soDxuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKy;
    private String trichYeu;
    private Integer soDviTsan;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienDatTruoc;
    private BigDecimal tongSoLuong;

    @Transient
    private XhDxKhBanDauGiaReq dxKhBanDauGiaReq ;
}
