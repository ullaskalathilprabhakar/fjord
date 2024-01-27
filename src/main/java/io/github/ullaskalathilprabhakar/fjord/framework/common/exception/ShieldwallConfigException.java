package io.github.ullaskalathilprabhakar.fjord.framework.common.exception;

public class ShieldwallConfigException extends RuntimeException {

    public ShieldwallConfigException() {
        super();
    }

    public ShieldwallConfigException(String message) {
        super(message);
    }

    public ShieldwallConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShieldwallConfigException(Throwable cause) {
        super(cause);
    }
}
