package com.tcdt.qlnvhang.table.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NH_HO_SO_BIEN_BAN")
@Data
public class NhHoSoBienBan extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "NH_HO_SO_BIEN_BAN";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NH_HO_SO_BIEN_BAN_SEQ")
    @SequenceGenerator(sequenceName = "NH_HO_SO_BIEN_BAN_SEQ", allocationSize = 1, name = "NH_HO_SO_BIEN_BAN_SEQ")

    private Long id;
    private String maDvi;
    private String soBienBan;
    private String loaiBienBan;

    @Transient
    private String tenDvi;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<NhHoSoBienBanCt> hoSoBienBanCtList= new ArrayList<>();

}
