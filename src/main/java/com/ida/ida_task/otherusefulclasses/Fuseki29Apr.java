package com.ida.ida_task.otherusefulclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

public class Fuseki29Apr {
		

	public static void uploadRDF(File rdf, String serviceURI)
			throws IOException {

		// parse the file
		Model m = ModelFactory.createDefaultModel();
		try (FileInputStream in = new FileInputStream(rdf)) {
			m.read(in, null, "RDF/XML");
		}

		// upload the resulting model
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		accessor.putModel(m);
	}

	public static void execSelectAndPrint(String serviceURI, String query) {
//		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
//				query);
//		ResultSet results = q.execSelect();
//
//		ResultSetFormatter.out(System.out, results);
//
//		while (results.hasNext()) {
//			QuerySolution soln = results.nextSolution();
//		//	RDFNode x = soln.get("x");
//			System.out.println(soln);
//		}
	}

	public static void execSelectAndProcess(String serviceURI, String query) {
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			// assumes that you have an "?x" in your query
			RDFNode x = soln.get("subject");
			RDFNode y = soln.get("predicate");
			RDFNode z = soln.get("object");
			System.out.println("subject"+x);
			System.out.println("predicate"+y);
			System.out.println("object"+z);
		}
	}

	public static void main(String[] argv) throws IOException {
		// uploadRDF(new File("data3.ttl"),"http://localhost:3030/test/update" );
		uploadRDF(new File("data2.rdf"),"http://localhost:3030/test/update" );
			//"http://localhost:3030/test"
		execSelectAndPrint(
				"http://localhost:3030/test",
				"SELECT ?subject ?predicate ?object\r\n" + 
				"WHERE {\r\n" + 
				"  ?subject ?predicate ?object\r\n" + 
				"}\r\n" + 
				"LIMIT 25");

		execSelectAndProcess(
				"http://localhost:3030/test",
				"SELECT ?subject ?predicate ?object\r\n" + 
				"WHERE {\r\n" + 
				"  ?subject ?predicate ?object\r\n" + 
				"}\r\n" + 
				"LIMIT 25");
	}
}