create table `order`(
	`order_id` int(11) not null auto_increment comment '订单id',
	`order_no` bigint(20) default null comment '订单号',
	`open_id` varchar(100) default null comment '用户id',
	`shipping_id` int(11) default null comment '消费id',
	`payment` decimal(20,2) default null comment '实际支付金额,单位元',
	`payment_type` int(4) default null comment '支付类型,1-微信支付',
	`status` int(10) default null comment '订单状态:0-已取消,10-未付款,20-已支付,30-交易成功',
	`order_desc` varchar(100) default null comment '用户留言',
	`payment_time` datetime default null comment '支付时间',
	`send_time`	datetime default null comment '发货时间',
	`end_time` datetime default null comment '交易完成时间',
	`close_time` datetime default null comment '交易关闭时间',
	`create_time` datetime default null comment '创建时间',
	`update_time` datetime default null comment '更新时间',
	primary key (`order_id`),
	unique key `order_no_index` (`order_no`) using btree
)engine=InnoDB auto_increment=103 default charset=utf8