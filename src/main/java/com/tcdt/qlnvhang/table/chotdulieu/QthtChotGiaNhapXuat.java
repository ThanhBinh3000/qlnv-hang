package com.tcdt.qlnvhang.table.chotdulieu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = QthtChotGiaNhapXuat.TABLE_NAME)
public class QthtChotGiaNhapXuat extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "QTHT_CHOT_GIA_NHAP_XUAT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = QthtChotGiaNhapXuat.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = QthtChotGiaNhapXuat.TABLE_NAME + "_SEQ", allocationSize = 1, name = QthtChotGiaNhapXuat.TABLE_NAME + "_SEQ")
    private Long id;

    private LocalDate ngayChot;

    private LocalDate ngayHluc;

    private LocalDate ngayHlucQdDC;
    //  CHOT_GIA = "CHOT_GIA",
    //  CHOT_NHAP_XUAT = "CHOT_NHAP_XUAT",

    private String maDonVi;

    private String type;

    @Transient
    private List<String> listMaDvi = new ArrayList<>();

    public List<String> getListDonVi() {
        if(StringUtils.isEmpty(maDonVi)){
            return listMaDvi;
        }else{
            return Arrays.asList(maDonVi.split(","));
        }
    }
}
