package ca.bc.gov.iamp.bcparis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;

@RestController
@RequestMapping("/api/v1/message/test")
@ConditionalOnProperty(name="endpoint.bcparis.message-test.activate", havingValue="true", matchIfMissing=false)
public class MessageApiTest {

	@Autowired
	private Layer7MessageRepository layer7Repository;

	@Autowired
	private SatelliteProcessor satellite;

	@PostMapping( path="/layer7", consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<String> testPutMessageLayer7( @RequestBody Layer7Message message ){
		final String response = layer7Repository.sendMessage(message);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping( path="/satellite")
	private ResponseEntity<String> testSatellite( @RequestParam String uri, @RequestParam String query ){
		satellite.test(uri, query);
		return ResponseEntity.ok("Sent to MQ.");
	}
	
	@PostMapping( path="/satellite/all")
	private ResponseEntity<String> testSatelliteAll(){
		satellite.sendSatelliteMessages();
		return ResponseEntity.ok("Satellite messages sent.");
	}

}
