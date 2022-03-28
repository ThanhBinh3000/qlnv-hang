package com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QdKqlcntGoiThauVtRepository extends BaseRepository<QdKqlcntGoiThauVt, Long> {
    List<QdKqlcntGoiThauVt> findAllByQdPdKhlcntId(Long qdPdKhlcntId);
}
