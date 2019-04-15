package com.ida.ida_task;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

	@RequestMapping("/query_2") //query is keyword 
	public String getQuery(@RequestBody recordclazz rec ) {


		System.out.println(rec.getQry());
		System.out.println(rec.getEndpt());

		// Create Query
		QueryFactory.create(rec.getQry());    

		StringWriter modelAsString = new StringWriter();	

		QueryExecution qexec = QueryExecutionFactory.sparqlService(rec.getEndpt(), rec.getQry());

		Model resultss = qexec.execConstruct();
		resultss.write(modelAsString, "N-Triples");
		String sparql_result = modelAsString.toString();

		System.out.println("-"+sparql_result);

		File file = new File("datastore1.ttl");
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(sparql_result);
			writer.close();

		} catch (IOException e) {		
			e.printStackTrace();
		}

		return sparql_result;		 

	}
	
	@RequestMapping("/fetch-all") //Fetch
	public String getfetch() {
		Model model = RDFDataMgr.loadModel("datastore1.ttl") ;
		//RDFDataMgr.read(model, "http://host/some-published-data" ) ;
		return model.toString();
	
	}
}
