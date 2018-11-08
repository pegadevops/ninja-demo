CREATE INDEX hist_orderful_hfr_idx
    ON pc_history_ojxu4t_orderful_wor USING btree
    (pxhistoryforreference, pxobjclass)
    TABLESPACE pg_default;