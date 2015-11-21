package com.coroptis.datasync;

import java.util.UUID;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;

import junit.framework.TestCase;

/**
 * This test upload simple rdf to triple store using jena API.
 * 
 * @author jan
 *
 */
public class UploadWithApiTest extends TestCase {

    /** A template for creating a nice SPARUL query */
    private static final String UPDATE_TEMPLATE = "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
	    + "INSERT DATA" + "{ <http://example/%s>    dc:title    \"A new book\" ;"
	    + "                         dc:creator  \"A.N.Other\" ." + "}   ";

    public void test_upload() throws Exception {
	// Add a new book to the collection
	String id = UUID.randomUUID().toString();
	System.out.println(String.format("Adding %s", id));
	UpdateProcessor upp = UpdateExecutionFactory.createRemote(
		UpdateFactory.create(String.format(UPDATE_TEMPLATE, id)),
		"http://localhost:3030/pokus/update");
	upp.execute();
	// Query the collection, dump output
	QueryExecution qe = QueryExecutionFactory.sparqlService("http://localhost:3030/pokus/query",
		"SELECT * WHERE {?s ?p ?o}");
	ResultSet results = qe.execSelect();
	ResultSetFormatter.out(System.out, results);
	qe.close();
    }

}
