package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScQuyetDinhXuatHangReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String soQd;
    private LocalDate ngayKy;
    private Long idQdSc;
    private String soQdSc;
    private LocalDate ngayKyQdSc;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private BigDecimal duToanKinhPhi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String trichYeu;
    private String trangThai;
    @Transient
    private List<FileDinhKemReq> fileDinhKemReq;
    @Transient
    private List<FileDinhKemReq> fileCanCuReq;

    //Region search
    private String maDviSr;
    private String trangThaiKtraCl;
    private LocalDate ngayTu;
    private LocalDate ngayDen;

}
