ALTER TABLE tb_complaint_message_history
    ADD COLUMN complaint_type VARCHAR(20) NOT NULL;

ALTER TABLE tb_complaint_message_history
    ADD COLUMN short_content_type VARCHAR(20);

ALTER TABLE tb_complaint_message_history
    ADD COLUMN complaint_message_type VARCHAR(20) NOT NULL;

ALTER TABLE tb_complaint_message_history
    ADD COLUMN location INT NOT NULL;