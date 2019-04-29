package com.ida.ida_task;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApplicationController {
	
	@RequestMapping("/create")
	public String getQuery3(@RequestBody recordclazz reco ) throws FileNotFoundException {		
		System.out.println(reco.getUserName());
		System.out.println(reco.getPassword());
		//System.out.println(reco.getQry());
	    
		// some definitions
	//	String userId = reco.getId();
		String userName = reco.getUserName();
		String password = reco.getPassword();
		String personURI = "http://userdata/" + userName.replaceAll("\\s+", "");
        
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        //   create the resource 
        //   and add the properties cascading style
        Resource rs 
          = model.createResource(personURI)
        	//	 .addProperty(VCARD.UID, userId)
                 .addProperty(VCARD.NAME, userName)
                 .addProperty(VCARD.Pcode, password);
        //this is working fine
        System.out.println("result set"+rs);
        // now write the model in XML form to a file
        FileOutputStream fout=new FileOutputStream("data3.ttl",true);
      //  Model model1 = ModelFactory.createDefaultModel();        
        		  //model.write(fout,"RDF/XML");
        		  
        	  model.add(model);
        	//  ((Object) model1).build()
        	  model.write(fout,"Turtle");
        	  
            //model.write(System.out, "RDF/XML");
        	  
		return "The userId, userName and Password are:" +userName + password;
	}
	// get endpoint and query and return result
	@RequestMapping("/query_2") //query is keyword 
	public String getQuery(@RequestBody recordclazz rec ) {
		
		System.out.println(rec.getQry());
	//	System.out.println(rec.getEndpt());

		// Create Query
		QueryFactory.create(rec.getQry());    

		StringWriter modelAsString = new StringWriter();	

		QueryExecution qexec = QueryExecutionFactory.sparqlService("data3.ttl", rec.getQry());

		Model resultss = qexec.execConstruct();
		resultss.write(modelAsString, "turtle");
		String sparql_result = modelAsString.toString();

		System.out.println("-"+sparql_result);

//		File file = new File("data3.ttl");
//		BufferedWriter writer;
//		try {
//			writer = new BufferedWriter(new FileWriter(file));
//			writer.write(sparql_result);
//			writer.close();
//
//		} catch (IOException e) {		
//			e.printStackTrace();
//		}

		return sparql_result;		 

	}
	
	@RequestMapping("/fetch-all") //Fetch-all
	public String getfetch() {
		Model model = RDFDataMgr.loadModel("data9.rdf") ;
		RDFDataMgr.read(model, "http://host/some-published-data" ) ;
		return model.toString();
		
//		// create an empty model
//        Model model = ModelFactory.createDefaultModel();
//       
//        // use the FileManager to find the input file
//        InputStream in = FileManager.get().open("data3.rdf");
//        if (in == null) {
//            throw new IllegalArgumentException( "File:  not found");
//        }
//        
//        // read the RDF/XML file
//        model.read( in, "");
//        
//        // select all the resources with a VCARD.FN property
//        ResIterator iter = model.listResourcesWithProperty(VCARD.FN);
//        if (iter.hasNext()) {
//            System.out.println("The database contains vcards for:");
//            while (iter.hasNext()) {
//                System.out.println("  " + iter.nextResource()
//                                              .getRequiredProperty(VCARD.FN)
//                                              .getString() );
//            }
//        } else {
//            System.out.println("No vcards were found in the database");
//        }
//return null; 
	}
	
	static final String inputFileName = "data3.ttl";
    //static final String johnSmithURI = "http://somewhere/gargdeepak";
    
	@RequestMapping("/fetch") //Fetch one by one
	public String getfetchOneByOne(@RequestBody recordclazz records) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
       
        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        
        // read the RDF/XML file
        model.read(new InputStreamReader(in), "");
        
        // retrieve the vcard resource from the model
        Resource vcard = model.getResource(records.getQry());
        
        // retrieve the value of the N property
//        Resource name = (Resource) vcard.getRequiredProperty(VCARD.N)
//                                        .getObject();        
        // retrieve the given name property
        String id = vcard.getRequiredProperty(VCARD.UID)
                               .getString();
        String username = vcard.getRequiredProperty(VCARD.NAME)
                			   .getString();
        String password = vcard.getRequiredProperty(VCARD.Pcode)
        					   .getString();
        System.out.println("The fullname is"  +id +username +password);
		return "The userId, userName and Password are:"+id +username + password;
	}
		
}
