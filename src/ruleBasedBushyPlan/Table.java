package ruleBasedBushyPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Table implements Comparable<Table>{

	long cardinalityEstimate;

	List<Table> joinList;
	long tableCardinality;
	String tablename;
	HashMap<Table, LinkedList<String>> joinExpressionHashSet;
	HashMap<Table, LinkedList<String>> residualJoinExpressionHashSet;

	/**
	 * @param tablename
	 * @param tableCardinality
	 * @param cardinalityEstimate
	 * @param joinList
	 */
	public Table(String tablename, long tableCardinality,
			long cardinalityEstimate) {
		super();
		this.tablename = tablename;
		this.tableCardinality = tableCardinality;
		this.cardinalityEstimate = cardinalityEstimate;
		this.joinExpressionHashSet = new HashMap<Table, LinkedList<String>>();
		this.residualJoinExpressionHashSet = new HashMap<Table, LinkedList<String>>();
	}

	public int compareTo(Table compareTable) {
		 
		long compareQuantity = (long) ((Table) compareTable).getTableCardinality(); 

		return (int) (this.tableCardinality - compareQuantity);
	}


	public int getNumberOfEdges() {
		return joinExpressionHashSet.size();
	}

	
	public long getTableCardinality() {
		return tableCardinality;
	}
	
	public void AddJoinExpressions(Table joinTable, LinkedList<String> joinExpressions) {
		this.joinExpressionHashSet.put(joinTable, joinExpressions);
	}

	
	public void setTableCardinality(long tableCardinality) {
		this.tableCardinality = tableCardinality;
	}
	
	public boolean isReducingJoin()
	{
		return tableCardinality > cardinalityEstimate; 
	}
	
	/**
	 * @param hubNodes
	 */
	public void trimJoinTableList(HashSet<String> hubNodes)
	{
		
		List<Table> toRemove = new ArrayList<Table>();
		String removeTables = " ";
		
		//for ( Table t : joinList)
		for ( Table t : joinExpressionHashSet.keySet())	
		{
			// Remove the tables which are already hub tables
			// If the table doesn't reduce the fact table then we should't bother having it in there for now
			
			if(hubNodes.contains(t.tablename) || !t.isReducingJoin())
			{
				toRemove.add(t);
				removeTables += t.tablename + " " ;
			}
		}
		
		// Remove the actual entry
		for (Table t : toRemove)
		{
			joinExpressionHashSet.remove(t);
			LinkedList<String> je = joinExpressionHashSet.get(t);
			residualJoinExpressionHashSet.put(t, je);
		}
		
		System.out.println("From " + this.tablename + " will remove:" + removeTables);
	}

	@Override
	public String toString() {
		  
		String joinListString = "";
		String joinExpression = "";
		String stringTable = null;
		
		for (Table table : joinExpressionHashSet.keySet()) 
		{ 
			joinListString += " " + table.tablename;
			joinExpression += " " + joinExpressionHashSet.get(table);
		}
		
		stringTable = "Table [ tablename=" + tablename + ", joinList="+ joinListString  + ", join expression="+ joinExpression 
				+ ", cardinalityEstimate=" + cardinalityEstimate
				+ ", tableCardinality="+ tableCardinality +"]";
		
		return stringTable;
	}
	
	public String getResidual()
	{
		String residualTables = "";
		LinkedList<String> residualExpression = new LinkedList<String>();
		String residualString = null;
		
		for (Table table : residualJoinExpressionHashSet.keySet())
		{
			residualTables += " " + table.tablename;
			 
			 LinkedList<String> exp= residualJoinExpressionHashSet.get(table);
			if(exp != null)
			{
				residualExpression.addAll(exp);
			}
		}
		
		if (residualTables.length() > 1)
		{
			residualString = "Residual [ tablename=" + tablename + ", residual joinList=" + residualTables +", residual join expressions="+residualExpression.toString();
		}

		return residualString;
	}
}
