package com.tcdt.qlnvhang.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhPaKhlcntHdr;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FileDKemJoinPaKhlcntHdr extends FileDinhKem implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dataId")
	@JsonBackReference
	private HhPaKhlcntHdr parent;
}
