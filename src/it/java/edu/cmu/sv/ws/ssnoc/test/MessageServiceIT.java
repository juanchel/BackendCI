package edu.cmu.sv.ws.ssnoc.test;

import com.eclipsesource.restfuse.*;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static com.eclipsesource.restfuse.Assert.assertBadRequest;
import static com.eclipsesource.restfuse.Assert.assertCreated;
import static com.eclipsesource.restfuse.Assert.assertOk;

@RunWith(HttpJUnitRunner.class)
public class MessageServiceIT {
	@Rule
	public Destination destination = new Destination(this, "http://localhost:8080/ssnoc/");

	@Context
	public Response response;

	@HttpTest(method = Method.GET, path = "/messages/wall")
	public void testWallFound() {
		assertOk(response);
	}

    @HttpTest(method = Method.GET, path = "/messages/demo/demo2")
    public void testCanGetPM() {
        assertOk(response);
    }

    @HttpTest(method = Method.POST, path = "/message/demo", type = MediaType.APPLICATION_JSON,
            content = "{\n" +
                    "\"author\": \"demo\",\n" +
                    " \"content\": \"asdf\"\n" +
                    "}")
    public void testPostToWall() {
        assertCreated(response);
    }

    @HttpTest(method = Method.POST, path = "/message/demo/demo2", type = MediaType.APPLICATION_JSON,
            content = "{\n" +
                    "\"author\": \"demo\",\n" +
                    "\"target\": \"demo2\",\n" +
                    " \"content\": \"asdf\"\n" +
                    "}")
    public void testSendPM() {
        assertCreated(response);
    }

    @HttpTest(method = Method.GET, path = "/users/clusters/1")
    public void testCanGetClusters() {
        assertOk(response);
    }
}
