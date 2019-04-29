create table `pay_info`(
	`pay_id` int(11) not null auto_increment comment '支付主键',
	`open_id`	varchar(100) default null comment '用户id',
	`order_no` bigint(20) default null comment '订单号',
	`pay_platform` int(10) default null comment '支付平台:1-微信',
	`platform_number` varchar(200) default null comment '支付宝支付流水号',
	`platform_status` varchar(20) default null comment '支付状态',
	`create_time` datetime default null comment '创建时间',
	`update_time` datetime default null comment '更新时间',
	primary key(`pay_id`)
)engine=innoDb auto_increment=53 default charset=utf8