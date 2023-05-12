package com.humuson.eurekaclient.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestDto {

    private String productName;
    private int cost;
    private int orderId;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestDto { ");
        sb.append("productName= '").append(productName).append('\'');
        sb.append(", cost= ").append(cost);
        sb.append(", orderId= ").append(orderId);
        sb.append(" }");
        return sb.toString();
    }
}
