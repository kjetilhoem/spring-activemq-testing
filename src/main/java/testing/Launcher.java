package testing;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Launcher {
    public static void main(String... args) {
        final boolean isProducer = args.length > 0
                && "producer".equals(args[0]);
        
        final ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(isProducer
                ? "testing.producer"
                : "testing.consumer");
        ctx.registerShutdownHook();
    }
}
