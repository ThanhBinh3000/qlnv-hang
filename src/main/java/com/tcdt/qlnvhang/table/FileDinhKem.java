package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

@Entity
@Table(name = "FILE_DINH_KEM")
@Data
public class FileDinhKem implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_DINH_KEM_SEQ")
	@SequenceGenerator(sequenceName = "FILE_DINH_KEM_SEQ", allocationSize = 1, name = "FILE_DINH_KEM_SEQ")
	Long id;
	String fileName;
	String fileSize;
	String fileUrl;
	String fileType;
	String dataType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE)
	Date createDate;
	Long dataId;
	String noiDung;
}
