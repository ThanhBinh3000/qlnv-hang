package com.tcdt.qlnvhang.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "QD_KH_LCNT_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuyetDinhKHLCNTVatTu extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_KH_LCNT_VT_SEQ")
	@SequenceGenerator(sequenceName = "QD_KH_LCNT_VT_SEQ", allocationSize = 1, name = "QD_KH_LCNT_VT_SEQ")
	private Long id;
	private String soQuyetDinh;
	private String veViec;
	private String ccPhapLy;
	private String diaDiemQuyMoDa;
	private String ghiChu;
	private LocalDate ngayBanHanh;
	private String loaiQuyetDinh;

	@Transient
	private List<QuyetDinhKHLCNTGoiThauVatTu> goiThauList = new ArrayList<>();


}