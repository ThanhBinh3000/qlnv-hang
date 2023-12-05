package com.tcdt.qlnvhang.request.object;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinKeLot;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhBbNghiemThuKlstPreview {
    private Long id;
    String soBbNtBq;
    String tenNguoiTao;
    Date ngayNghiemThu;
    Long idThuTruong;
    String tenThuTruong;
    Long idKeToan;
    String tenKeToan;
    Long idKyThuatVien;
    String tenKyThuatVien;
    private String tenNguoiPduyet;
    Long idThuKho;
    String tenThuKho;
    String lhKho;
    Double slThucNhap;
    Double tichLuong;
    String pthucBquan;
    String hthucBquan;
    Double dinhMuc;
    String ketLuan;
    String trangThai;
    String ldoTuchoi;
    String capDvi;
    String maDvi;
    Integer nam;
    String loaiVthh;
    String cloaiVthh;
    String tenCloaiVthh;
    String moTaHangHoa;
    String maVatTu;
    String maVatTuCha;
    private Integer so;
    private Long hopDongId;
    String soHopDong;
    String tenDvi;
    String maDiemKho;
    String tenDiemKho;
    String maNhaKho;
    String tenNhaKho;
    String maNganKho;
    String tenNganKho;
    String maLoKho;
    String tenLoKho;
    BigDecimal chiPhiThucHienTrongNam;
    BigDecimal chiPhiThucHienNamTruoc;
    BigDecimal tongGiaTri;
    String tongGiaTriBc;
    String tenTrangThai;
    String trangThaiDuyet;
    String tongGiaTriBangChu;
    String soQuyetDinhNhap;
    String tenVatTu;
    String tenVatTuCha;
    String maQhns;
    Long idDdiemGiaoNvNh;
    Long idQdGiaoNvNh;
    String soQdGiaoNvNh;
    String soPhieuNhapKho;
    private String ngayTaoFull;
    private String ngayTao;
    private String thangTao;
    private String namTao;
    private List<HhBbNghiemthuKlstDtl> children = new ArrayList<>();
    private List<FileDKemJoinKeLot> children1 = new ArrayList<>();
}
