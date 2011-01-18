package testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import no.fovea.commons.text.StringUtils;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import testing.producer.Producer;


public class Launcher {
    public static void main(String... args) throws Exception {
        final boolean isProducer = args.length > 0
                && "producer".equals(args[0]);
        
        final ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(isProducer
                ? "testing.producer"
                : "testing.consumer");
        ctx.registerShutdownHook();
        
        if (isProducer) {
            runProducerLoop(ctx.getBean(Producer.class));
        }
    }
    
    private static void runProducerLoop(Producer producer) throws IOException {
        final BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("> ");
                final String line = r.readLine();
                
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                
                final String[] commands = line.split(" ");
                
                if ("exit".equals(commands[0])) {
                    break;
                } else if ("validateEmailAddress".equals(commands[0])) {
                    producer.validateEmailAddress(commands[1]);
                } else if ("validateEmailDomain".equals(commands[0])) {
                    producer.validateEmailDomain(commands[1]);
                } else if ("createOrder".equals(commands[0])) {
                    producer.createOrder(commands[1]);
                } else if ("publishEvent".equals(commands[0])) {
                    producer.publishEvent(commands[1], commands[2]);
                } else if ("goCrazy".equals(commands[0])) {
                    for (int i = 0; i < 1000; i++) {
                        producer.createOrder("order#" + i);
                        producer.publishEvent("attempting to create order#" + i, null);
                    }
                } else {
                    System.out.println("unknown command, valid commands 'exit', 'goCrazy' +");
                    for (Method m : producer.getClass().getMethods()) {
                        if (m.getDeclaringClass() == Producer.class) {
                            System.out.println("\t" + m);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("producer died...");
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
