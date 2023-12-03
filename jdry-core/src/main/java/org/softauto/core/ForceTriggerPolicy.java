package org.softauto.core;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 * Force Trigger Policy for archiving trace log at test end
 */
@Plugin(name = "ForceTriggerPolicy", category = "Core")
public class ForceTriggerPolicy implements TriggeringPolicy{

        private static boolean isRolling;

        @Override
        public void initialize(RollingFileManager arg0) {
            setRolling(false);
        }

        @Override
        public boolean isTriggeringEvent(LogEvent arg0) {
            if(arg0.getMessage().getFormat().contains("roll test")){
                return true;
            }
            return isRolling();
        }

        public static boolean isRolling() {
            return isRolling;
        }

        public static void setRolling(boolean _isRolling) {
            isRolling = _isRolling;
        }

        @PluginFactory
        public static ForceTriggerPolicy createPolicy(){
            return new ForceTriggerPolicy();
        }
}
