package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_LCNT_VTU_DTL")
@Data
public class QlnvQdLcntVtuDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_VTU_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_VTU_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_VTU_DTL_SEQ")
	private Long id;
	BigDecimal giaDxKthue;
	BigDecimal giaDuyetCthue;
	BigDecimal giaDxCthue;
	String soHieu;
	String nguonvon;
	String tenGoithau;
	BigDecimal giaDuyetKthue;
	BigDecimal sluongDxuat;
	String dviTinh;
	BigDecimal sluongDuyet;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	private QlnvQdLcntVtuHdr header;

	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@OneToMany(mappedBy = "dtl", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QlnvQdLcntVtuDtlCtiet> detailList = new ArrayList<QlnvQdLcntVtuDtlCtiet>();

	public void setDetailList(List<QlnvQdLcntVtuDtlCtiet> details) {
		if (this.detailList == null) {
			this.detailList = details;
		} else if (this.detailList != details) { // not the same instance, in other case we can get
													// ConcurrentModificationException from hibernate
													// AbstractPersistentCollection
			this.detailList.clear();
			for (QlnvQdLcntVtuDtlCtiet detail : details) {
				detail.setDtl(this);
			}
			this.detailList.addAll(details);
		}
	}

	public QlnvQdLcntVtuDtl addDetail(QlnvQdLcntVtuDtlCtiet dt) {
		detailList.add(dt);
		dt.setDtl(this);
		return this;
	}

	public QlnvQdLcntVtuDtl removeDetail(QlnvQdLcntVtuDtlCtiet dt) {
		detailList.remove(dt);
		dt.setDtl(null);
		return this;
	}
}
