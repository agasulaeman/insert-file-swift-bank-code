package com.permatabank.insertbatchfile.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SwiftBankVO {

    private Long recId;

    private Date createdDate;

    private String swiftCode;

    private String branchCode;

    private String bankName;

    private String addressLine1;

    private String addressLine2;

    private String addressLine3;

    private String addressLine4;

    private String cityName;

    private String countryCode;

    private String countryName;
}
