package org.redhatchallenge.rhc2013.shared;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class RegStatus implements Serializable {

    private int status_id;
    private Boolean reg_status_bool;

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public boolean getReg_status_bool() {
        return reg_status_bool;
    }

    public void setReg_status_bool(boolean reg_status_bool) {
        this.reg_status_bool = reg_status_bool;
    }
}
