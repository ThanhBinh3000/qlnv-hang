package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.catalog.QlnvDmVattuBq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DmVattuBqRepository extends JpaRepository<QlnvDmVattuBq, Long> {


    List<QlnvDmVattuBq> findAllByMaAndType(String ma, String type);
}
