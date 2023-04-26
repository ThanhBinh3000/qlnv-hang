package com.tcdt.qlnvhang;

import com.tcdt.qlnvhang.service.feign.CategoryServiceProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class QlnvHangApplicationTests {
    @Autowired
    private CategoryServiceProxy categoryServiceProxy;

    @Test
    void contextLoads() {
        System.out.println("HELLO");
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucChung("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIk1BX1FEIjoiUcSQLVRDRFQiLCJNQV9UUiI6IlRUci1UQ0RUIiwiTUFfVENLVCI6IlRDRFROTi1UQ0tUIiwiTUFfRFZJIjoiMDEwMSIsIkNBUF9EVkkiOiIxIiwiVEVOX0RWSSI6IlThu5VuZyBj4bulYyBE4buxIHRy4buvIE5ow6Agbsaw4bubYyIsIlRFTl9QSE9OR19CQU4iOiJCTMSQIFThu5VuZyBj4bulYyBEVE5OIiwiTUFfUEhPTkdfQkFOIjoiMDEwMTMwIiwiVEVOX0RBWV9EVSI6IkFkbWluaXN0YXRvciIsImV4cCI6MTY4MTQ3Mjg5NX0.yOQqJIjaz1eCwV068RCAInHcpiEbx9Batho1Ij3gaslMHQPONqDWcS-pLXWzGDfgm_Clq78KKOi9TUdIzJ9EMg","1");
        System.out.println(response);
    }

}
