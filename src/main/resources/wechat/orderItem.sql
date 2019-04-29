create table `order_item`(
	`order_item_id` int(11) not null auto_increment comment '订单字表id',
	`open_id` varchar(100) default null,
	`order_no` bigint(20) default null comment '订单编号',
	`food_id` int(11) default null comment '美食id',
	`food_name` varchar(100) default null comment '商品名称',
	`food_img` varchar(200) default null comment '商品图片名称',
	`food_price` decimal(20,2) default null comment '商品单价',
	`food_num` int(10) default null comment '商品数量',
	`total_price` decimal(20,2) default null comment '商品总价,单位是元,保留2位小数',
	`create_time` datetime default null,
	`update_time` datetime default null,
	primary key(`order_item_id`),
	key `order_no_index` (`order_no`) using btree,
	key `order_no_user_id_index`(`open_id`,`order_no`) using btree
)engine=InnoDB auto_increment=113 default charset=utf8