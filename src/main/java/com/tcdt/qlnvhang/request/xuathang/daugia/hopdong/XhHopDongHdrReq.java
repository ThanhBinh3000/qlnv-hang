package com.tcdt.qlnvhang.request.xuathang.daugia.hopdong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.HhDviLquanReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhHopDongHdrReq extends BaseRequest {
    private Long id;

    private String soQdKq;

    private Integer nam;

    private LocalDate ngayKyQdKq;

    private String soQdPd;

    private String maDviTsan;

    private LocalDate tgianNkho;

    private String soHd;

    private String tenHd;

    private LocalDate ngayHluc;

    private String ghiChuNgayHluc;

    private String loaiHdong;

    private String ghiChuLoaiHdong;

    private Integer tgianThienHd;

    private Integer tgianBhanh;

    private String maDvi;

    private String diaChi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String stk;

    private String fax;

    private String moTai;

    private String uyQuyen;

    private String tenNhaThau;

    private String diaChiNhaThau;

    private String mstNhaThau;

    private String tenNguoiDdienNhaThau;

    private String chucVuNhaThau;

    private String sdtNhaThau;

    private String stkNhaThau;

    private String faxNhaThau;

    private String moTaiNhaThau;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private Double soLuong;

    private Double tongTien;

    private String ghiChu;

    // Transient
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenDvi;
    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<FileDinhKemReq> canCu = new ArrayList<>();
    @Transient
    private List<XhHopDongDtlReq> children = new ArrayList<>();
    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    //    Phụ lục

    private Long idHd;

    private String soPhuLuc;

    private LocalDate ngayHlucPhuLuc;

    private String noiDungPhuLuc;

    private LocalDate ngayHlucSauDcTu;

    private LocalDate ngayHlucSauDcDen;

    private Integer tgianThienHdSauDc;

    private String noiDungDcKhac;

    private String ghiChuPhuLuc;

    private String trangThaiPhuLuc;
    @Transient
    private String tenTrangThaiPhuLuc;

    @Transient
    private List<XhHopDongHdrReq> phuLuc = new ArrayList<>();

    @Transient
    private List<XhHopDongDtlReq> phuLucDtl = new ArrayList<>();

}
