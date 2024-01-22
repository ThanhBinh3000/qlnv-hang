package com.tcdt.qlnvhang.service.chotdulieu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcdt.qlnvhang.service.feign.CategoryServiceProxy;
import com.tcdt.qlnvhang.util.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class QlnvDmService {
    @Autowired
    private CategoryServiceProxy qlnvDmClient;


    public Map<String, String> getListDanhMucHangHoa() {
        ResponseEntity<String> response = qlnvDmClient.getDanhMucHangHoa();
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, String> data = new HashMap<String, String>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("ma")), String.valueOf(map.get("ten")));
        }
        return data;
    }

    public Map<String, String> getListDanhMucChung(String loai) {
        ResponseEntity<String> response = qlnvDmClient.getDanhMucChung(loai);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, String> data = new HashMap<String, String>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("ma")), String.valueOf(map.get("giaTri")));
        }
        return data;
    }

}
