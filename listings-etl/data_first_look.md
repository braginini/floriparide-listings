1. food cards - 473
   select count(1) from raw_data.data where (data->'food_cards')::text != 'null'
2. attributes - 6543
   select count(1) from raw_data.data where (data->'attributes')::text != 'null'
3. capacity - 761 	
   select count(1) from raw_data.data where (data->'capacity')::text != 'null'
4. working_hours - 8699
   select count(1) from raw_data.data where (data->'working_hours')::text != 'null'
5. categories - 16998
   select count(1) from raw_data.data where (data->'categories')::text != 'null'
6. address - 16526
   select count(1) from raw_data.data where (data->'address')::text != 'null'
7. debit cards - 3118	
   select count(1) from raw_data.data where (data->'debit_cards')::text != 'null'
8. credit cards - 3299
   select count(1) from raw_data.data where (data->'credit_cards')::text != 'null'
9. url - 4965
   select count(1) from raw_data.data where (data->'url')::text != 'null'
10. phones - 16770
    select count(1) from raw_data.data where (data->'phones')::text != 'null'
11. other payment methods - 5454
    select count(1) from raw_data.data where (data->'other_payment_methods')::text != 'null'
12. full description - 7408
    select count(1) from raw_data.data where (data->'full_description')::text != 'null'
13. short description - 210
    select count(1) from raw_data.data where (data->'short_description')::text != 'null'
14. name - 16998
    select count(1) from raw_data.data where (data->'name')::text != 'null'
    