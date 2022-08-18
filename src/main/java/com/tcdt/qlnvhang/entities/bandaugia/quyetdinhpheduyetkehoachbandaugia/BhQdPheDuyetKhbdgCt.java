package com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = BhQdPheDuyetKhbdgCt.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhQdPheDuyetKhbdgCt {
	public static final String TABLE_NAME = "BH_QD_PHE_DUYET_KHBDG_CT";
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_QD_PHE_DUYET_KHBDG_CT_SEQ")
	@SequenceGenerator(sequenceName = "BH_QD_PHE_DUYET_KHBDG_CT_SEQ", allocationSize = 1, name = "BH_QD_PHE_DUYET_KHBDG_CT_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "BH_DG_KE_HOACH_ID")
	private Long bhDgKeHoachId;

	@Column(name = "BH_QD_PHE_DUYET_KHBDG_ID")
	private Long quyetDinhPheDuyetId;

	@Transient
	List<BhQdPheDuyetKhBdgThongTinTaiSan> thongTinTaiSans;
}
