package io.bann.abk;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.logging.Logger;

public class ABK {

    private Logger logger;

    private Config config;

    private ReceiverFactory receiverFactory;

    public void startApplication() {
        this.logger = Logger.getLogger(this.getClass().getCanonicalName());
        this.logger.info("Loading configuration");
        this.config = ConfigFactory.load();
        this.logger.info("Initializing receiver factory");
        this.receiverFactory = new ReceiverFactory(this.config.getConfig("receiver"), this::handleMessage);
    }

    public void handleMessage(String str) {
        System.out.println(str);
    }

    public void startMessageLoop() {
        this.logger.info("Starting message loop");
        this.receiverFactory.start();
        this.logger.info("Ending message loop");
    }

    public static void main(String[] args) {
        ABK abk = new ABK();
        abk.startApplication();
        abk.startMessageLoop();
    }

}