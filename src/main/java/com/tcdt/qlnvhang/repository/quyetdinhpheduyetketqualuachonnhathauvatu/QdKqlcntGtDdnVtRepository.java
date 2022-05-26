package com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGtDdnVt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface QdKqlcntGtDdnVtRepository extends BaseRepository<QdKqlcntGtDdnVt, Long> {

    List<QdKqlcntGtDdnVt> findByGoiThauIdIn(Collection<Long> goiThauIds);
}
