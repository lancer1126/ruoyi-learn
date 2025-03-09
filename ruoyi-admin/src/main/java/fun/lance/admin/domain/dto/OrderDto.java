package fun.lance.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderDto implements Serializable {
    private String id;
    private String name;
    private Date createTime;
}
