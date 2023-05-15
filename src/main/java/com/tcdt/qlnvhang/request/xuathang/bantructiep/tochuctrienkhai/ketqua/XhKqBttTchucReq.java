package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhKqBttTchucReq {

    private Long id;

    private Long idDdiem;

    private String tochucCanhan;

    private String mst;

    private String diaDiemChaoGia;

    private String sdt;

    private LocalDate ngayChaoGia;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thueGtgt;

    private Boolean luaChon;

    @Transient
    private FileDinhKemReq fileDinhKems;
}
