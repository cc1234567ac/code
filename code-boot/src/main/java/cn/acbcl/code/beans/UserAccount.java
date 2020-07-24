package cn.acbcl.code.beans;

import lombok.Data;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


@Data
@ToString
public class UserAccount {
    // ID
    private Integer id;
    // 用户ID
    private Integer userId;
    // 类型 1 正常 3.永久卡
    private Integer type;
    // 剩余次数
    private Integer hasCount;
    // 剩余积分
    private Integer presentExp;
    // 到期时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime hasTime;


    public static Builder create() {
        return new Builder();
    }

    public UserAccount(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.type = builder.type;
        this.hasCount = builder.hasCount;
        this.presentExp = builder.presentExp;
        this.hasTime = builder.hasTime;
    }

    @Data
    public static class Builder {
        private Integer id;

        public Builder addId(Integer id) {
            this.id = id;
            return this;
        }

        private Integer userId;

        public Builder addUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        private Integer type;

        public Builder addType(Integer type) {
            this.type = type;
            return this;
        }

        private Integer hasCount;

        public Builder addHasCount(Integer hasCount) {
            this.hasCount = hasCount;
            return this;
        }

        private Integer presentExp;

        public Builder addPresentExp(Integer presentExp) {
            this.presentExp = presentExp;
            return this;
        }

        private LocalDateTime hasTime;

        public Builder addHasTime(LocalDateTime hasTime) {
            this.hasTime = hasTime;
            return this;
        }

        public UserAccount build() {
            return new UserAccount(this);
        }
    }
}