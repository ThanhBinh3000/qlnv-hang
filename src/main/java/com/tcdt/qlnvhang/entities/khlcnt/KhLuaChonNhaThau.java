package com.tcdt.qlnvhang.entities.khlcnt;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "KH_LCNT_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhLuaChonNhaThau extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_LCNT_VT_SEQ")
	@SequenceGenerator(sequenceName = "KH_LCNT_VT_SEQ", allocationSize = 1, name = "KH_LCNT_VT_SEQ")
	private Long id;
	private String soQdinh;
	private Integer namKhoach;
	private String maVatTu;
	private String tenVatTu;
	private Long vatTuId;
	private String tenDuAn;
	private Double tongMucDtu;
	private String donViTien;
	private String dienGiai;
	private String tenChuDtu;
	private String nguonVon;
	private String hthucLcnt;
	private String tgianThienDuAn;
	private String qchuanKthuat;
}
