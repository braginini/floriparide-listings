#regex regexp_replace(element::text, '(\(.*\))','') to clean up facilities from shit like here "Tele-entrega(com pedido mínimo de R$10)", we need only Tele-entrega
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


