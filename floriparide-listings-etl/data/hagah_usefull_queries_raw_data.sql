--regex regexp_replace(element::text, '(\(.*\))','') to clean up facilities from shit like here "Tele-entrega(com pedido mínimo de R$10)", we need only Tele-entrega
SELECT regexp_replace(element::text, '(\(.*\))','')
	FROM raw_data.data AS d,
	json_array_elements(d.data->'facilities') AS element
	WHERE d.source = 'hagah'
	GROUP BY regexp_replace(element::text, '(\(.*\))','');

SELECT element::text
	FROM raw_data.data AS d,
	json_array_elements(d.data->'categories') AS element
	WHERE d.source = 'hagah'
	GROUP BY element::text;

select count(*) from raw_data.data where source = 'hagah';

select * from raw_data.data where source = 'hagah';


delete from raw_data.data where source = 'hagah';

select * from raw_data.data as d where (d.data->>'name')::text = 'John Bull Pub';

--query to select food service categories
select distinct cat::text from raw_data.data as d, json_array_elements(d.data->'payment_options') as card, json_array_elements(d.data->'categories') as cat where card::text LIKE '%Refeição%' or card::text LIKE '%Alimentação%' order by cat::text;

--query to select facilities of food rubrics
select regexp_replace(cat::text, '(\(.*\))','') from raw_data.data as d, json_array_elements(d.data->'payment_options') as card, json_array_elements(d.data->'facilities') as cat where card::text LIKE '%Refeição%' or card::text LIKE '%Alimentação%' group by regexp_replace(cat::text, '(\(.*\))','') order by regexp_replace(cat::text, '(\(.*\))','');

--queries to count companies that offer food services
select distinct count(*) from raw_data.data as d, json_array_elements(d.data->'categories') as cat where cat::text IN ('"Eventos"')
select distinct count(*) from raw_data.data as d, json_array_elements(d.data->'categories') as cat where cat::text
	IN (select cat::text from raw_data.data as d, json_array_elements(d.data->'payment_options') as card, json_array_elements(d.data->'categories') as cat where card::text LIKE '%Refeição%' or card::text LIKE '%Alimentação%' group by cat::text order by cat::text)

select (d.data->>'name')::text from raw_data.data as d where source = 'hagah' group by (d.data->>'name')::text HAVING COUNT(*) > 1
select regexp_replace(element::text, '(\(.*\))','') from raw_data.data as d, json_array_elements(d.data->'facilities') as element where d.source = 'hagah' group by regexp_replace(element::text, '(\(.*\))','');
select element::text from raw_data.data as d, json_array_elements(d.data->'categories') as element where d.source = 'hagah' group by element::text order by element::text;
select element->>'name', element->>'value' from raw_data.data as d, json_array_elements(d.data->'add_info') as element where d.source = 'hagah' group by element->>'name', element->>'value' order by element->>'name'

select element::text from raw_data.data as d, json_array_elements(d.data->'categories') as element where d.source = 'hagah' and  group by element::text order by element::text;