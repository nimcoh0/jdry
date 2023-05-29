package org.softauto.podam.strategies;

import uk.co.jemos.podam.api.PodamUtils;
import uk.co.jemos.podam.common.AttributeStrategy;

public class EmailStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 5; k++) {
                    sb.append(PodamUtils.getNiceCharacter());
                }
                if (j == 0) {
                    sb.append(".");
                }
            }
            if (i == 0) {
                sb.append("@");
            }
        }
        return sb.toString();
    }
}
