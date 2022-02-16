package com.tcdt.qlnvhang.table;

import java.io.Serializable;
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
@Table(name = "QLNV_QD_LCNT_DTL")
@Data
public class QlnvQdLcntDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_DTL_SEQ")
	private Long id;
	String soDxuat;
	String maDvi;
	String soGoithau;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	private QlnvQdLcntHdr header;

	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@OneToMany(mappedBy = "dtl", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QlnvQdLcntDtlCtiet> detailList = new ArrayList<QlnvQdLcntDtlCtiet>();

	public void setDetailList(List<QlnvQdLcntDtlCtiet> details) {
		if (this.detailList == null) {
			this.detailList = details;
		} else if (this.detailList != details) { // not the same instance, in other case we can get
													// ConcurrentModificationException from hibernate
													// AbstractPersistentCollection
			this.detailList.clear();
			for (QlnvQdLcntDtlCtiet detail : details) {
				detail.setDtl(this);
			}
			this.detailList.addAll(details);
		}
	}

	public QlnvQdLcntDtl addDetail(QlnvQdLcntDtlCtiet dt) {
		detailList.add(dt);
		dt.setDtl(this);
		return this;
	}

	public QlnvQdLcntDtl removeDetail(QlnvQdLcntDtlCtiet dt) {
		detailList.remove(dt);
		dt.setDtl(null);
		return this;
	}
}
