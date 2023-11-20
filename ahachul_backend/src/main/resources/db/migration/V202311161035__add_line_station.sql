CREATE TABLE tb_subway_line_station
(
    subway_line_station_id BIGINT NOT NULL auto_increment,
    identity       BIGINT NOT NULL,
    subway_line_id BIGINT NOT NULL,
    created_at     TIMESTAMP NOT NULL,
    created_by     VARCHAR(50) NOT NULL,
    updated_at     TIMESTAMP NOT NULL,
    updated_by     VARCHAR(50) NOT NULL,
    PRIMARY KEY (subway_line_station_id)
);
