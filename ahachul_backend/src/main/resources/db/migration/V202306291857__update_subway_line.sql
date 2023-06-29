ALTER TABLE tb_subway_line
    ALTER created_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_subway_line
    ALTER updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_subway_line
    ALTER created_by SET DEFAULT 'SYSTEM';

ALTER TABLE tb_subway_line
    ALTER updated_by SET DEFAULT 'SYSTEM';
