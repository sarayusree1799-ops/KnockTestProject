package com.knock.dbstorage.dataplatform.enums;

import lombok.Getter;

@Getter
public enum OccupancyQueryColumnNames {
    COMPANY_ID("COMPANY_ID"),
    COMPANY_NAME("COMPANY_NAME"),
    PROPERTY_ID_SOURCE("PROPERTY_ID_SOURCE"),
    PROPERTY_NAME("PROPERTY_NAME"),
    TOTAL_UNITS("TOTAL_UNITS"),
    OCCUPIED_UNITS("OCCUPIED_UNITS"),
    OCCUPIED_PERCENTAGE("% Occupied"),
    VACANT_PERCENTAGE("% Vacant"),
    NOTICE_PERCENTAGE("% Notice"),
    NTR_PERCENTAGE("NTR % Total");

    private final String occupancyQueryColumnNames;

    OccupancyQueryColumnNames(String pAgentName) {
        this.occupancyQueryColumnNames = pAgentName;
    }
}