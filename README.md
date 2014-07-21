RuleBasedBushyPlan
==================

Rule based bushy plan generator

Input relations:
================

Table [ tablename=store_sales, joinList= store date_dim1 store_returns item, join expression= [store.s_store_sk = store_sales.ss_store_sk] [d1.d_date_sk = store_sales.ss_sold_date_sk] [store_sales.ss_customer_sk = store_returns.sr_customer_sk, store_sales.ss_item_sk = store_returns.sr_item_sk, store_sales.ss_ticket_number = store_returns.sr_ticket_number] [ item.i_item_sk = store_sales.ss_item_sk], cardinalityEstimate=25283812, tableCardinality=864001869]

Table [ tablename=catalog_sales, joinList= date_dim3 store_returns, join expression= [catalog_sales.cs_sold_date_sk = d3.d_date_sk] [store_returns.sr_customer_sk = catalog_sales.cs_bill_customer_sk, store_returns.sr_item_sk = catalog_sales.cs_item_sk], cardinalityEstimate=47807903, tableCardinality=431969836]

Table [ tablename=store_returns, joinList= store store_sales date_dim2 catalog_sales, join expression= [store.s_store_sk = store_returns.sr_store_sk] [store_sales.ss_customer_sk = store_returns.sr_customer_sk, store_sales.ss_item_sk = store_returns.sr_item_sk, store_sales.ss_ticket_number = store_returns.sr_ticket_number] [store_returns.sr_returned_date_sk = d2.d_date_sk] [store_returns.sr_customer_sk = catalog_sales.cs_bill_customer_sk, store_returns.sr_item_sk = catalog_sales.cs_item_sk], cardinalityEstimate=12258560, tableCardinality=86393244]

Table [ tablename=date_dim1, joinList= store_sales, join expression= [d1.d_date_sk = store_sales.ss_sold_date_sk], cardinalityEstimate=86, tableCardinality=1920800]

Table [ tablename=date_dim2, joinList= store_returns, join expression= [store_returns.sr_returned_date_sk = d2.d_date_sk], cardinalityEstimate=257, tableCardinality=1920800]

Table [ tablename=date_dim3, joinList= catalog_sales, join expression= [catalog_sales.cs_sold_date_sk = d3.d_date_sk], cardinalityEstimate=257, tableCardinality=1920800]

Table [ tablename=item, joinList= store_sales, join expression= [ item.i_item_sk = store_sales.ss_item_sk], cardinalityEstimate=204000, tableCardinality=204000]

Table [ tablename=store, joinList= store_sales store_returns, join expression= [store.s_store_sk = store_sales.ss_store_sk] [store.s_store_sk = store_returns.sr_store_sk], cardinalityEstimate=80, tableCardinality=804]

Output :
========

From store_sales will remove: store store_returns item 
Hub Id 0 store_sales
	Table [ tablename=store_sales, joinList= date_dim1, join expression= [d1.d_date_sk = store_sales.ss_sold_date_sk], cardinalityEstimate=25283812, tableCardinality=864001869]
	Residual [ tablename=store_sales, residual joinList= store store_returns item, residual join expressions=[]


From catalog_sales will remove: store_returns 
Hub Id 1 catalog_sales
	Table [ tablename=catalog_sales, joinList= date_dim3, join expression= [catalog_sales.cs_sold_date_sk = d3.d_date_sk], cardinalityEstimate=47807903, tableCardinality=431969836]
	Residual [ tablename=catalog_sales, residual joinList= store_returns, residual join expressions=[]


From store_returns will remove: store store_sales catalog_sales 
Hub Id 2 store_returns
	Table [ tablename=store_returns, joinList= date_dim2, join expression= [store_returns.sr_returned_date_sk = d2.d_date_sk], cardinalityEstimate=12258560, tableCardinality=86393244]
	Residual [ tablename=store_returns, residual joinList= store_sales store catalog_sales, residual join expressions=[]


From store will remove: store_sales store_returns 
Hub Id 3 store
	Table [ tablename=store, joinList=, join expression=, cardinalityEstimate=80, tableCardinality=804]
	Residual [ tablename=store, residual joinList= store_sales store_returns, residual join expressions=[]


