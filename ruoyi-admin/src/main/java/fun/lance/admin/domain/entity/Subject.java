package fun.lance.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class Subject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long subjectId;
    private String subjectNo;
    private String subjectName;
    private Long studySiteId;
    /**
     * 同步状态
     * 0: 未同步
     * 1: 同步中
     * 2: 同步成功
     * -1: 同步失败
     */
    private Integer syncStatus;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
}
