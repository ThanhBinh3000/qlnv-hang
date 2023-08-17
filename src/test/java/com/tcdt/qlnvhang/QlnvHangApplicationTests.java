package com.tcdt.qlnvhang;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.impl.DcnbKeHoachDcHdrServiceImpl;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.impl.DcnbQuyetDinhDcCHdrServiceImpl;
import com.tcdt.qlnvhang.service.feign.CategoryServiceProxy;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCHdr;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
@SpringBootTest
class QlnvHangApplicationTests {
    @Autowired
    private CategoryServiceProxy categoryServiceProxy;

    @Autowired
    private DcnbKeHoachDcHdrServiceImpl dcnbKeHoachDcHdrServiceImpl;
    @Autowired
    private DcnbQuyetDinhDcCHdrServiceImpl dcnbQuyetDinhDcCHdrServiceImpl;
    @Autowired
    private EntityManager entityManager;
    @Test
    void contextLoads() {
        System.out.println("HELLO");
//        ResponseEntity<String> response = categoryServiceProxy.getDanhMucChung("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIk1BX1FEIjoiUcSQLVRDRFQiLCJNQV9UUiI6IlRUci1UQ0RUIiwiTUFfVENLVCI6IlRDRFROTi1UQ0tUIiwiTUFfRFZJIjoiMDEwMSIsIkNBUF9EVkkiOiIxIiwiVEVOX0RWSSI6IlThu5VuZyBj4bulYyBE4buxIHRy4buvIE5ow6Agbsaw4bubYyIsIlRFTl9QSE9OR19CQU4iOiJCTMSQIFThu5VuZyBj4bulYyBEVE5OIiwiTUFfUEhPTkdfQkFOIjoiMDEwMTMwIiwiVEVOX0RBWV9EVSI6IkFkbWluaXN0YXRvciIsImV4cCI6MTY4MTQ3Mjg5NX0.yOQqJIjaz1eCwV068RCAInHcpiEbx9Batho1Ij3gaslMHQPONqDWcS-pLXWzGDfgm_Clq78KKOi9TUdIzJ9EMg","1");
//        System.out.println(response);
    }

//    @Test
//    void testCloneQdLoaiDc() throws Exception {
////        ID = 1541
//        StatusReq statusReq = new StatusReq();
//        statusReq.setId(1541l);
//        statusReq.setTrangThai("29");
//        DcnbQuyetDinhDcCHdr details = dcnbQuyetDinhDcCHdrServiceImpl.detail(statusReq.getId());
//        // clone chi cục xuat
//        dcnbQuyetDinhDcCHdrServiceImpl.cloneQuyetDinhDcCXuat(statusReq, Optional.of(details));
//    }
//    @Test
//    void testCloneQdLoaiDc() throws Exception {
//        String sql = "SELECT * FROM DCNB_QUYET_DINH_DC_C_HDR WHERE ID = :value";
//        Query query = entityManager.createNativeQuery(sql, DcnbQuyetDinhDcCHdr.class);
//        query.setParameter("value", 2441);
//        System.out.println(query.getResultList());
//    }
}
