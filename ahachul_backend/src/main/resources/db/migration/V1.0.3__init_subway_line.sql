alter table tb_subway_line
    modify created_at timestamp default current_timestamp;

alter table tb_subway_line
    modify created_by varchar(50) default 'system';

alter table tb_subway_line
    modify updated_at timestamp default current_timestamp;

alter table tb_subway_line
    modify updated_by varchar(50) default 'system';

insert into tb_subway_line(name, region_type) values('1호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('2호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('3호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('4호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('5호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('6호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('7호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('8호선', 'METROPOLITAN');
insert into tb_subway_line(name, region_type) values('9호선', 'METROPOLITAN');