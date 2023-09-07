package org.softauto.podam.strategies;

import org.softauto.core.Suite;
import org.softauto.core.SuiteFactory;
import uk.co.jemos.podam.common.AttributeStrategy;

public class ConsumeStrategy implements AttributeStrategy<Object> {

    String attribute;

    Class<?> type;

    public ConsumeStrategy(String attribute,Class<?> type){
        this.attribute = attribute;
        this.type = type;
    }

    @Override
    public Object getValue() {
        return SuiteFactory.getSuite().getPublish("/"+attribute,type.getTypeName());
    }
}
