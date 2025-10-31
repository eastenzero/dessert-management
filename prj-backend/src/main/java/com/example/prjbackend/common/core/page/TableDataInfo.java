package com.example.prjbackend.common.core.page;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class TableDataInfo implements Serializable {
    private long total; /** 总记录数 */
    private List<?> rows; /** 列表数据 */
    private int code;   /** 消息状态码 */
    private String msg;  /** 消息内容 */
    /**
     * 分页
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total)  {
        this.rows = list;
        this.total = total;
    }
}

