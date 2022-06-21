package com.tcdt.qlnvhang.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DeleteReq {
    @NotEmpty
    private List<Long> ids = new ArrayList<>();
}
