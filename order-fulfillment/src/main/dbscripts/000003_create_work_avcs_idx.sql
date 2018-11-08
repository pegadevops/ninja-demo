CREATE INDEX pc_orderful_work_avcs_idx
    ON pc_ojxu4t_orderful_work USING btree
    (pxapplication, pxapplicationversion, pxobjclass, pystatuswork);