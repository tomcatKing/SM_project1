create table `shipping`(
	`shipping_id` int(11) not null auto_increment,
	`open_id` varchar(100) default null comment '用户id',
	`receiver_name` varchar(20) default null comment '收货人性名',
	`receiver_phone` varchar(20) default null comment '收货人电话',
	`receiver_province` varchar(20) default null comment '省份',
	`receiver_city` varchar(20) default null comment '城市',
	`receiver_district` varchar(20) default null comment '区/县',
	`receiver_address` varchar(200) default null comment '详细地址',
	`create_time` datetime default null,
	`update_time` datetime default null,
	primary key(`shipping_id`)
)engine=innodb auto_increment=32 default charset=utf8