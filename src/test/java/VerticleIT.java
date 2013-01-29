import com.example.verticle.StandAloneVerticle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.test.TestVerticle;
import org.vertx.java.test.VertxConfiguration;
import org.vertx.java.test.junit.VertxJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

@RunWith(VertxJUnit4ClassRunner.class)
@VertxConfiguration
@TestVerticle(main = "com.example.verticle.StandAloneVerticle")
public class VerticleIT {
    @Test(timeout = 10_000)
    public void testHttpHandler() throws InterruptedException {
        final CountDownLatch counter = new CountDownLatch(1);
        final Map<String, Object> expected = new HashMap<>();

        HttpClient httpClient = Vertx.newVertx().createHttpClient().setHost("localhost").setPort(8088);
        System.out.println("Test: Sending HTTP request");
        HttpClientRequest request = httpClient.get("/", new Handler<HttpClientResponse>() {
            @Override
            public void handle(HttpClientResponse response) {
                System.out.println("Test: HTTP connected");
                response.bodyHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        System.out.println("Test: HTTP body received");
                        System.out.println("Test: body: " + buffer.toString());
                        expected.put("message", buffer.toString());
                        counter.countDown();
                    }
                });
            }
        });
        request.end();

        counter.await();
        assertEquals(StandAloneVerticle.MESSAGE, expected.get("message"));
    }
}
