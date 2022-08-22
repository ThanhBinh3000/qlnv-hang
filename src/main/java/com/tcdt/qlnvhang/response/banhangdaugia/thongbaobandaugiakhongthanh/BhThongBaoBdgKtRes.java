package com.tcdt.qlnvhang.response.banhangdaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.response.CommonResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhThongBaoBdgKtRes extends CommonResponse {
    private Long id;
    private String maThongBao;
    private String trichYeu;
    private LocalDate ngayKy;
    private String loaiVthh;
    private String maVatTuCha;
    private String tenVatTuCha;
    private Long thongBaoBdgId;
    private String maThongBaoBdg;
    private String donViThongBao;
    private LocalDate ngayToChuc;
    private String noiDung;

    private Long qdPdKhBdgId;
    private String soQdPdKhBdg;

    private Long qdPdKqBdgId;
    private String soQdPdKqBdg;

    private String hinhThucDauGia;
    private String phuongThucDauGia;
    private List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> cts = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
