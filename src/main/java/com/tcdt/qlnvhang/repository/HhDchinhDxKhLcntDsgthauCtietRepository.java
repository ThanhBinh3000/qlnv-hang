package com.tcdt.qlnvhang.repository;


import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthauCtiet;

import java.util.List;

public interface HhDchinhDxKhLcntDsgthauCtietRepository extends CrudRepository<HhDchinhDxKhLcntDsgthauCtiet, Long> {

    List<HhDchinhDxKhLcntDsgthauCtiet> findAllByIdGoiThau(Long idGoiThau);

}
