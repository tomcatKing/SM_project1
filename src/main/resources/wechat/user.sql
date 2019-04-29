create table `user`(
	`open_id` varchar(200) not null comment '微信用户的open_id',
	`user_img` varchar(500) not null comment '微信用户的头像',
	primary key (`open_id`)
)engine=innodb default charset=utf8