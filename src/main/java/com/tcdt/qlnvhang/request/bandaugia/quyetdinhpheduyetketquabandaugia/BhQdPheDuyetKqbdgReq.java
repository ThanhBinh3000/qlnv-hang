package com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BhQdPheDuyetKqbdgReq {
    private Long id;
    private Integer nam;
    private String soQuyetDinh;
    private String trichYeu;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayKy;
    private String maVatTuCha;
    private String maVatTu;
    private String loaiVthh;
    private Long thongBaoBdgId;
    private Long bienBanBdgId;
    private Long thongBaoBdgKtId;
    private String ghiChu;

    private List<BhQdPheDuyetKqbdgCtReq> cts = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();
}
