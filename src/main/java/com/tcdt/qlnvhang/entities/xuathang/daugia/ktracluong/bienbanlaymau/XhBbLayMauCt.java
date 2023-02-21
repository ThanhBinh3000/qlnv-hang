package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau;

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
public class XhBbLayMauCt implements Serializable {

	public static final String TABLE_NAME = "XH_BB_LAY_MAU_CT";
	private static final long serialVersionUID = 5219010961728948644L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_LAY_MAU_CT_SEQ")
	@SequenceGenerator(sequenceName = "XH_BB_LAY_MAU_CT_SEQ", allocationSize = 1, name = "XH_BB_LAY_MAU_CT_SEQ")
	@Column(name = "ID")
	private Long id;

	private Long bbLayMauId;
	private String loaiDaiDien;
	private String daiDien;
}
