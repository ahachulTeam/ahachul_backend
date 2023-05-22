alter table tb_lost_post
    modify member_id bigint null;

alter table tb_lost_post
    add page_url varchar(100) null;

alter table tb_lost_post
    add received_date timestamp null;

alter table tb_lost_post
    modify storage_number varchar(50) null;