package com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhBbLayMauCt.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class XhBbLayMauCt extends TrangThaiBaseEntity implements Serializable {

	public static final String TABLE_NAME = "XH_BB_LAY_MAU_CT";
	private static final long serialVersionUID = 5219010961728948644L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_LAY_MAU_CT_SEQ")
	@SequenceGenerator(sequenceName = "XH_BB_LAY_MAU_CT_SEQ", allocationSize = 1, name = "XH_BB_LAY_MAU_CT_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "XH_BB_LAY_MAU_ID")
	private Long xhBbLayMauId;

	@Column(name = "LOAI_DAI_DIEN")
	private String loaiDaiDien;

	@Column(name = "DAI_DIEN")
	private String daiDien;
}
