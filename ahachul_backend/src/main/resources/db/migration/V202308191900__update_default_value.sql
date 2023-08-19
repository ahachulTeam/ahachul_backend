ALTER TABLE tb_train
    MODIFY created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_train
    MODIFY updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_train
    MODIFY created_by VARCHAR(50) DEFAULT 'SYSTEM';

ALTER TABLE tb_train
    MODIFY updated_by VARCHAR(50) DEFAULT 'SYSTEM';

ALTER TABLE tb_complaint_message_history
    MODIFY created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_complaint_message_history
    MODIFY updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE tb_complaint_message_history
    MODIFY created_by VARCHAR(50) DEFAULT 'SYSTEM';

ALTER TABLE tb_complaint_message_history
    MODIFY updated_by VARCHAR(50) DEFAULT 'SYSTEM';
