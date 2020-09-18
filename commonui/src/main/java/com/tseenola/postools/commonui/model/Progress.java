package com.tseenola.postools.commonui.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: ZhengJuE
 * @CreateDate: 2020/8/31 20:16
 * @Description:
 * @UpdateUser: ZhengJuE
 * @UpdateDate: 2020/8/31 20:16
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Progress {
    private BigDecimal total;
    private BigDecimal currentProgress;
    private BigDecimal percent;

    public Progress(long total, long currentProgress) {
        this.total = new BigDecimal(total);
        this.currentProgress = new BigDecimal(currentProgress);
        if (total != 0L) {
            this.percent = this.currentProgress.divide(this.total, 2, RoundingMode.DOWN);
        }

    }

    public long getTotal() throws RuntimeException {
        return this.total.longValueExact();
    }

    public long getCurrentProgress() throws RuntimeException {
        return this.currentProgress.longValueExact();
    }

    public double getPercent() throws RuntimeException {
        return this.percent.doubleValue();
    }
}
