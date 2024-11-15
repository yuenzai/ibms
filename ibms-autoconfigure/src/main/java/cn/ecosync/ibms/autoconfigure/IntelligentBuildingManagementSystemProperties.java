package cn.ecosync.ibms.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ibms")
public class IntelligentBuildingManagementSystemProperties {
    private final Outbox outbox = new Outbox();

    public Outbox getOutbox() {
        return outbox;
    }

    public static class Outbox {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
