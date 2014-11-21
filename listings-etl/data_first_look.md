1. food cards - 488
   select count(1) from raw_data.data where (data->'food_cards')::text != 'null'
2. attributes - 7571
   select count(1) from raw_data.data where (data->'attributes')::text != 'null'
3. capacity - 802
   select count(1) from raw_data.data where (data->'capacity')::text != 'null'
4. working_hours - 10179
   select count(1) from raw_data.data where (data->'working_hours')::text != 'null'
5. categories - 21450
   select count(1) from raw_data.data where (data->'categories')::text != 'null'
6. address - 20865
   select count(1) from raw_data.data where (data->'address')::text != 'null'
7. debit cards - 3664
   select count(1) from raw_data.data where (data->'debit_cards')::text != 'null'
8. credit cards - 3903
   select count(1) from raw_data.data where (data->'credit_cards')::text != 'null'
9. url - 5755
   select count(1) from raw_data.data where (data->'url')::text != 'null'
10. phones - 21169
    select count(1) from raw_data.data where (data->'phones')::text != 'null'
11. other payment methods - 6453
    select count(1) from raw_data.data where (data->'other_payment_methods')::text != 'null'
12. full description - 8915
    select count(1) from raw_data.data where (data->'full_description')::text != 'null'
13. short description - 252
    select count(1) from raw_data.data where (data->'short_description')::text != 'null'
14. name - 21450
    select count(1) from raw_data.data where (data->'name')::text != 'null'
    