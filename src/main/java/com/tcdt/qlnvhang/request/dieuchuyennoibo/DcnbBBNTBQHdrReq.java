package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBBNTBQHdrReq extends BaseRequest {

    private Long id;
    private String loaiDc;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBban;
    private LocalDate ngayLap;
    private LocalDate ngayKetThucNt;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private String thuKho;
    private String kthuatVien;
    private String keToan;
    private String ldChiCuc;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String loaiHinhKho;
    private Double tichLuongKhaDung;
    private String dsPhieuNhapKho;
    private Double slThucNhapDc;
    private String soBbNhapDayKho;
    private Long bbNhapDayKhoId;
    private String hinhThucBaoQuan;
    private String phuongThucBaoQuan;
    private Double dinhMucDuocGiao;
    private Double dinhMucTT;
    private Double tongKinhPhiDaTh;
    private Double tongKinhPhiDaThBc;
    private String maDiemKhoXuat;
    private String tenDiemKhoXuat;
    private String maNhaKhoXuat;
    private String tenNhaKhoXuat;
    private String maNganKhoXuat;
    private String tenNganKhoXuat;
    private String maLoKhoXuat;
    private String tenLoKhoXuat;
    private String nhanXet;
    private String trangThai;
    private String lyDoTuChoi;

    private List<FileDinhKem> bienBan = new ArrayList<>();
    private List<DcnbBBNTBQDtl> dcnbBBNTBQDtlDtl = new ArrayList<>();
}
