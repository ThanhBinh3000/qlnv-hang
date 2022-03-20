package com.tcdt.qlnvhang.entities.ttdtvt;

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
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "THONG_TIN_DAU_THAU_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThongTinDauThauVT extends BaseEntity {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "THONG_TIN_DAU_THAU_VT_SEQ")
	@SequenceGenerator(sequenceName = "THONG_TIN_DAU_THAU_VT_SEQ", allocationSize = 1, name = "THONG_TIN_DAU_THAU_VT_SEQ")
	private Long id;
	private Long khlcntId;
	private String soDxuat;
	private LocalDate ngayDxuat;
	private String soQdinhPduyetKhlcnt;
	private String ngayPduyetKhlcnt;
	private String tenDuAn;
	private String tenChuDtu;
	private String benMoiThau;
	private String hthucDuThau;
	private LocalDate tgianDongThau;
	private Integer tgianHlucEhsmt;
	private Double tongMucDtu;
	private String ghiChu;
}
