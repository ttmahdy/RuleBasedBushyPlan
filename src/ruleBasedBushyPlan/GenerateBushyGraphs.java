package ruleBasedBushyPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class GenerateBushyGraphs {
	
	private
	final static String store_sales 	= "store_sales";
	final static String store_returns 	= "store_returns";
	final static String catalog_sales 	= "catalog_sales";
	final static String date_dim1 		= "date_dim1";
	final static String date_dim2 		= "date_dim2";
	final static String date_dim3 		= "date_dim3";
	final static String item 			= "item";
	final static String store 			= "store";
	
	Table store_sales_table;
	Table store_returns_table;
	Table catalog_sales_table;	
	Table date_dim1_table;
	Table date_dim2_table;
	Table date_dim3_table;
	Table item_table;
	Table store_table;
	List<Table> tableList;
	
	public List<Table> getTableList() {
		return tableList;
	}

	public void initTables()
	{
		// For TPC-DS Q17 						  		Table name 			Cardinality CE	
		store_sales_table 		= new Table (store_sales, 		864001869, 	25283812);
		store_returns_table 	= new Table (store_returns,  	86393244, 	12258560);
		catalog_sales_table 	= new Table (catalog_sales,  	431969836, 	47807903);
		date_dim1_table 		= new Table (date_dim1,			1920800,	86);
		date_dim2_table 		= new Table (date_dim2,			1920800,	257);
		date_dim3_table 		= new Table (date_dim3,			1920800,	257);
		item_table 				= new Table (item,				204000,		204000);
		//store_table 			= new Table (store,				804,		804);
		store_table 			= new Table (store,				804,		80);
	}
	
	@SuppressWarnings("serial")
	public void initJoinExpressions()
	{
		// Save the join expressions
		LinkedList<String> joinExpression;

		// store_sales x date_dim d1
		joinExpression = new LinkedList<String>(){{; add("d1.d_date_sk = store_sales.ss_sold_date_sk");}};
		store_sales_table.AddJoinExpressions(date_dim1_table, joinExpression);
		date_dim1_table.AddJoinExpressions(store_sales_table, joinExpression);

		// store_sales x item
		joinExpression = new LinkedList<String>(){{; add(" item.i_item_sk = store_sales.ss_item_sk");}};
		store_sales_table.AddJoinExpressions(item_table, joinExpression);
		item_table.AddJoinExpressions(store_sales_table, joinExpression);

		// store_sales x store
		joinExpression = new LinkedList<String>(){{; add("store.s_store_sk = store_sales.ss_store_sk");}};
		store_sales_table.AddJoinExpressions(store_table, joinExpression);
		store_table.AddJoinExpressions(store_sales_table, joinExpression);

		// store_sales x store_returns
		joinExpression = new LinkedList<String>(){{; 
			add("store_sales.ss_customer_sk = store_returns.sr_customer_sk");
			add("store_sales.ss_item_sk = store_returns.sr_item_sk");
			add("store_sales.ss_ticket_number = store_returns.sr_ticket_number");
			}};
		store_sales_table.AddJoinExpressions(store_returns_table, joinExpression);
		store_returns_table.AddJoinExpressions(store_sales_table, joinExpression);

		// Extra
		// store_sales x store
		joinExpression = new LinkedList<String>(){{; add("store.s_store_sk = store_returns.sr_store_sk");}};
		store_table.AddJoinExpressions(store_returns_table, joinExpression);
		store_returns_table.AddJoinExpressions(store_table, joinExpression);
		
		// store_returns x date_dim d2
		joinExpression = new LinkedList<String>(){{; add("store_returns.sr_returned_date_sk = d2.d_date_sk");}};
		store_returns_table.AddJoinExpressions(date_dim2_table, joinExpression);
		date_dim2_table.AddJoinExpressions(store_returns_table, joinExpression);
		
		// store_returns x catalog_sales
		joinExpression = new LinkedList<String>(){{; 
			add("store_returns.sr_customer_sk = catalog_sales.cs_bill_customer_sk");
			add("store_returns.sr_item_sk = catalog_sales.cs_item_sk");
		}};
		store_returns_table.AddJoinExpressions(catalog_sales_table, joinExpression);
		catalog_sales_table.AddJoinExpressions(store_returns_table, joinExpression);
		
		// catalog_sales x date_dim d3
		joinExpression = new LinkedList<String>(){{; add("catalog_sales.cs_sold_date_sk = d3.d_date_sk");}};
		catalog_sales_table.AddJoinExpressions(date_dim3_table, joinExpression);
		date_dim3_table.AddJoinExpressions(catalog_sales_table, joinExpression);
		
		tableList = Arrays.asList (store_sales_table, catalog_sales_table, store_returns_table, date_dim1_table, date_dim2_table, date_dim3_table, item_table, store_table );
	}
	
	public void generateSubGraphs()
	{
		
		// Find tables that join with more than one table
		// Don't confuse with tables that join on multiple columns with the same table
		List<Table> hubNodes = new ArrayList<Table>();
		HashSet<String> hubNodesHashSet = new HashSet<String>();
		
		for (Table table : tableList) 
		{ 
			System.out.println(table.toString());
			System.out.println();
			
			if (table.getNumberOfEdges() > 1)
			{
				hubNodes.add(table);
				hubNodesHashSet.add(table.tablename);
			}
		}
		
		
		int hubID = 0;
		for (Table hubTable : hubNodes)
		{
			// Now we need to trim the list of tables in each hub by eliminating the already existing node hubs
			hubTable.trimJoinTableList(hubNodesHashSet);
			System.out.println("Hub Id " + hubID + " " + hubTable.tablename);
			System.out.println("\t" + hubTable.toString());
			System.out.println("\t" + hubTable.getResidual());
			System.out.println("\n");
			hubID++;
		}
	}
	
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		
		
		GenerateBushyGraphs bushyGraphQ17 = new GenerateBushyGraphs();
		
		// Initialize the tables for TPC-DS Q17 https://github.com/ttmahdy/hive-testbench/blob/master/sample-queries-tpcds-impala/query17.sql
		bushyGraphQ17.initTables();
		
		// Set the join relations and join expressions
		bushyGraphQ17.initJoinExpressions();
		
		// Trim 
		bushyGraphQ17.generateSubGraphs();
	}

}
