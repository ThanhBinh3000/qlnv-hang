package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.response.CommonResponse;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BhQdPheDuyetKqbdgRes extends CommonResponse {
    private Long id;
    private Integer nam;
    private String soQuyetDinh;
    private String trichYeu;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayKy;
    private String maVatTuCha;
    private String tenVatTuCha;
    private String maVatTu;
    private String tenVatTu;
    private String loaiVthh;
    private Long thongBaoBdgId;
    private String maThongBaoBdg;
    private Long bienBanBdgId;
    private String soBienBanBdg;
    private Long thongBaoBdgKtId;
    private String maThongBaoBdgKt;
    private Long qdPheDuyetKhBdgId;
    private String soQdPheDuyetKhBdg;
    private String ghiChu;
    private String hinhThucDauGia;
    private String phuongThucDauGia;
    private LocalDate ngayToChuc;

    private List<BhQdPheDuyetKqbdgCtRes> cts = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
