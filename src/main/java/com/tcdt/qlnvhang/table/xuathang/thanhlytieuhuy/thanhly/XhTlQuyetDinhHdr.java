package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhTlQuyetDinhHdr.TABLE_NAME)
@Data
public class XhTlQuyetDinhHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_QUYET_DINH_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlQuyetDinhHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlQuyetDinhHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlQuyetDinhHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soQd;
    private LocalDate ngayKy;
    private Long idHoSo;
    private String soHoSo;
    private LocalDate thoiGianTlTu;
    private LocalDate thoiGianTlDen;
    private String trichYeu;
    private String trangThai;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private String lyDoTuChoi;
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<FileDinhKem> fileDinhKem =new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }
    @Transient
    private XhTlHoSoHdr xhTlHoSoHdr;


}
