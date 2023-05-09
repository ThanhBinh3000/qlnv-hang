package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhTlThongBaoKqHdr.TABLE_NAME)
@Data
public class XhTlThongBaoKqHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_THONH_BAO_KQ_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlThongBaoKqHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlThongBaoKqHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlThongBaoKqHdr.TABLE_NAME + "_SEQ")

    private Long id;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soThongBao;

    private Long idHoSo;

    private String soHoSo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTrinhDuyetHsTl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayThamDinhHsTl;

    private String trangThaiThanhLy;
    @Transient
    private String tenTrangThaiThanhLy;

    private String noiDung;

    private String lyDoKhongPduyet;

    private String trichYeu;

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
}
