package com.tcdt.qlnvhang.service.quyetdinhpheduyetketqualuachonnhathauvatu;


import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVt;
import com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVtRes;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class QdKqlcntGoiThauVtServiceImpl implements QdKqlcntGoiThauVtService {

    @Override
    public QdKqlcntGoiThauVtRes buildResponseForList(QdKqlcntGoiThauVt goiThauVt) {
        QdKqlcntGoiThauVtRes response = new QdKqlcntGoiThauVtRes();
        BeanUtils.copyProperties(goiThauVt, response);
        return response;
    }

}
