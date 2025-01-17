package fun.lance.common.database.core.page;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询实体类
 */
@Data
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 当前记录起始索引默认值
     */
    public static final int DEFAULT_PAGE_INDEX = 1;
    /**
     * 每页显示记录数默认值
     */
    public static final int DEFAULT_PAGE_SIZE = 1000;

    private Integer size;
    private Integer index;
    private String orderBy;
    private String isAsc;
}
