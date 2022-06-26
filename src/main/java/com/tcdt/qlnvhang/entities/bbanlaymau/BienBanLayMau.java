package com.tcdt.qlnvhang.entities.bbanlaymau;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "NH_BB_LAY_MAU")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienBanLayMau extends BaseEntity implements Serializable  {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_LAY_MAU_SEQ")
	@SequenceGenerator(sequenceName = "BB_LAY_MAU_SEQ", allocationSize = 1, name = "BB_LAY_MAU_SEQ")
	private Long id;
	private Long qdgnvnxId;
	private Long bbNhapDayKhoId;
	private String soBienBan;
	private Long hopDongId;
	private LocalDate ngayHopDong;
	private String donViCungCap;
	private LocalDate ngayLayMau;
	private String diaDiemLayMau;

	private String maVatTuCha;
	private String maVatTu;

	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;

	private Integer soLuongMau;
	private String ppLayMau;
	private String chiTieuKiemTra;

	private String trangThai;
	private String lyDoTuChoi;
	private String maDvi;
	private String capDvi;

	private Long nguoiGuiDuyetId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	private LocalDate ngayPduyet;

	@Transient
	private List<BienBanLayMauCt> chiTiets = new ArrayList<>();
}
