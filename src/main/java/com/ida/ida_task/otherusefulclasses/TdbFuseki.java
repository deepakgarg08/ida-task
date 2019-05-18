package com.ida.ida_task.otherusefulclasses;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

public class TdbFuseki {	
	public static void main(String[] args) {
		
		
		 // Make a TDB-backed dataset
		//  String directory = "MyDatabases/data3.ttl" ;
		 //Dataset dataset = TDBFactory.createDataset(directory) ;
		Model model = null;
		Dataset ds = null;
		
		ds = TDBFactory.createDataset( "MyDatabases/db1/" );
		ds.begin( ReadWrite.WRITE );
		//ds.commit();
		//try
		//{
			model = ds.getDefaultModel();
			FileManager.get().readModel( model, "MyDatabases/db1/data3.ttl" );
			ds.commit();
		//}
//		finally
//		{
//			
//			ds.end();
//		}
		
		List<Statement> results = new ArrayList<Statement>();
			
		ds.begin( ReadWrite.READ );
		try
		{				
			StmtIterator it = model.listStatements( );
			{
				while( it.hasNext() )
				{
					Statement stmt = it.next(); 
					results.add( stmt );
				}
			}
				
			ds.commit();
		}
		finally
		{
			if( model != null ) model.close();
			ds.end();
		}
			
		//Console (results);
		  
	}
	 
	  
}
