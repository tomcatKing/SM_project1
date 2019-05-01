package com.wx.pojo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //此注解将自动生成get/setter方法
@NoArgsConstructor
@AllArgsConstructor
public class PayInfo {
    private Integer pay_id;
    private String open_id;
    private Long order_no;
    private Integer pay_platform;
    private String platform_number;
    private String platform_status;
    private Date create_time;
    private Date update_time;   
}