# LookupMerge
# 01.01.07 lookupmerge
https://www.mongodb.com/docs/manual/reference/operator/aggregation/lookup/

```json
db.orders.insertMany( [
  { "_id" : 1, "item" : "almonds", "price" : 12, "ordered" : 2 },
  { "_id" : 2, "item" : "pecans", "price" : 20, "ordered" : 1 },
  { "_id" : 3, "item" : "cookies", "price" : 10, "ordered" : 60 }
] )
```


Create another collection warehouses with these documents:
```json
db.warehouses.insertMany( [
  { "_id" : 1, "stock_item" : "almonds", warehouse: "A", "instock" : 120 },
  { "_id" : 2, "stock_item" : "pecans", warehouse: "A", "instock" : 80 },
  { "_id" : 3, "stock_item" : "almonds", warehouse: "B", "instock" : 60 },
  { "_id" : 4, "stock_item" : "cookies", warehouse: "B", "instock" : 40 },
  { "_id" : 5, "stock_item" : "cookies", warehouse: "A", "instock" : 80 }
] )
```

he following example:

Uses a correlated subquery with a join on the orders.item and warehouse.stock_item fields.
Ensures the quantity of the item in stock can fulfill the ordered quantity
```json
db.orders.aggregate( [
   {
      $lookup:
         {
           from: "warehouses",
           let: { order_item: "$item", order_qty: "$ordered" },
           pipeline: [
              { $match:
                 { $expr:
                    { $and:
                       [
                         { $eq: [ "$stock_item",  "$$order_item" ] },
                         { $gte: [ "$instock", "$$order_qty" ] }
                       ]
                    }
                 }
              },
              { $project: { stock_item: 0, _id: 0 } }
           ],
           as: "stockdata"
         }
    }
] )
```
The operation returns these documents:
```json
{
  _id: 1,
  item: 'almonds',
  price: 12,
  ordered: 2,
  stockdata: [
    { warehouse: 'A', instock: 120 },
    { warehouse: 'B', instock: 60 }
  ]
},
{
  _id: 2,
  item: 'pecans',
  price: 20,
  ordered: 1,
  stockdata: [ { warehouse: 'A', instock: 80 } ]
},
{
  _id: 3,
  item: 'cookies',
  price: 10,
  ordered: 60,
  stockdata: [ { warehouse: 'A', instock: 80 } ]
}
```
The operation corresponds to this pseudo-SQL statement:
```sql
SELECT *, stockdata
FROM orders
WHERE stockdata IN (
   SELECT warehouse, instock
   FROM warehouses
   WHERE stock_item = orders.item
   AND instock >= orders.ordered
);
```