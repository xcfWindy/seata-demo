package com.example.windy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 
 * @TableName account_freeze_tbl
 */
@TableName(value ="account_freeze_tbl")
@Data
public class AccountFreezeTbl implements Serializable {
    /**
     * 
     */
    @TableId
    private String xid;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 冻结金额
     */
    private Integer freezeMoney;

    /**
     * 事务状态，0:try，1:confirm，2:cancel
     */
    private Integer state;


    public static abstract class State{
        public final static int TRY=0;
        public final static int CANCLE=1;
        public final static int CONFIRM=2;

    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AccountFreezeTbl other = (AccountFreezeTbl) that;
        return (this.getXid() == null ? other.getXid() == null : this.getXid().equals(other.getXid()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getFreezeMoney() == null ? other.getFreezeMoney() == null : this.getFreezeMoney().equals(other.getFreezeMoney()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getXid() == null) ? 0 : getXid().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getFreezeMoney() == null) ? 0 : getFreezeMoney().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", xid=").append(xid);
        sb.append(", user_id=").append(userId);
        sb.append(", freeze_money=").append(freezeMoney);
        sb.append(", state=").append(state);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}