import org.glassfish.embeddable.*;
public class Run {
    public static void main(String[] args) throws Exception {
        GlassFishRuntime runtime = GlassFishRuntime.bootstrap();
        GlassFishProperties props = new GlassFishProperties();
        props.setPort("http-listener", 8080);
        GlassFish glassfish = runtime.newGlassFish(props);
        glassfish.start();
        Deployer deployer = glassfish.getDeployer();
        deployer.deploy(new java.io.File("target/PR1automatizationUstynov-1.0-SNAPSHOT.war"));
        System.out.println("Deployed! http://localhost:8080/PR1automatizationUstynov-1.0-SNAPSHOT/");
        Thread.currentThread().join();
    }
}
