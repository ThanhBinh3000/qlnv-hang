package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import lombok.Data;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCgiaReq {

    private Long id;

    private Long idDtl;

    private Long idQdPdDtl;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String pthucBanTrucTiep;

    private String diaDiemChaoGia;

    private LocalDate ngayMkho;

    private LocalDate ngayKthuc;

    private String ghiChu;

    private LocalDate thoiHanBan;

    private List<FileDinhKemReq> fileDinhKemUyQuyen = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKemMuaLe = new ArrayList<>();

    private List<XhQdPdKhBttDviReq> children = new ArrayList<>();
}
