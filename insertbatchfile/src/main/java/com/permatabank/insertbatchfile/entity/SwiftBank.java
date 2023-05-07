package com.permatabank.insertbatchfile.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table
public class SwiftBank {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rec_id_seq")
    @SequenceGenerator(name = "rec_id_seq", sequenceName = "rec_id_seq")
    private Long recId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "SWIFT_CODE")
    private String swiftCode;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "ADDRESS_LINE1")
    private String addressLine1;

    @Column(name = "ADDRESS_LINE2")
    private String addressLine2;

    @Column(name = "ADDRESS_LINE3")
    private String addressLine3;

    @Column(name = "ADDRESS_LINE4")
    private String addressLine4;

    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "COUNTRY_NAME")
    private String countryName;
}
