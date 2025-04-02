package fun.lance.task.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class SubjectAgent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long subjectId;
    private String subjectNo;
    private String subjectName;
    private Long studySiteId;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    /**
     * 重试次数
     */
    private int retryCount;
}
