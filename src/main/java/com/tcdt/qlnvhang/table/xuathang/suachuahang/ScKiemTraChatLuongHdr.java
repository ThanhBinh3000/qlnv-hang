package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScKiemTraChatLuongHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScKiemTraChatLuongHdr extends BaseEntity implements Serializable {
    public static final String TABLE_NAME = "SC_KIEM_TRA_CL_HDR";
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScKiemTraChatLuongHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = ScKiemTraChatLuongHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScKiemTraChatLuongHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soPhieuKtcl;
    private LocalDate ngayLap;
    private Long idTruongPhongKtvq;
    private Long idQdXh;
    private String soQdXh;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private String dviKiemDinh;
    private LocalDate ngayKiemDinh;
    private String hinhThucBaoQuan;
    private String ketQua;
    private Integer dat;
    private String nhanXet;
    private String trangThai;
    private String lyDoTuChoi;
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTruongPhongKtbq;
    @Transient
    private String tenNguoiTao;
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private ScPhieuXuatKhoHdr scPhieuXuatKhoHdr;
    @Transient
    private List<ScKiemTraChatLuongDtl> children = new ArrayList<>();

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }
}
