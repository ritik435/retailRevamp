package com.neotechInnovations.retailrevamp.Model;

import java.sql.Timestamp;

public class StatsticsInfoModel {
    Timestamp startDate;
    Timestamp endDate;
    Integer bottomLine;
    Integer totalSales;

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Integer getBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(Integer bottomLine) {
        this.bottomLine = bottomLine;
    }


}
