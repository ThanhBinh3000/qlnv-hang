package com.tcdt.qlnvhang.table.chotdulieu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = QthtKetChuyenHdr.TABLE_NAME)
public class QthtKetChuyenHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "QTHT_KET_CHUYEN_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = QthtKetChuyenHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = QthtKetChuyenHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = QthtKetChuyenHdr.TABLE_NAME + "_SEQ")
    private Long id;

    private Integer nam;

    private Date ngayTu;

    private Date ngayDen;

    private String maDvi;

    private String tenVietTat;

    @Transient
    private String tenNguoiTao;

    @Transient
    private List<QthtKetChuyenDtl> children = new ArrayList<>();

    @Transient
    private String tenChiCuc;
}
