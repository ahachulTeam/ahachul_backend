ALTER TABLE tb_subway_line
    MODIFY created_at DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_subway_line
    MODIFY updated_at DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_subway_line
    MODIFY created_by DEFAULT 'SYSTEM';

ALTER TABLE tb_subway_line
    MODIFY updated_by DEFAULT 'SYSTEM';
