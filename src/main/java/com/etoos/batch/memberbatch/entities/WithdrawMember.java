package com.etoos.batch.memberbatch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.etoos.batch.memberbatch.enums.YNCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Table(name ="mmm_withdraw")
@ToString
public class WithdrawMember extends AuditableRegisterEntity{
    @Id
    @Column(name ="member_no", nullable = false)
    private Long no;

    @Column(name = "withdraw_terms_agree_yn", nullable = true)
    private YNCode agreeYn;

    @Column(name = "member_id", nullable = true, length = 50)
    private String id;

    @Column(name = "member_password", nullable = true, length = 256)
    private String password;

    @Column(name = "member_name", nullable = true, length = 50)
    private String name;

    @Column(name = "email", nullable = true, length = 100)
    private String email;

    @Column(name = "mobile_phone_number", nullable = true, length = 20)
    private String mobileNumber;

    @Column(name = "birthday_ymd", nullable = true, length = 10)
    private String birthday;

    @Column(name = "zipcode", nullable = true, length = 10)
    private String zipCode;

    @Column(name = "landlot_address", nullable = true, length = 200)
    private String landlotAddress;

    @Column(name = "landlot_detail_address", nullable = true, length = 200)
    private String landlotDetailAddress;

    @Column(name = "load_address", nullable = true, length = 200)
    private String loadAddress;

    @Column(name = "load_detail_address", nullable = true, length = 200)
    private String loadDetailAddress;

}
