package io.bann.abk;

import com.typesafe.config.Config;
import io.bann.abk.receiver.AbstractReceiver;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverFactory {

    private AbstractReceiver receiver;

    private Logger logger;

    public Optional<? extends AbstractReceiver> loadReceiver(String name) {
        this.logger.log(Level.INFO, "Loading receiver [" + name + "]");
        try {
            Class<? extends AbstractReceiver> clazz = Class.forName(name).asSubclass(AbstractReceiver.class);
            return Optional.of(clazz.getDeclaredConstructor().newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ex) {
            this.logger.log(Level.SEVERE, "Failed to find receiver class", ex);
        }
        return Optional.empty();
    }

    public ReceiverFactory(Config receiverConfig, Consumer<String> handler) {
        this.logger = Logger.getLogger(this.getClass().getCanonicalName());
        loadReceiver(receiverConfig.getString("class")).ifPresentOrElse(r -> this.receiver = r, () -> {
            System.exit(-1);
        });
        this.receiver.configure(receiverConfig, handler);
    }

    public void start() {
        this.receiver.start();
    }

    public void end() {
        this.receiver.end();
    }

}
